package DAO;

import Database.Conexion;
import Models.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public void create(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedor (" +
                "nombre, nit, telefono, email, direccion, estado, fecha_creacion, fecha_modificacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getNit());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());
            ps.setString(6, proveedor.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    proveedor.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM proveedor WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProveedor(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM proveedor ORDER BY id";

        List<String[]> proveedores = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        }

        return proveedores;
    }

    public void update(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedor SET " +
                "nombre = ?, " +
                "nit = ?, " +
                "telefono = ?, " +
                "email = ?, " +
                "direccion = ?, " +
                "estado = ?, " +
                "fecha_modificacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getNit());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());
            ps.setString(6, proveedor.getEstado());
            ps.setInt(7, proveedor.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM proveedor WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM proveedor WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToProveedor(ResultSet rs) throws SQLException {
        return new String[] {
                String.valueOf(rs.getInt("id")),
                rs.getString("nombre"),
                rs.getString("nit"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion"),
                rs.getString("estado"),
                rs.getDate("fecha_creacion") != null
                        ? rs.getDate("fecha_creacion").toString()
                        : "",
                rs.getDate("fecha_modificacion") != null
                        ? rs.getDate("fecha_modificacion").toString()
                        : ""
        };
    }
}