package Services;

import DAO.ProductoDAO;
import DAO.PromocionDAO;
import DAO.PromocionProductoDAO;
import Models.PromocionProducto;

import java.sql.SQLException;
import java.util.List;

public class PromocionProductoService {

    private final PromocionProductoDAO promocionProductoDAO;
    private final PromocionDAO promocionDAO;
    private final ProductoDAO productoDAO;

    public PromocionProductoService() {
        this.promocionProductoDAO = new PromocionProductoDAO();
        this.promocionDAO = new PromocionDAO();
        this.productoDAO = new ProductoDAO();
    }

    public void crearPromocionProducto(List<String> params) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Promocion Producto. " +
                    "Formato: promocion_producto agregar id_promocion,id_producto,aplica_mayorista,aplica_minorista"
            );
        }

        PromocionProducto promocionProducto = new PromocionProducto();

        promocionProducto.setIdPromocion(parseIntRequired(params.get(0), "id_promocion"));
        promocionProducto.setIdProducto(parseIntRequired(params.get(1), "id_producto"));
        promocionProducto.setAplicaMayorista(parseBooleanRequired(params.get(2), "aplica_mayorista"));
        promocionProducto.setAplicaMinorista(parseBooleanRequired(params.get(3), "aplica_minorista"));

        validarPromocionProducto(promocionProducto);

        if (!promocionDAO.existsById(promocionProducto.getIdPromocion())) {
            throw new IllegalArgumentException("No existe una promocion con id: " + promocionProducto.getIdPromocion());
        }

        if (!productoDAO.existsById(promocionProducto.getIdProducto())) {
            throw new IllegalArgumentException("No existe un producto con id: " + promocionProducto.getIdProducto());
        }

        if (promocionProductoDAO.existsByIds(promocionProducto.getIdPromocion(), promocionProducto.getIdProducto())) {
            throw new IllegalArgumentException(
                    "Ya existe una relacion entre la promocion " +
                    promocionProducto.getIdPromocion() +
                    " y el producto " +
                    promocionProducto.getIdProducto()
            );
        }

        promocionProductoDAO.create(promocionProducto);
    }

    public String[] obtenerPromocionProducto(int idPromocion, int idProducto) throws SQLException {
        if (idPromocion <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (idProducto <= 0) {
            throw new IllegalArgumentException("El id_producto debe ser mayor a 0");
        }

        return promocionProductoDAO.readByIds(idPromocion, idProducto);
    }

    public List<String[]> listarPromocionProductos() throws SQLException {
        return promocionProductoDAO.listAll();
    }

    public void actualizarPromocionProducto(List<String> params) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Promocion Producto. " +
                    "Formato: promocion_producto modificar id_promocion,id_producto,aplica_mayorista,aplica_minorista"
            );
        }

        PromocionProducto promocionProducto = new PromocionProducto();

        promocionProducto.setIdPromocion(parseIntRequired(params.get(0), "id_promocion"));
        promocionProducto.setIdProducto(parseIntRequired(params.get(1), "id_producto"));
        promocionProducto.setAplicaMayorista(parseBooleanRequired(params.get(2), "aplica_mayorista"));
        promocionProducto.setAplicaMinorista(parseBooleanRequired(params.get(3), "aplica_minorista"));

        validarPromocionProducto(promocionProducto);

        if (!promocionProductoDAO.existsByIds(promocionProducto.getIdPromocion(), promocionProducto.getIdProducto())) {
            throw new IllegalArgumentException(
                    "No existe una relacion entre la promocion " +
                    promocionProducto.getIdPromocion() +
                    " y el producto " +
                    promocionProducto.getIdProducto()
            );
        }

        promocionProductoDAO.update(promocionProducto);
    }

    public void eliminarPromocionProducto(int idPromocion, int idProducto) throws SQLException {
        if (idPromocion <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (idProducto <= 0) {
            throw new IllegalArgumentException("El id_producto debe ser mayor a 0");
        }

        if (!promocionProductoDAO.existsByIds(idPromocion, idProducto)) {
            throw new IllegalArgumentException(
                    "No existe una relacion entre la promocion " +
                    idPromocion +
                    " y el producto " +
                    idProducto
            );
        }

        promocionProductoDAO.delete(idPromocion, idProducto);
    }

    public String getComandosPromocionProducto() {
        return "Comandos Promocion Producto:\n" +
                "- promocion_producto listar\n" +
                "- promocion_producto agregar id_promocion,id_producto,aplica_mayorista,aplica_minorista\n" +
                "- promocion_producto modificar id_promocion,id_producto,aplica_mayorista,aplica_minorista\n" +
                "- promocion_producto eliminar id_promocion,id_producto";
    }

    private void validarPromocionProducto(PromocionProducto promocionProducto) {
        if (promocionProducto.getIdPromocion() <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (promocionProducto.getIdProducto() <= 0) {
            throw new IllegalArgumentException("El id_producto debe ser mayor a 0");
        }
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

    private boolean parseBooleanRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        String normalizado = value.trim().toLowerCase();

        if (normalizado.equals("true") ||
            normalizado.equals("si") ||
            normalizado.equals("sí") ||
            normalizado.equals("1")) {
            return true;
        }

        if (normalizado.equals("false") ||
            normalizado.equals("no") ||
            normalizado.equals("0")) {
            return false;
        }

        throw new IllegalArgumentException("El campo " + fieldName + " debe ser booleano: true, false, si, no, 1 o 0");
    }
}