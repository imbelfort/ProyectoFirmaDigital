package DAO;

import Database.Conexion;
import Models.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public void create(Compra compra) throws SQLException {
        String sql = "INSERT INTO compra (" +
                "id_propietario, id_proveedor, num_compra, precio_total, fecha_compra" +
                ") VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, compra.getIdPropietario());
            ps.setInt(2, compra.getIdProveedor());
            ps.setString(3, compra.getNumCompra());
            ps.setBigDecimal(4, compra.getPrecioTotal());

            if (compra.getFechaCompra() != null) {
                ps.setDate(5, compra.getFechaCompra());
            } else {
                ps.setDate(5, new Date(System.currentTimeMillis()));
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    compra.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM compra WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCompra(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM compra ORDER BY id";

        List<String[]> compras = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                compras.add(mapResultSetToCompra(rs));
            }
        }

        return compras;
    }

    public void update(Compra compra) throws SQLException {
        String sql = "UPDATE compra SET " +
                "id_propietario = ?, " +
                "id_proveedor = ?, " +
                "num_compra = ?, " +
                "precio_total = ?, " +
                "fecha_compra = ? " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compra.getIdPropietario());
            ps.setInt(2, compra.getIdProveedor());
            ps.setString(3, compra.getNumCompra());
            ps.setBigDecimal(4, compra.getPrecioTotal());

            if (compra.getFechaCompra() != null) {
                ps.setDate(5, compra.getFechaCompra());
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setInt(6, compra.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM compra WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM compra WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToCompra(ResultSet rs) throws SQLException {
        return new String[] {
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_propietario")),
                String.valueOf(rs.getInt("id_proveedor")),
                rs.getString("num_compra"),
                rs.getBigDecimal("precio_total") != null
                        ? rs.getBigDecimal("precio_total").toString()
                        : "",
                rs.getDate("fecha_compra") != null
                        ? rs.getDate("fecha_compra").toString()
                        : ""
        };
    }
}