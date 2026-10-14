package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class PagoCuota {

    private int id;
    private int idCredito;
    private int idMetodoPago;
    private int numeroCuota;
    private BigDecimal interesCuota;
    private Date fechaVencimiento;
    private Date fechaPago;
    private BigDecimal montoPagado;
    private int diasMora;
    private BigDecimal montoPendiente;
    private String estado;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    public PagoCuota() {
        this.interesCuota = BigDecimal.ZERO;
        this.montoPagado = BigDecimal.ZERO;
        this.montoPendiente = BigDecimal.ZERO;
        this.diasMora = 0;
        this.estado = "PENDIENTE";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(int idCredito) {
        this.idCredito = idCredito;
    }


    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }


    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }


    public BigDecimal getInteresCuota() {
        return interesCuota;
    }

    public void setInteresCuota(BigDecimal interesCuota) {
        this.interesCuota = interesCuota;
    }


    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }


    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }


    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }


    public int getDiasMora() {
        return diasMora;
    }

    public void setDiasMora(int diasMora) {
        this.diasMora = diasMora;
    }


    public BigDecimal getMontoPendiente() {
        return montoPendiente;
    }

    public void setMontoPendiente(BigDecimal montoPendiente) {
        this.montoPendiente = montoPendiente;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "PagoCuota{" +
                "id=" + id +
                ", idCredito=" + idCredito +
                ", idMetodoPago=" + idMetodoPago +
                ", numeroCuota=" + numeroCuota +
                ", interesCuota=" + interesCuota +
                ", fechaVencimiento=" + fechaVencimiento +
                ", fechaPago=" + fechaPago +
                ", montoPagado=" + montoPagado +
                ", diasMora=" + diasMora +
                ", montoPendiente=" + montoPendiente +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}