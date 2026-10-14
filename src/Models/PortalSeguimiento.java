package Models;

import java.sql.Timestamp;

public class PortalSeguimiento {
    private int id;
    private int idTramite;
    private String pinSeguimiento;
    private boolean portalActivo;
    private int totalVisitas;
    private Timestamp ultimoAcceso;

    public PortalSeguimiento() {
        this.portalActivo = true;
        this.totalVisitas = 0;
    }

    public PortalSeguimiento(int id, int idTramite, String pinSeguimiento, boolean portalActivo, int totalVisitas, Timestamp ultimoAcceso) {
        this.id = id;
        this.idTramite = idTramite;
        this.pinSeguimiento = pinSeguimiento;
        this.portalActivo = portalActivo;
        this.totalVisitas = totalVisitas;
        this.ultimoAcceso = ultimoAcceso;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public String getPinSeguimiento() { return pinSeguimiento; }
    public void setPinSeguimiento(String pinSeguimiento) { this.pinSeguimiento = pinSeguimiento; }
    public boolean isPortalActivo() { return portalActivo; }
    public void setPortalActivo(boolean portalActivo) { this.portalActivo = portalActivo; }
    public int getTotalVisitas() { return totalVisitas; }
    public void setTotalVisitas(int totalVisitas) { this.totalVisitas = totalVisitas; }
    public Timestamp getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(Timestamp ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
}
