package DAO;

import Database.Conexion;
import Models.GuiaProcesal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuiaDAO {

    public void create(GuiaProcesal guia) throws SQLException {
        String sql = "INSERT INTO guia_procesal (id_tipo_tramite, paso_numero, nombre_etapa, descripcion_instrucciones, rol_responsable, tiempo_estimado_horas) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, guia.getIdTipoTramite());
            ps.setInt(2, guia.getPasoNumero());
            ps.setString(3, guia.getNombreEtapa());
            ps.setString(4, guia.getDescripcionInstrucciones());
            ps.setString(5, guia.getRolResponsable());
            ps.setInt(6, guia.getTiempoEstimadoHoras());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) guia.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT g.id, tt.nombre AS tipo_tramite, g.paso_numero, g.nombre_etapa, " +
                     "g.descripcion_instrucciones, g.rol_responsable, g.tiempo_estimado_horas " +
                     "FROM guia_procesal g " +
                     "JOIN tipo_tramite tt ON g.id_tipo_tramite = tt.id " +
                     "ORDER BY g.id_tipo_tramite, g.paso_numero ASC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("tipo_tramite"),
                        String.valueOf(rs.getInt("paso_numero")),
                        rs.getString("nombre_etapa"),
                        rs.getString("descripcion_instrucciones"),
                        rs.getString("rol_responsable"),
                        String.valueOf(rs.getInt("tiempo_estimado_horas")) + "h"
                });
            }
        }
        return lista;
    }

    public List<String[]> listByTipoTramite(int idTipoTramite) throws SQLException {
        String sql = "SELECT g.id, tt.nombre AS tipo_tramite, g.paso_numero, g.nombre_etapa, " +
                     "g.descripcion_instrucciones, g.rol_responsable, g.tiempo_estimado_horas " +
                     "FROM guia_procesal g " +
                     "JOIN tipo_tramite tt ON g.id_tipo_tramite = tt.id " +
                     "WHERE g.id_tipo_tramite = ? " +
                     "ORDER BY g.paso_numero ASC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTipoTramite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("tipo_tramite"),
                            String.valueOf(rs.getInt("paso_numero")),
                            rs.getString("nombre_etapa"),
                            rs.getString("descripcion_instrucciones"),
                            rs.getString("rol_responsable"),
                            String.valueOf(rs.getInt("tiempo_estimado_horas")) + "h"
                    });
                }
            }
        }
        return lista;
    }
}
