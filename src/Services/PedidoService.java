package Services;

import DAO.CarritoDAO;
import DAO.ClienteDAO;
import DAO.PedidoDAO;
import Models.Pedido;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final ClienteDAO clienteDAO;
    private final CarritoDAO carritoDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.clienteDAO = new ClienteDAO();
        this.carritoDAO = new CarritoDAO();
    }

    public void crearPedido(List<String> params) throws SQLException {
        if (params.size() < 13) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: pedido agregar id_cliente,id_carrito,numero_pedido,fecha_pedido,fecha_pago?,fecha_envio?,fecha_entrega?,fecha_recibido_conforme?,tipo_venta,subtotal,descuento,total,estado,direccion_entrega?,observaciones?"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setIdCliente(parseIntRequired(params.get(0), "id_cliente"));
        pedido.setIdCarrito(parseIntRequired(params.get(1), "id_carrito"));
        pedido.setNumeroPedido(getParamOrDefault(params, 2, generarNumeroPedido()));
        pedido.setFechaPedido(parseDateRequired(params.get(3), "fecha_pedido"));
        pedido.setFechaPago(parseDateOptional(getParam(params, 4)));
        pedido.setFechaEnvio(parseDateOptional(getParam(params, 5)));
        pedido.setFechaEntrega(parseDateOptional(getParam(params, 6)));
        pedido.setFechaRecibidoConforme(parseDateOptional(getParam(params, 7)));
        pedido.setTipoVenta(getParamOrDefault(params, 8, "CORREO"));
        pedido.setSubtotal(parseBigDecimalRequired(params.get(9), "subtotal"));
        pedido.setDescuento(parseBigDecimalOptional(getParam(params, 10), BigDecimal.ZERO));
        pedido.setTotal(parseBigDecimalRequired(params.get(11), "total"));
        pedido.setEstado(getParamOrDefault(params, 12, "PENDIENTE"));
        pedido.setDireccionEntrega(getParam(params, 13));
        pedido.setObservaciones(getParam(params, 14));

        validarPedido(pedido);
        validarRelaciones(pedido);

        pedidoDAO.create(pedido);
    }

    public void actualizarPedido(List<String> params) throws SQLException {
        if (params.size() < 14) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: pedido modificar id,id_cliente,id_carrito,numero_pedido,fecha_pedido,fecha_pago?,fecha_envio?,fecha_entrega?,fecha_recibido_conforme?,tipo_venta,subtotal,descuento,total,estado,direccion_entrega?,observaciones?"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setId(parseIntRequired(params.get(0), "id"));
        pedido.setIdCliente(parseIntRequired(params.get(1), "id_cliente"));
        pedido.setIdCarrito(parseIntRequired(params.get(2), "id_carrito"));
        pedido.setNumeroPedido(getParamOrDefault(params, 3, generarNumeroPedido()));
        pedido.setFechaPedido(parseDateRequired(params.get(4), "fecha_pedido"));
        pedido.setFechaPago(parseDateOptional(getParam(params, 5)));
        pedido.setFechaEnvio(parseDateOptional(getParam(params, 6)));
        pedido.setFechaEntrega(parseDateOptional(getParam(params, 7)));
        pedido.setFechaRecibidoConforme(parseDateOptional(getParam(params, 8)));
        pedido.setTipoVenta(getParamOrDefault(params, 9, "CORREO"));
        pedido.setSubtotal(parseBigDecimalRequired(params.get(10), "subtotal"));
        pedido.setDescuento(parseBigDecimalOptional(getParam(params, 11), BigDecimal.ZERO));
        pedido.setTotal(parseBigDecimalRequired(params.get(12), "total"));
        pedido.setEstado(getParamOrDefault(params, 13, "PENDIENTE"));
        pedido.setDireccionEntrega(getParam(params, 14));
        pedido.setObservaciones(getParam(params, 15));

        if (!pedidoDAO.existsById(pedido.getId())) {
            throw new IllegalArgumentException("No existe un pedido con id: " + pedido.getId());
        }

        validarPedido(pedido);
        validarRelaciones(pedido);

        pedidoDAO.update(pedido);
    }

    public List<String[]> listarPedidos() throws SQLException {
        return pedidoDAO.listAll();
    }

    public String[] obtenerPedidoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del pedido debe ser mayor a 0");
        }

        return pedidoDAO.readById(id);
    }

    public void eliminarPedido(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del pedido debe ser mayor a 0");
        }

        if (!pedidoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un pedido con id: " + id);
        }

        pedidoDAO.delete(id);
    }

    public String getComandosPedido() {
        return "Comandos Pedido:\n" +
                "- pedido listar\n" +
                "- pedido agregar id_cliente,id_carrito,numero_pedido,fecha_pedido,fecha_pago?,fecha_envio?,fecha_entrega?,fecha_recibido_conforme?,tipo_venta,subtotal,descuento,total,estado,direccion_entrega?,observaciones?\n" +
                "- pedido modificar id,id_cliente,id_carrito,numero_pedido,fecha_pedido,fecha_pago?,fecha_envio?,fecha_entrega?,fecha_recibido_conforme?,tipo_venta,subtotal,descuento,total,estado,direccion_entrega?,observaciones?\n" +
                "- pedido eliminar id\n" +
                "- pedido help";
    }

    private void validarRelaciones(Pedido pedido) throws SQLException {
        if (!clienteDAO.existsById(pedido.getIdCliente())) {
            throw new IllegalArgumentException("No existe un cliente con id: " + pedido.getIdCliente());
        }

        if (!carritoDAO.existsById(pedido.getIdCarrito())) {
            throw new IllegalArgumentException("No existe un carrito con id: " + pedido.getIdCarrito());
        }
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El id_cliente debe ser mayor a 0");
        }

        if (pedido.getIdCarrito() <= 0) {
            throw new IllegalArgumentException("El id_carrito debe ser mayor a 0");
        }

        if (pedido.getNumeroPedido() == null || pedido.getNumeroPedido().trim().isEmpty()) {
            throw new IllegalArgumentException("El numero_pedido es obligatorio");
        }

        if (pedido.getFechaPedido() == null) {
            throw new IllegalArgumentException("La fecha_pedido es obligatoria");
        }

        if (pedido.getSubtotal() == null || pedido.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }

        if (pedido.getDescuento() == null || pedido.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }

        if (pedido.getTotal() == null || pedido.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }

        validarEstado(pedido.getEstado());
        validarTipoVenta(pedido.getTipoVenta());
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String value = estado.trim().toUpperCase();

        if (!value.equals("PENDIENTE") &&
            !value.equals("PAGADO") &&
            !value.equals("ENVIADO") &&
            !value.equals("ENTREGADO") &&
            !value.equals("RECIBIDO") &&
            !value.equals("CANCELADO")) {
            throw new IllegalArgumentException("Estado de pedido no valido");
        }
    }

    private void validarTipoVenta(String tipoVenta) {
        if (tipoVenta == null || tipoVenta.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo_venta es obligatorio");
        }

        String value = tipoVenta.trim().toUpperCase();

        if (!value.equals("ONLINE") &&
            !value.equals("TIENDA_FISICA") &&
            !value.equals("CORREO")) {
            throw new IllegalArgumentException("Tipo de venta no valido");
        }
    }

    private String generarNumeroPedido() {
        return "PED-" + (System.currentTimeMillis() % 1000000);
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

    private String getParamOrDefault(List<String> params, int index, String defaultValue) {
        String value = getParam(params, index);
        return value != null ? value : defaultValue;
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

    private Date parseDateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe tener formato yyyy-MM-dd");
        }
    }

    private Date parseDateOptional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd");
        }
    }
}