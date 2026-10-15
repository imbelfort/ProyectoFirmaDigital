package DAO;

import Database.Conexion;
import Models.Tramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TramiteDAO {

    public void create(Tramite tramite) throws SQLException {
        String sql = "INSERT INTO tramite (codigo_tramite, id_usuario_solicitante, id_tipo_tramite, asunto, estado, prioridad) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tramite.getCodigoTramite());
            ps.setInt(2, tramite.getIdUsuarioSolicitante());
            ps.setInt(3, tramite.getIdTipoTramite());
            ps.setString(4, tramite.getAsunto());
            ps.setString(5, tramite.getEstado());
            ps.setString(6, tramite.getPrioridad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) tramite.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT t.id, t.codigo_tramite, u.registro, u.nombre || ' ' || u.apellidos AS solicitante, " +
                     "tt.nombre AS tipo_tramite, t.asunto, t.estado, t.prioridad, t.fecha_solicitud " +
                     "FROM tramite t " +
                     "JOIN usuario u ON t.id_usuario_solicitante = u.id_usuario " +
                     "JOIN tipo_tramite tt ON t.id_tipo_tramite = tt.id " +
                     "ORDER BY t.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("registro"),
                        rs.getString("solicitante"),
                        rs.getString("tipo_tramite"),
                        rs.getString("asunto"),
                        rs.getString("estado"),
                        rs.getString("prioridad"),
                        rs.getTimestamp("fecha_solicitud").toString()
                });
            }
        }
        return lista;
    }

    public void update(int id, String asunto, String prioridad, String estado) throws SQLException {
        String sql = "UPDATE tramite SET asunto = ?, prioridad = ?, estado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, asunto);
            ps.setString(2, prioridad);
            ps.setString(3, estado);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tramite WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM tramite WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void updateEstado(int id, String estado) throws SQLException {
        String sql = "UPDATE tramite SET estado = ? WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void updateEstadoFinalizado(int id, String estado) throws SQLException {
        String sql = "UPDATE tramite SET estado = ?, fecha_finalizacion = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
