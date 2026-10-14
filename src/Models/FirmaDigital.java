package Models;

import java.sql.Timestamp;

public class FirmaDigital {
    private int id;
    private int idTramite;
    private int idFirmante;
    private String tipoFirma;
    private String algoritmo;
    private String hashDocumento;
    private String firmaDigitalValor;
    private String numeroCertificadoDigital;
    private Timestamp fechaFirma;
    private boolean valido;

    public FirmaDigital() {
        this.tipoFirma = "CONSOLIDADA";
        this.algoritmo = "SHA256withRSA";
        this.valido = true;
    }

    public FirmaDigital(int id, int idTramite, int idFirmante, String tipoFirma, String algoritmo,
                        String hashDocumento, String firmaDigitalValor, String numeroCertificadoDigital,
                        Timestamp fechaFirma, boolean valido) {
        this.id = id;
        this.idTramite = idTramite;
        this.idFirmante = idFirmante;
        this.tipoFirma = tipoFirma;
        this.algoritmo = algoritmo;
        this.hashDocumento = hashDocumento;
        this.firmaDigitalValor = firmaDigitalValor;
        this.numeroCertificadoDigital = numeroCertificadoDigital;
        this.fechaFirma = fechaFirma;
        this.valido = valido;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTramite() { return idTramite; }
    public void setIdTramite(int idTramite) { this.idTramite = idTramite; }
    public int getIdFirmante() { return idFirmante; }
    public void setIdFirmante(int idFirmante) { this.idFirmante = idFirmante; }
    public String getTipoFirma() { return tipoFirma; }
    public void setTipoFirma(String tipoFirma) { this.tipoFirma = tipoFirma; }
    public String getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
    public String getHashDocumento() { return hashDocumento; }
    public void setHashDocumento(String hashDocumento) { this.hashDocumento = hashDocumento; }
    public String getFirmaDigitalValor() { return firmaDigitalValor; }
    public void setFirmaDigitalValor(String firmaDigitalValor) { this.firmaDigitalValor = firmaDigitalValor; }
    public String getNumeroCertificadoDigital() { return numeroCertificadoDigital; }
    public void setNumeroCertificadoDigital(String numeroCertificadoDigital) { this.numeroCertificadoDigital = numeroCertificadoDigital; }
    public Timestamp getFechaFirma() { return fechaFirma; }
    public void setFechaFirma(Timestamp fechaFirma) { this.fechaFirma = fechaFirma; }
    public boolean isValido() { return valido; }
    public void setValido(boolean valido) { this.valido = valido; }
}
