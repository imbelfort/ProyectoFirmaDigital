package Services;

import java.sql.Timestamp;

public class PagoFacilQrResponse {

    private String transactionId;
    private String paymentMethodTransactionId;
    private String qrBase64;
    private String checkoutUrl;
    private String deepLink;
    private String qrContentUrl;
    private String universalUrl;
    private Timestamp expirationDate;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getPaymentMethodTransactionId() {
        return paymentMethodTransactionId;
    }

    public void setPaymentMethodTransactionId(String paymentMethodTransactionId) {
        this.paymentMethodTransactionId = paymentMethodTransactionId;
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
}