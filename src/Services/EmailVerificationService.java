package Services;

import Utils.ConfigEmailVerification;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class EmailVerificationService {

    public EmailVerificationResult verificarEmail(String email) throws Exception {
        EmailVerificationResult resultado = new EmailVerificationResult();

        if (!ConfigEmailVerification.EMAIL_VERIFICATION_ENABLED) {
            resultado.setValido(true);
            resultado.setRazon("Verificacion desactivada");
            return resultado;
        }

        if (email == null || email.trim().isEmpty()) {
            resultado.setValido(false);
            resultado.setRazon("El email es obligatorio");
            return resultado;
        }

        String emailLimpio = email.trim();
        String encodedEmail = URLEncoder.encode(emailLimpio, StandardCharsets.UTF_8);

        String url = ConfigEmailVerification.ABSTRACT_API_URL +
                "?api_key=" + ConfigEmailVerification.ABSTRACT_API_KEY +
                "&email=" + encodedEmail;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        System.out.println("RESPUESTA VERIFICACION EMAIL:");
        System.out.println(body);

        if (body == null || body.trim().isEmpty()) {
            resultado.setValido(false);
            resultado.setRazon("La API no devolvio respuesta");
            return resultado;
        }

        if (body.contains("\"Invalid API key") || body.contains("\"unauthorized\"")) {
            resultado.setValido(false);
            resultado.setRazon("API KEY invalida para el servicio de verificacion de email");
            return resultado;
        }

        if (body.contains("\"error\"")) {
            resultado.setValido(false);
            resultado.setRazon("Error en la API de verificacion: " + body);
            return resultado;
        }

        boolean formatoValido = getBoolean(body, "is_format_valid");
        boolean mxValido = getBoolean(body, "is_mx_valid");
        boolean smtpValido = getBoolean(body, "is_smtp_valid");
        boolean desechable = getBoolean(body, "is_disposable");

        String status = getString(body, "status");
        String addressRiskStatus = getString(body, "address_risk_status");
        String domainRiskStatus = getString(body, "domain_risk_status");
        double score = getDouble(body, "score", 0.0);

        resultado.setFormatoValido(formatoValido);
        resultado.setDominioValido(mxValido);
        resultado.setEntregable(smtpValido);
        resultado.setDesechable(desechable);

        if (!formatoValido) {
            resultado.setValido(false);
            resultado.setRazon("El formato del email no es valido");
            return resultado;
        }

        if (!mxValido) {
            resultado.setValido(false);
            resultado.setRazon("El dominio del email no existe o no recibe correos");
            return resultado;
        }

        if (!"deliverable".equalsIgnoreCase(status)) {
            resultado.setValido(false);
            resultado.setRazon("El email no es entregable. Estado: " + status);
            return resultado;
        }

        if (desechable) {
            resultado.setValido(false);
            resultado.setRazon("No se permiten correos temporales o desechables");
            return resultado;
        }

        if ("high".equalsIgnoreCase(addressRiskStatus) || "high".equalsIgnoreCase(domainRiskStatus)) {
            resultado.setValido(false);
            resultado.setRazon("El email fue marcado como riesgoso por la API");
            return resultado;
        }

        if (score > 0 && score < 0.50) {
            resultado.setValido(false);
            resultado.setRazon("El email tiene baja reputacion. Score: " + score);
            return resultado;
        }

        resultado.setValido(true);
        resultado.setRazon("Email verificado correctamente");
        return resultado;
    }

    private boolean getBoolean(String json, String key) {
        if (json == null || key == null) {
            return false;
        }

        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);

        if (index < 0) {
            return false;
        }

        int colon = json.indexOf(":", index);

        if (colon < 0) {
            return false;
        }

        String value = json.substring(colon + 1).trim();

        return value.startsWith("true");
    }

    private String getString(String json, String key) {
        if (json == null || key == null) {
            return null;
        }

        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);

        if (index < 0) {
            return null;
        }

        int colon = json.indexOf(":", index);

        if (colon < 0) {
            return null;
        }

        int firstQuote = json.indexOf("\"", colon + 1);

        if (firstQuote < 0) {
            return null;
        }

        int secondQuote = json.indexOf("\"", firstQuote + 1);

        if (secondQuote < 0) {
            return null;
        }

        return json.substring(firstQuote + 1, secondQuote);
    }

    private double getDouble(String json, String key, double defaultValue) {
        if (json == null || key == null) {
            return defaultValue;
        }

        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);

        if (index < 0) {
            return defaultValue;
        }

        int colon = json.indexOf(":", index);

        if (colon < 0) {
            return defaultValue;
        }

        int start = colon + 1;
        int end = start;

        while (end < json.length()) {
            char c = json.charAt(end);

            if ((c >= '0' && c <= '9') || c == '.') {
                end++;
            } else {
                break;
            }
        }

        try {
            return Double.parseDouble(json.substring(start, end).trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}