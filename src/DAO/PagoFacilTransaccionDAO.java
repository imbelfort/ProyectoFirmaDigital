package DAO;

import Database.Conexion;
import Models.PagoFacilTransaccion;

import java.sql.*;

public class PagoFacilTransaccionDAO {

    public void create(PagoFacilTransaccion transaccion) throws SQLException {
        String sql = "INSERT INTO pago_facil_transaccion (" +
                "id_pago_contado, id_pago_cuota, payment_number, pagofacil_transaction_id, " +
                "payment_method_transaction_id, payment_method_id, qr_base64, checkout_url, deep_link, " +
                "qr_content_url, universal_url, expiration_date, estado, correo_solicitante, fecha_actualizacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (transaccion.getIdPagoContado() > 0) {
                ps.setInt(1, transaccion.getIdPagoContado());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (transaccion.getIdPagoCuota() > 0) {
                ps.setInt(2, transaccion.getIdPagoCuota());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, transaccion.getPaymentNumber());
            ps.setString(4, transaccion.getPagofacilTransactionId());
            ps.setString(5, transaccion.getPaymentMethodTransactionId());
            ps.setInt(6, transaccion.getPaymentMethodId());
            ps.setString(7, transaccion.getQrBase64());
            ps.setString(8, transaccion.getCheckoutUrl());
            ps.setString(9, transaccion.getDeepLink());
            ps.setString(10, transaccion.getQrContentUrl());
            ps.setString(11, transaccion.getUniversalUrl());
            ps.setTimestamp(12, transaccion.getExpirationDate());
            ps.setString(13, transaccion.getEstado());
            ps.setString(14, transaccion.getCorreoSolicitante());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    transaccion.setId(rs.getInt(1));
                }
            }
        }
    }

    public PagoFacilTransaccion findByPaymentNumber(String paymentNumber) throws SQLException {
        String sql = "SELECT * FROM pago_facil_transaccion WHERE payment_number = ? ORDER BY id DESC LIMIT 1";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    public void updateEstadoByPaymentNumber(String paymentNumber, String estado) throws SQLException {
        String sql = "UPDATE pago_facil_transaccion " +
                "SET estado = ?, fecha_actualizacion = CURRENT_TIMESTAMP " +
                "WHERE payment_number = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setString(2, paymentNumber);

            int filas = ps.executeUpdate();

            System.out.println("[PagoFacilTransaccionDAO] Filas actualizadas: " + filas);
        }
    }

    private PagoFacilTransaccion map(ResultSet rs) throws SQLException {
        PagoFacilTransaccion t = new PagoFacilTransaccion();

        t.setId(rs.getInt("id"));
        t.setIdPagoContado(rs.getInt("id_pago_contado"));
        t.setIdPagoCuota(rs.getInt("id_pago_cuota"));
        t.setPaymentNumber(rs.getString("payment_number"));
        t.setPagofacilTransactionId(rs.getString("pagofacil_transaction_id"));
        t.setPaymentMethodTransactionId(rs.getString("payment_method_transaction_id"));
        t.setPaymentMethodId(rs.getInt("payment_method_id"));
        t.setQrBase64(rs.getString("qr_base64"));
        t.setCheckoutUrl(rs.getString("checkout_url"));
        t.setDeepLink(rs.getString("deep_link"));
        t.setQrContentUrl(rs.getString("qr_content_url"));
        t.setUniversalUrl(rs.getString("universal_url"));
        t.setExpirationDate(rs.getTimestamp("expiration_date"));
        t.setEstado(rs.getString("estado"));
        t.setCorreoSolicitante(rs.getString("correo_solicitante"));
        t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        t.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));

        return t;
    }
}