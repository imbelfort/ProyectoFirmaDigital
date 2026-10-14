package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class MovimientoInventario {

    private int id;
    private int idProductoVariante;
    private int idTecnicaInventario;
    private int idTecnicaCosto;
    private String tipoMovimiento;
    private String motivo;
    private int cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private int stockAnterior;
    private int stockResultante;
    private Date fecha;
    private String observacion;

    public MovimientoInventario() {
        this.costoUnitario = BigDecimal.ZERO;
        this.costoTotal = BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdProductoVariante() {
        return idProductoVariante;
    }

    public void setIdProductoVariante(int idProductoVariante) {
        this.idProductoVariante = idProductoVariante;
    }


    public int getIdTecnicaInventario() {
        return idTecnicaInventario;
    }

    public void setIdTecnicaInventario(int idTecnicaInventario) {
        this.idTecnicaInventario = idTecnicaInventario;
    }


    public int getIdTecnicaCosto() {
        return idTecnicaCosto;
    }

    public void setIdTecnicaCosto(int idTecnicaCosto) {
        this.idTecnicaCosto = idTecnicaCosto;
    }


    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }


    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }


    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }


    public int getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(int stockAnterior) {
        this.stockAnterior = stockAnterior;
    }


    public int getStockResultante() {
        return stockResultante;
    }

    public void setStockResultante(int stockResultante) {
        this.stockResultante = stockResultante;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}