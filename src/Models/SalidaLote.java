package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class SalidaLote {

    private int id;
    private int idDetalleVenta;
    private int idLote;
    private int cantidad;
    private BigDecimal costoUnitarioAplicado;
    private BigDecimal costoTotal;
    private Date fechaSalida;

    public SalidaLote() {
        this.costoUnitarioAplicado = BigDecimal.ZERO;
        this.costoTotal = BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }


    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public BigDecimal getCostoUnitarioAplicado() {
        return costoUnitarioAplicado;
    }

    public void setCostoUnitarioAplicado(BigDecimal costoUnitarioAplicado) {
        this.costoUnitarioAplicado = costoUnitarioAplicado;
    }


    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }


    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}