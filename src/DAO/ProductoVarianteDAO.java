package DAO;

import Database.Conexion;
import Models.ProductoVariante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoVarianteDAO {

    public void create(ProductoVariante variante) throws SQLException {
        String sql = "INSERT INTO producto_variante (" +
                "id_producto, sku, talla, color, precio_compra, precio_venta, precio_venta_mayorista, " +
                "stock_actual, stock_minimo, punto_reorden, costo_promedio, " +
                "id_tecnica_inventario, id_tecnica_costo, estado, fecha_creacion, fecha_modificacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, variante.getIdProducto());
            ps.setString(2, variante.getSku());
            ps.setString(3, variante.getTalla());
            ps.setString(4, variante.getColor());
            ps.setBigDecimal(5, variante.getPrecioCompra());
            ps.setBigDecimal(6, variante.getPrecioVenta());
            ps.setBigDecimal(7, variante.getPrecioVentaMayorista());
            ps.setInt(8, variante.getStockActual());
            ps.setInt(9, variante.getStockMinimo());
            ps.setInt(10, variante.getPuntoReorden());
            ps.setBigDecimal(11, variante.getCostoPromedio());

            if (variante.getIdTecnicaInventario() != null) {
                ps.setInt(12, variante.getIdTecnicaInventario());
            } else {
                ps.setNull(12, Types.INTEGER);
            }

            if (variante.getIdTecnicaCosto() != null) {
                ps.setInt(13, variante.getIdTecnicaCosto());
            } else {
                ps.setNull(13, Types.INTEGER);
            }

            ps.setString(14, variante.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    variante.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM producto_variante WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProductoVariante(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM producto_variante ORDER BY id";

        List<String[]> variantes = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                variantes.add(mapResultSetToProductoVariante(rs));
            }
        }

        return variantes;
    }

    public List<String[]> listByProducto(int idProducto) throws SQLException {
        String sql = "SELECT * FROM producto_variante WHERE id_producto = ? ORDER BY id";

        List<String[]> variantes = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    variantes.add(mapResultSetToProductoVariante(rs));
                }
            }
        }

        return variantes;
    }

    public void update(ProductoVariante variante) throws SQLException {
        String sql = "UPDATE producto_variante SET " +
                "id_producto = ?, " +
                "sku = ?, " +
                "talla = ?, " +
                "color = ?, " +
                "precio_compra = ?, " +
                "precio_venta = ?, " +
                "precio_venta_mayorista = ?, " +
                "stock_actual = ?, " +
                "stock_minimo = ?, " +
                "punto_reorden = ?, " +
                "costo_promedio = ?, " +
                "id_tecnica_inventario = ?, " +
                "id_tecnica_costo = ?, " +
                "estado = ?, " +
                "fecha_modificacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variante.getIdProducto());
            ps.setString(2, variante.getSku());
            ps.setString(3, variante.getTalla());
            ps.setString(4, variante.getColor());
            ps.setBigDecimal(5, variante.getPrecioCompra());
            ps.setBigDecimal(6, variante.getPrecioVenta());
            ps.setBigDecimal(7, variante.getPrecioVentaMayorista());
            ps.setInt(8, variante.getStockActual());
            ps.setInt(9, variante.getStockMinimo());
            ps.setInt(10, variante.getPuntoReorden());
            ps.setBigDecimal(11, variante.getCostoPromedio());

            if (variante.getIdTecnicaInventario() != null) {
                ps.setInt(12, variante.getIdTecnicaInventario());
            } else {
                ps.setNull(12, Types.INTEGER);
            }

            if (variante.getIdTecnicaCosto() != null) {
                ps.setInt(13, variante.getIdTecnicaCosto());
            } else {
                ps.setNull(13, Types.INTEGER);
            }

            ps.setString(14, variante.getEstado());
            ps.setInt(15, variante.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM producto_variante WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM producto_variante WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToProductoVariante(ResultSet rs) throws SQLException {
        return new String[] {
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_producto")),
                rs.getString("sku"),
                rs.getString("talla"),
                rs.getString("color"),
                rs.getBigDecimal("precio_compra") != null
                        ? rs.getBigDecimal("precio_compra").toString()
                        : "",
                rs.getBigDecimal("precio_venta") != null
                        ? rs.getBigDecimal("precio_venta").toString()
                        : "",
                rs.getBigDecimal("precio_venta_mayorista") != null
                        ? rs.getBigDecimal("precio_venta_mayorista").toString()
                        : "",
                String.valueOf(rs.getInt("stock_actual")),
                String.valueOf(rs.getInt("stock_minimo")),
                String.valueOf(rs.getInt("punto_reorden")),
                rs.getBigDecimal("costo_promedio") != null
                        ? rs.getBigDecimal("costo_promedio").toString()
                        : "",
                rs.getObject("id_tecnica_inventario") != null
                        ? String.valueOf(rs.getInt("id_tecnica_inventario"))
                        : "",
                rs.getObject("id_tecnica_costo") != null
                        ? String.valueOf(rs.getInt("id_tecnica_costo"))
                        : "",
                rs.getString("estado"),
                rs.getDate("fecha_creacion") != null
                        ? rs.getDate("fecha_creacion").toString()
                        : "",
                rs.getDate("fecha_modificacion") != null
                        ? rs.getDate("fecha_modificacion").toString()
                        : ""
        };
    }

    public int obtenerStockActual(Connection con, int idProductoVariante) throws SQLException {
        String sql = "SELECT stock_actual FROM producto_variante WHERE id = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock_actual");
                }
            }
        }

        throw new SQLException("No existe producto_variante con id: " + idProductoVariante);
    }

    public void actualizarStockActual(Connection con, int idProductoVariante, int nuevoStock) throws SQLException {
        String sql = "UPDATE producto_variante SET stock_actual = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProductoVariante);
            ps.executeUpdate();
        }
    }

    public int obtenerTecnicaInventario(Connection con, int idProductoVariante) throws SQLException {
        String sql = "SELECT id_tecnica_inventario FROM producto_variante WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject("id_tecnica_inventario");

                    if (value != null) {
                        return rs.getInt("id_tecnica_inventario");
                    }
                }
            }
        }

        return obtenerPrimeraTecnicaInventario(con);
    }

    public int obtenerTecnicaCosto(Connection con, int idProductoVariante) throws SQLException {
        String sql = "SELECT id_tecnica_costo FROM producto_variante WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject("id_tecnica_costo");

                    if (value != null) {
                        return rs.getInt("id_tecnica_costo");
                    }
                }
            }
        }

        return obtenerPrimeraTecnicaCosto(con);
    }

    private int obtenerPrimeraTecnicaInventario(Connection con) throws SQLException {
        String sql = "SELECT id FROM tecnica_inventario ORDER BY id LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        throw new SQLException("No existe ninguna tecnica_inventario registrada.");
    }

    private int obtenerPrimeraTecnicaCosto(Connection con) throws SQLException {
        String sql = "SELECT id FROM tecnica_costo ORDER BY id LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        throw new SQLException("No existe ninguna tecnica_costo registrada.");
    }

