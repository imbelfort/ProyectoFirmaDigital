package Services;

import DAO.CreditoDAO;
import DAO.PagoContadoDAO;
import DAO.PagoCuotaDAO;
import DAO.PagoFacilTransaccionDAO;
import DAO.VentaDAO;
import Models.PagoCuota;
import Models.PagoFacilTransaccion;
import Utils.SimpleJson;
import Comunication.HtmlBuilder;

import java.util.List;

public class PagoFacilCallbackService {

    private final PagoFacilTransaccionDAO transaccionDAO;
    private final VentaDAO ventaDAO;
    private final PagoContadoDAO pagoContadoDAO;
    private final PagoCuotaDAO pagoCuotaDAO;
    private final CreditoDAO creditoDAO;

    public PagoFacilCallbackService() {
        this.transaccionDAO = new PagoFacilTransaccionDAO();
        this.ventaDAO = new VentaDAO();
        this.pagoContadoDAO = new PagoContadoDAO();
        this.pagoCuotaDAO = new PagoCuotaDAO();
        this.creditoDAO = new CreditoDAO();
    }

    public String procesarCallback(String json) throws Exception {
        System.out.println("========================================");
        System.out.println("[PAGOFACIL CALLBACK] Callback recibido");
        System.out.println(json);
        System.out.println("========================================");

        String pedidoId = SimpleJson.getString(json, "PedidoID");
        int estadoPagoFacil = extraerEntero(json, "Estado");
        int metodoPago = extraerEntero(json, "MetodoPago");

        System.out.println("[PAGOFACIL CALLBACK] PedidoID: " + pedidoId);
        System.out.println("[PAGOFACIL CALLBACK] Estado PagoFacil: " + estadoPagoFacil);
        System.out.println("[PAGOFACIL CALLBACK] MetodoPago: " + metodoPago);

        if (pedidoId == null || pedidoId.trim().isEmpty()) {
            System.out.println("[PAGOFACIL CALLBACK] Callback recibido sin PedidoID");
            return respuestaOk("Callback recibido sin PedidoID");
        }

        PagoFacilTransaccion transaccion = transaccionDAO.findByPaymentNumber(pedidoId);

        if (transaccion == null) {
            System.out.println("[PAGOFACIL CALLBACK] Transaccion no encontrada para payment_number: " + pedidoId);
            return respuestaOk("Transaccion no encontrada");
        }

        System.out.println("[PAGOFACIL CALLBACK] Transaccion encontrada ID: " + transaccion.getId());
        System.out.println("[PAGOFACIL CALLBACK] id_pago_contado: " + transaccion.getIdPagoContado());
        System.out.println("[PAGOFACIL CALLBACK] id_pago_cuota: " + transaccion.getIdPagoCuota());
        System.out.println("[PAGOFACIL CALLBACK] correo_solicitante: " + transaccion.getCorreoSolicitante());

        String estadoNormalizado = normalizarEstadoPagoFacil(estadoPagoFacil);

        System.out.println("[PAGOFACIL CALLBACK] Estado normalizado: " + estadoNormalizado);

        transaccionDAO.updateEstadoByPaymentNumber(pedidoId, estadoNormalizado);
        System.out.println("[PAGOFACIL CALLBACK] pago_facil_transaccion actualizado a: " + estadoNormalizado);

        if ("PAGADO".equals(estadoNormalizado)) {
            if (transaccion.getIdPagoContado() > 0) {
                System.out.println("[PAGOFACIL CALLBACK] Procesando pago contado...");

                int idVenta = pagoContadoDAO.getIdVentaById(transaccion.getIdPagoContado());

                System.out.println("[PAGOFACIL CALLBACK] id_venta relacionado: " + idVenta);

                pagoContadoDAO.marcarPagado(transaccion.getIdPagoContado());
                System.out.println("[PAGOFACIL CALLBACK] pago_contado actualizado a PAGADO");

                if (idVenta > 0) {
                    ventaDAO.updateEstado(idVenta, "PAGADA");
                    System.out.println("[PAGOFACIL CALLBACK] venta actualizada a PAGADA");
                }

                enviarCorreoPagoContadoExitoso(transaccion);
            }

            if (transaccion.getIdPagoCuota() > 0) {
                System.out.println("[PAGOFACIL CALLBACK] Procesando pago cuota...");

                confirmarPagoCuota(transaccion);
                enviarCorreoPagoCuotaExitoso(transaccion);
            }
        } else {
            System.out.println("[PAGOFACIL CALLBACK] El pago no fue confirmado. No se actualizan pagos.");
        }

        return respuestaOk("Callback procesado correctamente");
    }

