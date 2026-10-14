package Models;

import java.math.BigDecimal;

public class CarritoDetalle {

    private int idCarrito;
    private int idProductoVariante;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;

    public CarritoDetalle() {
        this.precioUnitario = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
    }

    public CarritoDetalle(int idCarrito, int idProductoVariante, int cantidad,
                          BigDecimal precioUnitario, BigDecimal descuento) {
        this.idCarrito = idCarrito;
        this.idProductoVariante = idProductoVariante;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
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

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "CarritoDetalle{" +
                "idCarrito=" + idCarrito +
                ", idProductoVariante=" + idProductoVariante +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", descuento=" + descuento +
                '}';
    }
}