package Models;

import java.math.BigDecimal;

public class DetalleCompra {

    private int idDetalle;
    private int idCompra;
    private int idProductoVariante;
    private int cantidad;
    private BigDecimal precio;
    private BigDecimal descuento;
    private BigDecimal subtotal;

    public DetalleCompra() {
        this.precio = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    public DetalleCompra(int idDetalle, int idCompra, int idProductoVariante,
                         int cantidad, BigDecimal precio, BigDecimal descuento,
                         BigDecimal subtotal) {
        this.idDetalle = idDetalle;
        this.idCompra = idCompra;
        this.idProductoVariante = idProductoVariante;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.subtotal = subtotal;
    }

    public DetalleCompra(int idCompra, int idProductoVariante, int cantidad,
                         BigDecimal precio, BigDecimal descuento, BigDecimal subtotal) {
        this.idCompra = idCompra;
        this.idProductoVariante = idProductoVariante;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.subtotal = subtotal;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdProductoVariante() {
        return idProductoVariante;
    }

    public void setIdProductoVariante(int idProductoVariante) {
        this.idProductoVariante = idProductoVariante;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" +
                "idDetalle=" + idDetalle +
                ", idCompra=" + idCompra +
                ", idProductoVariante=" + idProductoVariante +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", descuento=" + descuento +
                ", subtotal=" + subtotal +
                '}';
    }
}