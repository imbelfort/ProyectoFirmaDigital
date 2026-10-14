package DAO;

import Database.Conexion;
import Models.FirmaDigital;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FirmaDAO {

    public void create(FirmaDigital firma) throws SQLException {
        String sql = "INSERT INTO firma_digital (id_tramite, id_firmante, tipo_firma, algoritmo, hash_documento, firma_digital_valor, numero_certificado_digital, valido) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, firma.getIdTramite());
            ps.setInt(2, firma.getIdFirmante());
            ps.setString(3, firma.getTipoFirma());
            ps.setString(4, firma.getAlgoritmo());
            ps.setString(5, firma.getHashDocumento());
            ps.setString(6, firma.getFirmaDigitalValor());
            ps.setString(7, firma.getNumeroCertificadoDigital());
            ps.setBoolean(8, firma.isValido());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) firma.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT f.id, t.codigo_tramite, u.nombre || ' ' || u.apellidos AS firmante, " +
                     "f.tipo_firma, f.algoritmo, f.numero_certificado_digital, f.fecha_firma, f.valido " +
                     "FROM firma_digital f " +
                     "JOIN tramite t ON f.id_tramite = t.id " +
                     "JOIN usuario u ON f.id_firmante = u.id_usuario " +
                     "ORDER BY f.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("firmante"),
                        rs.getString("tipo_firma"),
                        rs.getString("algoritmo"),
                        rs.getString("numero_certificado_digital"),
                        rs.getTimestamp("fecha_firma").toString(),
                        rs.getBoolean("valido") ? "VALIDA" : "INVALIDA"
                });
            }
        }
        return lista;
    }
}
