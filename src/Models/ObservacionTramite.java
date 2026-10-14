package Models;

import java.sql.Timestamp;

public class ObservacionTramite {
    private int id;
    private int idTramite;
    private int idUsuario;
    private String tipoObservacion;
    private String descripcion;
    private String estado;
    private Timestamp fechaObservacion;
    private Timestamp fechaSubsanacion;

    public ObservacionTramite() {
        this.tipoObservacion = "DOCUMENTO_OBSERVADO";
        this.estado = "ABIERTA";
    }

    public ObservacionTramite(int id, int idTramite, int idUsuario, String tipoObservacion,
                              String descripcion, String estado, Timestamp fechaObservacion, Timestamp fechaSubsanacion) {
        this.id = id;
        this.idTramite = idTramite;
        this.idUsuario = idUsuario;
        this.tipoObservacion = tipoObservacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaObservacion = fechaObservacion;
        this.fechaSubsanacion = fechaSubsanacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getTipoObservacion() { return tipoObservacion; }
    public void setTipoObservacion(String tipoObservacion) { this.tipoObservacion = tipoObservacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Timestamp getFechaObservacion() { return fechaObservacion; }
    public void setFechaObservacion(Timestamp fechaObservacion) { this.fechaObservacion = fechaObservacion; }
    public Timestamp getFechaSubsanacion() { return fechaSubsanacion; }
    public void setFechaSubsanacion(Timestamp fechaSubsanacion) { this.fechaSubsanacion = fechaSubsanacion; }
}
