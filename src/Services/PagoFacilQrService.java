package Services;

import Utils.ConfigPagoFacil;
import Utils.SimpleJson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class PagoFacilQrService {

    private final PagoFacilAuthService authService;

    public PagoFacilQrService() {
        this.authService = new PagoFacilAuthService();
    }

    public PagoFacilQrResponse generarQr(String paymentNumber,
                                         String clientName,
                                         String documentId,
                                         String phoneNumber,
                                         String email,
                                         String clientCode,
                                         String product,
                                         double montoReal) throws Exception {

        String token = authService.obtenerAccessToken();

        String json = "{"
                + "\"paymentMethod\":" + obtenerPrimerPaymentMethodIdHabilitado() + ","
                + "\"clientName\":\"" + SimpleJson.escape(clientName) + "\","
                + "\"documentType\":1,"
                + "\"documentId\":\"" + SimpleJson.escape(documentId) + "\","
                + "\"phoneNumber\":\"" + SimpleJson.escape(phoneNumber) + "\","
                + "\"email\":\"" + SimpleJson.escape(email) + "\","
                + "\"paymentNumber\":\"" + SimpleJson.escape(paymentNumber) + "\","
                + "\"amount\":" + ConfigPagoFacil.MONTO_PRUEBA_QR + ","
                + "\"currency\":" + ConfigPagoFacil.CURRENCY_BOB + ","
                + "\"clientCode\":\"" + SimpleJson.escape(clientCode) + "\","
                + "\"callbackUrl\":\"" + SimpleJson.escape(ConfigPagoFacil.CALLBACK_URL) + "\","
                + "\"orderDetail\":["
                + "{"
                + "\"serial\":1,"
                + "\"product\":\"" + SimpleJson.escape(product) + "\","
                + "\"quantity\":1,"
                + "\"price\":" + ConfigPagoFacil.MONTO_PRUEBA_QR + ","
                + "\"discount\":0,"
                + "\"total\":" + ConfigPagoFacil.MONTO_PRUEBA_QR
                + "}"
                + "]"
                + "}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ConfigPagoFacil.URL_BASE + "/generate-qr"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("Response-Language", ConfigPagoFacil.RESPONSE_LANGUAGE)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        String qrBase64 = SimpleJson.getString(body, "qrBase64");

        if (qrBase64 == null || qrBase64.trim().isEmpty()) {
            throw new RuntimeException("PagoFacil no devolvio QR. Respuesta: " + body);
        }

        PagoFacilQrResponse qr = new PagoFacilQrResponse();

        qr.setTransactionId(SimpleJson.getString(body, "transactionId"));
        qr.setPaymentMethodTransactionId(SimpleJson.getString(body, "paymentMethodTransactionId"));
        qr.setQrBase64(qrBase64);
        qr.setCheckoutUrl(SimpleJson.getString(body, "checkoutUrl"));
        qr.setDeepLink(SimpleJson.getString(body, "deepLink"));
        qr.setQrContentUrl(SimpleJson.getString(body, "qrContentUrl"));
        qr.setUniversalUrl(SimpleJson.getString(body, "universalUrl"));

        String expiration = SimpleJson.getString(body, "expirationDate");

        if (expiration != null && !expiration.trim().isEmpty()) {
            try {
                qr.setExpirationDate(Timestamp.valueOf(expiration));
            } catch (Exception ignored) {
            }
        }

        return qr;
    }

    public int obtenerPrimerPaymentMethodIdHabilitado() throws Exception {
        String token = authService.obtenerAccessToken();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ConfigPagoFacil.URL_BASE + "/list-enabled-services"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("Response-Language", ConfigPagoFacil.RESPONSE_LANGUAGE)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        System.out.println("RESPUESTA /list-enabled-services:");
        System.out.println(body);

        String paymentMethodId = SimpleJson.getNumberAsString(body, "paymentMethodId");

        if (paymentMethodId == null || paymentMethodId.trim().isEmpty()) {
            throw new RuntimeException("No se encontro paymentMethodId habilitado. Respuesta: " + body);
        }

        return Integer.parseInt(paymentMethodId);
    }
}