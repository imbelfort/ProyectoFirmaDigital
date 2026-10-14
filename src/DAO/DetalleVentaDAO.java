package DAO;

import Database.Conexion;
import Models.DetalleVenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    public int create(Connection con, DetalleVenta detalle) throws SQLException {
        String sql = "INSERT INTO detalle_venta (" +
                "id_venta, id_producto_variante, cantidad, precio_unitario, subtotal, descuento" +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getIdVenta());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());
            ps.setBigDecimal(6, detalle.getDescuento());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    detalle.setIdDetalle(idGenerado);
                    return idGenerado;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID generado de detalle_venta.");
    }

    public DetalleVenta findByIdForUpdate(Connection con, int idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalle_venta WHERE id_detalle = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalle(rs.getInt("id_detalle"));
                    detalle.setIdVenta(rs.getInt("id_venta"));
                    detalle.setIdProductoVariante(rs.getInt("id_producto_variante"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    detalle.setSubtotal(rs.getBigDecimal("subtotal"));
                    detalle.setDescuento(rs.getBigDecimal("descuento"));
                    return detalle;
                }
            }
        }

        return null;
    }

    public String[] readById(int idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalle_venta WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDetalleVenta(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM detalle_venta ORDER BY id_detalle";

        List<String[]> detalles = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapResultSetToDetalleVenta(rs));
            }
        }

        return detalles;
    }

    public void update(Connection con, DetalleVenta detalle) throws SQLException {
        String sql = "UPDATE detalle_venta SET " +
                "id_venta = ?, " +
                "id_producto_variante = ?, " +
                "cantidad = ?, " +
                "precio_unitario = ?, " +
                "subtotal = ?, " +
                "descuento = ? " +
                "WHERE id_detalle = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdVenta());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());
            ps.setBigDecimal(6, detalle.getDescuento());
            ps.setInt(7, detalle.getIdDetalle());

            ps.executeUpdate();
        }
    }

    public void delete(Connection con, int idDetalle) throws SQLException {
        String sql = "DELETE FROM detalle_venta WHERE id_detalle = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int idDetalle) throws SQLException {
        String sql = "SELECT 1 FROM detalle_venta WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToDetalleVenta(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_detalle")),
                String.valueOf(rs.getInt("id_venta")),
                String.valueOf(rs.getInt("id_producto_variante")),
                String.valueOf(rs.getInt("cantidad")),
                rs.getBigDecimal("precio_unitario") != null ? rs.getBigDecimal("precio_unitario").toString() : "",
                rs.getBigDecimal("subtotal") != null ? rs.getBigDecimal("subtotal").toString() : "",
                rs.getBigDecimal("descuento") != null ? rs.getBigDecimal("descuento").toString() : ""
        };
    }
}