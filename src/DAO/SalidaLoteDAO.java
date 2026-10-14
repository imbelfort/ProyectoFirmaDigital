package DAO;

import Database.Conexion;
import Models.SalidaLote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalidaLoteDAO {

    public void create(Connection con, SalidaLote salida) throws SQLException {
        String sql = "INSERT INTO salida_lote (" +
                "id_detalle_venta, id_lote, cantidad, costo_unitario_aplicado, costo_total, fecha_salida" +
                ") VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, salida.getIdDetalleVenta());
            ps.setInt(2, salida.getIdLote());
            ps.setInt(3, salida.getCantidad());
            ps.setBigDecimal(4, salida.getCostoUnitarioAplicado());
            ps.setBigDecimal(5, salida.getCostoTotal());

            ps.executeUpdate();
        }
    }

    public List<SalidaLote> findByDetalleVentaForUpdate(Connection con, int idDetalleVenta) throws SQLException {
        String sql = "SELECT * FROM salida_lote WHERE id_detalle_venta = ? FOR UPDATE";

        List<SalidaLote> salidas = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalleVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SalidaLote salida = new SalidaLote();

                    salida.setId(rs.getInt("id"));
                    salida.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    salida.setIdLote(rs.getInt("id_lote"));
                    salida.setCantidad(rs.getInt("cantidad"));
                    salida.setCostoUnitarioAplicado(rs.getBigDecimal("costo_unitario_aplicado"));
                    salida.setCostoTotal(rs.getBigDecimal("costo_total"));
                    salida.setFechaSalida(rs.getDate("fecha_salida"));

                    salidas.add(salida);
                }
            }
        }

        return salidas;
    }

    public void deleteByDetalleVenta(Connection con, int idDetalleVenta) throws SQLException {
        String sql = "DELETE FROM salida_lote WHERE id_detalle_venta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalleVenta);
            ps.executeUpdate();
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM salida_lote ORDER BY id";

        List<String[]> salidas = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salidas.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("id_detalle_venta")),
                        String.valueOf(rs.getInt("id_lote")),
                        String.valueOf(rs.getInt("cantidad")),
                        rs.getBigDecimal("costo_unitario_aplicado") != null
                                ? rs.getBigDecimal("costo_unitario_aplicado").toString()
                                : "",
                        rs.getBigDecimal("costo_total") != null
                                ? rs.getBigDecimal("costo_total").toString()
                                : "",
                        rs.getDate("fecha_salida") != null
                                ? rs.getDate("fecha_salida").toString()
                                : ""
                });
            }
        }

        return salidas;
    }
}