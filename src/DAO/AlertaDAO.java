package DAO;

import Database.Conexion;
import Models.AlertaSLA;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    public void create(AlertaSLA alerta) throws SQLException {
        String sql = "INSERT INTO alerta_sla (id_tramite, tipo_alerta, mensaje, notificado_a, atendida) " +
                     "VALUES (?, ?, ?, ?, FALSE)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alerta.getIdTramite());
            ps.setString(2, alerta.getTipoAlerta());
            ps.setString(3, alerta.getMensaje());
            ps.setString(4, alerta.getNotificadoA() != null ? alerta.getNotificadoA() : "ENCARGADO_DESPACHO");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) alerta.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT a.id, t.codigo_tramite, a.tipo_alerta, a.mensaje, a.notificado_a, " +
                     "a.fecha_generacion, CASE WHEN a.atendida THEN 'ATENDIDA' ELSE 'PENDIENTE' END AS estado_alerta " +
                     "FROM alerta_sla a " +
                     "JOIN tramite t ON a.id_tramite = t.id " +
                     "ORDER BY a.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("tipo_alerta"),
                        rs.getString("mensaje"),
                        rs.getString("notificado_a"),
                        rs.getTimestamp("fecha_generacion").toString(),
                        rs.getString("estado_alerta")
                });
            }
        }
        return lista;
    }

    public void marcarAtendida(int idAlerta) throws SQLException {
        String sql = "UPDATE alerta_sla SET atendida = TRUE WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAlerta);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró alerta con ID: " + idAlerta);
            }
        }
    }
}
