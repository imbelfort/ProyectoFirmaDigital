package DAO;

import Database.Conexion;
import Models.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public void create(Venta venta) throws SQLException {
        String sql = "INSERT INTO venta (" +
                "id_vendedor, id_cliente, id_pedido, numero_venta, tipo, origen, tipo_pago, total, estado, observaciones, fecha_venta" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (venta.getIdVendedor() > 0) {
                ps.setInt(1, venta.getIdVendedor());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, venta.getIdCliente());

            if (venta.getIdPedido() > 0) {
                ps.setInt(3, venta.getIdPedido());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, venta.getNumeroVenta());
            ps.setString(5, venta.getTipo());
            ps.setString(6, venta.getOrigen());
            ps.setString(7, venta.getTipoPago());
            ps.setBigDecimal(8, venta.getTotal());
            ps.setString(9, venta.getEstado());
            ps.setString(10, venta.getObservaciones());
            ps.setDate(11, venta.getFechaVenta());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    venta.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM venta WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVenta(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM venta ORDER BY id";

        List<String[]> ventas = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
        }

        return ventas;
    }

    public void update(Venta venta) throws SQLException {
        String sql = "UPDATE venta SET " +
                "id_vendedor = ?, " +
                "id_cliente = ?, " +
                "id_pedido = ?, " +
                "numero_venta = ?, " +
                "tipo = ?, " +
                "origen = ?, " +
                "tipo_pago = ?, " +
                "total = ?, " +
                "estado = ?, " +
                "observaciones = ?, " +
                "fecha_venta = ? " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (venta.getIdVendedor() > 0) {
                ps.setInt(1, venta.getIdVendedor());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, venta.getIdCliente());

            if (venta.getIdPedido() > 0) {
                ps.setInt(3, venta.getIdPedido());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, venta.getNumeroVenta());
            ps.setString(5, venta.getTipo());
            ps.setString(6, venta.getOrigen());
            ps.setString(7, venta.getTipoPago());
            ps.setBigDecimal(8, venta.getTotal());
            ps.setString(9, venta.getEstado());
            ps.setString(10, venta.getObservaciones());
            ps.setDate(11, venta.getFechaVenta());
            ps.setInt(12, venta.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM venta WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM venta WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToVenta(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getObject("id_vendedor") != null ? String.valueOf(rs.getInt("id_vendedor")) : "",
                String.valueOf(rs.getInt("id_cliente")),
                rs.getObject("id_pedido") != null ? String.valueOf(rs.getInt("id_pedido")) : "",
                rs.getString("numero_venta"),
                rs.getString("tipo"),
                rs.getString("origen"),
                rs.getString("tipo_pago"),
                rs.getBigDecimal("total") != null ? rs.getBigDecimal("total").toString() : "",
                rs.getString("estado"),
                rs.getString("observaciones"),
                rs.getDate("fecha_venta") != null ? rs.getDate("fecha_venta").toString() : ""
        };
    }

    public void updateEstado(int idVenta, String estado) throws SQLException {
        String sql = "UPDATE venta SET estado = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idVenta);
            ps.executeUpdate();
        }
    }
}