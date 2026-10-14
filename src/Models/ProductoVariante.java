package Models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductoVariante {

    private int id;
    private int idProducto;

    private String sku;
    private String talla;
    private String color;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal precioVentaMayorista;

    private int stockActual;
    private int stockMinimo;
    private int puntoReorden;

    private BigDecimal costoPromedio;

    private Integer idTecnicaInventario;
    private Integer idTecnicaCosto;

    private String estado;

    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public ProductoVariante() {
        this.precioCompra = BigDecimal.ZERO;
        this.precioVenta = BigDecimal.ZERO;
        this.precioVentaMayorista = BigDecimal.ZERO;
        this.stockActual = 0;
        this.stockMinimo = 0;
        this.puntoReorden = 0;
        this.costoPromedio = BigDecimal.ZERO;
        this.estado = "ACTIVO";
    }

    public ProductoVariante(int id, int idProducto, String sku,
                            String talla, String color,
                            BigDecimal precioCompra,
                            BigDecimal precioVenta,
                            BigDecimal precioVentaMayorista,
                            int stockActual,
                            int stockMinimo,
                            int puntoReorden,
                            BigDecimal costoPromedio,
                            Integer idTecnicaInventario,
                            Integer idTecnicaCosto,
                            String estado,
                            Timestamp fechaCreacion,
                            Timestamp fechaModificacion) {

        this.id = id;
        this.idProducto = idProducto;
        this.sku = sku;
        this.talla = talla;
        this.color = color;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.precioVentaMayorista = precioVentaMayorista;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.puntoReorden = puntoReorden;
        this.costoPromedio = costoPromedio;
        this.idTecnicaInventario = idTecnicaInventario;
        this.idTecnicaCosto = idTecnicaCosto;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public ProductoVariante(int idProducto, String sku,
                            String talla, String color,
                            BigDecimal precioCompra,
                            BigDecimal precioVenta,
                            BigDecimal precioVentaMayorista,
                            int stockActual,
                            int stockMinimo,
                            int puntoReorden,
                            BigDecimal costoPromedio,
                            Integer idTecnicaInventario,
                            Integer idTecnicaCosto,
                            String estado) {

        this.idProducto = idProducto;
        this.sku = sku;
        this.talla = talla;
        this.color = color;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.precioVentaMayorista = precioVentaMayorista;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.puntoReorden = puntoReorden;
        this.costoPromedio = costoPromedio;
        this.idTecnicaInventario = idTecnicaInventario;
        this.idTecnicaCosto = idTecnicaCosto;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }


    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }


    public BigDecimal getPrecioVentaMayorista() {
        return precioVentaMayorista;
    }

    public void setPrecioVentaMayorista(BigDecimal precioVentaMayorista) {
        this.precioVentaMayorista = precioVentaMayorista;
    }


    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }


    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }


    public int getPuntoReorden() {
        return puntoReorden;
    }

    public void setPuntoReorden(int puntoReorden) {
        this.puntoReorden = puntoReorden;
    }


    public BigDecimal getCostoPromedio() {
        return costoPromedio;
    }

    public void setCostoPromedio(BigDecimal costoPromedio) {
        this.costoPromedio = costoPromedio;
    }


    public Integer getIdTecnicaInventario() {
        return idTecnicaInventario;
    }

    public void setIdTecnicaInventario(Integer idTecnicaInventario) {
        this.idTecnicaInventario = idTecnicaInventario;
    }


    public Integer getIdTecnicaCosto() {
        return idTecnicaCosto;
    }

    public void setIdTecnicaCosto(Integer idTecnicaCosto) {
        this.idTecnicaCosto = idTecnicaCosto;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }


    @Override
    public String toString() {
        return "ProductoVariante{" +
                "id=" + id +
                ", idProducto=" + idProducto +
                ", sku='" + sku + '\'' +
                ", talla='" + talla + '\'' +
                ", color='" + color + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", precioVentaMayorista=" + precioVentaMayorista +
                ", stockActual=" + stockActual +
                ", stockMinimo=" + stockMinimo +
                ", puntoReorden=" + puntoReorden +
                ", costoPromedio=" + costoPromedio +
                ", idTecnicaInventario=" + idTecnicaInventario +
                ", idTecnicaCosto=" + idTecnicaCosto +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                '}';
    }
}