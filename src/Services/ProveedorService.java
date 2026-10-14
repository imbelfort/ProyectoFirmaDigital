package Services;

import DAO.ProveedorDAO;
import Models.Proveedor;

import java.sql.SQLException;
import java.util.List;

public class ProveedorService {

    private final ProveedorDAO proveedorDAO;

    public ProveedorService() {
        this.proveedorDAO = new ProveedorDAO();
    }

    public void crearProveedor(List<String> params) throws SQLException {
        /*
         * Orden esperado:
         * 0 nombre
         * 1 nit?
         * 2 telefono?
         * 3 email?
         * 4 direccion?
         * 5 estado?
         */

        if (params.size() < 1) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Proveedor. " +
                    "Formato: proveedor agregar nombre,nit?,telefono?,email?,direccion?,estado?"
            );
        }

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(parseStringRequired(params.get(0), "nombre"));
        proveedor.setNit(getParam(params, 1));
        proveedor.setTelefono(getParam(params, 2));
        proveedor.setEmail(getParam(params, 3));
        proveedor.setDireccion(getParam(params, 4));
        proveedor.setEstado(getParamOrDefault(params, 5, "ACTIVO"));

        validarEstadoProveedor(proveedor.getEstado());

        proveedorDAO.create(proveedor);
    }

    public String[] obtenerProveedorPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del proveedor debe ser mayor a 0");
        }

        return proveedorDAO.readById(id);
    }

    public List<String[]> listarProveedores() throws SQLException {
        return proveedorDAO.listAll();
    }

    public void actualizarProveedor(List<String> params) throws SQLException {
        /*
         * Orden esperado:
         * 0 id
         * 1 nombre
         * 2 nit
         * 3 telefono
         * 4 email
         * 5 direccion
         * 6 estado
         */

        if (params.size() < 7) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Proveedor. " +
                    "Formato: proveedor modificar id,nombre,nit,telefono,email,direccion,estado"
            );
        }

        Proveedor proveedor = new Proveedor();

        proveedor.setId(parseIntRequired(params.get(0), "id"));
        proveedor.setNombre(parseStringRequired(params.get(1), "nombre"));
        proveedor.setNit(getParam(params, 2));
        proveedor.setTelefono(getParam(params, 3));
        proveedor.setEmail(getParam(params, 4));
        proveedor.setDireccion(getParam(params, 5));
        proveedor.setEstado(getParamOrDefault(params, 6, "ACTIVO"));

        validarEstadoProveedor(proveedor.getEstado());

        if (!proveedorDAO.existsById(proveedor.getId())) {
            throw new IllegalArgumentException("No existe un proveedor con id: " + proveedor.getId());
        }

        proveedorDAO.update(proveedor);
    }

    public void eliminarProveedor(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del proveedor debe ser mayor a 0");
        }

        if (!proveedorDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un proveedor con id: " + id);
        }

        proveedorDAO.delete(id);
    }

    public String getComandosProveedor() {
        return "Comandos Proveedor:\n" +
                "- proveedor listar\n" +
                "- proveedor agregar nombre,nit?,telefono?,email?,direccion?,estado?\n" +
                "- proveedor modificar id,nombre,nit,telefono,email,direccion,estado\n" +
                "- proveedor eliminar id";
                
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

    private String parseStringRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        return value.trim();
    }

    private void validarEstadoProveedor(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return;
        }

        String estadoNormalizado = estado.trim().toUpperCase();

        if (!estadoNormalizado.equals("ACTIVO") &&
            !estadoNormalizado.equals("INACTIVO")) {
            throw new IllegalArgumentException("Estado de proveedor no valido. Use: ACTIVO o INACTIVO");
        }
    }
}