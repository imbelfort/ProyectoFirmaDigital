package DAO;

import Database.Conexion;
import Models.Promocion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO {

    public void create(Promocion promocion) throws SQLException {
        String sql = "INSERT INTO promocion (" +
                "id_propietario, nombre, descripcion, porcentaje_descuento, fecha_inicio, fecha_fin, estado, fecha_creacion, fecha_modificacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, promocion.getIdPropietario());
            ps.setString(2, promocion.getNombre());
            ps.setString(3, promocion.getDescripcion());
            ps.setBigDecimal(4, promocion.getPorcentajeDescuento());
            ps.setDate(5, promocion.getFechaInicio());
            ps.setDate(6, promocion.getFechaFin());
            ps.setString(7, promocion.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    promocion.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM promocion WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPromocion(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM promocion ORDER BY id";

        List<String[]> promociones = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                promociones.add(mapResultSetToPromocion(rs));
            }
        }

        return promociones;
    }

    public void update(Promocion promocion) throws SQLException {
        String sql = "UPDATE promocion SET " +
                "id_propietario = ?, " +
                "nombre = ?, " +
                "descripcion = ?, " +
                "porcentaje_descuento = ?, " +
                "fecha_inicio = ?, " +
                "fecha_fin = ?, " +
                "estado = ?, " +
                "fecha_modificacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, promocion.getIdPropietario());
            ps.setString(2, promocion.getNombre());
            ps.setString(3, promocion.getDescripcion());
            ps.setBigDecimal(4, promocion.getPorcentajeDescuento());
            ps.setDate(5, promocion.getFechaInicio());
            ps.setDate(6, promocion.getFechaFin());
            ps.setString(7, promocion.getEstado());
            ps.setInt(8, promocion.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM promocion WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM promocion WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToPromocion(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_propietario")),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("porcentaje_descuento") != null
                        ? rs.getBigDecimal("porcentaje_descuento").toString()
                        : "",
                rs.getDate("fecha_inicio") != null
                        ? rs.getDate("fecha_inicio").toString()
                        : "",
                rs.getDate("fecha_fin") != null
                        ? rs.getDate("fecha_fin").toString()
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
}