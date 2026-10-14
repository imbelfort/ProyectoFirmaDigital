package DAO;

import Database.Conexion;
import Models.PedidoDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDetalleDAO {

    public void create(PedidoDetalle detalle) throws SQLException {
        String sql = "INSERT INTO pedido_detalle (" +
                "id_pedido, id_producto_variante, cantidad, precio_unitario, subtotal, descuento" +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());
            ps.setBigDecimal(6, detalle.getDescuento());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int idDetalle) throws SQLException {
        String sql = "SELECT * FROM pedido_detalle WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedidoDetalle(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM pedido_detalle ORDER BY id_detalle";

        List<String[]> detalles = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapResultSetToPedidoDetalle(rs));
            }
        }

        return detalles;
    }

    public void update(PedidoDetalle detalle) throws SQLException {
        String sql = "UPDATE pedido_detalle SET " +
                "id_pedido = ?, " +
                "id_producto_variante = ?, " +
                "cantidad = ?, " +
                "precio_unitario = ?, " +
                "subtotal = ?, " +
                "descuento = ? " +
                "WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProductoVariante());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());
            ps.setBigDecimal(6, detalle.getDescuento());
            ps.setInt(7, detalle.getIdDetalle());

            ps.executeUpdate();
        }
    }

    public void delete(int idDetalle) throws SQLException {
        String sql = "DELETE FROM pedido_detalle WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int idDetalle) throws SQLException {
        String sql = "SELECT 1 FROM pedido_detalle WHERE id_detalle = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idDetalle);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToPedidoDetalle(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_detalle")),
                String.valueOf(rs.getInt("id_pedido")),
                String.valueOf(rs.getInt("id_producto_variante")),
                String.valueOf(rs.getInt("cantidad")),
                rs.getBigDecimal("precio_unitario") != null ? rs.getBigDecimal("precio_unitario").toString() : "",
                rs.getBigDecimal("subtotal") != null ? rs.getBigDecimal("subtotal").toString() : "",
                rs.getBigDecimal("descuento") != null ? rs.getBigDecimal("descuento").toString() : ""
        };
    }
}