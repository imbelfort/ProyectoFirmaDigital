package Models;

import java.sql.Timestamp;

public class BitacoraTramite {
    private int id;
    private int idTramite;
    private Integer idUsuario;
    private String accion;
    private String detalle;
    private String ipOrigen;
    private Timestamp fechaEvento;

    public BitacoraTramite() {
    }

    public BitacoraTramite(int id, int idTramite, Integer idUsuario, String accion,
                           String detalle, String ipOrigen, Timestamp fechaEvento) {
        this.id = id;
        this.idTramite = idTramite;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.detalle = detalle;
        this.ipOrigen = ipOrigen;
        this.fechaEvento = fechaEvento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }
    public Timestamp getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(Timestamp fechaEvento) { this.fechaEvento = fechaEvento; }
}
