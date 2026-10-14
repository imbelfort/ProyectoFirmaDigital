package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class Credito {

    private int id;
    private int idVenta;
    private BigDecimal montoCredito;
    private BigDecimal montoPendiente;
    private BigDecimal montoPagado;
    private Date fechaOtorgamiento;
    private Date fechaVencimiento;
    private BigDecimal interes;
    private String estado;
    private int cuotasTotal;
    private int diasMora;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    public Credito() {
        this.montoCredito = BigDecimal.ZERO;
        this.montoPendiente = BigDecimal.ZERO;
        this.montoPagado = BigDecimal.ZERO;
        this.interes = BigDecimal.ZERO;
        this.estado = "ACTIVO";
        this.cuotasTotal = 0;
        this.diasMora = 0;
    }

    public Credito(int id, int idVenta, BigDecimal montoCredito, BigDecimal montoPendiente,
                   BigDecimal montoPagado, Date fechaOtorgamiento, Date fechaVencimiento,
                   BigDecimal interes, String estado, int cuotasTotal, int diasMora,
                   Date fechaCreacion, Date fechaActualizacion) {
        this.id = id;
        this.idVenta = idVenta;
        this.montoCredito = montoCredito;
        this.montoPendiente = montoPendiente;
        this.montoPagado = montoPagado;
        this.fechaOtorgamiento = fechaOtorgamiento;
        this.fechaVencimiento = fechaVencimiento;
        this.interes = interes;
        this.estado = estado;
        this.cuotasTotal = cuotasTotal;
        this.diasMora = diasMora;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
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


    public BigDecimal getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(BigDecimal montoCredito) {
        this.montoCredito = montoCredito;
    }


    public BigDecimal getMontoPendiente() {
        return montoPendiente;
    }

    public void setMontoPendiente(BigDecimal montoPendiente) {
        this.montoPendiente = montoPendiente;
    }


    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }


    public Date getFechaOtorgamiento() {
        return fechaOtorgamiento;
    }

    public void setFechaOtorgamiento(Date fechaOtorgamiento) {
        this.fechaOtorgamiento = fechaOtorgamiento;
    }


    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }


    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public int getCuotasTotal() {
        return cuotasTotal;
    }

    public void setCuotasTotal(int cuotasTotal) {
        this.cuotasTotal = cuotasTotal;
    }


    public int getDiasMora() {
        return diasMora;
    }

    public void setDiasMora(int diasMora) {
        this.diasMora = diasMora;
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
        return "Credito{" +
                "id=" + id +
                ", idVenta=" + idVenta +
                ", montoCredito=" + montoCredito +
                ", montoPendiente=" + montoPendiente +
                ", montoPagado=" + montoPagado +
                ", fechaOtorgamiento=" + fechaOtorgamiento +
                ", fechaVencimiento=" + fechaVencimiento +
                ", interes=" + interes +
                ", estado='" + estado + '\'' +
                ", cuotasTotal=" + cuotasTotal +
                ", diasMora=" + diasMora +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}