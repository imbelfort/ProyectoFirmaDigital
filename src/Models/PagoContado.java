package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class PagoContado {

    private int id;
    private int idVenta;
    private int idMetodoPago;
    private Date fecha;
    private BigDecimal montoPago;
    private String estado;
    private BigDecimal interesMoraCobrado;
    private BigDecimal recargoExtra;
    private String observaciones;

    public PagoContado() {
        this.montoPago = BigDecimal.ZERO;
        this.interesMoraCobrado = BigDecimal.ZERO;
        this.recargoExtra = BigDecimal.ZERO;
        this.estado = "PENDIENTE";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }


    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public BigDecimal getInteresMoraCobrado() {
        return interesMoraCobrado;
    }

    public void setInteresMoraCobrado(BigDecimal interesMoraCobrado) {
        this.interesMoraCobrado = interesMoraCobrado;
    }


    public BigDecimal getRecargoExtra() {
        return recargoExtra;
    }

    public void setRecargoExtra(BigDecimal recargoExtra) {
        this.recargoExtra = recargoExtra;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}