package Services;

import DAO.CompraDAO;
import DAO.DetalleCompraDAO;
import DAO.LoteInventarioDAO;
import DAO.MovimientoInventarioDAO;
import DAO.ProductoVarianteDAO;
import Database.Conexion;
import Models.DetalleCompra;
import Models.LoteInventario;
import Models.MovimientoInventario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DetalleCompraService {

    private final DetalleCompraDAO detalleCompraDAO;
    private final CompraDAO compraDAO;
    private final ProductoVarianteDAO productoVarianteDAO;
    private final LoteInventarioDAO loteInventarioDAO;
    private final MovimientoInventarioDAO movimientoInventarioDAO;

    public DetalleCompraService() {
        this.detalleCompraDAO = new DetalleCompraDAO();
        this.compraDAO = new CompraDAO();
        this.productoVarianteDAO = new ProductoVarianteDAO();
        this.loteInventarioDAO = new LoteInventarioDAO();
        this.movimientoInventarioDAO = new MovimientoInventarioDAO();
    }

    public void crearDetalleCompra(List<String> params) throws SQLException {
        DetalleCompra detalle = construirDetalleCompraParaAgregar(params);

        if (!compraDAO.existsById(detalle.getIdCompra())) {
            throw new IllegalArgumentException("No existe una compra con id: " + detalle.getIdCompra());
        }

        if (!productoVarianteDAO.existsById(detalle.getIdProductoVariante())) {
            throw new IllegalArgumentException("No existe producto_variante con id: " + detalle.getIdProductoVariante());
        }

        Connection con = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            int stockAnterior = productoVarianteDAO.obtenerStockActual(con, detalle.getIdProductoVariante());
            int stockResultante = stockAnterior + detalle.getCantidad();

            int idDetalleGenerado = detalleCompraDAO.create(con, detalle);

            LoteInventario lote = new LoteInventario();
            lote.setIdProductoVariante(detalle.getIdProductoVariante());
            lote.setIdDetalleCompra(idDetalleGenerado);
            lote.setCantidadInicial(detalle.getCantidad());
            lote.setCantidadDisponible(detalle.getCantidad());
            lote.setCostoUnitario(detalle.getPrecio());
            lote.setEstado("DISPONIBLE");

            loteInventarioDAO.create(con, lote);

            productoVarianteDAO.actualizarStockActual(con, detalle.getIdProductoVariante(), stockResultante);

            MovimientoInventario movimiento = construirMovimiento(
                    con,
                    detalle.getIdProductoVariante(),
                    "INGRESO",
                    "Ingreso por compra",
                    detalle.getCantidad(),
                    detalle.getPrecio(),
                    detalle.getSubtotal(),
                    stockAnterior,
                    stockResultante,
                    "Movimiento generado automaticamente al agregar detalle_compra"
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

    public void actualizarDetalleCompra(List<String> params) throws SQLException {
        DetalleCompra detalleNuevo = construirDetalleCompraParaModificar(params);

        if (!detalleCompraDAO.existsById(detalleNuevo.getIdDetalle())) {
            throw new IllegalArgumentException("No existe detalle_compra con id: " + detalleNuevo.getIdDetalle());
        }

        if (!compraDAO.existsById(detalleNuevo.getIdCompra())) {
            throw new IllegalArgumentException("No existe una compra con id: " + detalleNuevo.getIdCompra());
        }

        if (!productoVarianteDAO.existsById(detalleNuevo.getIdProductoVariante())) {
            throw new IllegalArgumentException("No existe producto_variante con id: " + detalleNuevo.getIdProductoVariante());
        }

        Connection con = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            DetalleCompra detalleAnterior = detalleCompraDAO.findByIdForUpdate(con, detalleNuevo.getIdDetalle());

            if (detalleAnterior == null) {
                throw new SQLException("No existe detalle_compra con id: " + detalleNuevo.getIdDetalle());
            }

            LoteInventario loteAnterior = loteInventarioDAO.findByDetalleCompraForUpdate(con, detalleNuevo.getIdDetalle());

            if (loteAnterior == null) {
                throw new SQLException("No existe lote_inventario relacionado al detalle_compra: " + detalleNuevo.getIdDetalle());
            }

            int cantidadUsada = loteAnterior.getCantidadInicial() - loteAnterior.getCantidadDisponible();

            if (loteInventarioDAO.tieneSalidas(con, loteAnterior.getId()) && detalleAnterior.getIdProductoVariante() != detalleNuevo.getIdProductoVariante()) {
                throw new SQLException("No se puede cambiar la variante porque el lote ya tiene salidas.");
            }

            if (detalleNuevo.getCantidad() < cantidadUsada) {
                throw new SQLException("La nueva cantidad no puede ser menor a la cantidad ya usada del lote.");
            }

            ajustarStockPorModificacion(con, detalleAnterior, detalleNuevo);

            detalleCompraDAO.update(con, detalleNuevo);

            LoteInventario loteNuevo = new LoteInventario();
            loteNuevo.setIdProductoVariante(detalleNuevo.getIdProductoVariante());
            loteNuevo.setIdDetalleCompra(detalleNuevo.getIdDetalle());
            loteNuevo.setCantidadInicial(detalleNuevo.getCantidad());
            loteNuevo.setCantidadDisponible(detalleNuevo.getCantidad() - cantidadUsada);
            loteNuevo.setCostoUnitario(detalleNuevo.getPrecio());
            loteNuevo.setEstado((detalleNuevo.getCantidad() - cantidadUsada) > 0 ? "DISPONIBLE" : "AGOTADO");

            loteInventarioDAO.updateByDetalleCompra(con, loteNuevo);

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

    public void eliminarDetalleCompra(int idDetalle) throws SQLException {
        if (idDetalle <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        if (!detalleCompraDAO.existsById(idDetalle)) {
            throw new IllegalArgumentException("No existe detalle_compra con id: " + idDetalle);
        }

        Connection con = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();
            con.setAutoCommit(false);

            DetalleCompra detalle = detalleCompraDAO.findByIdForUpdate(con, idDetalle);

            if (detalle == null) {
                throw new SQLException("No existe detalle_compra con id: " + idDetalle);
            }

            LoteInventario lote = loteInventarioDAO.findByDetalleCompraForUpdate(con, idDetalle);

            if (lote == null) {
                throw new SQLException("No existe lote_inventario relacionado al detalle_compra: " + idDetalle);
            }

            if (loteInventarioDAO.tieneSalidas(con, lote.getId())) {
                throw new SQLException("No se puede eliminar porque el lote ya tiene salidas.");
            }

            int stockAnterior = productoVarianteDAO.obtenerStockActual(con, detalle.getIdProductoVariante());
            int stockResultante = stockAnterior - detalle.getCantidad();

            if (stockResultante < 0) {
                throw new SQLException("No se puede eliminar porque el stock quedaria negativo.");
            }

            productoVarianteDAO.actualizarStockActual(con, detalle.getIdProductoVariante(), stockResultante);

            MovimientoInventario movimiento = construirMovimiento(
                    con,
                    detalle.getIdProductoVariante(),
                    "ANULACION_INGRESO",
                    "Anulacion de ingreso por eliminacion de detalle_compra",
                    detalle.getCantidad(),
                    detalle.getPrecio(),
                    detalle.getSubtotal(),
                    stockAnterior,
                    stockResultante,
                    "Movimiento generado automaticamente al eliminar detalle_compra"
            );

            movimientoInventarioDAO.create(con, movimiento);

            loteInventarioDAO.deleteByDetalleCompra(con, idDetalle);
            detalleCompraDAO.delete(con, idDetalle);

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

    public String[] obtenerDetalleCompraPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        return detalleCompraDAO.readById(id);
    }

    public List<String[]> listarDetalleCompras() throws SQLException {
        return detalleCompraDAO.listAll();
    }

    public String getComandosDetalleCompra() {
        return "Comandos Detalle Compra:\n" +
                "- detalle_compra listar\n" +
                "- detalle_compra agregar id_compra,id_producto_variante,cantidad,precio,descuento?,subtotal?\n" +
                "- detalle_compra modificar id_detalle,id_compra,id_producto_variante,cantidad,precio,descuento,subtotal\n" +
                "- detalle_compra eliminar id_detalle\n\n" +
                "Ejemplo agregar:\n" +
                "detalle_compra agregar 1,2,20,45,0,900\n\n" +
                "Al agregar detalle_compra, el sistema crea automaticamente lote_inventario, movimiento_inventario y actualiza stock_actual.";
    }

    private DetalleCompra construirDetalleCompraParaAgregar(List<String> params) {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: detalle_compra agregar id_compra,id_producto_variante,cantidad,precio,descuento?,subtotal?"
            );
        }

        DetalleCompra detalle = new DetalleCompra();

        detalle.setIdCompra(parseIntRequired(params.get(0), "id_compra"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(1), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(2), "cantidad"));
        detalle.setPrecio(parseBigDecimalRequired(params.get(3), "precio"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 4), BigDecimal.ZERO));

        BigDecimal subtotalCalculado = calcularSubtotal(detalle.getCantidad(), detalle.getPrecio(), detalle.getDescuento());
        detalle.setSubtotal(parseBigDecimalOptional(getParam(params, 5), subtotalCalculado));

        validarDetalle(detalle);

        return detalle;
    }

    private DetalleCompra construirDetalleCompraParaModificar(List<String> params) {
        if (params.size() < 7) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: detalle_compra modificar id_detalle,id_compra,id_producto_variante,cantidad,precio,descuento,subtotal"
            );
        }

        DetalleCompra detalle = new DetalleCompra();

        detalle.setIdDetalle(parseIntRequired(params.get(0), "id_detalle"));
        detalle.setIdCompra(parseIntRequired(params.get(1), "id_compra"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(2), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(3), "cantidad"));
        detalle.setPrecio(parseBigDecimalRequired(params.get(4), "precio"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 5), BigDecimal.ZERO));

        BigDecimal subtotalCalculado = calcularSubtotal(detalle.getCantidad(), detalle.getPrecio(), detalle.getDescuento());
        detalle.setSubtotal(parseBigDecimalOptional(getParam(params, 6), subtotalCalculado));

        validarDetalle(detalle);

        return detalle;
    }

    private void ajustarStockPorModificacion(Connection con, DetalleCompra anterior, DetalleCompra nuevo) throws SQLException {
        if (anterior.getIdProductoVariante() == nuevo.getIdProductoVariante()) {
            int diferencia = nuevo.getCantidad() - anterior.getCantidad();

            if (diferencia == 0) {
                return;
            }

            int stockAnterior = productoVarianteDAO.obtenerStockActual(con, anterior.getIdProductoVariante());
            int stockResultante = stockAnterior + diferencia;

            if (stockResultante < 0) {
                throw new SQLException("No se puede modificar porque el stock quedaria negativo.");
            }

            productoVarianteDAO.actualizarStockActual(con, anterior.getIdProductoVariante(), stockResultante);

            MovimientoInventario movimiento = construirMovimiento(
                    con,
                    anterior.getIdProductoVariante(),
                    diferencia > 0 ? "AJUSTE_INGRESO" : "AJUSTE_SALIDA",
                    "Ajuste por modificacion de detalle_compra",
                    Math.abs(diferencia),
                    nuevo.getPrecio(),
                    nuevo.getPrecio().multiply(new BigDecimal(Math.abs(diferencia))),
                    stockAnterior,
                    stockResultante,
                    "Movimiento generado automaticamente al modificar detalle_compra"
            );

            movimientoInventarioDAO.create(con, movimiento);
            return;
        }

        int stockAnteriorVarianteAnterior = productoVarianteDAO.obtenerStockActual(con, anterior.getIdProductoVariante());
        int stockResultanteVarianteAnterior = stockAnteriorVarianteAnterior - anterior.getCantidad();

        if (stockResultanteVarianteAnterior < 0) {
            throw new SQLException("No se puede cambiar la variante porque el stock anterior quedaria negativo.");
        }

        productoVarianteDAO.actualizarStockActual(con, anterior.getIdProductoVariante(), stockResultanteVarianteAnterior);

        MovimientoInventario salida = construirMovimiento(
                con,
                anterior.getIdProductoVariante(),
                "AJUSTE_SALIDA",
                "Salida por cambio de variante en detalle_compra",
                anterior.getCantidad(),
                anterior.getPrecio(),
                anterior.getSubtotal(),
                stockAnteriorVarianteAnterior,
                stockResultanteVarianteAnterior,
                "Movimiento generado automaticamente al cambiar variante del detalle_compra"
        );

        movimientoInventarioDAO.create(con, salida);

        int stockAnteriorVarianteNueva = productoVarianteDAO.obtenerStockActual(con, nuevo.getIdProductoVariante());
        int stockResultanteVarianteNueva = stockAnteriorVarianteNueva + nuevo.getCantidad();

        productoVarianteDAO.actualizarStockActual(con, nuevo.getIdProductoVariante(), stockResultanteVarianteNueva);

        MovimientoInventario ingreso = construirMovimiento(
                con,
                nuevo.getIdProductoVariante(),
                "AJUSTE_INGRESO",
                "Ingreso por cambio de variante en detalle_compra",
                nuevo.getCantidad(),
                nuevo.getPrecio(),
                nuevo.getSubtotal(),
                stockAnteriorVarianteNueva,
                stockResultanteVarianteNueva,
                "Movimiento generado automaticamente al cambiar variante del detalle_compra"
        );

        movimientoInventarioDAO.create(con, ingreso);
    }

    private MovimientoInventario construirMovimiento(Connection con,
                                                     int idProductoVariante,
                                                     String tipoMovimiento,
                                                     String motivo,
                                                     int cantidad,
                                                     BigDecimal costoUnitario,
                                                     BigDecimal costoTotal,
                                                     int stockAnterior,
                                                     int stockResultante,
                                                     String observacion) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setIdProductoVariante(idProductoVariante);
        movimiento.setIdTecnicaInventario(productoVarianteDAO.obtenerTecnicaInventario(con, idProductoVariante));
        movimiento.setIdTecnicaCosto(productoVarianteDAO.obtenerTecnicaCosto(con, idProductoVariante));
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setMotivo(motivo);
        movimiento.setCantidad(cantidad);
        movimiento.setCostoUnitario(costoUnitario);
        movimiento.setCostoTotal(costoTotal);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockResultante(stockResultante);
        movimiento.setObservacion(observacion);

        return movimiento;
    }

    private void validarDetalle(DetalleCompra detalle) {
        if (detalle.getIdCompra() <= 0) {
            throw new IllegalArgumentException("El id_compra debe ser mayor a 0");
        }

        if (detalle.getIdProductoVariante() <= 0) {
            throw new IllegalArgumentException("El id_producto_variante debe ser mayor a 0");
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalle.getPrecio() == null || detalle.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (detalle.getDescuento() == null || detalle.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }

        if (detalle.getSubtotal() == null || detalle.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
    }

    private BigDecimal calcularSubtotal(int cantidad, BigDecimal precio, BigDecimal descuento) {
        return precio.multiply(new BigDecimal(cantidad)).subtract(descuento);
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        String value = params.get(index);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
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