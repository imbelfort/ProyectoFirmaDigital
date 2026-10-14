package Models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Promocion {

    private int id;
    private int idPropietario;
    private String nombre;
    private String descripcion;
    private BigDecimal porcentajeDescuento;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public Promocion() {
        this.porcentajeDescuento = BigDecimal.ZERO;
        this.estado = "ACTIVA";
    }

    public Promocion(int id, int idPropietario, String nombre, String descripcion,
                     BigDecimal porcentajeDescuento, Date fechaInicio, Date fechaFin,
                     String estado, Timestamp fechaCreacion, Timestamp fechaModificacion) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
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


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }


    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
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
}