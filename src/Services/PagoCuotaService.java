package Services;

import DAO.CreditoDAO;
import DAO.MetodoPagoDAO;
import DAO.PagoCuotaDAO;
import DAO.PagoFacilTransaccionDAO;
import Models.PagoCuota;
import Models.PagoFacilTransaccion;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PagoCuotaService {

    private final PagoCuotaDAO pagoCuotaDAO;
    private final CreditoDAO creditoDAO;
    private final MetodoPagoDAO metodoPagoDAO;
    private final PagoFacilQrService pagoFacilQrService;
    private final PagoFacilTransaccionDAO pagoFacilTransaccionDAO;

    public PagoCuotaService() {
        this.pagoCuotaDAO = new PagoCuotaDAO();
        this.creditoDAO = new CreditoDAO();
        this.metodoPagoDAO = new MetodoPagoDAO();
        this.pagoFacilQrService = new PagoFacilQrService();
        this.pagoFacilTransaccionDAO = new PagoFacilTransaccionDAO();
    }

    public PagoCuotaResultado crearPagoCuota(List<String> params, String correoSolicitante) throws Exception {
        if (params.size() != 9) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: pago_cuota agregar " +
                    "id_credito,id_metodo_pago,numero_cuota,interes_cuota," +
                    "fecha_vencimiento,fecha_pago,monto_pagado,dias_mora,monto_pendiente"
            );
        }

        PagoCuota pago = construirPagoCuotaAgregar(params);
        pago.setEstado("PENDIENTE");
        validarPagoCuota(pago);

        if (!creditoDAO.existsById(pago.getIdCredito())) {
            throw new IllegalArgumentException("No existe credito con id: " + pago.getIdCredito());
        }

        String nombreMetodoPago = metodoPagoDAO.getNombreById(pago.getIdMetodoPago());

        if (nombreMetodoPago == null) {
            throw new IllegalArgumentException("No existe metodo_pago con id: " + pago.getIdMetodoPago());
        }

        boolean esQr = nombreMetodoPago.trim().equalsIgnoreCase("QR") ||
                nombreMetodoPago.toUpperCase().contains("QR");

        

        int idPagoCuota = pagoCuotaDAO.create(pago);

        PagoCuotaResultado resultado = new PagoCuotaResultado();

        if (esQr) {
            String paymentNumber = "PCU-" + idPagoCuota + "-CRE-" + pago.getIdCredito();

            PagoFacilQrResponse qr = pagoFacilQrService.generarQr(
                    paymentNumber,
                    "Cliente credito",
                    "0",
                    "70000000",
                    correoSolicitante,
                    String.valueOf(pago.getIdCredito()),
                    "Pago cuota credito " + pago.getIdCredito() + " cuota " + pago.getNumeroCuota(),
                    pago.getMontoPagado().doubleValue()
            );

            PagoFacilTransaccion transaccion = new PagoFacilTransaccion();

            transaccion.setIdPagoContado(0);
            transaccion.setIdPagoCuota(idPagoCuota);
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
            pagoCuotaDAO.marcarPagado(idPagoCuota);
            creditoDAO.registrarPagoCuota(pago.getIdCredito(), pago.getMontoPagado());

            resultado.setQrGenerado(false);
            resultado.setHtmlRespuesta("<html><body>" +
                    "<h2>Pago de cuota registrado exitosamente</h2>" +
                    "<p>La cuota fue marcada como PAGADA.</p>" +
                    "</body></html>");
        }

        return resultado;
    }

    public void actualizarPagoCuota(List<String> params) throws SQLException {
        if (params.size() != 11) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: pago_cuota modificar id,id_credito,id_metodo_pago,numero_cuota,interes_cuota,fecha_vencimiento,fecha_pago,monto_pagado,dias_mora,monto_pendiente,estado"
            );
        }

        PagoCuota pago = new PagoCuota();

        pago.setId(parseIntRequired(params.get(0), "id"));
        pago.setIdCredito(parseIntRequired(params.get(1), "id_credito"));
        pago.setIdMetodoPago(parseIntRequired(params.get(2), "id_metodo_pago"));
        pago.setNumeroCuota(parseIntRequired(params.get(3), "numero_cuota"));
        pago.setInteresCuota(parseBigDecimalRequired(params.get(4), "interes_cuota"));
        pago.setFechaVencimiento(parseDateRequired(params.get(5), "fecha_vencimiento"));
        pago.setFechaPago(parseDateOptional(params.get(6)));
        pago.setMontoPagado(parseBigDecimalRequired(params.get(7), "monto_pagado"));
        pago.setDiasMora(parseIntRequired(params.get(8), "dias_mora"));
        pago.setMontoPendiente(parseBigDecimalRequired(params.get(9), "monto_pendiente"));
        pago.setEstado(parseStringRequired(params.get(10), "estado"));

        if (!pagoCuotaDAO.existsById(pago.getId())) {
            throw new IllegalArgumentException("No existe pago_cuota con id: " + pago.getId());
        }

        validarPagoCuota(pago);

        pagoCuotaDAO.update(pago);
    }

    public List<String[]> listarPagosCuota() throws SQLException {
        return pagoCuotaDAO.listAll();
    }

    public void eliminarPagoCuota(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a 0");
        }

        if (!pagoCuotaDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe pago_cuota con id: " + id);
        }

        pagoCuotaDAO.delete(id);
    }

    public String getComandosPagoCuota() {
        return "Comandos Pago Cuota:\n" +
                "- pago_cuota listar\n" +
                "- pago_cuota agregar id_credito,id_metodo_pago,numero_cuota,interes_cuota,fecha_vencimiento,fecha_pago,monto_pagado,dias_mora,monto_pendiente\n" +
                "- pago_cuota modificar id,id_credito,id_metodo_pago,numero_cuota,interes_cuota,fecha_vencimiento,fecha_pago,monto_pagado,dias_mora,monto_pendiente,estado\n" +
                "- pago_cuota eliminar id\n" +
                "- pago_cuota help\n\n" +
                "Si id_metodo_pago corresponde a QR, se genera automaticamente un QR de PagoFacil.";
    }

    private PagoCuota construirPagoCuotaAgregar(List<String> params) {
        PagoCuota pago = new PagoCuota();

        pago.setIdCredito(parseIntRequired(params.get(0), "id_credito"));
        pago.setIdMetodoPago(parseIntRequired(params.get(1), "id_metodo_pago"));
        pago.setNumeroCuota(parseIntRequired(params.get(2), "numero_cuota"));
        pago.setInteresCuota(parseBigDecimalRequired(params.get(3), "interes_cuota"));
        pago.setFechaVencimiento(parseDateRequired(params.get(4), "fecha_vencimiento"));
        pago.setFechaPago(parseDateOptional(params.get(5)));
        pago.setMontoPagado(parseBigDecimalRequired(params.get(6), "monto_pagado"));
        pago.setDiasMora(parseIntRequired(params.get(7), "dias_mora"));
        pago.setMontoPendiente(parseBigDecimalRequired(params.get(8), "monto_pendiente"));
        pago.setEstado("PENDIENTE");

        return pago;
    }

    private String construirHtmlQr(PagoFacilQrResponse qr, PagoCuota pago, String paymentNumber) {
        String img = qr.getQrBase64();

        if (img != null && !img.startsWith("data:image")) {
            img = "data:image/png;base64," + img;
        }

        String url = qr.getQrContentUrl() != null ? qr.getQrContentUrl() : qr.getCheckoutUrl();

        return "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h2>Solicitud de pago de cuota generada exitosamente</h2>" +
                "<p><b>Credito:</b> " + pago.getIdCredito() + "</p>" +
                "<p><b>Cuota:</b> " + pago.getNumeroCuota() + "</p>" +
                "<p><b>Monto registrado:</b> " + pago.getMontoPagado() + " Bs</p>" +
                "<p><b>Monto QR de prueba:</b> 0.01 Bs</p>" +
                "<p><b>Codigo interno:</b> " + paymentNumber + "</p>" +
                "<p>Escanee el siguiente QR desde su banca movil:</p>" +
                "<img src='" + img + "' style='width:260px;height:260px;border:1px solid #ddd;padding:10px;'/>" +
                (url != null && !url.trim().isEmpty() ? "<p><a href='" + url + "'>Abrir link de pago</a></p>" : "") +
                "<p>Estado inicial: <b>PENDIENTE</b></p>" +
                "</body></html>";
    }

    private void validarPagoCuota(PagoCuota pago) {
        if (pago.getIdCredito() <= 0) {
            throw new IllegalArgumentException("id_credito debe ser mayor a 0");
        }

        if (pago.getIdMetodoPago() <= 0) {
            throw new IllegalArgumentException("id_metodo_pago debe ser mayor a 0");
        }

        if (pago.getNumeroCuota() <= 0) {
            throw new IllegalArgumentException("numero_cuota debe ser mayor a 0");
        }

        if (pago.getInteresCuota() == null || pago.getInteresCuota().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("interes_cuota no puede ser negativo");
        }

        if (pago.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("fecha_vencimiento es obligatoria");
        }

        if (pago.getMontoPagado() == null || pago.getMontoPagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("monto_pagado debe ser mayor a 0");
        }

        if (pago.getDiasMora() < 0) {
            throw new IllegalArgumentException("dias_mora no puede ser negativo");
        }

        if (pago.getMontoPendiente() == null || pago.getMontoPendiente().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("monto_pendiente no puede ser negativo");
        }

        validarEstado(pago.getEstado());
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("estado es obligatorio");
        }

        String value = estado.trim().toUpperCase();

        if (!value.equals("PENDIENTE") &&
            !value.equals("PAGADO") &&
            !value.equals("VENCIDO") &&
            !value.equals("ANULADO")) {
            throw new IllegalArgumentException("Estado no valido. Use: PENDIENTE, PAGADO, VENCIDO o ANULADO");
        }
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

    private Date parseDateOptional(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return null;
        }

        try {
            return Date.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd");
        }
    }

    private String parseStringRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        return value.trim();
    }
}