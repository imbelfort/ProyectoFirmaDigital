package Services;

import DAO.CategoriaDAO;
import DAO.ProductoDAO;
import Models.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
        this.categoriaDAO = new CategoriaDAO();
    }

    public void crearProducto(List<String> params, String imagenAdjunta, String qrAdjunto) throws SQLException {
        if (params.size() < 2) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: producto agregar id_categoria,nombre,codigo?,descripcion?,marca?,genero?,precio_venta_base?,precio_venta_mayorista_base?,estado?"
            );
        }

        Producto producto = new Producto();

        producto.setIdCategoria(parseIntRequired(params.get(0), "id_categoria"));
        producto.setNombre(parseStringRequired(params.get(1), "nombre"));
        producto.setCodigo(getParam(params, 2));

        producto.setImagen(normalizarAdjunto(imagenAdjunta));
        producto.setQr(normalizarAdjunto(qrAdjunto));

        producto.setDescripcion(getParam(params, 3));
        producto.setMarca(getParam(params, 4));
        producto.setGenero(getParam(params, 5));
        producto.setPrecioVentaBase(parseBigDecimalOptional(getParam(params, 6), BigDecimal.ZERO));
        producto.setPrecioVentaMayoristaBase(parseBigDecimalOptional(getParam(params, 7), BigDecimal.ZERO));
        producto.setEstado(getParamOrDefault(params, 8, "ACTIVO"));

        validarProducto(producto);

        if (!categoriaDAO.existsById(producto.getIdCategoria())) {
            throw new IllegalArgumentException("No existe una categoria con id: " + producto.getIdCategoria());
        }

        productoDAO.create(producto);
    }

    public void actualizarProducto(List<String> params, String imagenAdjunta, String qrAdjunto) throws SQLException {
        if (params.size() < 10) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: producto modificar id,id_categoria,nombre,codigo?,descripcion?,marca?,genero?,precio_venta_base?,precio_venta_mayorista_base?,estado?"
            );
        }

        Producto producto = new Producto();

        producto.setId(parseIntRequired(params.get(0), "id"));
        producto.setIdCategoria(parseIntRequired(params.get(1), "id_categoria"));
        producto.setNombre(parseStringRequired(params.get(2), "nombre"));
        producto.setCodigo(getParam(params, 3));

        String[] productoActual = productoDAO.readById(producto.getId());

        if (productoActual == null) {
            throw new IllegalArgumentException("No existe un producto con id: " + producto.getId());
        }

        String imagenActual = productoActual[4];
        String qrActual = productoActual[5];

        producto.setImagen(imagenAdjunta != null ? normalizarAdjunto(imagenAdjunta) : normalizarTexto(imagenActual));
        producto.setQr(qrAdjunto != null ? normalizarAdjunto(qrAdjunto) : normalizarTexto(qrActual));

        producto.setDescripcion(getParam(params, 4));
        producto.setMarca(getParam(params, 5));
        producto.setGenero(getParam(params, 6));
        producto.setPrecioVentaBase(parseBigDecimalOptional(getParam(params, 7), BigDecimal.ZERO));
        producto.setPrecioVentaMayoristaBase(parseBigDecimalOptional(getParam(params, 8), BigDecimal.ZERO));
        producto.setEstado(getParamOrDefault(params, 9, "ACTIVO"));

        validarProducto(producto);

        if (!categoriaDAO.existsById(producto.getIdCategoria())) {
            throw new IllegalArgumentException("No existe una categoria con id: " + producto.getIdCategoria());
        }

        productoDAO.update(producto);
    }

    public List<String[]> listarProductos() throws SQLException {
        return productoDAO.listAll();
    }

    public String[] obtenerProductoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del producto debe ser mayor a 0");
        }

        return productoDAO.readById(id);
    }

    public void eliminarProducto(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del producto debe ser mayor a 0");
        }

        if (!productoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un producto con id: " + id);
        }

        productoDAO.delete(id);
    }

    public String getComandosProducto() {
        return "Comandos Producto:\n" +
                "- producto listar\n" +
                "- producto agregar id_categoria,nombre,codigo?,descripcion?,marca?,genero?,precio_venta_base?,precio_venta_mayorista_base?,estado?\n" +
                "- producto modificar id,id_categoria,nombre,codigo?,descripcion?,marca?,genero?,precio_venta_base?,precio_venta_mayorista_base?,estado?\n" +
                "- producto eliminar id\n" +
                "- producto help";
    }

    private void validarProducto(Producto producto) {
        if (producto.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("El id_categoria debe ser mayor a 0");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (producto.getPrecioVentaBase() != null && producto.getPrecioVentaBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_venta_base no puede ser negativo");
        }

        if (producto.getPrecioVentaMayoristaBase() != null && producto.getPrecioVentaMayoristaBase().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_venta_mayorista_base no puede ser negativo");
        }
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        return normalizarTexto(params.get(index));
    }

    private String getParamOrDefault(List<String> params, int index, String defaultValue) {
        String value = getParam(params, index);
        return value != null ? value : defaultValue;
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