package DAO;

import Database.Conexion;
import Models.DespachoLegajo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DespachoDAO {

    public void create(DespachoLegajo despacho) throws SQLException {
        String sql = "INSERT INTO despacho_legajo (id_tramite, id_usuario_despachador, numero_despacho, legajo_previsualizado_url, tipo_entrega, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, despacho.getIdTramite());
            ps.setInt(2, despacho.getIdUsuarioDespachador());
            ps.setString(3, despacho.getNumeroDespacho());
            ps.setString(4, despacho.getLegajoPrevisualizadoUrl());
            ps.setString(5, despacho.getTipoEntrega());
            ps.setString(6, despacho.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) despacho.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT d.id, t.codigo_tramite, u.nombre || ' ' || u.apellidos AS despachador, " +
                     "d.numero_despacho, d.tipo_entrega, d.fecha_despacho, d.estado " +
                     "FROM despacho_legajo d " +
                     "JOIN tramite t ON d.id_tramite = t.id " +
                     "JOIN usuario u ON d.id_usuario_despachador = u.id_usuario " +
                     "ORDER BY d.id DESC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo_tramite"),
                        rs.getString("despachador"),
                        rs.getString("numero_despacho"),
                        rs.getString("tipo_entrega"),
                        rs.getTimestamp("fecha_despacho").toString(),
                        rs.getString("estado")
                });
            }
        }
        return lista;
    }
}
