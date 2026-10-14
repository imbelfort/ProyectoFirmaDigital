package Models;

import java.sql.Timestamp;

public class MonitoreoSLA {
    private int id;
    private int idTramite;
    private int horasSlaLimite;
    private Timestamp fechaInicioSla;
    private Timestamp fechaLimiteSla;
    private double horasTranscurridas;
    private String estadoSla;
    private int alertasEmitidas;
    private Timestamp ultimaAlerta;

    public MonitoreoSLA() {
        this.horasSlaLimite = 72;
        this.estadoSla = "EN_TIEMPO";
        this.alertasEmitidas = 0;
    }

    public MonitoreoSLA(int id, int idTramite, int horasSlaLimite, Timestamp fechaInicioSla,
                        Timestamp fechaLimiteSla, double horasTranscurridas, String estadoSla,
                        int alertasEmitidas, Timestamp ultimaAlerta) {
        this.id = id;
        this.idTramite = idTramite;
        this.horasSlaLimite = horasSlaLimite;
        this.fechaInicioSla = fechaInicioSla;
        this.fechaLimiteSla = fechaLimiteSla;
        this.horasTranscurridas = horasTranscurridas;
        this.estadoSla = estadoSla;
        this.alertasEmitidas = alertasEmitidas;
        this.ultimaAlerta = ultimaAlerta;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getHorasSlaLimite() { return horasSlaLimite; }
    public void setHorasSlaLimite(int horasSlaLimite) { this.horasSlaLimite = horasSlaLimite; }
    public Timestamp getFechaInicioSla() { return fechaInicioSla; }
    public void setFechaInicioSla(Timestamp fechaInicioSla) { this.fechaInicioSla = fechaInicioSla; }
    public Timestamp getFechaLimiteSla() { return fechaLimiteSla; }
    public void setFechaLimiteSla(Timestamp fechaLimiteSla) { this.fechaLimiteSla = fechaLimiteSla; }
    public double getHorasTranscurridas() { return horasTranscurridas; }
    public void setHorasTranscurridas(double horasTranscurridas) { this.horasTranscurridas = horasTranscurridas; }
    public String getEstadoSla() { return estadoSla; }
    public void setEstadoSla(String estadoSla) { this.estadoSla = estadoSla; }
    public int getAlertasEmitidas() { return alertasEmitidas; }
    public void setAlertasEmitidas(int alertasEmitidas) { this.alertasEmitidas = alertasEmitidas; }
    public Timestamp getUltimaAlerta() { return ultimaAlerta; }
    public void setUltimaAlerta(Timestamp ultimaAlerta) { this.ultimaAlerta = ultimaAlerta; }
}
