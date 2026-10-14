package Services;

import DAO.CarritoDAO;
import DAO.CarritoDetalleDAO;
import DAO.ProductoVarianteDAO;
import Models.CarritoDetalle;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CarritoDetalleService {

    private final CarritoDetalleDAO carritoDetalleDAO;
    private final CarritoDAO carritoDAO;
    private final ProductoVarianteDAO productoVarianteDAO;

    public CarritoDetalleService() {
        this.carritoDetalleDAO = new CarritoDetalleDAO();
        this.carritoDAO = new CarritoDAO();
        this.productoVarianteDAO = new ProductoVarianteDAO();
    }

    public void crearCarritoDetalle(List<String> params) throws SQLException {
        if (params.size() < 5) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: carrito_detalle agregar id_carrito,id_producto_variante,cantidad,precio_unitario,descuento"
            );
        }

        CarritoDetalle detalle = new CarritoDetalle();

        detalle.setIdCarrito(parseIntRequired(params.get(0), "id_carrito"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(1), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(2), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(3), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(params.get(4), BigDecimal.ZERO));

        validarCarritoDetalle(detalle);

        if (!carritoDAO.existsById(detalle.getIdCarrito())) {
            throw new IllegalArgumentException("No existe un carrito con id: " + detalle.getIdCarrito());
        }

        if (!productoVarianteDAO.existsById(detalle.getIdProductoVariante())) {
            throw new IllegalArgumentException("No existe producto_variante con id: " + detalle.getIdProductoVariante());
        }

        if (carritoDetalleDAO.existsByIds(detalle.getIdCarrito(), detalle.getIdProductoVariante())) {
            throw new IllegalArgumentException(
                    "Ya existe ese producto_variante en el carrito indicado"
            );
        }

        carritoDetalleDAO.create(detalle);
    }

    public List<String[]> listarCarritoDetalles() throws SQLException {
        return carritoDetalleDAO.listAll();
    }

    public String[] obtenerCarritoDetalle(int idCarrito, int idProductoVariante) throws SQLException {
        if (idCarrito <= 0) {
            throw new IllegalArgumentException("El id_carrito debe ser mayor a 0");
        }

        if (idProductoVariante <= 0) {
            throw new IllegalArgumentException("El id_producto_variante debe ser mayor a 0");
        }

        return carritoDetalleDAO.readByIds(idCarrito, idProductoVariante);
    }

    public void actualizarCarritoDetalle(List<String> params) throws SQLException {
        if (params.size() < 5) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: carrito_detalle modificar id_carrito,id_producto_variante,cantidad,precio_unitario,descuento"
            );
        }

        CarritoDetalle detalle = new CarritoDetalle();

        detalle.setIdCarrito(parseIntRequired(params.get(0), "id_carrito"));
        detalle.setIdProductoVariante(parseIntRequired(params.get(1), "id_producto_variante"));
        detalle.setCantidad(parseIntRequired(params.get(2), "cantidad"));
        detalle.setPrecioUnitario(parseBigDecimalRequired(params.get(3), "precio_unitario"));
        detalle.setDescuento(parseBigDecimalOptional(params.get(4), BigDecimal.ZERO));

        validarCarritoDetalle(detalle);

        if (!carritoDetalleDAO.existsByIds(detalle.getIdCarrito(), detalle.getIdProductoVariante())) {
            throw new IllegalArgumentException(
                    "No existe ese producto_variante en el carrito indicado"
            );
        }

        carritoDetalleDAO.update(detalle);
    }

    public void eliminarCarritoDetalle(int idCarrito, int idProductoVariante) throws SQLException {
        if (idCarrito <= 0) {
            throw new IllegalArgumentException("El id_carrito debe ser mayor a 0");
        }

        if (idProductoVariante <= 0) {
            throw new IllegalArgumentException("El id_producto_variante debe ser mayor a 0");
        }

        if (!carritoDetalleDAO.existsByIds(idCarrito, idProductoVariante)) {
            throw new IllegalArgumentException(
                    "No existe ese producto_variante en el carrito indicado"
            );
        }

        carritoDetalleDAO.delete(idCarrito, idProductoVariante);
    }

    public String getComandosCarritoDetalle() {
        return "Comandos Carrito Detalle:\n" +
                "- carrito_detalle listar\n" +
                "- carrito_detalle agregar id_carrito,id_producto_variante,cantidad,precio_unitario,descuento\n" +
                "- carrito_detalle modificar id_carrito,id_producto_variante,cantidad,precio_unitario,descuento\n" +
                "- carrito_detalle eliminar id_carrito,id_producto_variante\n" +
                "- carrito_detalle help";
    }

    private void validarCarritoDetalle(CarritoDetalle detalle) {
        if (detalle.getIdCarrito() <= 0) {
            throw new IllegalArgumentException("El id_carrito debe ser mayor a 0");
        }

        if (detalle.getIdProductoVariante() <= 0) {
            throw new IllegalArgumentException("El id_producto_variante debe ser mayor a 0");
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio_unitario no puede ser negativo");
        }

        if (detalle.getDescuento() == null || detalle.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
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