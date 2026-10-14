package DAO;

import Database.Conexion;
import Models.CertificadoOficial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificadoDAO {

    public void create(CertificadoOficial cert) throws SQLException {
        String sql = "INSERT INTO certificado_oficial (id_tramite, id_firma_digital, codigo_verificacion, qr_data, titulo_documento, portal_url, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cert.getIdTramite());
            ps.setInt(2, cert.getIdFirmaDigital());
            ps.setString(3, cert.getCodigoVerificacion());
            ps.setString(4, cert.getQrData());
            ps.setString(5, cert.getTituloDocumento());
            ps.setString(6, cert.getPortalUrl());
            ps.setString(7, cert.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) cert.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT c.id, t.codigo_tramite, c.codigo_verificacion, c.titulo_documento, c.portal_url, c.fecha_emision, c.estado " +
                     "FROM certificado_oficial c " +
                     "JOIN tramite t ON c.id_tramite = t.id " +
                     "ORDER BY c.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("codigo_verificacion"),
                        rs.getString("titulo_documento"),
                        rs.getString("portal_url"),
                        rs.getTimestamp("fecha_emision").toString(),
                        rs.getString("estado")
                });
            }
        }
        return lista;
    }

    public String[] consultarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT c.id, t.codigo_tramite, u.registro, u.nombre || ' ' || u.apellidos AS interesado, " +
                     "c.codigo_verificacion, c.titulo_documento, c.fecha_emision, c.portal_url, c.estado " +
                     "FROM certificado_oficial c " +
                     "JOIN tramite t ON c.id_tramite = t.id " +
                     "JOIN usuario u ON t.id_usuario_solicitante = u.id_usuario " +
                     "WHERE c.codigo_verificacion = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("codigo_tramite"),
                            rs.getString("registro"),
                            rs.getString("interesado"),
                            rs.getString("codigo_verificacion"),
                            rs.getString("titulo_documento"),
                            rs.getTimestamp("fecha_emision").toString(),
                            rs.getString("portal_url"),
                            rs.getString("estado")
                    };
                }
            }
        }
        return null;
    }
}
