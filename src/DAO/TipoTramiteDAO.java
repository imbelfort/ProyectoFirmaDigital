package DAO;

import Database.Conexion;
import Models.TipoTramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoTramiteDAO {

    public void create(TipoTramite tipo) throws SQLException {
        String sql = "INSERT INTO tipo_tramite (codigo, nombre, descripcion, tiempo_sla_horas, costo_bs, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.getCodigo());
            ps.setString(2, tipo.getNombre());
            ps.setString(3, tipo.getDescripcion());
            ps.setInt(4, tipo.getTiempoSlaHoras());
            ps.setDouble(5, tipo.getCostoBs());
            ps.setString(6, tipo.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) tipo.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT id, codigo, nombre, descripcion, tiempo_sla_horas, costo_bs, estado " +
                     "FROM tipo_tramite ORDER BY id ASC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        String.valueOf(rs.getInt("tiempo_sla_horas")),
                        String.valueOf(rs.getDouble("costo_bs")),
                        rs.getString("estado")
                });
            }
        }
        return lista;
    }
}
