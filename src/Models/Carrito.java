package Models;

import java.sql.Date;

public class Carrito {

    private int id;
    private int idCliente;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    public Carrito() {
    }

    public Carrito(int id, int idCliente, Date fechaCreacion, Date fechaActualizacion) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Carrito(int idCliente, Date fechaCreacion, Date fechaActualizacion) {
        this.idCliente = idCliente;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
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
        return "Carrito{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}