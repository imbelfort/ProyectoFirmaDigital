package DAO;

import Database.Conexion;
import Models.ObservacionTramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObservacionDAO {

    public void create(ObservacionTramite obs) throws SQLException {
        String sql = "INSERT INTO observacion_tramite (id_tramite, id_usuario, tipo_observacion, descripcion, estado) " +
                     "VALUES (?, ?, ?, ?, 'ABIERTA')";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, obs.getIdTramite());
            ps.setInt(2, obs.getIdUsuario());
            ps.setString(3, obs.getTipoObservacion());
            ps.setString(4, obs.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) obs.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT o.id, t.codigo_tramite, u.nombre || ' ' || u.apellidos AS observador, " +
                     "o.tipo_observacion, o.descripcion, o.estado, o.fecha_observacion, o.fecha_subsanacion " +
                     "FROM observacion_tramite o " +
                     "JOIN tramite t ON o.id_tramite = t.id " +
                     "JOIN usuario u ON o.id_usuario = u.id_usuario " +
                     "ORDER BY o.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("observador"),
                        rs.getString("tipo_observacion"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_observacion").toString(),
                        rs.getTimestamp("fecha_subsanacion") != null ? rs.getTimestamp("fecha_subsanacion").toString() : "-"
                });
            }
        }
        return lista;
    }

    public void subsanar(int idObservacion, String detalle) throws SQLException {
        String sql = "UPDATE observacion_tramite SET estado = 'SUBSANADA', fecha_subsanacion = CURRENT_TIMESTAMP, " +
                     "descripcion = descripcion || ' | Subsanacion: ' || ? " +
                     "WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, detalle != null ? detalle : "Subsanado por el solicitante");
            ps.setInt(2, idObservacion);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró observación con ID: " + idObservacion);
            }
        }
    }
}
