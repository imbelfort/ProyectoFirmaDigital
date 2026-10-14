package DAO;

import Database.Conexion;
import Models.Indicador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndicadorDAO {

    public void create(Indicador indicador) throws SQLException {
        String sql = "INSERT INTO indicador_gestion (codigo, nombre, descripcion, meta_esperada, valor_actual, unidad_medida, periodo, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, indicador.getCodigo());
            ps.setString(2, indicador.getNombre());
            ps.setString(3, indicador.getDescripcion());
            ps.setDouble(4, indicador.getMetaEsperada());
            ps.setDouble(5, indicador.getValorActual());
            ps.setString(6, indicador.getUnidadMedida());
            ps.setString(7, indicador.getPeriodo());
            ps.setString(8, indicador.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) indicador.setId(rs.getInt(1));
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT id, codigo, nombre, meta_esperada, valor_actual, unidad_medida, periodo, estado " +
                     "FROM indicador_gestion ORDER BY id ASC";
        List<String[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        String.valueOf(rs.getDouble("meta_esperada")),
                        String.valueOf(rs.getDouble("valor_actual")),
                        rs.getString("unidad_medida"),
                        rs.getString("periodo"),
                        rs.getString("estado")
                });
            }
        }
        return lista;
    }

    public void update(int id, double meta, double actual, String estado) throws SQLException {
        String sql = "UPDATE indicador_gestion SET meta_esperada = ?, valor_actual = ?, estado = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, meta);
            ps.setDouble(2, actual);
            ps.setString(3, estado);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }
}
