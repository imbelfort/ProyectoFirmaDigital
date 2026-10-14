package Models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Producto {

    private int id;
    private int idCategoria;

    private String nombre;
    private String codigo;
    private String imagen;
    private String qr;
    private String descripcion;

    private String marca;
    private String genero;

    private BigDecimal precioVentaBase;
    private BigDecimal precioVentaMayoristaBase;

    private String estado;

    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public Producto() {
        this.precioVentaBase = BigDecimal.ZERO;
        this.precioVentaMayoristaBase = BigDecimal.ZERO;
        this.estado = "ACTIVO";
    }

    public Producto(int id, int idCategoria, String nombre, String codigo,
                    String imagen, String qr, String descripcion,
                    String marca, String genero,
                    BigDecimal precioVentaBase,
                    BigDecimal precioVentaMayoristaBase,
                    String estado,
                    Timestamp fechaCreacion,
                    Timestamp fechaModificacion) {

        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.qr = qr;
        this.descripcion = descripcion;
        this.marca = marca;
        this.genero = genero;
        this.precioVentaBase = precioVentaBase;
        this.precioVentaMayoristaBase = precioVentaMayoristaBase;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Producto(int idCategoria, String nombre, String codigo,
                    String imagen, String qr, String descripcion,
                    String marca, String genero,
                    BigDecimal precioVentaBase,
                    BigDecimal precioVentaMayoristaBase,
                    String estado) {

        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.codigo = codigo;
        this.imagen = imagen;
        this.qr = qr;
        this.descripcion = descripcion;
        this.marca = marca;
        this.genero = genero;
        this.precioVentaBase = precioVentaBase;
        this.precioVentaMayoristaBase = precioVentaMayoristaBase;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }


    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }


    public BigDecimal getPrecioVentaBase() {
        return precioVentaBase;
    }

    public void setPrecioVentaBase(BigDecimal precioVentaBase) {
        this.precioVentaBase = precioVentaBase;
    }


    public BigDecimal getPrecioVentaMayoristaBase() {
        return precioVentaMayoristaBase;
    }

    public void setPrecioVentaMayoristaBase(BigDecimal precioVentaMayoristaBase) {
        this.precioVentaMayoristaBase = precioVentaMayoristaBase;
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
        return "Producto{" +
                "id=" + id +
                ", idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", qr='" + qr + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", marca='" + marca + '\'' +
                ", genero='" + genero + '\'' +
                ", precioVentaBase=" + precioVentaBase +
                ", precioVentaMayoristaBase=" + precioVentaMayoristaBase +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                '}';
    }
}