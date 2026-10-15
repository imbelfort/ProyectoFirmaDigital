package DAO;

import Database.Conexion;
import Models.MonitoreoSLA;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlaDAO {

    public void create(MonitoreoSLA sla) throws SQLException {
        String sql = "INSERT INTO monitoreo_sla (id_tramite, horas_sla_limite, fecha_limite_sla, horas_transcurridas, estado_sla) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sla.getIdTramite());
            ps.setInt(2, sla.getHorasSlaLimite());
            ps.setTimestamp(3, sla.getFechaLimiteSla());
            ps.setDouble(4, sla.getHorasTranscurridas());
            ps.setString(5, sla.getEstadoSla());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) sla.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT m.id, t.codigo_tramite, m.horas_sla_limite, m.horas_transcurridas, m.estado_sla, " +
                     "m.fecha_inicio_sla, m.fecha_limite_sla, m.alertas_emitidas " +
                     "FROM monitoreo_sla m " +
                     "JOIN tramite t ON m.id_tramite = t.id " +
                     "ORDER BY m.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        String.valueOf(rs.getInt("horas_sla_limite")) + "h",
                        String.valueOf(rs.getDouble("horas_transcurridas")) + "h",
                        rs.getString("estado_sla"),
                        rs.getTimestamp("fecha_inicio_sla").toString(),
                        rs.getTimestamp("fecha_limite_sla").toString(),
                        String.valueOf(rs.getInt("alertas_emitidas"))
                });
            }
        }
        return lista;
    }

    public void verificarYActualizarSla(int idTramite) throws SQLException {
        String sql = "UPDATE monitoreo_sla SET " +
                     "horas_transcurridas = ROUND(CAST(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - fecha_inicio_sla)) / 3600 AS numeric), 2), " +
                     "estado_sla = CASE " +
                     "  WHEN CURRENT_TIMESTAMP > fecha_limite_sla THEN 'VENCIDO' " +
                     "  WHEN fecha_limite_sla - CURRENT_TIMESTAMP <= INTERVAL '24 hours' THEN 'POR_VENCER' " +
                     "  ELSE 'EN_TIEMPO' END " +
                     "WHERE id_tramite = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTramite);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se encontró monitoreo SLA para el trámite con ID: " + idTramite + ". Verifique con 'sla listar'.");
            }
        }
    }
}
