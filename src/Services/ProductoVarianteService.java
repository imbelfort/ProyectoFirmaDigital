package Services;

import DAO.ProductoDAO;
import DAO.ProductoVarianteDAO;
import Models.ProductoVariante;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductoVarianteService {

    private final ProductoVarianteDAO productoVarianteDAO;
    private final ProductoDAO productoDAO;

    public ProductoVarianteService() {
        this.productoVarianteDAO = new ProductoVarianteDAO();
        this.productoDAO = new ProductoDAO();
    }

    public void crearProductoVariante(List<String> params) throws SQLException {

        if (params.size() < 1) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Producto Variante. " +
                    "Formato: producto_variante agregar id_producto,sku?,talla?,color?,precio_compra?,precio_venta?,precio_venta_mayorista?,stock_actual?,stock_minimo?,punto_reorden?,costo_promedio?,id_tecnica_inventario?,id_tecnica_costo?,estado?"
            );
        }

        ProductoVariante variante = new ProductoVariante();

        int idProducto = parseIntRequired(params.get(0), "id_producto");

        if (!productoDAO.existsById(idProducto)) {
            throw new IllegalArgumentException("No existe un producto con id: " + idProducto);
        }

        variante.setIdProducto(idProducto);
        variante.setSku(getParam(params, 1));
        variante.setTalla(getParam(params, 2));
        variante.setColor(getParam(params, 3));
        variante.setPrecioCompra(parseBigDecimalOptional(getParam(params, 4), BigDecimal.ZERO));
        variante.setPrecioVenta(parseBigDecimalOptional(getParam(params, 5), BigDecimal.ZERO));
        variante.setPrecioVentaMayorista(parseBigDecimalOptional(getParam(params, 6), BigDecimal.ZERO));
        variante.setStockActual(parseIntOptional(getParam(params, 7), 0));
        variante.setStockMinimo(parseIntOptional(getParam(params, 8), 0));
        variante.setPuntoReorden(parseIntOptional(getParam(params, 9), 0));
        variante.setCostoPromedio(parseBigDecimalOptional(getParam(params, 10), BigDecimal.ZERO));
        variante.setIdTecnicaInventario(parseIntegerOptional(getParam(params, 11)));
        variante.setIdTecnicaCosto(parseIntegerOptional(getParam(params, 12)));
        variante.setEstado(getParamOrDefault(params, 13, "ACTIVO"));

        if (variante.getSku() == null || variante.getSku().trim().isEmpty()) {
            variante.setSku(generarSku(variante));
        }

        validarEstadoVariante(variante.getEstado());
        validarNumerosVariante(variante);

        productoVarianteDAO.create(variante);
    }

    public String[] obtenerProductoVariantePorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la variante debe ser mayor a 0");
        }

        return productoVarianteDAO.readById(id);
    }

    public List<String[]> listarProductoVariantes() throws SQLException {
        return productoVarianteDAO.listAll();
    }

    public List<String[]> listarVariantesPorProducto(int idProducto) throws SQLException {
        if (idProducto <= 0) {
            throw new IllegalArgumentException("El id_producto debe ser mayor a 0");
        }

        return productoVarianteDAO.listByProducto(idProducto);
    }

    public void actualizarProductoVariante(List<String> params) throws SQLException {

        if (params.size() < 15) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Producto Variante. " +
                    "Formato: producto_variante modificar id,id_producto,sku,talla,color,precio_compra,precio_venta,precio_venta_mayorista,stock_actual,stock_minimo,punto_reorden,costo_promedio,id_tecnica_inventario,id_tecnica_costo,estado"
            );
        }

        ProductoVariante variante = new ProductoVariante();

        variante.setId(parseIntRequired(params.get(0), "id"));

        int idProducto = parseIntRequired(params.get(1), "id_producto");

        if (!productoDAO.existsById(idProducto)) {
            throw new IllegalArgumentException("No existe un producto con id: " + idProducto);
        }

        variante.setIdProducto(idProducto);
        variante.setSku(getParam(params, 2));
        variante.setTalla(getParam(params, 3));
        variante.setColor(getParam(params, 4));
        variante.setPrecioCompra(parseBigDecimalOptional(getParam(params, 5), BigDecimal.ZERO));
        variante.setPrecioVenta(parseBigDecimalOptional(getParam(params, 6), BigDecimal.ZERO));
        variante.setPrecioVentaMayorista(parseBigDecimalOptional(getParam(params, 7), BigDecimal.ZERO));
        variante.setStockActual(parseIntOptional(getParam(params, 8), 0));
        variante.setStockMinimo(parseIntOptional(getParam(params, 9), 0));
        variante.setPuntoReorden(parseIntOptional(getParam(params, 10), 0));
        variante.setCostoPromedio(parseBigDecimalOptional(getParam(params, 11), BigDecimal.ZERO));
        variante.setIdTecnicaInventario(parseIntegerOptional(getParam(params, 12)));
        variante.setIdTecnicaCosto(parseIntegerOptional(getParam(params, 13)));
        variante.setEstado(getParamOrDefault(params, 14, "ACTIVO"));

        if (variante.getSku() == null || variante.getSku().trim().isEmpty()) {
            variante.setSku(generarSku(variante));
        }

        validarEstadoVariante(variante.getEstado());
        validarNumerosVariante(variante);

        productoVarianteDAO.update(variante);
    }

    public void eliminarProductoVariante(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la variante debe ser mayor a 0");
        }

        productoVarianteDAO.delete(id);
    }

    public String getComandosProductoVariante() {
        return "Comandos Producto Variante:\n" +
                "- producto_variante listar\n" +
                "- producto_variante ver id\n" +
                "- producto_variante listar_por_producto id_producto\n" +
                "- producto_variante agregar id_producto,sku?,talla?,color?,precio_compra?,precio_venta?,precio_venta_mayorista?,stock_actual?,stock_minimo?,punto_reorden?,costo_promedio?,id_tecnica_inventario?,id_tecnica_costo?,estado?\n" +
                "- producto_variante modificar id,id_producto,sku,talla,color,precio_compra,precio_venta,precio_venta_mayorista,stock_actual,stock_minimo,punto_reorden,costo_promedio,id_tecnica_inventario,id_tecnica_costo,estado\n" +
                "- producto_variante eliminar id";
    }

    private String generarSku(ProductoVariante variante) {
        String talla = variante.getTalla() != null && !variante.getTalla().isBlank()
                ? variante.getTalla().trim().toUpperCase()
                : "SIN-TALLA";

        String color = variante.getColor() != null && !variante.getColor().isBlank()
                ? variante.getColor().trim().toUpperCase().replaceAll("\\s+", "-")
                : "SIN-COLOR";

        return "SKU-P" + variante.getIdProducto() + "-" + talla + "-" + color + "-" + (System.currentTimeMillis() % 10000);
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

    private Integer parseIntegerOptional(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor entero no es valido: " + value);
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

    private void validarEstadoVariante(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return;
        }

        String estadoNormalizado = estado.trim().toUpperCase();

        if (!estadoNormalizado.equals("ACTIVO") &&
            !estadoNormalizado.equals("INACTIVO")) {
            throw new IllegalArgumentException("Estado de variante no valido. Use: ACTIVO o INACTIVO");
        }
    }

    private void validarNumerosVariante(ProductoVariante variante) {
        if (variante.getPrecioCompra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_compra no puede ser negativo");
        }

        if (variante.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_venta no puede ser negativo");
        }

        if (variante.getPrecioVentaMayorista().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_venta_mayorista no puede ser negativo");
        }

        if (variante.getCostoPromedio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo_promedio no puede ser negativo");
        }

        if (variante.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock_actual no puede ser negativo");
        }

        if (variante.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock_minimo no puede ser negativo");
        }

        if (variante.getPuntoReorden() < 0) {
            throw new IllegalArgumentException("El punto_reorden no puede ser negativo");
        }
    }
}