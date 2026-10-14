package Models;

import java.sql.Timestamp;

public class AlertaSLA {
    private int id;
    private int idMonitoreo;
    private int idTramite;
    private String tipoAlerta;
    private String mensaje;
    private Timestamp fechaAlerta;
    private String notificadoA;

    public AlertaSLA() {
        this.tipoAlerta = "ADVERTENCIA_24H";
    }

    public AlertaSLA(int id, int idMonitoreo, int idTramite, String tipoAlerta, String mensaje, Timestamp fechaAlerta, String notificadoA) {
        this.id = id;
        this.idMonitoreo = idMonitoreo;
        this.idTramite = idTramite;
        this.tipoAlerta = tipoAlerta;
        this.mensaje = mensaje;
        this.fechaAlerta = fechaAlerta;
        this.notificadoA = notificadoA;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMonitoreo() { return idMonitoreo; }
    public void setIdMonitoreo(int idMonitoreo) { this.idMonitoreo = idMonitoreo; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Timestamp getFechaAlerta() { return fechaAlerta; }
    public void setFechaAlerta(Timestamp fechaAlerta) { this.fechaAlerta = fechaAlerta; }
    public String getNotificadoA() { return notificadoA; }
    public void setNotificadoA(String notificadoA) { this.notificadoA = notificadoA; }
}
