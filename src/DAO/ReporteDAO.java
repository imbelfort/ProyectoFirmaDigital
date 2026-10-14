package DAO;

import Database.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<String[]> ventasUltimoMes() throws SQLException {
        String sql =
                "SELECT " +
                "COUNT(*) AS cantidad_ventas, " +
                "COALESCE(SUM(total), 0) AS monto_total " +
                "FROM venta " +
                "WHERE fecha_venta >= CURRENT_DATE - INTERVAL '1 month' " +
                "AND UPPER(COALESCE(estado, '')) <> 'ANULADA'";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                lista.add(new String[]{
                        "Ultimo mes",
                        String.valueOf(rs.getInt("cantidad_ventas")),
                        rs.getBigDecimal("monto_total") != null ? rs.getBigDecimal("monto_total").toString() : "0"
                });
            }
        }

        return lista;
    }

    public List<String[]> ventasUltimoTrimestre() throws SQLException {
        String sql =
                "SELECT " +
                "COUNT(*) AS cantidad_ventas, " +
                "COALESCE(SUM(total), 0) AS monto_total " +
                "FROM venta " +
                "WHERE fecha_venta >= CURRENT_DATE - INTERVAL '3 months' " +
                "AND UPPER(COALESCE(estado, '')) <> 'ANULADA'";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                lista.add(new String[]{
                        "Ultimo trimestre",
                        String.valueOf(rs.getInt("cantidad_ventas")),
                        rs.getBigDecimal("monto_total") != null ? rs.getBigDecimal("monto_total").toString() : "0"
                });
            }
        }

        return lista;
    }

    public List<String[]> ventasUltimoAnio() throws SQLException {
        String sql =
                "SELECT " +
                "COUNT(*) AS cantidad_ventas, " +
                "COALESCE(SUM(total), 0) AS monto_total " +
                "FROM venta " +
                "WHERE fecha_venta >= CURRENT_DATE - INTERVAL '1 year' " +
                "AND UPPER(COALESCE(estado, '')) <> 'ANULADA'";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                lista.add(new String[]{
                        "Ultimo anio",
                        String.valueOf(rs.getInt("cantidad_ventas")),
                        rs.getBigDecimal("monto_total") != null ? rs.getBigDecimal("monto_total").toString() : "0"
                });
            }
        }

        return lista;
    }

    public List<String[]> clientesRegistradosUltimoMes() throws SQLException {
        String sql =
                "SELECT " +
                "COUNT(*) AS cantidad_clientes " +
                "FROM cliente c " +
                "INNER JOIN usuario u ON u.id_usuario = c.id_usuario " +
                "WHERE u.fecha_creacion >= CURRENT_DATE - INTERVAL '1 month'";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                lista.add(new String[]{
                        "Ultimo mes",
                        String.valueOf(rs.getInt("cantidad_clientes"))
                });
            }
        }

        return lista;
    }

    public List<String[]> ventasUltimoAnioPorMetodoPago() throws SQLException {
        String sql =
                "SELECT " +
                "COALESCE(mp.nombre, 'SIN METODO') AS metodo_pago, " +
                "COUNT(pc.id) AS cantidad_pagos, " +
                "COALESCE(SUM(pc.monto_pago), 0) AS monto_total " +
                "FROM pago_contado pc " +
                "INNER JOIN venta v ON v.id = pc.id_venta " +
                "INNER JOIN metodo_pago mp ON mp.id = pc.id_metodo_pago " +
                "WHERE pc.fecha >= CURRENT_DATE - INTERVAL '1 year' " +
                "AND UPPER(COALESCE(v.estado, '')) <> 'ANULADA' " +
                "GROUP BY mp.nombre " +
                "ORDER BY monto_total DESC";

        List<String[]> lista = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("metodo_pago"),
                        String.valueOf(rs.getInt("cantidad_pagos")),
                        rs.getBigDecimal("monto_total") != null ? rs.getBigDecimal("monto_total").toString() : "0"
                });
            }
        }

        return lista;
    }
}