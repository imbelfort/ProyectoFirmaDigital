package DAO;

import Database.Conexion;
import Models.BitacoraTramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {

    public void create(BitacoraTramite b) throws SQLException {
        String sql = "INSERT INTO bitacora_tramite (id_tramite, id_usuario, accion, detalle, ip_origen) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, b.getIdTramite());
            if (b.getIdUsuario() != null) {
                ps.setInt(2, b.getIdUsuario());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, b.getAccion());
            ps.setString(4, b.getDetalle());
            ps.setString(5, b.getIpOrigen() != null ? b.getIpOrigen() : "127.0.0.1");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) b.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT b.id, t.codigo_tramite, COALESCE(u.nombre || ' ' || u.apellidos, 'SISTEMA') AS usuario, " +
                     "b.accion, b.detalle, b.ip_origen, b.fecha_evento " +
                     "FROM bitacora_tramite b " +
                     "JOIN tramite t ON b.id_tramite = t.id " +
                     "LEFT JOIN usuario u ON b.id_usuario = u.id_usuario " +
                     "ORDER BY b.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("usuario"),
                        rs.getString("accion"),
                        rs.getString("detalle"),
                        rs.getString("ip_origen"),
                        rs.getTimestamp("fecha_evento").toString()
                });
            }
        }
        return lista;
    }

    public List<String[]> listByTramite(int idTramite) throws SQLException {
        String sql = "SELECT b.id, t.codigo_tramite, COALESCE(u.nombre || ' ' || u.apellidos, 'SISTEMA') AS usuario, " +
                     "b.accion, b.detalle, b.ip_origen, b.fecha_evento " +
                     "FROM bitacora_tramite b " +
                     "JOIN tramite t ON b.id_tramite = t.id " +
                     "LEFT JOIN usuario u ON b.id_usuario = u.id_usuario " +
                     "WHERE b.id_tramite = ? " +
                     "ORDER BY b.id ASC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTramite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("codigo_tramite"),
                            rs.getString("usuario"),
                            rs.getString("accion"),
                            rs.getString("detalle"),
                            rs.getString("ip_origen"),
                            rs.getTimestamp("fecha_evento").toString()
                    });
                }
            }
        }
        return lista;
    }
}