public int obtenerPuntoReorden(
        Connection con,
        int idProductoVariante
) throws SQLException {

    String sql = "SELECT punto_reorden " +
            "FROM producto_variante " +
            "WHERE id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idProductoVariante);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("punto_reorden");
            }
        }
    }

    throw new SQLException(
            "No existe producto_variante con id: "
                    + idProductoVariante
    );
}

public String[] obtenerDatosAlertaReorden(
        Connection con,
        int idProductoVariante
) throws SQLException {

    String sql =
            "SELECT pv.id, pv.sku, pv.talla, pv.color, " +
            "pv.stock_actual, pv.stock_minimo, pv.punto_reorden, " +
            "p.nombre AS nombre_producto " +
            "FROM producto_variante pv " +
            "INNER JOIN producto p ON p.id = pv.id_producto " +
            "WHERE pv.id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idProductoVariante);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre_producto") != null
                                ? rs.getString("nombre_producto")
                                : "",
                        rs.getString("sku") != null
                                ? rs.getString("sku")
                                : "",
                        rs.getString("talla") != null
                                ? rs.getString("talla")
                                : "",
                        rs.getString("color") != null
                                ? rs.getString("color")
                                : "",
                        String.valueOf(rs.getInt("stock_actual")),
                        String.valueOf(rs.getInt("stock_minimo")),
                        String.valueOf(rs.getInt("punto_reorden"))
                };
            }
        }
    }

    return null;
}

}