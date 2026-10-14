package Services;

import DAO.CategoriaDAO;
import DAO.PromocionCategoriaDAO;
import DAO.PromocionDAO;
import Models.PromocionCategoria;

import java.sql.SQLException;
import java.util.List;

public class PromocionCategoriaService {

    private final PromocionCategoriaDAO promocionCategoriaDAO;
    private final PromocionDAO promocionDAO;
    private final CategoriaDAO categoriaDAO;

    public PromocionCategoriaService() {
        this.promocionCategoriaDAO = new PromocionCategoriaDAO();
        this.promocionDAO = new PromocionDAO();
        this.categoriaDAO = new CategoriaDAO();
    }

    public void crearPromocionCategoria(List<String> params) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Promocion Categoria. " +
                    "Formato: promocion_categoria agregar id_promocion,id_categoria,aplica_mayorista,aplica_minorista"
            );
        }

        PromocionCategoria promocionCategoria = new PromocionCategoria();

        promocionCategoria.setIdPromocion(parseIntRequired(params.get(0), "id_promocion"));
        promocionCategoria.setIdCategoria(parseIntRequired(params.get(1), "id_categoria"));
        promocionCategoria.setAplicaMayorista(parseBooleanRequired(params.get(2), "aplica_mayorista"));
        promocionCategoria.setAplicaMinorista(parseBooleanRequired(params.get(3), "aplica_minorista"));

        validarPromocionCategoria(promocionCategoria);

        if (!promocionDAO.existsById(promocionCategoria.getIdPromocion())) {
            throw new IllegalArgumentException("No existe una promocion con id: " + promocionCategoria.getIdPromocion());
        }

        if (!categoriaDAO.existsById(promocionCategoria.getIdCategoria())) {
            throw new IllegalArgumentException("No existe una categoria con id: " + promocionCategoria.getIdCategoria());
        }

        if (promocionCategoriaDAO.existsByIds(promocionCategoria.getIdPromocion(), promocionCategoria.getIdCategoria())) {
            throw new IllegalArgumentException(
                    "Ya existe una relacion entre la promocion " +
                            promocionCategoria.getIdPromocion() +
                            " y la categoria " +
                            promocionCategoria.getIdCategoria()
            );
        }

        promocionCategoriaDAO.create(promocionCategoria);
    }

    public List<String[]> listarPromocionCategorias() throws SQLException {
        return promocionCategoriaDAO.listAll();
    }

    public String[] obtenerPromocionCategoria(int idPromocion, int idCategoria) throws SQLException {
        if (idPromocion <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (idCategoria <= 0) {
            throw new IllegalArgumentException("El id_categoria debe ser mayor a 0");
        }

        return promocionCategoriaDAO.readByIds(idPromocion, idCategoria);
    }

    public void actualizarPromocionCategoria(List<String> params) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Promocion Categoria. " +
                    "Formato: promocion_categoria modificar id_promocion,id_categoria,aplica_mayorista,aplica_minorista"
            );
        }

        PromocionCategoria promocionCategoria = new PromocionCategoria();

        promocionCategoria.setIdPromocion(parseIntRequired(params.get(0), "id_promocion"));
        promocionCategoria.setIdCategoria(parseIntRequired(params.get(1), "id_categoria"));
        promocionCategoria.setAplicaMayorista(parseBooleanRequired(params.get(2), "aplica_mayorista"));
        promocionCategoria.setAplicaMinorista(parseBooleanRequired(params.get(3), "aplica_minorista"));

        validarPromocionCategoria(promocionCategoria);

        if (!promocionCategoriaDAO.existsByIds(promocionCategoria.getIdPromocion(), promocionCategoria.getIdCategoria())) {
            throw new IllegalArgumentException(
                    "No existe una relacion entre la promocion " +
                            promocionCategoria.getIdPromocion() +
                            " y la categoria " +
                            promocionCategoria.getIdCategoria()
            );
        }

        promocionCategoriaDAO.update(promocionCategoria);
    }

    public void eliminarPromocionCategoria(int idPromocion, int idCategoria) throws SQLException {
        if (idPromocion <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (idCategoria <= 0) {
            throw new IllegalArgumentException("El id_categoria debe ser mayor a 0");
        }

        if (!promocionCategoriaDAO.existsByIds(idPromocion, idCategoria)) {
            throw new IllegalArgumentException(
                    "No existe una relacion entre la promocion " +
                            idPromocion +
                            " y la categoria " +
                            idCategoria
            );
        }

        promocionCategoriaDAO.delete(idPromocion, idCategoria);
    }

    public String getComandosPromocionCategoria() {
        return "Comandos Promocion Categoria:\n" +
                "- promocion_categoria listar\n" +
                "- promocion_categoria agregar id_promocion,id_categoria,aplica_mayorista,aplica_minorista\n" +
                "- promocion_categoria modificar id_promocion,id_categoria,aplica_mayorista,aplica_minorista\n" +
                "- promocion_categoria eliminar id_promocion,id_categoria";
    }

    private void validarPromocionCategoria(PromocionCategoria promocionCategoria) {
        if (promocionCategoria.getIdPromocion() <= 0) {
            throw new IllegalArgumentException("El id_promocion debe ser mayor a 0");
        }

        if (promocionCategoria.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("El id_categoria debe ser mayor a 0");
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