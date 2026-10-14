package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class Venta {

    private int id;
    private int idVendedor;
    private int idCliente;
    private int idPedido;
    private String numeroVenta;
    private String tipo;
    private String origen;
    private String tipoPago;
    private BigDecimal total;
    private String estado;
    private String observaciones;
    private Date fechaVenta;

    public Venta() {
        this.tipo = "MINORISTA";
        this.origen = "TIENDA_FISICA";
        this.tipoPago = "CONTADO";
        this.total = BigDecimal.ZERO;
        this.estado = "PENDIENTE";
    }

    public Venta(int id, int idVendedor, int idCliente, int idPedido, String numeroVenta,
                 String tipo, String origen, String tipoPago, BigDecimal total,
                 String estado, String observaciones, Date fechaVenta) {
        this.id = id;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.numeroVenta = numeroVenta;
        this.tipo = tipo;
        this.origen = origen;
        this.tipoPago = tipoPago;
        this.total = total;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaVenta = fechaVenta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }


    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }


    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }


    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }


    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
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


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", idVendedor=" + idVendedor +
                ", idCliente=" + idCliente +
                ", idPedido=" + idPedido +
                ", numeroVenta='" + numeroVenta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", origen='" + origen + '\'' +
                ", tipoPago='" + tipoPago + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", fechaVenta=" + fechaVenta +
                '}';
    }
}