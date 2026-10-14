package DAO;

import Database.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<String[]> reporteTramitesPorEstado() throws SQLException {
        String sql = "SELECT estado, COUNT(*) AS total, ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM tramite), 2) AS porcentaje " +
                     "FROM tramite GROUP BY estado ORDER BY total DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("estado"),
                        String.valueOf(rs.getInt("total")),
                        String.valueOf(rs.getDouble("porcentaje")) + "%"
                });
            }
        }
        return lista;
    }

    public List<String[]> reporteCumplimientoSla() throws SQLException {
        String sql = "SELECT estado_sla, COUNT(*) AS cantidad, " +
                     "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM monitoreo_sla), 2) AS tasa_porcentaje " +
                     "FROM monitoreo_sla GROUP BY estado_sla ORDER BY cantidad DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("estado_sla"),
                        String.valueOf(rs.getInt("cantidad")),
                        String.valueOf(rs.getDouble("tasa_porcentaje")) + "%"
                });
            }
        }
        return lista;
    }

    public List<String[]> reporteFirmasPorVicerrector() throws SQLException {
        String sql = "SELECT u.nombre || ' ' || u.apellidos AS firmante, f.tipo_firma, COUNT(*) AS total_firmados " +
                     "FROM firma_digital f " +
                     "JOIN usuario u ON f.id_firmante = u.id_usuario " +
                     "GROUP BY u.nombre, u.apellidos, f.tipo_firma ORDER BY total_firmados DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("firmante"),
                        rs.getString("tipo_firma"),
                        String.valueOf(rs.getInt("total_firmados"))
                });
            }
        }
        return lista;
    }
}