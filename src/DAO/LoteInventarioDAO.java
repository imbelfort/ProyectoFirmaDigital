package DAO;

import Database.Conexion;
import Models.LoteInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoteInventarioDAO {

    public void create(Connection con, LoteInventario lote) throws SQLException {
        String sql = "INSERT INTO lote_inventario (" +
                "id_producto_variante, id_detalle_compra, fecha_ingreso, cantidad_inicial, cantidad_disponible, costo_unitario, estado" +
                ") VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lote.getIdProductoVariante());
            ps.setInt(2, lote.getIdDetalleCompra());
            ps.setInt(3, lote.getCantidadInicial());
            ps.setInt(4, lote.getCantidadDisponible());
            ps.setBigDecimal(5, lote.getCostoUnitario());
            ps.setString(6, lote.getEstado());

            ps.executeUpdate();
        }
    }

    public LoteInventario findByDetalleCompraForUpdate(Connection con, int idDetalleCompra) throws SQLException {
        String sql = "SELECT * FROM lote_inventario WHERE id_detalle_compra = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalleCompra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoteInventario lote = new LoteInventario();
                    lote.setId(rs.getInt("id"));
                    lote.setIdProductoVariante(rs.getInt("id_producto_variante"));
                    lote.setIdDetalleCompra(rs.getInt("id_detalle_compra"));
                    lote.setFechaIngreso(rs.getDate("fecha_ingreso"));
                    lote.setCantidadInicial(rs.getInt("cantidad_inicial"));
                    lote.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    lote.setCostoUnitario(rs.getBigDecimal("costo_unitario"));
                    lote.setEstado(rs.getString("estado"));
                    return lote;
                }
            }
        }

        return null;
    }

    public void updateByDetalleCompra(Connection con, LoteInventario lote) throws SQLException {
        String sql = "UPDATE lote_inventario SET " +
                "id_producto_variante = ?, " +
                "cantidad_inicial = ?, " +
                "cantidad_disponible = ?, " +
                "costo_unitario = ?, " +
                "estado = ? " +
                "WHERE id_detalle_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lote.getIdProductoVariante());
            ps.setInt(2, lote.getCantidadInicial());
            ps.setInt(3, lote.getCantidadDisponible());
            ps.setBigDecimal(4, lote.getCostoUnitario());
            ps.setString(5, lote.getEstado());
            ps.setInt(6, lote.getIdDetalleCompra());

            ps.executeUpdate();
        }
    }

    public void deleteByDetalleCompra(Connection con, int idDetalleCompra) throws SQLException {
        String sql = "DELETE FROM lote_inventario WHERE id_detalle_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalleCompra);
            ps.executeUpdate();
        }
    }

    public boolean tieneSalidas(Connection con, int idLote) throws SQLException {
        String sql = "SELECT 1 FROM salida_lote WHERE id_lote = ? LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM lote_inventario ORDER BY id";

        List<String[]> lotes = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lotes.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.valueOf(rs.getInt("id_producto_variante")),
                        String.valueOf(rs.getInt("id_detalle_compra")),
                        rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso").toString() : "",
                        String.valueOf(rs.getInt("cantidad_inicial")),
                        String.valueOf(rs.getInt("cantidad_disponible")),
                        rs.getBigDecimal("costo_unitario") != null ? rs.getBigDecimal("costo_unitario").toString() : "",
                        rs.getString("estado")
                });
            }
        }

        return lotes;
    }

    public List<LoteInventario> findDisponiblesByProductoVarianteForUpdate(Connection con, int idProductoVariante) throws SQLException {
        String sql = "SELECT * FROM lote_inventario " +
                "WHERE id_producto_variante = ? " +
                "AND cantidad_disponible > 0 " +
                "ORDER BY fecha_ingreso ASC, id ASC " +
                "FOR UPDATE";

        List<LoteInventario> lotes = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoteInventario lote = new LoteInventario();
                    lote.setId(rs.getInt("id"));
                    lote.setIdProductoVariante(rs.getInt("id_producto_variante"));
                    lote.setIdDetalleCompra(rs.getInt("id_detalle_compra"));
                    lote.setFechaIngreso(rs.getDate("fecha_ingreso"));
                    lote.setCantidadInicial(rs.getInt("cantidad_inicial"));
                    lote.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    lote.setCostoUnitario(rs.getBigDecimal("costo_unitario"));
                    lote.setEstado(rs.getString("estado"));
                    lotes.add(lote);
                }
            }
        }

        return lotes;
    }

    public LoteInventario findByIdForUpdate(Connection con, int idLote) throws SQLException {
        String sql = "SELECT * FROM lote_inventario WHERE id = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LoteInventario lote = new LoteInventario();
                    lote.setId(rs.getInt("id"));
                    lote.setIdProductoVariante(rs.getInt("id_producto_variante"));
                    lote.setIdDetalleCompra(rs.getInt("id_detalle_compra"));
                    lote.setFechaIngreso(rs.getDate("fecha_ingreso"));
                    lote.setCantidadInicial(rs.getInt("cantidad_inicial"));
                    lote.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    lote.setCostoUnitario(rs.getBigDecimal("costo_unitario"));
                    lote.setEstado(rs.getString("estado"));
                    return lote;
                }
            }
        }

        return null;
    }

    public void actualizarCantidadDisponible(Connection con, int idLote, int nuevaCantidadDisponible) throws SQLException {
        String estado = nuevaCantidadDisponible > 0 ? "DISPONIBLE" : "AGOTADO";

        String sql = "UPDATE lote_inventario SET " +
                "cantidad_disponible = ?, " +
                "estado = ? " +
                "WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidadDisponible);
            ps.setString(2, estado);
            ps.setInt(3, idLote);
            ps.executeUpdate();
        }
    }

}