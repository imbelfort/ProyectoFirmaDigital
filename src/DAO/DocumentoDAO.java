package DAO;

import Database.Conexion;
import Models.DocumentoExpediente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {

    public void create(DocumentoExpediente doc) throws SQLException {
        String sql = "INSERT INTO documento_expediente (id_tramite, id_requisito, nombre_documento, tipo_documento, archivo_url, hash_documento, estado_validacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doc.getIdTramite());
            if (doc.getIdRequisito() > 0) ps.setInt(2, doc.getIdRequisito());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, doc.getNombreDocumento());
            ps.setString(4, doc.getTipoDocumento());
            ps.setString(5, doc.getArchivoUrl());
            ps.setString(6, doc.getHashDocumento());
            ps.setString(7, doc.getEstadoValidacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) doc.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT d.id, t.codigo_tramite, d.nombre_documento, d.tipo_documento, d.archivo_url, d.estado_validacion, d.fecha_validacion " +
                     "FROM documento_expediente d " +
                     "JOIN tramite t ON d.id_tramite = t.id " +
                     "ORDER BY d.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("nombre_documento"),
                        rs.getString("tipo_documento"),
                        rs.getString("archivo_url") != null ? rs.getString("archivo_url") : "",
                        rs.getString("estado_validacion"),
                        rs.getTimestamp("fecha_validacion") != null ? rs.getTimestamp("fecha_validacion").toString() : "PENDIENTE"
                });
            }
        }
        return lista;
    }

    public void validarDocumento(int id, String estado, String observacion) throws SQLException {
        String sql = "UPDATE documento_expediente SET estado_validacion = ?, observacion_validacion = ?, fecha_validacion = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, observacion);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }
}
