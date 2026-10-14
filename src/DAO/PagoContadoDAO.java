package DAO;

import Database.Conexion;
import Models.PagoContado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoContadoDAO {

    public int create(PagoContado pago) throws SQLException {
        String sql = "INSERT INTO pago_contado (" +
                "id_venta, id_metodo_pago, fecha, monto_pago, interes_mora_cobrado, recargo_extra, observaciones, estado" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getIdVenta());
            ps.setInt(2, pago.getIdMetodoPago());
            ps.setDate(3, pago.getFecha());
            ps.setBigDecimal(4, pago.getMontoPago());
            ps.setBigDecimal(5, pago.getInteresMoraCobrado());
            ps.setBigDecimal(6, pago.getRecargoExtra());
            ps.setString(7, pago.getObservaciones());
            ps.setString(8, pago.getEstado() != null ? pago.getEstado() : "PENDIENTE");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    pago.setId(idGenerado);
                    return idGenerado;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID generado de pago_contado.");
    }

    public List<String[]> listAll() throws SQLException {
        String sql =
                "SELECT pc.id, pc.id_venta, pc.id_metodo_pago, pc.fecha, pc.monto_pago, " +
                "pc.interes_mora_cobrado, pc.recargo_extra, pc.observaciones, " +
                "COALESCE(pc.estado, pft.estado, 'PENDIENTE') AS estado_pago " +
                "FROM pago_contado pc " +
                "LEFT JOIN pago_facil_transaccion pft ON pft.id_pago_contado = pc.id " +
                "ORDER BY pc.id";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("id_venta")),
                        String.valueOf(rs.getInt("id_metodo_pago")),
                        rs.getDate("fecha") != null ? rs.getDate("fecha").toString() : "",
                        rs.getBigDecimal("monto_pago") != null ? rs.getBigDecimal("monto_pago").toString() : "",
                        rs.getBigDecimal("interes_mora_cobrado") != null ? rs.getBigDecimal("interes_mora_cobrado").toString() : "",
                        rs.getBigDecimal("recargo_extra") != null ? rs.getBigDecimal("recargo_extra").toString() : "",
                        rs.getString("observaciones") != null ? rs.getString("observaciones") : "",
                        rs.getString("estado_pago") != null ? rs.getString("estado_pago") : ""
                });
            }
        }

        return lista;
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM pago_contado WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pago_contado WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int getIdVentaById(int idPagoContado) throws SQLException {
        String sql = "SELECT id_venta FROM pago_contado WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPagoContado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_venta");
                }
            }
        }

        return 0;
    }

    public void marcarPagado(int idPagoContado) throws SQLException {
        String sql = "UPDATE pago_contado SET estado = 'PAGADO' WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPagoContado);
            ps.executeUpdate();
        }
    }
}