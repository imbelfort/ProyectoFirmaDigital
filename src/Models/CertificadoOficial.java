package Models;

import java.sql.Date;
import java.sql.Timestamp;

public class CertificadoOficial {
    private int id;
    private int idTramite;
    private int idFirmaDigital;
    private String codigoVerificacion;
    private String qrData;
    private String tituloDocumento;
    private Timestamp fechaEmision;
    private Date fechaExpiracion;
    private String portalUrl;
    private String estado;

    public CertificadoOficial() {
        this.estado = "EMITIDO";
    }

    public CertificadoOficial(int id, int idTramite, int idFirmaDigital, String codigoVerificacion,
                              String qrData, String tituloDocumento, Timestamp fechaEmision,
                              Date fechaExpiracion, String portalUrl, String estado) {
        this.id = id;
        this.idTramite = idTramite;
        this.idFirmaDigital = idFirmaDigital;
        this.codigoVerificacion = codigoVerificacion;
        this.qrData = qrData;
        this.tituloDocumento = tituloDocumento;
        this.fechaEmision = fechaEmision;
        this.fechaExpiracion = fechaExpiracion;
        this.portalUrl = portalUrl;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getIdFirmaDigital() { return idFirmaDigital; }
    public void setIdFirmaDigital(int idFirmaDigital) { this.idFirmaDigital = idFirmaDigital; }
    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }
    public String getQrData() { return qrData; }
    public void setQrData(String qrData) { this.qrData = qrData; }
    public String getTituloDocumento() { return tituloDocumento; }
    public void setTituloDocumento(String tituloDocumento) { this.tituloDocumento = tituloDocumento; }
    public Timestamp getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Timestamp fechaEmision) { this.fechaEmision = fechaEmision; }
    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getPortalUrl() { return portalUrl; }
    public void setPortalUrl(String portalUrl) { this.portalUrl = portalUrl; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
