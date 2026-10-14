package Models;

import java.sql.Date;

public class Delegacion {
    private int id;
    private int idAutoridadTitular;
    private int idAutoridadDelegada;
    private String resolucion;
    private String motivo;
    private Date fechaInicio;
    private Date fechaFin;
    private String alcanceFirma;
    private String estado;

    public Delegacion() {
        this.alcanceFirma = "TOTAL";
        this.estado = "ACTIVA";
    }

    public Delegacion(int id, int idAutoridadTitular, int idAutoridadDelegada, String resolucion,
                      String motivo, Date fechaInicio, Date fechaFin, String alcanceFirma, String estado) {
        this.id = id;
        this.idAutoridadTitular = idAutoridadTitular;
        this.idAutoridadDelegada = idAutoridadDelegada;
        this.resolucion = resolucion;
        this.motivo = motivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.alcanceFirma = alcanceFirma;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdAutoridadTitular() { return idAutoridadTitular; }
    public void setIdAutoridadTitular(int idAutoridadTitular) { this.idAutoridadTitular = idAutoridadTitular; }
    public int getIdAutoridadDelegada() { return idAutoridadDelegada; }
    public void setIdAutoridadDelegada(int idAutoridadDelegada) { this.idAutoridadDelegada = idAutoridadDelegada; }
    public String getResolucion() { return resolucion; }
    public void setResolucion(String resolucion) { this.resolucion = resolucion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public String getAlcanceFirma() { return alcanceFirma; }
    public void setAlcanceFirma(String alcanceFirma) { this.alcanceFirma = alcanceFirma; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
