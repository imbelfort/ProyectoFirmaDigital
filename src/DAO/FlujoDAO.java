package DAO;

import Database.Conexion;
import Models.FlujoTramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlujoDAO {

    public void avanzarEtapa(int idTramite, String etapa, int idResponsable) throws SQLException {
        String updateAnterior = "UPDATE flujo_tramite SET fecha_fin_etapa = CURRENT_TIMESTAMP, estado_etapa = 'COMPLETADO' " +
                                "WHERE id_tramite = ? AND estado_etapa = 'EN_PROCESO'";
        String insertNuevo = "INSERT INTO flujo_tramite (id_tramite, etapa_actual, id_usuario_responsable, estado_etapa) VALUES (?, ?, ?, 'EN_PROCESO')";
        String updateTramite = "UPDATE tramite SET estado = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps1 = con.prepareStatement(updateAnterior)) {
                    ps1.setInt(1, idTramite);
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = con.prepareStatement(insertNuevo)) {
                    ps2.setInt(1, idTramite);
                    ps2.setString(2, etapa);
                    ps2.setInt(3, idResponsable);
                    ps2.executeUpdate();
                }
                try (PreparedStatement ps3 = con.prepareStatement(updateTramite)) {
                    ps3.setString(1, etapa);
                    ps3.setInt(2, idTramite);
                    ps3.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT f.id, t.codigo_tramite, f.etapa_actual, u.nombre || ' ' || u.apellidos AS responsable, " +
                     "f.fecha_inicio_etapa, f.fecha_fin_etapa, f.estado_etapa " +
                     "FROM flujo_tramite f " +
                     "JOIN tramite t ON f.id_tramite = t.id " +
                     "LEFT JOIN usuario u ON f.id_usuario_responsable = u.id_usuario " +
                     "ORDER BY f.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("etapa_actual"),
                        rs.getString("responsable") != null ? rs.getString("responsable") : "SIN ASIGNAR",
                        rs.getTimestamp("fecha_inicio_etapa").toString(),
                        rs.getTimestamp("fecha_fin_etapa") != null ? rs.getTimestamp("fecha_fin_etapa").toString() : "-",
                        rs.getString("estado_etapa")
                });
            }
        }
        return lista;
    }
}
