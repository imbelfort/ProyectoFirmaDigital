package Models;

import java.sql.Timestamp;

public class Categoria {
    private int id;
    private int idPropietario;
    private String nombre;
    private String descripcion;
    private String imagen;
    private Timestamp fechaModificacion;
    private Timestamp fechaCreacion;

    public Categoria() {
    }

    public Categoria(int id, int idPropietario, String nombre, String descripcion, Timestamp fechaModificacion) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaModificacion = fechaModificacion;
    }

    public Categoria(int id, int idPropietario, String nombre, String descripcion, String imagen, Timestamp fechaModificacion) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fechaModificacion = fechaModificacion;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
            ", idPropietario=" + idPropietario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                '}';
    }
}
