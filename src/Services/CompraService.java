package Services;

import DAO.CompraDAO;
import Models.Compra;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CompraService {

    private final CompraDAO compraDAO;

    public CompraService() {
        this.compraDAO = new CompraDAO();
    }

    public void crearCompra(List<String> params) throws SQLException {
        if (params.size() < 3) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Compra. " +
                    "Formato: compra agregar id_propietario,id_proveedor,num_compra?,precio_total?,fecha_compra?"
            );
        }

        Compra compra = new Compra();

        compra.setIdPropietario(parseIntRequired(params.get(0), "id_propietario"));
        compra.setIdProveedor(parseIntRequired(params.get(1), "id_proveedor"));
        compra.setNumCompra(getParam(params, 2));
        compra.setPrecioTotal(parseBigDecimalOptional(getParam(params, 3), BigDecimal.ZERO));
        compra.setFechaCompra(parseDateOptional(getParam(params, 4)));

        if (compra.getNumCompra() == null || compra.getNumCompra().trim().isEmpty()) {
            compra.setNumCompra(generarNumeroCompra());
        }

        validarCompra(compra);

        compraDAO.create(compra);
    }

    public String[] obtenerCompraPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la compra debe ser mayor a 0");
        }

        return compraDAO.readById(id);
    }

    public List<String[]> listarCompras() throws SQLException {
        return compraDAO.listAll();
    }

    public void actualizarCompra(List<String> params) throws SQLException {
        if (params.size() < 6) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Compra. " +
                    "Formato: compra modificar id,id_propietario,id_proveedor,num_compra,precio_total,fecha_compra"
            );
        }

        Compra compra = new Compra();

        compra.setId(parseIntRequired(params.get(0), "id"));
        compra.setIdPropietario(parseIntRequired(params.get(1), "id_propietario"));
        compra.setIdProveedor(parseIntRequired(params.get(2), "id_proveedor"));
        compra.setNumCompra(getParam(params, 3));
        compra.setPrecioTotal(parseBigDecimalOptional(getParam(params, 4), BigDecimal.ZERO));
        compra.setFechaCompra(parseDateOptional(getParam(params, 5)));

        if (!compraDAO.existsById(compra.getId())) {
            throw new IllegalArgumentException("No existe una compra con id: " + compra.getId());
        }

        if (compra.getNumCompra() == null || compra.getNumCompra().trim().isEmpty()) {
            compra.setNumCompra(generarNumeroCompra());
        }

        validarCompra(compra);

        compraDAO.update(compra);
    }

    public void eliminarCompra(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la compra debe ser mayor a 0");
        }

        if (!compraDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe una compra con id: " + id);
        }

        compraDAO.delete(id);
    }

    public String getComandosCompra() {
        return "Comandos Compra:\n" +
                "- compra listar\n" +
                "- compra agregar id_propietario,id_proveedor,num_compra?,precio_total?,fecha_compra?\n" +
                "- compra modificar id,id_propietario,id_proveedor,num_compra,precio_total,fecha_compra\n" +
                "- compra eliminar id";
    }

    private void validarCompra(Compra compra) {
        if (compra.getIdPropietario() <= 0) {
            throw new IllegalArgumentException("El id_propietario debe ser mayor a 0");
        }

        if (compra.getIdProveedor() <= 0) {
            throw new IllegalArgumentException("El id_proveedor debe ser mayor a 0");
        }

        if (compra.getPrecioTotal() != null && compra.getPrecioTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_total no puede ser negativo");
        }
    }

    private String generarNumeroCompra() {
        return "COMP-" + (System.currentTimeMillis() % 100000);
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

    private Date parseDateOptional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new Date(System.currentTimeMillis());
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd");
        }
    }
}