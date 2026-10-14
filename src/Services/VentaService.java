package Services;

import DAO.ClienteDAO;
import DAO.PedidoDAO;
import DAO.VendedorDAO;
import DAO.VentaDAO;
import Models.Venta;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class VentaService {

    private final VentaDAO ventaDAO;
    private final VendedorDAO vendedorDAO;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;

    public VentaService() {
        this.ventaDAO = new VentaDAO();
        this.vendedorDAO = new VendedorDAO();
        this.clienteDAO = new ClienteDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    public void crearVenta(List<String> params) throws SQLException {
        if (params.size() < 10 || params.size() > 11) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: venta agregar id_vendedor,id_cliente,id_pedido?,numero_venta,tipo,origen,tipo_pago,total,estado,observaciones?,fecha_venta"
            );
        }

        Venta venta = new Venta();

        venta.setIdVendedor(parseIntOptional(getParam(params, 0), 0));
        venta.setIdCliente(parseIntRequired(params.get(1), "id_cliente"));
        venta.setIdPedido(parseIntOptional(getParam(params, 2), 0));
        venta.setNumeroVenta(getParamOrDefault(params, 3, generarNumeroVenta()));
        venta.setTipo(getParamOrDefault(params, 4, "MINORISTA"));
        venta.setOrigen(getParamOrDefault(params, 5, "TIENDA_FISICA"));
        venta.setTipoPago(getParamOrDefault(params, 6, "CONTADO"));
        venta.setTotal(parseBigDecimalRequired(params.get(7), "total"));
        venta.setEstado(getParamOrDefault(params, 8, "PENDIENTE"));

        if (params.size() == 10) {
            venta.setObservaciones(null);
            venta.setFechaVenta(parseDateRequired(params.get(9), "fecha_venta"));
        } else {
            venta.setObservaciones(getParam(params, 9));
            venta.setFechaVenta(parseDateRequired(params.get(10), "fecha_venta"));
        }

        validarVenta(venta);
        validarRelaciones(venta);

        ventaDAO.create(venta);
    }

    public void actualizarVenta(List<String> params) throws SQLException {
        if (params.size() < 11 || params.size() > 12) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: venta modificar id,id_vendedor,id_cliente,id_pedido?,numero_venta,tipo,origen,tipo_pago,total,estado,observaciones?,fecha_venta"
            );
        }

        Venta venta = new Venta();

        venta.setId(parseIntRequired(params.get(0), "id"));
        venta.setIdVendedor(parseIntOptional(getParam(params, 1), 0));
        venta.setIdCliente(parseIntRequired(params.get(2), "id_cliente"));
        venta.setIdPedido(parseIntOptional(getParam(params, 3), 0));
        venta.setNumeroVenta(getParamOrDefault(params, 4, generarNumeroVenta()));
        venta.setTipo(getParamOrDefault(params, 5, "MINORISTA"));
        venta.setOrigen(getParamOrDefault(params, 6, "TIENDA_FISICA"));
        venta.setTipoPago(getParamOrDefault(params, 7, "CONTADO"));
        venta.setTotal(parseBigDecimalRequired(params.get(8), "total"));
        venta.setEstado(getParamOrDefault(params, 9, "PENDIENTE"));

        if (params.size() == 11) {
            venta.setObservaciones(null);
            venta.setFechaVenta(parseDateRequired(params.get(10), "fecha_venta"));
        } else {
            venta.setObservaciones(getParam(params, 10));
            venta.setFechaVenta(parseDateRequired(params.get(11), "fecha_venta"));
        }

        if (!ventaDAO.existsById(venta.getId())) {
            throw new IllegalArgumentException("No existe una venta con id: " + venta.getId());
        }

        validarVenta(venta);
        validarRelaciones(venta);

        ventaDAO.update(venta);
    }

    public List<String[]> listarVentas() throws SQLException {
        return ventaDAO.listAll();
    }

    public String[] obtenerVentaPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la venta debe ser mayor a 0");
        }

        return ventaDAO.readById(id);
    }

    public void eliminarVenta(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la venta debe ser mayor a 0");
        }

        if (!ventaDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe una venta con id: " + id);
        }

        ventaDAO.delete(id);
    }

    public String getComandosVenta() {
        return "Comandos Venta:\n" +
                "- venta listar\n" +
                "- venta agregar id_vendedor,id_cliente,id_pedido?,numero_venta,tipo,origen,tipo_pago,total,estado,observaciones?,fecha_venta\n" +
                "- venta modificar id,id_vendedor,id_cliente,id_pedido?,numero_venta,tipo,origen,tipo_pago,total,estado,observaciones?,fecha_venta\n" +
                "- venta eliminar id\n" +
                "- venta help";
    }

    private void validarRelaciones(Venta venta) throws SQLException {
        if (venta.getIdVendedor() > 0 && !vendedorDAO.existsById(venta.getIdVendedor())) {
            throw new IllegalArgumentException("No existe un vendedor con id: " + venta.getIdVendedor());
        }

        if (!clienteDAO.existsById(venta.getIdCliente())) {
            throw new IllegalArgumentException("No existe un cliente con id: " + venta.getIdCliente());
        }

        if (venta.getIdPedido() > 0 && !pedidoDAO.existsById(venta.getIdPedido())) {
            throw new IllegalArgumentException("No existe un pedido con id: " + venta.getIdPedido());
        }
    }

    private void validarVenta(Venta venta) {
        if (venta.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El id_cliente debe ser mayor a 0");
        }

        if (venta.getNumeroVenta() == null || venta.getNumeroVenta().trim().isEmpty()) {
            throw new IllegalArgumentException("El numero_venta es obligatorio");
        }

        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }

        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha_venta es obligatoria");
        }

        validarTipo(venta.getTipo());
        validarOrigen(venta.getOrigen());
        validarTipoPago(venta.getTipoPago());
        validarEstado(venta.getEstado());
    }

    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        String value = tipo.trim().toUpperCase();

        if (!value.equals("MINORISTA") &&
            !value.equals("MAYORISTA")) {
            throw new IllegalArgumentException("Tipo no valido. Use: MINORISTA o MAYORISTA");
        }
    }

    private void validarOrigen(String origen) {
        if (origen == null || origen.trim().isEmpty()) {
            throw new IllegalArgumentException("El origen es obligatorio");
        }

        String value = origen.trim().toUpperCase();

        if (!value.equals("TIENDA_FISICA") &&
            !value.equals("ONLINE") &&
            !value.equals("CORREO") &&
            !value.equals("WHATSAPP")) {
            throw new IllegalArgumentException("Origen no valido. Use: TIENDA_FISICA, ONLINE, CORREO o WHATSAPP");
        }
    }

    private void validarTipoPago(String tipoPago) {
        if (tipoPago == null || tipoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo_pago es obligatorio");
        }

        String value = tipoPago.trim().toUpperCase();

        if (!value.equals("CONTADO") &&
            !value.equals("CREDITO")) {
            throw new IllegalArgumentException("Tipo de pago no valido. Use: CONTADO o CREDITO");
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String value = estado.trim().toUpperCase();

        if (!value.equals("PENDIENTE") &&
            !value.equals("PAGADA") &&
            !value.equals("ANULADA") &&
            !value.equals("CREDITO")) {
            throw new IllegalArgumentException("Estado no valido. Use: PENDIENTE, PAGADA, ANULADA o CREDITO");
        }
    }

    private String generarNumeroVenta() {
        return "VEN-" + (System.currentTimeMillis() % 1000000);
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

    private int parseIntOptional(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor entero no es valido: " + value);
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
}