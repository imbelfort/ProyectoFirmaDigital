package DAO;

import Database.Conexion;
import Models.Credito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreditoDAO {

    public void create(Credito credito) throws SQLException {
        String sql = "INSERT INTO credito (" +
                "id_venta, monto_credito, monto_pendiente, monto_pagado, fecha_otorgamiento, " +
                "fecha_vencimiento, interes, estado, cuotas_total, dias_mora, fecha_creacion, fecha_actualizacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, credito.getIdVenta());
            ps.setBigDecimal(2, credito.getMontoCredito());
            ps.setBigDecimal(3, credito.getMontoPendiente());
            ps.setBigDecimal(4, credito.getMontoPagado());
            ps.setDate(5, credito.getFechaOtorgamiento());
            ps.setDate(6, credito.getFechaVencimiento());
            ps.setBigDecimal(7, credito.getInteres());
            ps.setString(8, credito.getEstado());
            ps.setInt(9, credito.getCuotasTotal());
            ps.setInt(10, credito.getDiasMora());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    credito.setId(rs.getInt(1));
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM credito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCredito(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM credito ORDER BY id";

        List<String[]> creditos = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                creditos.add(mapResultSetToCredito(rs));
            }
        }

        return creditos;
    }

    public void update(Credito credito) throws SQLException {
        String sql = "UPDATE credito SET " +
                "id_venta = ?, " +
                "monto_credito = ?, " +
                "monto_pendiente = ?, " +
                "monto_pagado = ?, " +
                "fecha_otorgamiento = ?, " +
                "fecha_vencimiento = ?, " +
                "interes = ?, " +
                "estado = ?, " +
                "cuotas_total = ?, " +
                "dias_mora = ?, " +
                "fecha_actualizacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, credito.getIdVenta());
            ps.setBigDecimal(2, credito.getMontoCredito());
            ps.setBigDecimal(3, credito.getMontoPendiente());
            ps.setBigDecimal(4, credito.getMontoPagado());
            ps.setDate(5, credito.getFechaOtorgamiento());
            ps.setDate(6, credito.getFechaVencimiento());
            ps.setBigDecimal(7, credito.getInteres());
            ps.setString(8, credito.getEstado());
            ps.setInt(9, credito.getCuotasTotal());
            ps.setInt(10, credito.getDiasMora());
            ps.setInt(11, credito.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM credito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM credito WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToCredito(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id")),
                String.valueOf(rs.getInt("id_venta")),
                rs.getBigDecimal("monto_credito") != null ? rs.getBigDecimal("monto_credito").toString() : "",
                rs.getBigDecimal("monto_pendiente") != null ? rs.getBigDecimal("monto_pendiente").toString() : "",
                rs.getBigDecimal("monto_pagado") != null ? rs.getBigDecimal("monto_pagado").toString() : "",
                rs.getDate("fecha_otorgamiento") != null ? rs.getDate("fecha_otorgamiento").toString() : "",
                rs.getDate("fecha_vencimiento") != null ? rs.getDate("fecha_vencimiento").toString() : "",
                rs.getBigDecimal("interes") != null ? rs.getBigDecimal("interes").toString() : "",
                rs.getString("estado"),
                String.valueOf(rs.getInt("cuotas_total")),
                String.valueOf(rs.getInt("dias_mora")),
                rs.getDate("fecha_creacion") != null ? rs.getDate("fecha_creacion").toString() : "",
                rs.getDate("fecha_actualizacion") != null ? rs.getDate("fecha_actualizacion").toString() : ""
        };
    }

    public void registrarPagoCuota(int idCredito, java.math.BigDecimal montoPagado) throws SQLException {
        String sql = "UPDATE credito SET " +
                "monto_pagado = COALESCE(monto_pagado, 0) + ?, " +
                "monto_pendiente = GREATEST(COALESCE(monto_pendiente, 0) - ?, 0), " +
                "fecha_actualizacion = CURRENT_DATE, " +
                "estado = CASE " +
                "WHEN GREATEST(COALESCE(monto_pendiente, 0) - ?, 0) = 0 THEN 'PAGADO' " +
                "ELSE estado END " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, montoPagado);
            ps.setBigDecimal(2, montoPagado);
            ps.setBigDecimal(3, montoPagado);
            ps.setInt(4, idCredito);

            ps.executeUpdate();
        }
    }

}