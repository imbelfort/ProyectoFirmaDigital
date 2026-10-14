package Services;

import DAO.DetalleVentaDAO;
import DAO.LoteInventarioDAO;
import DAO.MovimientoInventarioDAO;
import DAO.ProductoVarianteDAO;
import DAO.SalidaLoteDAO;
import DAO.VentaDAO;
import Database.Conexion;
import Models.DetalleVenta;
import Models.LoteInventario;
import Models.MovimientoInventario;
import Models.SalidaLote;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DetalleVentaService {

    private final DetalleVentaDAO detalleVentaDAO;
    private final VentaDAO ventaDAO;
    private final ProductoVarianteDAO productoVarianteDAO;
    private final LoteInventarioDAO loteInventarioDAO;
    private final SalidaLoteDAO salidaLoteDAO;
    private final MovimientoInventarioDAO movimientoInventarioDAO;

    public DetalleVentaService() {
        this.detalleVentaDAO = new DetalleVentaDAO();
        this.ventaDAO = new VentaDAO();
        this.productoVarianteDAO = new ProductoVarianteDAO();
        this.loteInventarioDAO = new LoteInventarioDAO();
        this.salidaLoteDAO = new SalidaLoteDAO();
        this.movimientoInventarioDAO = new MovimientoInventarioDAO();
    }

    public String crearDetalleVenta(List<String> params) throws SQLException {
        DetalleVenta detalle = construirDetalleVentaParaAgregar(params);

        if (!ventaDAO.existsById(detalle.getIdVenta())) {
            throw new IllegalArgumentException(
                    "No existe una venta con id: "
                            + detalle.getIdVenta()
            );
        }

        if (!productoVarianteDAO.existsById(
                detalle.getIdProductoVariante()
        )) {
            throw new IllegalArgumentException(
                    "No existe producto_variante con id: "
                            + detalle.getIdProductoVariante()
            );
        }

        Connection con = null;
        String alertaReorden = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            int stockAnterior =
                    productoVarianteDAO.obtenerStockActual(
                            con,
                            detalle.getIdProductoVariante()
                    );

            if (stockAnterior < detalle.getCantidad()) {
                throw new SQLException(
                        "Stock insuficiente. Stock actual: "
                                + stockAnterior
                                + ", cantidad solicitada: "
                                + detalle.getCantidad()
                );
            }

            int stockResultante =
                    stockAnterior - detalle.getCantidad();

            int idDetalleGenerado =
                    detalleVentaDAO.create(con, detalle);

            BigDecimal costoTotalSalida =
                    generarSalidasDeLote(
                            con,
                            idDetalleGenerado,
                            detalle
                    );

            productoVarianteDAO.actualizarStockActual(
                    con,
                    detalle.getIdProductoVariante(),
                    stockResultante
            );

            MovimientoInventario movimiento =
                    construirMovimientoSalida(
                            con,
                            detalle.getIdProductoVariante(),
                            "SALIDA",
                            "Salida por venta",
                            detalle.getCantidad(),
                            costoTotalSalida,
                            stockAnterior,
                            stockResultante,
                            "Movimiento generado automaticamente "
                                    + "al agregar detalle_venta"
                    );

            movimientoInventarioDAO.create(con, movimiento);

            int puntoReorden =
                    productoVarianteDAO.obtenerPuntoReorden(
                            con,
                            detalle.getIdProductoVariante()
                    );

            if (puntoReorden > 0
                    && stockResultante <= puntoReorden) {

                String[] datosProducto =
                        productoVarianteDAO.obtenerDatosAlertaReorden(
                                con,
                                detalle.getIdProductoVariante()
                        );

                alertaReorden = construirAlertaReorden(
                        datosProducto,
                        stockResultante,
                        puntoReorden
                );

                System.out.println(
                        "[ALERTA REORDEN] Producto variante ID: "
                                + detalle.getIdProductoVariante()
                                + " | Stock actual: "
                                + stockResultante
                                + " | Punto de reorden: "
                                + puntoReorden
                );
            }

            con.commit();

            return alertaReorden;

        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }

            throw e;

        } finally {
            cerrarConexion(con);
        }
    }

    public void actualizarDetalleVenta(List<String> params) throws SQLException {
        DetalleVenta detalleNuevo = construirDetalleVentaParaModificar(params);

        if (!detalleVentaDAO.existsById(detalleNuevo.getIdDetalle())) {
            throw new IllegalArgumentException("No existe detalle_venta con id: " + detalleNuevo.getIdDetalle());
        }

        if (!ventaDAO.existsById(detalleNuevo.getIdVenta())) {
            throw new IllegalArgumentException("No existe una venta con id: " + detalleNuevo.getIdVenta());
        }

        if (!productoVarianteDAO.existsById(detalleNuevo.getIdProductoVariante())) {
            throw new IllegalArgumentException("No existe producto_variante con id: " + detalleNuevo.getIdProductoVariante());
        }

        Connection con = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            DetalleVenta detalleAnterior = detalleVentaDAO.findByIdForUpdate(con, detalleNuevo.getIdDetalle());

            if (detalleAnterior == null) {
                throw new SQLException("No existe detalle_venta con id: " + detalleNuevo.getIdDetalle());
            }

            revertirSalidasDetalleVenta(con, detalleAnterior);

            detalleVentaDAO.update(con, detalleNuevo);

            int stockAnterior = productoVarianteDAO.obtenerStockActual(con, detalleNuevo.getIdProductoVariante());

            if (stockAnterior < detalleNuevo.getCantidad()) {
                throw new SQLException("Stock insuficiente para modificar. Stock actual: " + stockAnterior + ", cantidad solicitada: " + detalleNuevo.getCantidad());
            }

            int stockResultante = stockAnterior - detalleNuevo.getCantidad();

            BigDecimal costoTotalSalida = generarSalidasDeLote(con, detalleNuevo.getIdDetalle(), detalleNuevo);

            productoVarianteDAO.actualizarStockActual(con, detalleNuevo.getIdProductoVariante(), stockResultante);

            MovimientoInventario movimiento = construirMovimientoSalida(
                    con,
                    detalleNuevo.getIdProductoVariante(),
                    "SALIDA",
                    "Salida por modificacion de detalle_venta",
                    detalleNuevo.getCantidad(),
                    costoTotalSalida,
                    stockAnterior,
                    stockResultante,
                    "Movimiento generado automaticamente al modificar detalle_venta"
            );

            movimientoInventarioDAO.create(con, movimiento);

            con.commit();

        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            cerrarConexion(con);
        }
    }

    public void eliminarDetalleVenta(int idDetalle) throws SQLException {
        if (idDetalle <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        if (!detalleVentaDAO.existsById(idDetalle)) {
            throw new IllegalArgumentException("No existe detalle_venta con id: " + idDetalle);
        }

        Connection con = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            DetalleVenta detalle = detalleVentaDAO.findByIdForUpdate(con, idDetalle);

            if (detalle == null) {
                throw new SQLException("No existe detalle_venta con id: " + idDetalle);
            }

            revertirSalidasDetalleVenta(con, detalle);

            detalleVentaDAO.delete(con, idDetalle);

            con.commit();

        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            cerrarConexion(con);
        }
    }

    public List<String[]> listarDetalleVentas() throws SQLException {
        return detalleVentaDAO.listAll();
    }

    public String[] obtenerDetalleVentaPorId(int idDetalle) throws SQLException {
        if (idDetalle <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        return detalleVentaDAO.readById(idDetalle);
    }

    public String getComandosDetalleVenta() {
        return "Comandos Detalle Venta:\n" +
                "- detalle_venta listar\n" +
                "- detalle_venta agregar id_venta,id_producto_variante,cantidad,precio_unitario,descuento?\n" +
                "- detalle_venta modificar id_detalle,id_venta,id_producto_variante,cantidad,precio_unitario,descuento?\n" +
                "- detalle_venta eliminar id_detalle\n" +
                "- detalle_venta help";
    }

    private BigDecimal generarSalidasDeLote(Connection con, int idDetalleVenta, DetalleVenta detalle) throws SQLException {
        int cantidadPendiente = detalle.getCantidad();
        BigDecimal costoTotalSalida = BigDecimal.ZERO;

        List<LoteInventario> lotes = loteInventarioDAO.findDisponiblesByProductoVarianteForUpdate(
                con,
                detalle.getIdProductoVariante()
        );

        for (LoteInventario lote : lotes) {
            if (cantidadPendiente <= 0) {
                break;
            }

            int cantidadTomada = Math.min(cantidadPendiente, lote.getCantidadDisponible());

            BigDecimal costoUnitario = lote.getCostoUnitario();
            BigDecimal costoTotal = costoUnitario.multiply(new BigDecimal(cantidadTomada));

            SalidaLote salida = new SalidaLote();
            salida.setIdDetalleVenta(idDetalleVenta);
            salida.setIdLote(lote.getId());
            salida.setCantidad(cantidadTomada);
            salida.setCostoUnitarioAplicado(costoUnitario);
            salida.setCostoTotal(costoTotal);

            salidaLoteDAO.create(con, salida);

            int nuevaCantidadDisponible = lote.getCantidadDisponible() - cantidadTomada;
            loteInventarioDAO.actualizarCantidadDisponible(con, lote.getId(), nuevaCantidadDisponible);

            costoTotalSalida = costoTotalSalida.add(costoTotal);
            cantidadPendiente -= cantidadTomada;
        }

        if (cantidadPendiente > 0) {
            throw new SQLException("No hay cantidad suficiente en lotes disponibles para completar la venta.");
        }

        return costoTotalSalida;
    }

    private void revertirSalidasDetalleVenta(Connection con, DetalleVenta detalle) throws SQLException {
        List<SalidaLote> salidas = salidaLoteDAO.findByDetalleVentaForUpdate(con, detalle.getIdDetalle());

        BigDecimal costoTotalRevertido = BigDecimal.ZERO;
        int cantidadTotalRevertida = 0;

        for (SalidaLote salida : salidas) {
            LoteInventario lote = loteInventarioDAO.findByIdForUpdate(con, salida.getIdLote());

            if (lote == null) {
                throw new SQLException("No existe lote_inventario con id: " + salida.getIdLote());
            }

            int nuevaCantidadDisponible = lote.getCantidadDisponible() + salida.getCantidad();

            if (nuevaCantidadDisponible > lote.getCantidadInicial()) {
                throw new SQLException("No se puede revertir salida_lote porque excede la cantidad inicial del lote.");
            }

            loteInventarioDAO.actualizarCantidadDisponible(con, lote.getId(), nuevaCantidadDisponible);

            costoTotalRevertido = costoTotalRevertido.add(salida.getCostoTotal());
            cantidadTotalRevertida += salida.getCantidad();
        }

        salidaLoteDAO.deleteByDetalleVenta(con, detalle.getIdDetalle());

        int stockAnterior = productoVarianteDAO.obtenerStockActual(con, detalle.getIdProductoVariante());
        int stockResultante = stockAnterior + detalle.getCantidad();

        productoVarianteDAO.actualizarStockActual(con, detalle.getIdProductoVariante(), stockResultante);

        if (cantidadTotalRevertida > 0) {
            MovimientoInventario movimiento = construirMovimientoSalida(
                    con,
                    detalle.getIdProductoVariante(),
                    "ANULACION_SALIDA",
                    "Anulacion de salida por modificacion o eliminacion de detalle_venta",
                    detalle.getCantidad(),
                    costoTotalRevertido,
                    stockAnterior,
                    stockResultante,
                    "Movimiento generado automaticamente al revertir detalle_venta"
            );

            movimientoInventarioDAO.create(con, movimiento);
        }
    }

    private MovimientoInventario construirMovimientoSalida(Connection con,
                                                           int idProductoVariante,
                                                           String tipoMovimiento,
                                                           String motivo,
                                                           int cantidad,
                                                           BigDecimal costoTotal,
                                                           int stockAnterior,
                                                           int stockResultante,
                                                           String observacion) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();

        BigDecimal costoUnitario = BigDecimal.ZERO;

        if (cantidad > 0 && costoTotal != null) {
            costoUnitario = costoTotal.divide(new BigDecimal(cantidad), 2, RoundingMode.HALF_UP);
        }

        movimiento.setIdProductoVariante(idProductoVariante);
        movimiento.setIdTecnicaInventario(productoVarianteDAO.obtenerTecnicaInventario(con, idProductoVariante));
        movimiento.setIdTecnicaCosto(productoVarianteDAO.obtenerTecnicaCosto(con, idProductoVariante));
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMotivo(motivo);
        movimiento.setCantidad(cantidad);
        movimiento.setCostoUnitario(costoUnitario);
        movimiento.setCostoTotal(costoTotal != null ? costoTotal : BigDecimal.ZERO);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockResultante(stockResultante);
        movimiento.setObservacion(observacion);

        return movimiento;
    }

    private DetalleVenta construirDetalleVentaParaAgregar(List<String> params) {
        if (params.size() < 4 || params.size() > 5) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: detalle_venta agregar id_venta,id_producto_variante,cantidad,precio_unitario,descuento?"
            );
        }

        DetalleVenta detalle = new DetalleVenta();

        detalle.setIdVenta(parseIntRequired(params.get(0), "id_venta"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(1), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(2), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(3), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 4), BigDecimal.ZERO));

        BigDecimal subtotal = calcularSubtotal(
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getDescuento()
        );

        detalle.setSubtotal(subtotal);

        validarDetalle(detalle);

        return detalle;
    }

    private DetalleVenta construirDetalleVentaParaModificar(List<String> params) {
        if (params.size() < 5 || params.size() > 6) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: detalle_venta modificar id_detalle,id_venta,id_producto_variante,cantidad,precio_unitario,descuento?"
            );
        }

        DetalleVenta detalle = new DetalleVenta();

        detalle.setIdDetalle(parseIntRequired(params.get(0), "id_detalle"));
        detalle.setIdVenta(parseIntRequired(params.get(1), "id_venta"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(2), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(3), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(4), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 5), BigDecimal.ZERO));

        BigDecimal subtotal = calcularSubtotal(
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getDescuento()
        );

        detalle.setSubtotal(subtotal);

        validarDetalle(detalle);

        return detalle;
    }

    private void validarDetalle(DetalleVenta detalle) {
        if (detalle.getIdVenta() <= 0) {
            throw new IllegalArgumentException("El id_venta debe ser mayor a 0");
        }

        if (detalle.getIdProductoVariante() <= 0) {
            throw new IllegalArgumentException("El id_producto_variante debe ser mayor a 0");
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_unitario no puede ser negativo");
        }

        if (detalle.getDescuento() == null || detalle.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }

        if (detalle.getSubtotal() == null || detalle.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
    }

    private BigDecimal calcularSubtotal(int cantidad, BigDecimal precioUnitario, BigDecimal descuento) {
        return precioUnitario.multiply(new BigDecimal(cantidad)).subtract(descuento);
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        String value = params.get(index);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        if (value.trim().equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }

    private String construirAlertaReorden(
            String[] datos,
            int stockActual,
            int puntoReorden
    ) {
        String idVariante = "";
        String nombreProducto = "";
        String sku = "";
        String talla = "";
        String color = "";
        String stockMinimo = "";

        if (datos != null) {
            idVariante = datos.length > 0 ? datos[0] : "";
            nombreProducto = datos.length > 1 ? datos[1] : "";
            sku = datos.length > 2 ? datos[2] : "";
            talla = datos.length > 3 ? datos[3] : "";
            color = datos.length > 4 ? datos[4] : "";
            stockMinimo = datos.length > 6 ? datos[6] : "";
        }

        return "<div style=\""
                + "width:92%;"
                + "margin:18px auto;"
                + "padding:16px;"
                + "background-color:#FFF3CD;"
                + "border:1px solid #FFCA2C;"
                + "border-left:6px solid #F59E0B;"
                + "border-radius:7px;"
                + "font-family:Arial;"
                + "color:#664D03;"
                + "\">"

                + "<h3 style=\"margin-top:0;color:#9A6700;\">"
                + "&#9888; Alerta de stock bajo"
                + "</h3>"

                + "<p>El producto vendido alcanzó su punto de reorden. "
                + "Se recomienda realizar una nueva compra para reponer "
                + "el inventario.</p>"

                + "<p><b>ID de la variante:</b> "
                + idVariante
                + "</p>"

                + "<p><b>Producto:</b> "
                + nombreProducto
                + "</p>"

                + "<p><b>SKU:</b> "
                + sku
                + "</p>"

                + "<p><b>Talla:</b> "
                + talla
                + "</p>"

                + "<p><b>Color:</b> "
                + color
                + "</p>"

                + "<p><b>Stock actual:</b> "
                + stockActual
                + "</p>"

                + "<p><b>Punto de reorden:</b> "
                + puntoReorden
                + "</p>"

                + "<p><b>Stock mínimo:</b> "
                + stockMinimo
                + "</p>"

                + "</div>";
    }

    private int parseIntRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un numero entero");
        }
    }

    private BigDecimal parseBigDecimalRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser decimal");
        }
    }

    private BigDecimal parseBigDecimalOptional(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor decimal no es valido: " + value);
        }
    }

    private void cerrarConexion(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException ignored) {
        }
    }
}