package Services;

import DAO.CategoriaDAO;
import Models.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public void crearCategoria(List<String> params) throws SQLException {
        crearCategoria(params, null);
    }

    public void crearCategoria(List<String> params, String imagenAdjunta) throws SQLException {
        if (params.size() < 3) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: categoria agregar id_propietario,nombre,descripcion"
            );
        }

        Categoria categoria = new Categoria();

        categoria.setIdPropietario(parseIntRequired(params.get(0), "id_propietario"));
        categoria.setNombre(parseStringRequired(params.get(1), "nombre"));
        categoria.setDescripcion(getParam(params, 2));

        categoria.setImagen(normalizarAdjunto(imagenAdjunta));

        categoriaDAO.create(categoria);
    }

    public void actualizarCategoria(List<String> params) throws SQLException {
        actualizarCategoria(params, null);
    }

    public void actualizarCategoria(List<String> params, String imagenAdjunta) throws SQLException {
        if (params.size() < 4) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: categoria modificar id,id_propietario,nombre,descripcion"
            );
        }

        Categoria categoria = new Categoria();

        categoria.setId(parseIntRequired(params.get(0), "id"));
        categoria.setIdPropietario(parseIntRequired(params.get(1), "id_propietario"));
        categoria.setNombre(parseStringRequired(params.get(2), "nombre"));
        categoria.setDescripcion(getParam(params, 3));

        String[] categoriaActual = categoriaDAO.readById(categoria.getId());

        if (categoriaActual == null) {
            throw new IllegalArgumentException("No existe una categoria con id: " + categoria.getId());
        }

        String imagenActual = categoriaActual[4];

        categoria.setImagen(
                imagenAdjunta != null ? normalizarAdjunto(imagenAdjunta) : normalizarTexto(imagenActual)
        );

        categoriaDAO.update(categoria);
    }

    public List<String[]> listarCategorias() throws SQLException {
        return categoriaDAO.listAll();
    }

    public String[] obtenerCategoriaPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la categoria debe ser mayor a 0");
        }

        return categoriaDAO.readById(id);
    }

    public void eliminarCategoria(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la categoria debe ser mayor a 0");
        }

        if (!categoriaDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe una categoria con id: " + id);
        }

        categoriaDAO.delete(id);
    }

    public String getComandosCategoria() {
        return "Comandos Categoria:\n" +
                "- categoria listar\n" +
                "- categoria agregar id_propietario,nombre,descripcion\n" +
                "- categoria modificar id,id_propietario,nombre,descripcion\n" +
                "- categoria eliminar id\n" +
                "- categoria help";
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        return normalizarTexto(params.get(index));
    }

    private String normalizarTexto(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        if (value.trim().equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }

    private String normalizarAdjunto(String value) {
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

    private String parseStringRequired(String value, String fieldName) {
        String normalizado = normalizarTexto(value);

        if (normalizado == null) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        return normalizado;
    }
}