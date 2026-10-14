package Services;

import DAO.MetodoPagoDAO;
import DAO.PagoContadoDAO;
import DAO.PagoFacilTransaccionDAO;
import DAO.VentaDAO;
import Models.PagoContado;
import Models.PagoFacilTransaccion;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PagoContadoService {

    private final PagoContadoDAO pagoContadoDAO;
    private final MetodoPagoDAO metodoPagoDAO;
    private final VentaDAO ventaDAO;
    private final PagoFacilQrService pagoFacilQrService;
    private final PagoFacilTransaccionDAO pagoFacilTransaccionDAO;

    public PagoContadoService() {
        this.pagoContadoDAO = new PagoContadoDAO();
        this.metodoPagoDAO = new MetodoPagoDAO();
        this.ventaDAO = new VentaDAO();
        this.pagoFacilQrService = new PagoFacilQrService();
        this.pagoFacilTransaccionDAO = new PagoFacilTransaccionDAO();
    }

    public PagoContadoResultado crearPagoContado(List<String> params, String correoSolicitante) throws Exception {
        if (params.size() < 6 || params.size() > 7) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: pago_contado agregar id_venta,id_metodo_pago,fecha,monto_pago,interes_mora_cobrado,recargo_extra,observaciones?"
            );
        }

        PagoContado pago = new PagoContado();

        pago.setIdVenta(parseIntRequired(params.get(0), "id_venta"));
        pago.setIdMetodoPago(parseIntRequired(params.get(1), "id_metodo_pago"));
        pago.setFecha(parseDateRequired(params.get(2), "fecha"));
        pago.setMontoPago(parseBigDecimalRequired(params.get(3), "monto_pago"));
        pago.setInteresMoraCobrado(parseBigDecimalRequired(params.get(4), "interes_mora_cobrado"));
        pago.setRecargoExtra(parseBigDecimalRequired(params.get(5), "recargo_extra"));
        pago.setObservaciones(getParam(params, 6));

        validarPagoContado(pago);

        if (!ventaDAO.existsById(pago.getIdVenta())) {
            throw new IllegalArgumentException("No existe una venta con id: " + pago.getIdVenta());
        }

        String metodoPago = metodoPagoDAO.getNombreById(pago.getIdMetodoPago());

        if (metodoPago == null) {
            throw new IllegalArgumentException("No existe metodo_pago con id: " + pago.getIdMetodoPago());
        }

        boolean esQr = metodoPago.trim().equalsIgnoreCase("QR") ||
                metodoPago.toUpperCase().contains("QR");
        pago.setEstado(esQr ? "PENDIENTE" : "PAGADO");
        int idPago = pagoContadoDAO.create(pago);

        PagoContadoResultado resultado = new PagoContadoResultado();

        if (esQr) {
            String paymentNumber = "PC-" + idPago + "-VEN-" + pago.getIdVenta();

            PagoFacilQrResponse qr = pagoFacilQrService.generarQr(
                    paymentNumber,
                    "Cliente tienda",
                    "0",
                    "70000000",
                    correoSolicitante,
                    String.valueOf(pago.getIdVenta()),
                    "Pago venta " + pago.getIdVenta(),
                    pago.getMontoPago().doubleValue()
            );

            PagoFacilTransaccion transaccion = new PagoFacilTransaccion();

            transaccion.setIdPagoContado(idPago);
            transaccion.setIdPagoCuota(0);
            transaccion.setPaymentNumber(paymentNumber);
            transaccion.setPagofacilTransactionId(qr.getTransactionId());
            transaccion.setPaymentMethodTransactionId(qr.getPaymentMethodTransactionId());
            transaccion.setPaymentMethodId(Utils.ConfigPagoFacil.PAYMENT_METHOD_ID_QR);
            transaccion.setQrBase64(qr.getQrBase64());
            transaccion.setCheckoutUrl(qr.getCheckoutUrl());
            transaccion.setDeepLink(qr.getDeepLink());
            transaccion.setQrContentUrl(qr.getQrContentUrl());
            transaccion.setUniversalUrl(qr.getUniversalUrl());
            transaccion.setExpirationDate(qr.getExpirationDate());
            transaccion.setEstado("PENDIENTE");
            transaccion.setCorreoSolicitante(correoSolicitante);

            pagoFacilTransaccionDAO.create(transaccion);

            resultado.setQrGenerado(true);
            resultado.setHtmlRespuesta(construirHtmlQr(qr, pago, paymentNumber));
        } else {
            ventaDAO.updateEstado(pago.getIdVenta(), "PAGADA");

            resultado.setQrGenerado(false);
            resultado.setHtmlRespuesta("<html><body>" +
                    "<h2>Pago contado registrado exitosamente</h2>" +
                    "<p>La venta fue marcada como PAGADA.</p>" +
                    "</body></html>");
        }

        return resultado;
    }

    public List<String[]> listarPagosContado() throws SQLException {
        return pagoContadoDAO.listAll();
    }

    public void eliminarPagoContado(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a 0");
        }

        if (!pagoContadoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe pago_contado con id: " + id);
        }

        pagoContadoDAO.delete(id);
    }

    public String getComandosPagoContado() {
        return "Comandos Pago Contado:\n" +
                "- pago_contado listar\n" +
                "- pago_contado agregar id_venta,id_metodo_pago,fecha,monto_pago,interes_mora_cobrado,recargo_extra,observaciones?\n" +
                "- pago_contado eliminar id\n" +
                "- pago_contado help\n\n" +
                "Si id_metodo_pago corresponde a QR, se genera automaticamente un QR de PagoFacil.";
    }

    private String construirHtmlQr(PagoFacilQrResponse qr, PagoContado pago, String paymentNumber) {
        String img = qr.getQrBase64();

        if (img != null && !img.startsWith("data:image")) {
            img = "data:image/png;base64," + img;
        }

        String url = qr.getQrContentUrl() != null ? qr.getQrContentUrl() : qr.getCheckoutUrl();

        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>Solicitud de pago generada exitosamente</h2>" +
                "<p><b>Venta:</b> " + pago.getIdVenta() + "</p>" +
                "<p><b>Monto registrado:</b> " + pago.getMontoPago() + " Bs</p>" +
                "<p><b>Monto QR de prueba:</b> 0.01 Bs</p>" +
                "<p><b>Codigo interno:</b> " + paymentNumber + "</p>" +
                "<p>Escanee el siguiente QR desde su banca movil:</p>" +
                "<img src='" + img + "' style='width:260px;height:260px;border:1px solid #ddd;padding:10px;'/>" +
                (url != null && !url.trim().isEmpty() ? "<p><a href='" + url + "'>Abrir link de pago</a></p>" : "") +
                "<p>Estado inicial: <b>PENDIENTE</b></p>" +
                "</body></html>";
    }

    private void validarPagoContado(PagoContado pago) {
        if (pago.getIdVenta() <= 0) {
            throw new IllegalArgumentException("id_venta debe ser mayor a 0");
        }

        if (pago.getIdMetodoPago() <= 0) {
            throw new IllegalArgumentException("id_metodo_pago debe ser mayor a 0");
        }

        if (pago.getFecha() == null) {
            throw new IllegalArgumentException("fecha es obligatoria");
        }

        if (pago.getMontoPago() == null || pago.getMontoPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("monto_pago debe ser mayor a 0");
        }

        if (pago.getInteresMoraCobrado() == null || pago.getInteresMoraCobrado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("interes_mora_cobrado no puede ser negativo");
        }

        if (pago.getRecargoExtra() == null || pago.getRecargoExtra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("recargo_extra no puede ser negativo");
        }
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        String value = params.get(index);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        if (value.trim().equalsIgnoreCase("null")) {
            return null;
        }

        return value.trim();
    }

    private int parseIntRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser entero");
        }
    }

    private BigDecimal parseBigDecimalRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser decimal");
        }
    }

    private Date parseDateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Date.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe tener formato yyyy-MM-dd");
        }
    }
}