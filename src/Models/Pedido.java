package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class Pedido {

    private int id;
    private int idCliente;
    private int idCarrito;
    private String numeroPedido;
    private Date fechaPedido;
    private Date fechaPago;
    private Date fechaEnvio;
    private Date fechaEntrega;
    private Date fechaRecibidoConforme;
    private String tipoVenta;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String estado;
    private String direccionEntrega;
    private String observaciones;

    public Pedido() {
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.estado = "PENDIENTE";
        this.tipoVenta = "CORREO";
    }

    public Pedido(int id, int idCliente, int idCarrito, String numeroPedido, Date fechaPedido,
                  Date fechaPago, Date fechaEnvio, Date fechaEntrega, Date fechaRecibidoConforme,
                  String tipoVenta, BigDecimal subtotal, BigDecimal descuento, BigDecimal total,
                  String estado, String direccionEntrega, String observaciones) {
        this.id = id;
        this.idCliente = idCliente;
        this.idCarrito = idCarrito;
        this.numeroPedido = numeroPedido;
        this.fechaPedido = fechaPedido;
        this.fechaPago = fechaPago;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.fechaRecibidoConforme = fechaRecibidoConforme;
        this.tipoVenta = tipoVenta;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }


    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }


    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }


    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }


    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }


    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }


    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }


    public Date getFechaRecibidoConforme() {
        return fechaRecibidoConforme;
    }

    public void setFechaRecibidoConforme(Date fechaRecibidoConforme) {
        this.fechaRecibidoConforme = fechaRecibidoConforme;
    }


    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
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


    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", idCarrito=" + idCarrito +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", fechaPedido=" + fechaPedido +
                ", fechaPago=" + fechaPago +
                ", fechaEnvio=" + fechaEnvio +
                ", fechaEntrega=" + fechaEntrega +
                ", fechaRecibidoConforme=" + fechaRecibidoConforme +
                ", tipoVenta='" + tipoVenta + '\'' +
                ", subtotal=" + subtotal +
                ", descuento=" + descuento +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}