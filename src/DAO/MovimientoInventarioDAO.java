package DAO;

import Database.Conexion;
import Models.MovimientoInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAO {

    public void create(Connection con, MovimientoInventario movimiento) throws SQLException {
        String sql = "INSERT INTO movimiento_inventario (" +
                "id_producto_variante, id_tecnica_inventario, id_tecnica_costo, tipo_movimiento, motivo, " +
                "cantidad, costo_unitario, costo_total, stock_anterior, stock_resultante, fecha, observacion" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, movimiento.getIdProductoVariante());
            ps.setInt(2, movimiento.getIdTecnicaInventario());
            ps.setInt(3, movimiento.getIdTecnicaCosto());
            ps.setString(4, movimiento.getTipoMovimiento());
            ps.setString(5, movimiento.getMotivo());
            ps.setInt(6, movimiento.getCantidad());
            ps.setBigDecimal(7, movimiento.getCostoUnitario());
            ps.setBigDecimal(8, movimiento.getCostoTotal());
            ps.setInt(9, movimiento.getStockAnterior());
            ps.setInt(10, movimiento.getStockResultante());
            ps.setString(11, movimiento.getObservacion());

            ps.executeUpdate();
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM movimiento_inventario ORDER BY id";

        List<String[]> movimientos = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movimientos.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("id_producto_variante")),
                        String.valueOf(rs.getInt("id_tecnica_inventario")),
                        String.valueOf(rs.getInt("id_tecnica_costo")),
                        rs.getString("tipo_movimiento"),
                        rs.getString("motivo"),
                        String.valueOf(rs.getInt("cantidad")),
                        rs.getBigDecimal("costo_unitario") != null ? rs.getBigDecimal("costo_unitario").toString() : "",
                        rs.getBigDecimal("costo_total") != null ? rs.getBigDecimal("costo_total").toString() : "",
                        String.valueOf(rs.getInt("stock_anterior")),
                        String.valueOf(rs.getInt("stock_resultante")),
                        rs.getDate("fecha") != null ? rs.getDate("fecha").toString() : "",
                        rs.getString("observacion")
                });
            }
        }

        return movimientos;
    }
}