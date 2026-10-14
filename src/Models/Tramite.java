package Models;

import java.sql.Timestamp;

public class Tramite {
    private int id;
    private String codigoTramite;
    private int idUsuarioSolicitante;
    private int idTipoTramite;
    private String asunto;
    private String estado;
    private String prioridad;
    private Timestamp fechaSolicitud;
    private Timestamp fechaFinalizacion;

    public Tramite() {
        this.estado = "INICIADO";
        this.prioridad = "NORMAL";
    }

    public Tramite(int id, String codigoTramite, int idUsuarioSolicitante, int idTipoTramite,
                   String asunto, String estado, String prioridad, Timestamp fechaSolicitud, Timestamp fechaFinalizacion) {
        this.id = id;
        this.codigoTramite = codigoTramite;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idTipoTramite = idTipoTramite;
        this.asunto = asunto;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigoTramite() { return codigoTramite; }
    public void setCodigoTramite(String codigoTramite) { this.codigoTramite = codigoTramite; }
    public int getIdUsuarioSolicitante() { return idUsuarioSolicitante; }
    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) { this.idUsuarioSolicitante = idUsuarioSolicitante; }
    public int getIdTipoTramite() { return idTipoTramite; }
    public void setIdTipoTramite(int idTipoTramite) { this.idTipoTramite = idTipoTramite; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public Timestamp getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Timestamp fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public Timestamp getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(Timestamp fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }
}
