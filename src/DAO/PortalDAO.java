package DAO;

import Database.Conexion;
import Models.PortalSeguimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortalDAO {

    public void create(PortalSeguimiento portal) throws SQLException {
        String sql = "INSERT INTO portal_seguimiento (id_tramite, pin_seguimiento, portal_activo, total_visitas) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, portal.getIdTramite());
            ps.setString(2, portal.getPinSeguimiento());
            ps.setBoolean(3, portal.isPortalActivo());
            ps.setInt(4, portal.getTotalVisitas());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) portal.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT p.id, t.codigo_tramite, u.registro, u.nombre || ' ' || u.apellidos AS estudiante, " +
                     "tt.nombre AS tramite, t.asunto, t.estado, t.prioridad, t.fecha_solicitud, p.total_visitas " +
                     "FROM portal_seguimiento p " +
                     "JOIN tramite t ON p.id_tramite = t.id " +
                     "JOIN usuario u ON t.id_usuario_solicitante = u.id_usuario " +
                     "JOIN tipo_tramite tt ON t.id_tipo_tramite = tt.id " +
                     "ORDER BY p.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("registro"),
                        rs.getString("estudiante"),
                        rs.getString("tramite"),
                        rs.getString("asunto"),
                        rs.getString("estado"),
                        rs.getString("prioridad"),
                        rs.getTimestamp("fecha_solicitud").toString(),
                        String.valueOf(rs.getInt("total_visitas"))
                });
            }
        }
        return lista;
    }

    public String[] consultarPorPin(String pin) throws SQLException {
        String sql = "SELECT p.id, t.codigo_tramite, u.registro, u.nombre || ' ' || u.apellidos AS estudiante, " +
                     "tt.nombre AS tramite, t.asunto, t.estado, t.prioridad, t.fecha_solicitud, p.total_visitas " +
                     "FROM portal_seguimiento p " +
                     "JOIN tramite t ON p.id_tramite = t.id " +
                     "JOIN usuario u ON t.id_usuario_solicitante = u.id_usuario " +
                     "JOIN tipo_tramite tt ON t.id_tipo_tramite = tt.id " +
                     "WHERE p.pin_seguimiento = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("codigo_tramite"),
                            rs.getString("registro"),
                            rs.getString("estudiante"),
                            rs.getString("tramite"),
                            rs.getString("asunto"),
                            rs.getString("estado"),
                            rs.getString("prioridad"),
                            rs.getTimestamp("fecha_solicitud").toString(),
                            String.valueOf(rs.getInt("total_visitas") + 1)
                    };
                }
            }
        }
        return null;
    }
}
