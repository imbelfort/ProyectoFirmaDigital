package DAO;

import Database.Conexion;
import Models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void create(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario " +
                "(registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // REGLA: Si la contraseña no está definida, por defecto es igual al CI
            String pass = (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty())
                    ? usuario.getContrasena()
                    : usuario.getCi();

            ps.setString(1, usuario.getRegistro());
            ps.setString(2, usuario.getCi());
            ps.setString(3, pass);
            ps.setString(4, usuario.getNombre());
            ps.setString(5, usuario.getApellidos());
            ps.setString(6, usuario.getEmail());
            ps.setString(7, usuario.getTelefono());
            ps.setString(8, usuario.getRol());
            ps.setString(9, usuario.getUnidadAcademica());
            ps.setString(10, usuario.getCargo());
            ps.setString(11, usuario.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT id_usuario, registro, ci, nombre, apellidos, email, telefono, rol, unidad_academica, cargo, estado " +
                "FROM usuario WHERE id_usuario = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT id_usuario, registro, ci, nombre, apellidos, email, telefono, rol, unidad_academica, cargo, estado " +
                "FROM usuario ORDER BY id_usuario";

        List<String[]> usuarios = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }

        return usuarios;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET " +
                "registro = ?, " +
                "ci = ?, " +
                "nombre = ?, " +
                "apellidos = ?, " +
                "email = ?, " +
                "telefono = ?, " +
                "rol = ?, " +
                "unidad_academica = ?, " +
                "cargo = ?, " +
                "estado = ?, " +
                "fecha_modificacion = CURRENT_TIMESTAMP " +
                "WHERE id_usuario = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getRegistro());
            ps.setString(2, usuario.getCi());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getTelefono());
            ps.setString(7, usuario.getRol());
            ps.setString(8, usuario.getUnidadAcademica());
            ps.setString(9, usuario.getCargo());
            ps.setString(10, usuario.getEstado());
            ps.setInt(11, usuario.getIdUsuario());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE id_usuario = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByCi(String ci) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE ci = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ci);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByRegistro(String registro) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE registro = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, registro);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_usuario")),
                rs.getString("registro") != null ? rs.getString("registro") : "",
                rs.getString("ci") != null ? rs.getString("ci") : "",
                rs.getString("nombre") != null ? rs.getString("nombre") : "",
                rs.getString("apellidos") != null ? rs.getString("apellidos") : "",
                rs.getString("email") != null ? rs.getString("email") : "",
                rs.getString("telefono") != null ? rs.getString("telefono") : "",
                rs.getString("rol") != null ? rs.getString("rol") : "",
                rs.getString("unidad_academica") != null ? rs.getString("unidad_academica") : "",
                rs.getString("cargo") != null ? rs.getString("cargo") : "",
                rs.getString("estado") != null ? rs.getString("estado") : ""
        };
    }
}