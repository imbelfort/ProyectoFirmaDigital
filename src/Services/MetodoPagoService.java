package Services;

import DAO.MetodoPagoDAO;
import Models.MetodoPago;

import java.sql.SQLException;
import java.util.List;

public class MetodoPagoService {

    private final MetodoPagoDAO metodoPagoDAO;

    public MetodoPagoService() {
        this.metodoPagoDAO = new MetodoPagoDAO();
    }

    public void crearMetodoPago(List<String> params) throws SQLException {
        if (params.size() < 1 || params.size() > 2) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: metodo_pago agregar nombre,descripcion?"
            );
        }

        MetodoPago metodoPago = new MetodoPago();

        metodoPago.setNombre(parseStringRequired(params.get(0), "nombre"));
        metodoPago.setDescripcion(getParam(params, 1));

        validarMetodoPago(metodoPago);

        metodoPagoDAO.create(metodoPago);
    }

    public void actualizarMetodoPago(List<String> params) throws SQLException {
        if (params.size() < 2 || params.size() > 3) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: metodo_pago modificar id,nombre,descripcion?"
            );
        }

        MetodoPago metodoPago = new MetodoPago();

        metodoPago.setId(parseIntRequired(params.get(0), "id"));
        metodoPago.setNombre(parseStringRequired(params.get(1), "nombre"));
        metodoPago.setDescripcion(getParam(params, 2));

        if (!metodoPagoDAO.existsById(metodoPago.getId())) {
            throw new IllegalArgumentException("No existe un metodo_pago con id: " + metodoPago.getId());
        }

        validarMetodoPago(metodoPago);

        metodoPagoDAO.update(metodoPago);
    }

    public List<String[]> listarMetodosPago() throws SQLException {
        return metodoPagoDAO.listAll();
    }

    public String[] obtenerMetodoPagoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del metodo_pago debe ser mayor a 0");
        }

        return metodoPagoDAO.readById(id);
    }

    public void eliminarMetodoPago(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del metodo_pago debe ser mayor a 0");
        }

        if (!metodoPagoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un metodo_pago con id: " + id);
        }

        metodoPagoDAO.delete(id);
    }

    public String getComandosMetodoPago() {
        return "Comandos Metodo Pago:\n" +
                "- metodo_pago listar\n" +
                "- metodo_pago agregar nombre,descripcion?\n" +
                "- metodo_pago modificar id,nombre,descripcion?\n" +
                "- metodo_pago eliminar id\n" +
                "- metodo_pago help";
    }

    private void validarMetodoPago(MetodoPago metodoPago) {
        if (metodoPago.getNombre() == null || metodoPago.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del metodo_pago es obligatorio");
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

        if (value.trim().equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }

    private String parseStringRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
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
}