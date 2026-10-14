package DAO;

import Database.Conexion;
import Models.Carrito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoDAO {

    public void create(Carrito carrito) throws SQLException {
        String sql = "INSERT INTO carrito (" +
                "id_cliente, fecha_creacion, fecha_actualizacion" +
                ") VALUES (?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, carrito.getIdCliente());

            if (carrito.getFechaCreacion() != null) {
                ps.setDate(2, carrito.getFechaCreacion());
            } else {
                ps.setDate(2, new Date(System.currentTimeMillis()));
            }

            if (carrito.getFechaActualizacion() != null) {
                ps.setDate(3, carrito.getFechaActualizacion());
            } else {
                ps.setDate(3, new Date(System.currentTimeMillis()));
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    carrito.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM carrito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCarrito(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM carrito ORDER BY id";

        List<String[]> carritos = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                carritos.add(mapResultSetToCarrito(rs));
            }
        }

        return carritos;
    }

    public void update(Carrito carrito) throws SQLException {
        String sql = "UPDATE carrito SET " +
                "id_cliente = ?, " +
                "fecha_creacion = ?, " +
                "fecha_actualizacion = ? " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carrito.getIdCliente());

            if (carrito.getFechaCreacion() != null) {
                ps.setDate(2, carrito.getFechaCreacion());
            } else {
                ps.setNull(2, Types.DATE);
            }

            if (carrito.getFechaActualizacion() != null) {
                ps.setDate(3, carrito.getFechaActualizacion());
            } else {
                ps.setDate(3, new Date(System.currentTimeMillis()));
            }

            ps.setInt(4, carrito.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM carrito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM carrito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToCarrito(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_cliente")),
                rs.getDate("fecha_creacion") != null
                        ? rs.getDate("fecha_creacion").toString()
                        : "",
                rs.getDate("fecha_actualizacion") != null
                        ? rs.getDate("fecha_actualizacion").toString()
                        : ""
        };
    }
}