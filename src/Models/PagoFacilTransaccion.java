package Models;

import java.sql.Timestamp;

public class PagoFacilTransaccion {

    private int id;
    private int idPagoContado;
    private int idPagoCuota;
    private String paymentNumber;
    private String pagofacilTransactionId;
    private String paymentMethodTransactionId;
    private int paymentMethodId;
    private String qrBase64;
    private String checkoutUrl;
    private String deepLink;
    private String qrContentUrl;
    private String universalUrl;
    private Timestamp expirationDate;
    private String estado;
    private String correoSolicitante;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

    public PagoFacilTransaccion() {
        this.estado = "PENDIENTE";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPagoCuota() {
        return idPagoCuota;
    }

    public void setIdPagoCuota(int idPagoCuota) {
        this.idPagoCuota = idPagoCuota;
    }


    public int getIdPagoContado() {
        return idPagoContado;
    }

    public void setIdPagoContado(int idPagoContado) {
        this.idPagoContado = idPagoContado;
    }


    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }


    public String getPagofacilTransactionId() {
        return pagofacilTransactionId;
    }

    public void setPagofacilTransactionId(String pagofacilTransactionId) {
        this.pagofacilTransactionId = pagofacilTransactionId;
    }


    public String getPaymentMethodTransactionId() {
        return paymentMethodTransactionId;
    }

    public void setPaymentMethodTransactionId(String paymentMethodTransactionId) {
        this.paymentMethodTransactionId = paymentMethodTransactionId;
    }


    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }


    public String getQrBase64() {
        return qrBase64;
    }

    public void setQrBase64(String qrBase64) {
        this.qrBase64 = qrBase64;
    }


    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }


    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }


    public String getQrContentUrl() {
        return qrContentUrl;
    }

    public void setQrContentUrl(String qrContentUrl) {
        this.qrContentUrl = qrContentUrl;
    }


    public String getUniversalUrl() {
        return universalUrl;
    }

    public void setUniversalUrl(String universalUrl) {
        this.universalUrl = universalUrl;
    }


    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getCorreoSolicitante() {
        return correoSolicitante;
    }

    public void setCorreoSolicitante(String correoSolicitante) {
        this.correoSolicitante = correoSolicitante;
    }


    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}