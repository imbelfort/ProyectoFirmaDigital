package DAO;

import Database.Conexion;
import Models.PromocionCategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionCategoriaDAO {

    public void create(PromocionCategoria promocionCategoria) throws SQLException {
        String sql = "INSERT INTO promocion_categoria (" +
                "id_promocion, id_categoria, aplica_mayorista, aplica_minorista" +
                ") VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, promocionCategoria.getIdPromocion());
            ps.setInt(2, promocionCategoria.getIdCategoria());
            ps.setBoolean(3, promocionCategoria.isAplicaMayorista());
            ps.setBoolean(4, promocionCategoria.isAplicaMinorista());

            ps.executeUpdate();
        }
    }

    public String[] readByIds(int idPromocion, int idCategoria) throws SQLException {
        String sql = "SELECT * FROM promocion_categoria WHERE id_promocion = ? AND id_categoria = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPromocionCategoria(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM promocion_categoria ORDER BY id_promocion, id_categoria";

        List<String[]> promocionesCategoria = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                promocionesCategoria.add(mapResultSetToPromocionCategoria(rs));
            }
        }

        return promocionesCategoria;
    }

    public void update(PromocionCategoria promocionCategoria) throws SQLException {
        String sql = "UPDATE promocion_categoria SET " +
                "aplica_mayorista = ?, " +
                "aplica_minorista = ? " +
                "WHERE id_promocion = ? AND id_categoria = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, promocionCategoria.isAplicaMayorista());
            ps.setBoolean(2, promocionCategoria.isAplicaMinorista());
            ps.setInt(3, promocionCategoria.getIdPromocion());
            ps.setInt(4, promocionCategoria.getIdCategoria());

            ps.executeUpdate();
        }
    }

    public void delete(int idPromocion, int idCategoria) throws SQLException {
        String sql = "DELETE FROM promocion_categoria WHERE id_promocion = ? AND id_categoria = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idCategoria);

            ps.executeUpdate();
        }
    }

    public boolean existsByIds(int idPromocion, int idCategoria) throws SQLException {
        String sql = "SELECT 1 FROM promocion_categoria WHERE id_promocion = ? AND id_categoria = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPromocion);
            ps.setInt(2, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToPromocionCategoria(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_promocion")),
                String.valueOf(rs.getInt("id_categoria")),
                String.valueOf(rs.getBoolean("aplica_mayorista")),
                String.valueOf(rs.getBoolean("aplica_minorista"))
        };
    }
}