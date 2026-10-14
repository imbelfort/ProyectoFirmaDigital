package DAO;

import Database.Conexion;
import Models.PromocionProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionProductoDAO {

    public void create(PromocionProducto promocionProducto) throws SQLException {
        String sql = "INSERT INTO promocion_producto (" +
                "id_promocion, id_producto, aplica_mayorista, aplica_minorista" +
                ") VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, promocionProducto.getIdPromocion());
            ps.setInt(2, promocionProducto.getIdProducto());
            ps.setBoolean(3, promocionProducto.isAplicaMayorista());
            ps.setBoolean(4, promocionProducto.isAplicaMinorista());

            ps.executeUpdate();
        }
    }

    public String[] readByIds(int idPromocion, int idProducto) throws SQLException {
        String sql = "SELECT * FROM promocion_producto WHERE id_promocion = ? AND id_producto = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPromocionProducto(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM promocion_producto ORDER BY id_promocion, id_producto";

        List<String[]> promocionesProducto = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                promocionesProducto.add(mapResultSetToPromocionProducto(rs));
            }
        }

        return promocionesProducto;
    }

    public void update(PromocionProducto promocionProducto) throws SQLException {
        String sql = "UPDATE promocion_producto SET " +
                "aplica_mayorista = ?, " +
                "aplica_minorista = ? " +
                "WHERE id_promocion = ? AND id_producto = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, promocionProducto.isAplicaMayorista());
            ps.setBoolean(2, promocionProducto.isAplicaMinorista());
            ps.setInt(3, promocionProducto.getIdPromocion());
            ps.setInt(4, promocionProducto.getIdProducto());

            ps.executeUpdate();
        }
    }

    public void delete(int idPromocion, int idProducto) throws SQLException {
        String sql = "DELETE FROM promocion_producto WHERE id_promocion = ? AND id_producto = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idProducto);

            ps.executeUpdate();
        }
    }

    public boolean existsByIds(int idPromocion, int idProducto) throws SQLException {
        String sql = "SELECT 1 FROM promocion_producto WHERE id_promocion = ? AND id_producto = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToPromocionProducto(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_promocion")),
                String.valueOf(rs.getInt("id_producto")),
                String.valueOf(rs.getBoolean("aplica_mayorista")),
                String.valueOf(rs.getBoolean("aplica_minorista"))
        };
    }
}