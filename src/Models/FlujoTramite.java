package Models;

import java.sql.Timestamp;

public class FlujoTramite {
    private int id;
    private int idTramite;
    private String etapaActual;
    private int idUsuarioResponsable;
    private Timestamp fechaInicioEtapa;
    private Timestamp fechaFinEtapa;
    private String estadoEtapa;

    public FlujoTramite() {
        this.estadoEtapa = "EN_PROCESO";
    }

    public FlujoTramite(int id, int idTramite, String etapaActual, int idUsuarioResponsable,
                        Timestamp fechaInicioEtapa, Timestamp fechaFinEtapa, String estadoEtapa) {
        this.id = id;
        this.idTramite = idTramite;
        this.etapaActual = etapaActual;
        this.idUsuarioResponsable = idUsuarioResponsable;
        this.fechaInicioEtapa = fechaInicioEtapa;
        this.fechaFinEtapa = fechaFinEtapa;
        this.estadoEtapa = estadoEtapa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public String getEtapaActual() { return etapaActual; }
    public void setEtapaActual(String etapaActual) { this.etapaActual = etapaActual; }
    public int getIdUsuarioResponsable() { return idUsuarioResponsable; }
    public void setIdUsuarioResponsable(int idUsuarioResponsable) { this.idUsuarioResponsable = idUsuarioResponsable; }
    public Timestamp getFechaInicioEtapa() { return fechaInicioEtapa; }
    public void setFechaInicioEtapa(Timestamp fechaInicioEtapa) { this.fechaInicioEtapa = fechaInicioEtapa; }
    public Timestamp getFechaFinEtapa() { return fechaFinEtapa; }
    public void setFechaFinEtapa(Timestamp fechaFinEtapa) { this.fechaFinEtapa = fechaFinEtapa; }
    public String getEstadoEtapa() { return estadoEtapa; }
    public void setEstadoEtapa(String estadoEtapa) { this.estadoEtapa = estadoEtapa; }
}
