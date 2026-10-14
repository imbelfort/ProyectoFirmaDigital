package Models;

import java.math.BigDecimal;

public class PedidoDetalle {

    private int idDetalle;
    private int idPedido;
    private int idProductoVariante;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;

    public PedidoDetalle() {
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
    }

    public PedidoDetalle(int idDetalle, int idPedido, int idProductoVariante, int cantidad,
                         BigDecimal precioUnitario, BigDecimal subtotal, BigDecimal descuento) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProductoVariante = idProductoVariante;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.descuento = descuento;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    @Override
    public String toString() {
        return "PedidoDetalle{" +
                "idDetalle=" + idDetalle +
                ", idPedido=" + idPedido +
                ", idProductoVariante=" + idProductoVariante +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                '}';
    }
}