package DAO;

import Database.Conexion;
import Models.DetalleCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraDAO {

    public int create(Connection con, DetalleCompra detalle) throws SQLException {
        String sql = "INSERT INTO detalle_compra (" +
                "id_compra, id_producto_variante, cantidad, precio, descuento, subtotal" +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getIdCompra());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecio());
            ps.setBigDecimal(5, detalle.getDescuento());
            ps.setBigDecimal(6, detalle.getSubtotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    detalle.setIdDetalle(idGenerado);
                    return idGenerado;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID generado de detalle_compra.");
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM detalle_compra WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDetalleCompra(rs);
                }
            }
        }

        return null;
    }

    public DetalleCompra findByIdForUpdate(Connection con, int idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalle_compra WHERE id_detalle = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetalleCompra detalle = new DetalleCompra();
                    detalle.setIdDetalle(rs.getInt("id_detalle"));
                    detalle.setIdCompra(rs.getInt("id_compra"));
                    detalle.setIdProductoVariante(rs.getInt("id_producto_variante"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio"));
                    detalle.setDescuento(rs.getBigDecimal("descuento"));
                    detalle.setSubtotal(rs.getBigDecimal("subtotal"));
                    return detalle;
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM detalle_compra ORDER BY id_detalle";

        List<String[]> detalles = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapResultSetToDetalleCompra(rs));
            }
        }

        return detalles;
    }

    public void update(Connection con, DetalleCompra detalle) throws SQLException {
        String sql = "UPDATE detalle_compra SET " +
                "id_compra = ?, " +
                "id_producto_variante = ?, " +
                "cantidad = ?, " +
                "precio = ?, " +
                "descuento = ?, " +
                "subtotal = ? " +
                "WHERE id_detalle = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdCompra());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecio());
            ps.setBigDecimal(5, detalle.getDescuento());
            ps.setBigDecimal(6, detalle.getSubtotal());
            ps.setInt(7, detalle.getIdDetalle());

            ps.executeUpdate();
        }
    }

    public void delete(Connection con, int idDetalle) throws SQLException {
        String sql = "DELETE FROM detalle_compra WHERE id_detalle = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM detalle_compra WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToDetalleCompra(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_detalle")),
                String.valueOf(rs.getInt("id_compra")),
                String.valueOf(rs.getInt("id_producto_variante")),
                String.valueOf(rs.getInt("cantidad")),
                rs.getBigDecimal("precio") != null ? rs.getBigDecimal("precio").toString() : "",
                rs.getBigDecimal("descuento") != null ? rs.getBigDecimal("descuento").toString() : "",
                rs.getBigDecimal("subtotal") != null ? rs.getBigDecimal("subtotal").toString() : ""
        };
    }
}