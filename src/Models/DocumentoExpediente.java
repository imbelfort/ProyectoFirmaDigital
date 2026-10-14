package Models;

import java.sql.Timestamp;

public class DocumentoExpediente {
    private int id;
    private int idTramite;
    private int idRequisito;
    private String nombreDocumento;
    private String tipoDocumento;
    private String archivoUrl;
    private String hashDocumento;
    private String estadoValidacion;
    private String observacionValidacion;
    private Timestamp fechaValidacion;

    public DocumentoExpediente() {
        this.tipoDocumento = "PDF";
        this.estadoValidacion = "PENDIENTE";
    }

    public DocumentoExpediente(int id, int idTramite, int idRequisito, String nombreDocumento,
                               String tipoDocumento, String archivoUrl, String hashDocumento,
                               String estadoValidacion, String observacionValidacion, Timestamp fechaValidacion) {
        this.id = id;
        this.idTramite = idTramite;
        this.idRequisito = idRequisito;
        this.nombreDocumento = nombreDocumento;
        this.tipoDocumento = tipoDocumento;
        this.archivoUrl = archivoUrl;
        this.hashDocumento = hashDocumento;
        this.estadoValidacion = estadoValidacion;
        this.observacionValidacion = observacionValidacion;
        this.fechaValidacion = fechaValidacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getIdRequisito() { return idRequisito; }
    public void setIdRequisito(int idRequisito) { this.idRequisito = idRequisito; }
    public String getNombreDocumento() { return nombreDocumento; }
    public void setNombreDocumento(String nombreDocumento) { this.nombreDocumento = nombreDocumento; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getArchivoUrl() { return archivoUrl; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }
    public String getHashDocumento() { return hashDocumento; }
    public void setHashDocumento(String hashDocumento) { this.hashDocumento = hashDocumento; }
    public String getEstadoValidacion() { return estadoValidacion; }
    public void setEstadoValidacion(String estadoValidacion) { this.estadoValidacion = estadoValidacion; }
    public String getObservacionValidacion() { return observacionValidacion; }
    public void setObservacionValidacion(String observacionValidacion) { this.observacionValidacion = observacionValidacion; }
    public Timestamp getFechaValidacion() { return fechaValidacion; }
    public void setFechaValidacion(Timestamp fechaValidacion) { this.fechaValidacion = fechaValidacion; }
}
