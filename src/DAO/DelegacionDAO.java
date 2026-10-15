package DAO;

import Database.Conexion;
import Models.Delegacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DelegacionDAO {

    public void create(Delegacion delegacion) throws SQLException {
        String sql = "INSERT INTO delegacion_autoridad (id_autoridad_titular, id_autoridad_delegada, resolucion, motivo, fecha_inicio, fecha_fin, alcance_firma, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, delegacion.getIdAutoridadTitular());
            ps.setInt(2, delegacion.getIdAutoridadDelegada());
            ps.setString(3, delegacion.getResolucion());
            ps.setString(4, delegacion.getMotivo());
            ps.setDate(5, delegacion.getFechaInicio());
            ps.setDate(6, delegacion.getFechaFin());
            ps.setString(7, delegacion.getAlcanceFirma());
            ps.setString(8, delegacion.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) delegacion.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT d.id, u1.nombre || ' ' || u1.apellidos AS titular, u2.nombre || ' ' || u2.apellidos AS delegado, " +
                     "d.resolucion, d.motivo, d.fecha_inicio, d.fecha_fin, d.alcance_firma, d.estado " +
                     "FROM delegacion_autoridad d " +
                     "JOIN usuario u1 ON d.id_autoridad_titular = u1.id_usuario " +
                     "JOIN usuario u2 ON d.id_autoridad_delegada = u2.id_usuario " +
                     "ORDER BY d.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("titular"),
                        rs.getString("delegado"),
                        rs.getString("resolucion"),
                        rs.getString("motivo"),
                        rs.getDate("fecha_inicio").toString(),
                        rs.getDate("fecha_fin").toString(),
                        rs.getString("alcance_firma"),
                        rs.getString("estado")
                });
            }
        }
        return lista;
    }

    public void update(Delegacion delegacion) throws SQLException {
        String sql = "UPDATE delegacion_autoridad SET resolucion = ?, motivo = ?, fecha_inicio = ?, fecha_fin = ?, alcance_firma = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, delegacion.getResolucion());
            ps.setString(2, delegacion.getMotivo());
            ps.setDate(3, delegacion.getFechaInicio());
            ps.setDate(4, delegacion.getFechaFin());
            ps.setString(5, delegacion.getAlcanceFirma());
            ps.setString(6, delegacion.getEstado());
            ps.setInt(7, delegacion.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró ninguna delegación con el ID: " + delegacion.getId() + ". Verifique los IDs con 'delegacion listar'.");
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM delegacion_autoridad WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró ninguna delegación con el ID: " + id + ". Verifique los IDs con 'delegacion listar'.");
            }
        }
    }
}
