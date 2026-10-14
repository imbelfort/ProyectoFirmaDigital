package DAO;

import Database.Conexion;
import Models.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void create(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (" +
                "id_propietario, nombre, descripcion, imagen, fecha_creacion, fecha_modificacion" +
                ") VALUES (?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, categoria.getIdPropietario());
            ps.setString(2, categoria.getNombre());
            ps.setString(3, categoria.getDescripcion());
            ps.setString(4, categoria.getImagen());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategoria(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM categoria ORDER BY id";

        List<String[]> categorias = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(mapResultSetToCategoria(rs));
            }
        }

        return categorias;
    }

    public void update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET " +
                "id_propietario = ?, " +
                "nombre = ?, " +
                "descripcion = ?, " +
                "imagen = ?, " +
                "fecha_modificacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoria.getIdPropietario());
            ps.setString(2, categoria.getNombre());
            ps.setString(3, categoria.getDescripcion());
            ps.setString(4, categoria.getImagen());
            ps.setInt(5, categoria.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM categoria WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToCategoria(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_propietario")),
                rs.getString("nombre") != null ? rs.getString("nombre") : "",
                rs.getString("descripcion") != null ? rs.getString("descripcion") : "",
                rs.getString("imagen") != null ? rs.getString("imagen") : "",
                rs.getDate("fecha_creacion") != null ? rs.getDate("fecha_creacion").toString() : "",
                rs.getDate("fecha_modificacion") != null ? rs.getDate("fecha_modificacion").toString() : ""
        };
    }
}