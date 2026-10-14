package DAO;

import Database.Conexion;
import Models.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (" +
                "id_cliente, id_carrito, numero_pedido, fecha_pedido, fecha_pago, fecha_envio, fecha_entrega, " +
                "fecha_recibido_conforme, tipo_venta, subtotal, descuento, total, estado, direccion_entrega, observaciones" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pedido.getIdCliente());
            ps.setInt(2, pedido.getIdCarrito());
            ps.setString(3, pedido.getNumeroPedido());
            ps.setDate(4, pedido.getFechaPedido());
            ps.setDate(5, pedido.getFechaPago());
            ps.setDate(6, pedido.getFechaEnvio());
            ps.setDate(7, pedido.getFechaEntrega());
            ps.setDate(8, pedido.getFechaRecibidoConforme());
            ps.setString(9, pedido.getTipoVenta());
            ps.setBigDecimal(10, pedido.getSubtotal());
            ps.setBigDecimal(11, pedido.getDescuento());
            ps.setBigDecimal(12, pedido.getTotal());
            ps.setString(13, pedido.getEstado());
            ps.setString(14, pedido.getDireccionEntrega());
            ps.setString(15, pedido.getObservaciones());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPedido(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM pedido ORDER BY id";

        List<String[]> pedidos = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }

        return pedidos;
    }

    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedido SET " +
                "id_cliente = ?, " +
                "id_carrito = ?, " +
                "numero_pedido = ?, " +
                "fecha_pedido = ?, " +
                "fecha_pago = ?, " +
                "fecha_envio = ?, " +
                "fecha_entrega = ?, " +
                "fecha_recibido_conforme = ?, " +
                "tipo_venta = ?, " +
                "subtotal = ?, " +
                "descuento = ?, " +
                "total = ?, " +
                "estado = ?, " +
                "direccion_entrega = ?, " +
                "observaciones = ? " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pedido.getIdCliente());
            ps.setInt(2, pedido.getIdCarrito());
            ps.setString(3, pedido.getNumeroPedido());
            ps.setDate(4, pedido.getFechaPedido());
            ps.setDate(5, pedido.getFechaPago());
            ps.setDate(6, pedido.getFechaEnvio());
            ps.setDate(7, pedido.getFechaEntrega());
            ps.setDate(8, pedido.getFechaRecibidoConforme());
            ps.setString(9, pedido.getTipoVenta());
            ps.setBigDecimal(10, pedido.getSubtotal());
            ps.setBigDecimal(11, pedido.getDescuento());
            ps.setBigDecimal(12, pedido.getTotal());
            ps.setString(13, pedido.getEstado());
            ps.setString(14, pedido.getDireccionEntrega());
            ps.setString(15, pedido.getObservaciones());
            ps.setInt(16, pedido.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM pedido WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToPedido(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_cliente")),
                String.valueOf(rs.getInt("id_carrito")),
                rs.getString("numero_pedido"),
                rs.getDate("fecha_pedido") != null ? rs.getDate("fecha_pedido").toString() : "",
                rs.getDate("fecha_pago") != null ? rs.getDate("fecha_pago").toString() : "",
                rs.getDate("fecha_envio") != null ? rs.getDate("fecha_envio").toString() : "",
                rs.getDate("fecha_entrega") != null ? rs.getDate("fecha_entrega").toString() : "",
                rs.getDate("fecha_recibido_conforme") != null ? rs.getDate("fecha_recibido_conforme").toString() : "",
                rs.getString("tipo_venta"),
                rs.getBigDecimal("subtotal") != null ? rs.getBigDecimal("subtotal").toString() : "",
                rs.getBigDecimal("descuento") != null ? rs.getBigDecimal("descuento").toString() : "",
                rs.getBigDecimal("total") != null ? rs.getBigDecimal("total").toString() : "",
                rs.getString("estado"),
                rs.getString("direccion_entrega"),
                rs.getString("observaciones")
        };
    }
}