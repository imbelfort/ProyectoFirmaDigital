package Models;

import java.math.BigDecimal;

public class DetalleVenta {

    private int idDetalle;
    private int idVenta;
    private int idProductoVariante;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;

    public DetalleVenta() {
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
}