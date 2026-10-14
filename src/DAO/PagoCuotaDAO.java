package DAO;

import Database.Conexion;
import Models.PagoCuota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoCuotaDAO {

    public int create(PagoCuota pago) throws SQLException {
        String sql = "INSERT INTO pago_cuota (" +
                "id_credito, id_metodo_pago, numero_cuota, interes_cuota, fecha_vencimiento, " +
                "fecha_pago, monto_pagado, dias_mora, monto_pendiente, estado, fecha_creacion, fecha_actualizacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getIdCredito());
            ps.setInt(2, pago.getIdMetodoPago());
            ps.setInt(3, pago.getNumeroCuota());
            ps.setBigDecimal(4, pago.getInteresCuota());
            ps.setDate(5, pago.getFechaVencimiento());
            ps.setDate(6, pago.getFechaPago());
            ps.setBigDecimal(7, pago.getMontoPagado());
            ps.setInt(8, pago.getDiasMora());
            ps.setBigDecimal(9, pago.getMontoPendiente());
            ps.setString(10, pago.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    pago.setId(idGenerado);
                    return idGenerado;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID generado de pago_cuota.");
    }

    public void update(PagoCuota pago) throws SQLException {
        String sql = "UPDATE pago_cuota SET " +
                "id_credito = ?, " +
                "id_metodo_pago = ?, " +
                "numero_cuota = ?, " +
                "interes_cuota = ?, " +
                "fecha_vencimiento = ?, " +
                "fecha_pago = ?, " +
                "monto_pagado = ?, " +
                "dias_mora = ?, " +
                "monto_pendiente = ?, " +
                "estado = ?, " +
                "fecha_actualizacion = CURRENT_DATE " +
                "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pago.getIdCredito());
            ps.setInt(2, pago.getIdMetodoPago());
            ps.setInt(3, pago.getNumeroCuota());
            ps.setBigDecimal(4, pago.getInteresCuota());
            ps.setDate(5, pago.getFechaVencimiento());
            ps.setDate(6, pago.getFechaPago());
            ps.setBigDecimal(7, pago.getMontoPagado());
            ps.setInt(8, pago.getDiasMora());
            ps.setBigDecimal(9, pago.getMontoPendiente());
            ps.setString(10, pago.getEstado());
            ps.setInt(11, pago.getId());

            ps.executeUpdate();
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql =
                "SELECT pc.id, pc.id_credito, pc.id_metodo_pago, pc.numero_cuota, pc.interes_cuota, " +
                "pc.fecha_vencimiento, pc.fecha_pago, pc.monto_pagado, pc.dias_mora, pc.monto_pendiente, " +
                "COALESCE(pft.estado, pc.estado) AS estado_final, pc.fecha_creacion, pc.fecha_actualizacion " +
                "FROM pago_cuota pc " +
                "LEFT JOIN pago_facil_transaccion pft ON pft.id_pago_cuota = pc.id " +
                "ORDER BY pc.id";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("id_credito")),
                        String.valueOf(rs.getInt("id_metodo_pago")),
                        String.valueOf(rs.getInt("numero_cuota")),
                        rs.getBigDecimal("interes_cuota") != null ? rs.getBigDecimal("interes_cuota").toString() : "",
                        rs.getDate("fecha_vencimiento") != null ? rs.getDate("fecha_vencimiento").toString() : "",
                        rs.getDate("fecha_pago") != null ? rs.getDate("fecha_pago").toString() : "",
                        rs.getBigDecimal("monto_pagado") != null ? rs.getBigDecimal("monto_pagado").toString() : "",
                        String.valueOf(rs.getInt("dias_mora")),
                        rs.getBigDecimal("monto_pendiente") != null ? rs.getBigDecimal("monto_pendiente").toString() : "",
                        rs.getString("estado_final"),
                        rs.getDate("fecha_creacion") != null ? rs.getDate("fecha_creacion").toString() : "",
                        rs.getDate("fecha_actualizacion") != null ? rs.getDate("fecha_actualizacion").toString() : ""
                });
            }
        }

        return lista;
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM pago_cuota WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pago_cuota WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void marcarPagado(int idPagoCuota) throws SQLException {
        String sql = "UPDATE pago_cuota SET estado = 'PAGADO', fecha_pago = CURRENT_DATE, fecha_actualizacion = CURRENT_DATE WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPagoCuota);
            ps.executeUpdate();
        }
    }

    public PagoCuota findById(int id) throws SQLException {
        String sql = "SELECT * FROM pago_cuota WHERE id = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PagoCuota pago = new PagoCuota();

                    pago.setId(rs.getInt("id"));
                    pago.setIdCredito(rs.getInt("id_credito"));
                    pago.setIdMetodoPago(rs.getInt("id_metodo_pago"));
                    pago.setNumeroCuota(rs.getInt("numero_cuota"));
                    pago.setInteresCuota(rs.getBigDecimal("interes_cuota"));
                    pago.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                    pago.setFechaPago(rs.getDate("fecha_pago"));
                    pago.setMontoPagado(rs.getBigDecimal("monto_pagado"));
                    pago.setDiasMora(rs.getInt("dias_mora"));
                    pago.setMontoPendiente(rs.getBigDecimal("monto_pendiente"));
                    pago.setEstado(rs.getString("estado"));
                    pago.setFechaCreacion(rs.getDate("fecha_creacion"));
                    pago.setFechaActualizacion(rs.getDate("fecha_actualizacion"));

                    return pago;
                }
            }
        }

        return null;
    }
}