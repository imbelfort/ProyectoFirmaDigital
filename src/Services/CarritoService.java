package Services;

import DAO.CarritoDAO;
import DAO.ClienteDAO;
import Models.Carrito;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CarritoService {

    private final CarritoDAO carritoDAO;
    private final ClienteDAO clienteDAO;

    public CarritoService() {
        this.carritoDAO = new CarritoDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public void crearCarrito(List<String> params) throws SQLException {
        if (params.size() < 1) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Carrito. " +
                    "Formato: carrito agregar id_cliente,fecha_creacion?,fecha_actualizacion?"
            );
        }

        Carrito carrito = new Carrito();

        carrito.setIdCliente(parseIntRequired(params.get(0), "id_cliente"));
        carrito.setFechaCreacion(parseDateOptional(getParam(params, 1)));
        carrito.setFechaActualizacion(parseDateOptional(getParam(params, 2)));

        if (carrito.getFechaCreacion() == null) {
            carrito.setFechaCreacion(new Date(System.currentTimeMillis()));
        }

        if (carrito.getFechaActualizacion() == null) {
            carrito.setFechaActualizacion(new Date(System.currentTimeMillis()));
        }

        validarCarrito(carrito);

        if (!clienteDAO.existsById(carrito.getIdCliente())) {
            throw new IllegalArgumentException("No existe un cliente con id: " + carrito.getIdCliente());
        }

        carritoDAO.create(carrito);
    }

    public String[] obtenerCarritoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del carrito debe ser mayor a 0");
        }

        return carritoDAO.readById(id);
    }

    public List<String[]> listarCarritos() throws SQLException {
        return carritoDAO.listAll();
    }

    public void actualizarCarrito(List<String> params) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Carrito. " +
                    "Formato: carrito modificar id,id_cliente,fecha_creacion,fecha_actualizacion"
            );
        }

        Carrito carrito = new Carrito();

        carrito.setId(parseIntRequired(params.get(0), "id"));
        carrito.setIdCliente(parseIntRequired(params.get(1), "id_cliente"));
        carrito.setFechaCreacion(parseDateRequired(params.get(2), "fecha_creacion"));
        carrito.setFechaActualizacion(parseDateRequired(params.get(3), "fecha_actualizacion"));

        if (!carritoDAO.existsById(carrito.getId())) {
            throw new IllegalArgumentException("No existe un carrito con id: " + carrito.getId());
        }

        if (!clienteDAO.existsById(carrito.getIdCliente())) {
            throw new IllegalArgumentException("No existe un cliente con id: " + carrito.getIdCliente());
        }

        validarCarrito(carrito);

        carritoDAO.update(carrito);
    }

    public void eliminarCarrito(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del carrito debe ser mayor a 0");
        }

        if (!carritoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un carrito con id: " + id);
        }

        carritoDAO.delete(id);
    }

    public String getComandosCarrito() {
        return "Comandos Carrito:\n" +
                "- carrito listar\n" +
                "- carrito agregar id_cliente,fecha_creacion?,fecha_actualizacion?\n" +
                "- carrito modificar id,id_cliente,fecha_creacion,fecha_actualizacion\n" +
                "- carrito eliminar id";
    }

    private void validarCarrito(Carrito carrito) {
        if (carrito.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El id_cliente debe ser mayor a 0");
        }

        if (carrito.getFechaCreacion() == null) {
            throw new IllegalArgumentException("La fecha_creacion no puede ser nula");
        }

        if (carrito.getFechaActualizacion() == null) {
            throw new IllegalArgumentException("La fecha_actualizacion no puede ser nula");
        }

        if (carrito.getFechaActualizacion().before(carrito.getFechaCreacion())) {
            throw new IllegalArgumentException("La fecha_actualizacion no puede ser menor a la fecha_creacion");
        }
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