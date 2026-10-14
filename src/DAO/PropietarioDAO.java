package DAO;

import Database.Conexion;
import Models.Propietario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropietarioDAO {

    public void create(Propietario propietario) throws SQLException {
        String sql = "INSERT INTO PROPIETARIO (id_usuario, nivel_acceso) VALUES (?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, propietario.getIdUsuario());
            ps.setString(2, propietario.getNivelAcceso());
            ps.executeUpdate();
        }
    }

    public Propietario readById(int id) throws SQLException {
        String sql = "SELECT * FROM PROPIETARIO WHERE id_propietario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Propietario(
                        rs.getInt("id_propietario"),
                        rs.getInt("id_usuario"),
                        rs.getString("nivel_acceso")
                    );
                }
            }
        }
        return null;
    }

    public Propietario readByUsuarioId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM PROPIETARIO WHERE id_usuario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Propietario(
                        rs.getInt("id_propietario"),
                        rs.getInt("id_usuario"),
                        rs.getString("nivel_acceso")
                    );
                }
            }
        }
        return null;
    }

    public List<Propietario> listAll() throws SQLException {
        List<Propietario> propietarios = new ArrayList<>();
        String sql = "SELECT * FROM PROPIETARIO";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                propietarios.add(new Propietario(
                    rs.getInt("id_propietario"),
                    rs.getInt("id_usuario"),
                    rs.getString("nivel_acceso")
                ));
            }
        }
        return propietarios;
    }

    public void update(Propietario propietario) throws SQLException {
        String sql = "UPDATE PROPIETARIO SET id_usuario = ?, nivel_acceso = ? WHERE id_propietario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, propietario.getIdUsuario());
            ps.setString(2, propietario.getNivelAcceso());
            ps.setInt(3, propietario.getIdPropietario());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM PROPIETARIO WHERE id_propietario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
