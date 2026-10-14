package Models;

import java.sql.Timestamp;

public class DespachoLegajo {
    private int id;
    private int idTramite;
    private int idUsuarioDespachador;
    private String numeroDespacho;
    private String legajoPrevisualizadoUrl;
    private String tipoEntrega;
    private Timestamp fechaDespacho;
    private String estado;

    public DespachoLegajo() {
        this.tipoEntrega = "DIGITAL_CORREO";
        this.estado = "DESPACHADO";
    }

    public DespachoLegajo(int id, int idTramite, int idUsuarioDespachador, String numeroDespacho,
                          String legajoPrevisualizadoUrl, String tipoEntrega, Timestamp fechaDespacho, String estado) {
        this.id = id;
        this.idTramite = idTramite;
        this.idUsuarioDespachador = idUsuarioDespachador;
        this.numeroDespacho = numeroDespacho;
        this.legajoPrevisualizadoUrl = legajoPrevisualizadoUrl;
        this.tipoEntrega = tipoEntrega;
        this.fechaDespacho = fechaDespacho;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getIdUsuarioDespachador() { return idUsuarioDespachador; }
    public void setIdUsuarioDespachador(int idUsuarioDespachador) { this.idUsuarioDespachador = idUsuarioDespachador; }
    public String getNumeroDespacho() { return numeroDespacho; }
    public void setNumeroDespacho(String numeroDespacho) { this.numeroDespacho = numeroDespacho; }
    public String getLegajoPrevisualizadoUrl() { return legajoPrevisualizadoUrl; }
    public void setLegajoPrevisualizadoUrl(String legajoPrevisualizadoUrl) { this.legajoPrevisualizadoUrl = legajoPrevisualizadoUrl; }
    public String getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(String tipoEntrega) { this.tipoEntrega = tipoEntrega; }
    public Timestamp getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(Timestamp fechaDespacho) { this.fechaDespacho = fechaDespacho; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
