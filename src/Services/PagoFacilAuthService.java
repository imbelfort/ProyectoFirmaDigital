package Services;

import Utils.ConfigPagoFacil;
import Utils.SimpleJson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class PagoFacilAuthService {

    private String accessToken;
    private LocalDateTime expiresAt;

    public String obtenerAccessToken() throws Exception {
        if (accessToken != null && expiresAt != null && LocalDateTime.now().isBefore(expiresAt.minusMinutes(2))) {
            return accessToken;
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ConfigPagoFacil.URL_BASE + "/login"))
                .header("tcTokenService", ConfigPagoFacil.TOKEN_SERVICE)
                .header("tcTokenSecret", ConfigPagoFacil.TOKEN_SECRET)
                .header("Response-Language", ConfigPagoFacil.RESPONSE_LANGUAGE)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        String token = SimpleJson.getString(body, "accessToken");
        String expires = SimpleJson.getNumberAsString(body, "expiresInMinutes");

        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("No se pudo obtener accessToken de PagoFacil. Respuesta: " + body);
        }

        double minutes = 10;

        if (expires != null) {
            try {
                minutes = Double.parseDouble(expires);
            } catch (Exception ignored) {
            }
        }

        this.accessToken = token;
        this.expiresAt = LocalDateTime.now().plusMinutes((long) minutes);

        return accessToken;
    }
}