package DAO;

import Database.Conexion;
import Models.CarritoDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarritoDetalleDAO {

    public void create(CarritoDetalle carritoDetalle) throws SQLException {
        String sql = "INSERT INTO carrito_detalle (" +
                "id_carrito, id_producto_variante, cantidad, precio_unitario, descuento" +
                ") VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carritoDetalle.getIdCarrito());
            ps.setInt(2, carritoDetalle.getIdProductoVariante());
            ps.setInt(3, carritoDetalle.getCantidad());
            ps.setBigDecimal(4, carritoDetalle.getPrecioUnitario());
            ps.setBigDecimal(5, carritoDetalle.getDescuento());

            ps.executeUpdate();
        }
    }

    public String[] readByIds(int idCarrito, int idProductoVariante) throws SQLException {
        String sql = "SELECT * FROM carrito_detalle " +
                "WHERE id_carrito = ? AND id_producto_variante = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCarrito);
            ps.setInt(2, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCarritoDetalle(rs);
                }
            }
        }

        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM carrito_detalle ORDER BY id_carrito, id_producto_variante";

        List<String[]> detalles = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapResultSetToCarritoDetalle(rs));
            }
        }

        return detalles;
    }

    public void update(CarritoDetalle carritoDetalle) throws SQLException {
        String sql = "UPDATE carrito_detalle SET " +
                "cantidad = ?, " +
                "precio_unitario = ?, " +
                "descuento = ? " +
                "WHERE id_carrito = ? AND id_producto_variante = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carritoDetalle.getCantidad());
            ps.setBigDecimal(2, carritoDetalle.getPrecioUnitario());
            ps.setBigDecimal(3, carritoDetalle.getDescuento());
            ps.setInt(4, carritoDetalle.getIdCarrito());
            ps.setInt(5, carritoDetalle.getIdProductoVariante());

            ps.executeUpdate();
        }
    }

    public void delete(int idCarrito, int idProductoVariante) throws SQLException {
        String sql = "DELETE FROM carrito_detalle " +
                "WHERE id_carrito = ? AND id_producto_variante = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCarrito);
            ps.setInt(2, idProductoVariante);

            ps.executeUpdate();
        }
    }

    public boolean existsByIds(int idCarrito, int idProductoVariante) throws SQLException {
        String sql = "SELECT 1 FROM carrito_detalle " +
                "WHERE id_carrito = ? AND id_producto_variante = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCarrito);
            ps.setInt(2, idProductoVariante);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String[] mapResultSetToCarritoDetalle(ResultSet rs) throws SQLException {
        return new String[]{
                String.valueOf(rs.getInt("id_carrito")),
                String.valueOf(rs.getInt("id_producto_variante")),
                String.valueOf(rs.getInt("cantidad")),
                rs.getBigDecimal("precio_unitario") != null
                        ? rs.getBigDecimal("precio_unitario").toString()
                        : "",
                rs.getBigDecimal("descuento") != null
                        ? rs.getBigDecimal("descuento").toString()
                        : ""
        };
    }
}