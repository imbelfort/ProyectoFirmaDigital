package Models;

import java.math.BigDecimal;
import java.sql.Date;

public class Compra {

    private int id;
    private int idPropietario;
    private int idProveedor;
    private String numCompra;
    private BigDecimal precioTotal;
    private Date fechaCompra;

    public Compra() {
        this.precioTotal = BigDecimal.ZERO;
    }

    public Compra(int id, int idPropietario, int idProveedor, String numCompra,
                  BigDecimal precioTotal, Date fechaCompra) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.idProveedor = idProveedor;
        this.numCompra = numCompra;
        this.precioTotal = precioTotal;
        this.fechaCompra = fechaCompra;
    }

    public Compra(int idPropietario, int idProveedor, String numCompra,
                  BigDecimal precioTotal, Date fechaCompra) {
        this.idPropietario = idPropietario;
        this.idProveedor = idProveedor;
        this.numCompra = numCompra;
        this.precioTotal = precioTotal;
        this.fechaCompra = fechaCompra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }


    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }


    public String getNumCompra() {
        return numCompra;
    }

    public void setNumCompra(String numCompra) {
        this.numCompra = numCompra;
    }


    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }


    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }


    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", idPropietario=" + idPropietario +
                ", idProveedor=" + idProveedor +
                ", numCompra='" + numCompra + '\'' +
                ", precioTotal=" + precioTotal +
                ", fechaCompra=" + fechaCompra +
                '}';
    }
}