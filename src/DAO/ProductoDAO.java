package DAO;

import Database.Conexion;
import Models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void create(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (" +
                "id_categoria, nombre, codigo, imagen, qr, descripcion, marca, genero, " +
                "precio_venta_base, precio_venta_mayorista_base, estado, fecha_creacion, fecha_modificacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, producto.getIdCategoria());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCodigo());
            ps.setString(4, producto.getImagen());
            ps.setString(5, producto.getQr());
            ps.setString(6, producto.getDescripcion());
            ps.setString(7, producto.getMarca());
            ps.setString(8, producto.getGenero());
            ps.setBigDecimal(9, producto.getPrecioVentaBase());
            ps.setBigDecimal(10, producto.getPrecioVentaMayoristaBase());
            ps.setString(11, producto.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM producto WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProducto(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM producto ORDER BY id";

        List<String[]> productos = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
        }

        return productos;
    }

    public void update(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET " +
                "id_categoria = ?, " +
                "nombre = ?, " +
                "codigo = ?, " +
                "imagen = ?, " +
                "qr = ?, " +
                "descripcion = ?, " +
                "marca = ?, " +
                "genero = ?, " +
                "precio_venta_base = ?, " +
                "precio_venta_mayorista_base = ?, " +
                "estado = ?, " +
                "fecha_modificacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, producto.getIdCategoria());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCodigo());
            ps.setString(4, producto.getImagen());
            ps.setString(5, producto.getQr());
            ps.setString(6, producto.getDescripcion());
            ps.setString(7, producto.getMarca());
            ps.setString(8, producto.getGenero());
            ps.setBigDecimal(9, producto.getPrecioVentaBase());
            ps.setBigDecimal(10, producto.getPrecioVentaMayoristaBase());
            ps.setString(11, producto.getEstado());
            ps.setInt(12, producto.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM producto WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToProducto(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_categoria")),
                rs.getString("nombre"),
                rs.getString("codigo"),
                rs.getString("imagen"),
                rs.getString("qr"),
                rs.getString("descripcion"),
                rs.getString("marca"),
                rs.getString("genero"),
                rs.getBigDecimal("precio_venta_base") != null ? rs.getBigDecimal("precio_venta_base").toString() : "",
                rs.getBigDecimal("precio_venta_mayorista_base") != null ? rs.getBigDecimal("precio_venta_mayorista_base").toString() : "",
                rs.getString("estado"),
                rs.getDate("fecha_creacion") != null ? rs.getDate("fecha_creacion").toString() : "",
                rs.getDate("fecha_modificacion") != null ? rs.getDate("fecha_modificacion").toString() : ""
        };
    }
}