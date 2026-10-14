package Comunication;

import Services.PagoFacilCallbackService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class PagoFacilCallbackServer {

    private static HttpServer server;

    public static void iniciar(int puerto) {
        try {
            if (server != null) {
                return;
            }

            server = HttpServer.create(new InetSocketAddress(puerto), 0);

            server.createContext("/pagofacil/callback", PagoFacilCallbackServer::handleCallback);

            server.setExecutor(null);
            server.start();

            System.out.println("[OK] Callback PagoFacil activo en puerto " + puerto);

        } catch (Exception e) {
            System.out.println("[ERROR] No se pudo iniciar callback PagoFacil: " + e.getMessage());
        }
    }

    private static void handleCallback(HttpExchange exchange) {
        try {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                responder(exchange, 405, "{\"error\":1,\"status\":0,\"message\":\"Metodo no permitido\",\"values\":false}");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            PagoFacilCallbackService service = new PagoFacilCallbackService();
            String respuesta = service.procesarCallback(body);

            responder(exchange, 200, respuesta);

        } catch (Exception e) {
            try {
                responder(exchange, 200, "{\"error\":0,\"status\":1,\"message\":\"Callback recibido con observacion\",\"values\":true}");
            } catch (Exception ignored) {
            }
        }
    }

    private static void responder(HttpExchange exchange, int statusCode, String respuesta) throws Exception {
        byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}