    private void confirmarPagoCuota(PagoFacilTransaccion transaccion) throws Exception {
        PagoCuota cuota = pagoCuotaDAO.findById(transaccion.getIdPagoCuota());

        if (cuota == null) {
            return;
        }

        pagoCuotaDAO.marcarPagado(cuota.getId());
        creditoDAO.registrarPagoCuota(cuota.getIdCredito(), cuota.getMontoPagado());
    }

    private int extraerEntero(String json, String campo) {
        try {
            String buscar = "\"" + campo + "\"";
            int pos = json.indexOf(buscar);

            if (pos == -1) {
                return 0;
            }

            int dosPuntos = json.indexOf(":", pos);
            int coma = json.indexOf(",", dosPuntos + 1);
            int llave = json.indexOf("}", dosPuntos + 1);

            int fin;

            if (coma == -1) {
                fin = llave;
            } else if (llave == -1) {
                fin = coma;
            } else {
                fin = Math.min(coma, llave);
            }

            String valor = json.substring(dosPuntos + 1, fin).trim();

            return Integer.parseInt(valor);

        } catch (Exception e) {
            return 0;
        }
    }

    private String normalizarEstadoPagoFacil(int estadoPagoFacil) {
        if (estadoPagoFacil == 2) {
            return "PAGADO";
        }

        if (estadoPagoFacil == 1) {
            return "PENDIENTE";
        }

        if (estadoPagoFacil == 3) {
            return "ANULADO";
        }

        return "PENDIENTE";
    }

    private void enviarCorreoPagoContadoExitoso(PagoFacilTransaccion transaccion) throws Exception {
        if (transaccion.getCorreoSolicitante() == null || transaccion.getCorreoSolicitante().trim().isEmpty()) {
            return;
        }

        List<String[]> pagos = pagoContadoDAO.listAll();

        String tablaPagos = HtmlBuilder.generateTableContentSinHtml(
                "Lista actualizada de pagos contado",
                HtmlBuilder.HEADERS_PAGO_CONTADO,
                pagos
        );

        String html = "<html><body style='font-family: Arial; padding:20px;'>" +
                "<center>" +
                "<h2 style='color:#4A148C;'>Pago contado realizado exitosamente</h2>" +
                "<p style='font-size:14px; color:#333;'>" +
                "La transaccion QR de pago contado fue confirmada por PagoFacil y registrada correctamente." +
                "</p>" +
                "</center>" +
                tablaPagos +
                "</body></html>";

        enviarCorreo(
                transaccion.getCorreoSolicitante(),
                "Pago contado realizado exitosamente",
                html
        );
    }

    private void enviarCorreoPagoCuotaExitoso(PagoFacilTransaccion transaccion) throws Exception {
        if (transaccion.getCorreoSolicitante() == null || transaccion.getCorreoSolicitante().trim().isEmpty()) {
            return;
        }

        List<String[]> pagos = pagoCuotaDAO.listAll();

        String tablaPagos = HtmlBuilder.generateTableContentSinHtml(
                "Lista actualizada de pagos cuota",
                HtmlBuilder.HEADERS_PAGO_CUOTA,
                pagos
        );

        String html = "<html><body style='font-family: Arial; padding:20px;'>" +
                "<center>" +
                "<h2 style='color:#4A148C;'>Pago de cuota realizado exitosamente</h2>" +
                "<p style='font-size:14px; color:#333;'>" +
                "La transaccion QR de pago de cuota fue confirmada por PagoFacil y registrada correctamente." +
                "</p>" +
                "</center>" +
                tablaPagos +
                "</body></html>";

        enviarCorreo(
                transaccion.getCorreoSolicitante(),
                "Pago de cuota realizado exitosamente",
                html
        );
    }

    private void enviarCorreo(String destino, String asunto, String html) {
        Comunication.SendEmail sendEmail = new Comunication.SendEmail();

        Utils.Email emailRespuesta = new Utils.Email(
                destino,
                asunto
        );

        sendEmail.responseUser(emailRespuesta, html);
    }


    private String respuestaOk(String message) {
        return "{"
                + "\"error\":0,"
                + "\"status\":1,"
                + "\"message\":\"" + SimpleJson.escape(message) + "\","
                + "\"values\":true"
                + "}";
    }
}