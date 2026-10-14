package Services;

import DAO.PedidoDAO;
import DAO.PedidoDetalleDAO;
import DAO.ProductoVarianteDAO;
import Models.PedidoDetalle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PedidoDetalleService {

    private final PedidoDetalleDAO pedidoDetalleDAO;
    private final PedidoDAO pedidoDAO;
    private final ProductoVarianteDAO productoVarianteDAO;

    public PedidoDetalleService() {
        this.pedidoDetalleDAO = new PedidoDetalleDAO();
        this.pedidoDAO = new PedidoDAO();
        this.productoVarianteDAO = new ProductoVarianteDAO();
    }

    public void crearPedidoDetalle(List<String> params) throws SQLException {
        if (params.size() < 5) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: pedido_detalle agregar id_pedido,id_producto_variante,cantidad,precio_unitario,descuento?"
            );
        }

        PedidoDetalle detalle = new PedidoDetalle();

        detalle.setIdPedido(parseIntRequired(params.get(0), "id_pedido"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(1), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(2), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(3), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 4), BigDecimal.ZERO));

        BigDecimal subtotalCalculado = calcularSubtotal(
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getDescuento()
        );

        detalle.setSubtotal(subtotalCalculado);

        validarPedidoDetalle(detalle);
        validarRelaciones(detalle);

        pedidoDetalleDAO.create(detalle);
    }

    public void actualizarPedidoDetalle(List<String> params) throws SQLException {
        if (params.size() < 6) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: pedido_detalle modificar id_detalle,id_pedido,id_producto_variante,cantidad,precio_unitario,descuento?"
            );
        }

        PedidoDetalle detalle = new PedidoDetalle();

        detalle.setIdDetalle(parseIntRequired(params.get(0), "id_detalle"));
        detalle.setIdPedido(parseIntRequired(params.get(1), "id_pedido"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(2), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(3), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(4), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(getParam(params, 5), BigDecimal.ZERO));

        BigDecimal subtotalCalculado = calcularSubtotal(
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getDescuento()
        );

        detalle.setSubtotal(subtotalCalculado);

        if (!pedidoDetalleDAO.existsById(detalle.getIdDetalle())) {
            throw new IllegalArgumentException("No existe un pedido_detalle con id: " + detalle.getIdDetalle());
        }

        validarPedidoDetalle(detalle);
        validarRelaciones(detalle);

        pedidoDetalleDAO.update(detalle);
    }

    public List<String[]> listarPedidoDetalles() throws SQLException {
        return pedidoDetalleDAO.listAll();
    }

    public String[] obtenerPedidoDetallePorId(int idDetalle) throws SQLException {
        if (idDetalle <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        return pedidoDetalleDAO.readById(idDetalle);
    }

    public void eliminarPedidoDetalle(int idDetalle) throws SQLException {
        if (idDetalle <= 0) {
            throw new IllegalArgumentException("El id_detalle debe ser mayor a 0");
        }

        if (!pedidoDetalleDAO.existsById(idDetalle)) {
            throw new IllegalArgumentException("No existe un pedido_detalle con id: " + idDetalle);
        }

        pedidoDetalleDAO.delete(idDetalle);
    }

    public String getComandosPedidoDetalle() {
        return "Comandos Pedido Detalle:\n" +
                "- pedido_detalle listar\n" +
                "- pedido_detalle agregar id_pedido,id_producto_variante,cantidad,precio_unitario,descuento?\n" +
                "- pedido_detalle modificar id_detalle,id_pedido,id_producto_variante,cantidad,precio_unitario,descuento?\n" +
                "- pedido_detalle eliminar id_detalle\n" +
                "- pedido_detalle help";
    }

    private void validarRelaciones(PedidoDetalle detalle) throws SQLException {
        if (!pedidoDAO.existsById(detalle.getIdPedido())) {
            throw new IllegalArgumentException("No existe un pedido con id: " + detalle.getIdPedido());
        }

        if (!productoVarianteDAO.existsById(detalle.getIdProductoVariante())) {
            throw new IllegalArgumentException("No existe producto_variante con id: " + detalle.getIdProductoVariante());
        }
    }

    private void validarPedidoDetalle(PedidoDetalle detalle) {
        if (detalle.getIdPedido() <= 0) {
            throw new IllegalArgumentException("El id_pedido debe ser mayor a 0");
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
        return precioUnitario
                .multiply(new BigDecimal(cantidad))
                .subtract(descuento);
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
}