package Services;

import DAO.CreditoDAO;
import DAO.VentaDAO;
import Models.Credito;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CreditoService {

    private final CreditoDAO creditoDAO;
    private final VentaDAO ventaDAO;

    public CreditoService() {
        this.creditoDAO = new CreditoDAO();
        this.ventaDAO = new VentaDAO();
    }

    public void crearCredito(List<String> params) throws SQLException {
        if (params.size() < 10) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: credito agregar id_venta,monto_credito,monto_pendiente,monto_pagado,fecha_otorgamiento,fecha_vencimiento,interes,estado,cuotas_total,dias_mora"
            );
        }

        Credito credito = new Credito();

        credito.setIdVenta(parseIntRequired(params.get(0), "id_venta"));
        credito.setMontoCredito(parseBigDecimalRequired(params.get(1), "monto_credito"));
        credito.setMontoPendiente(parseBigDecimalRequired(params.get(2), "monto_pendiente"));
        credito.setMontoPagado(parseBigDecimalRequired(params.get(3), "monto_pagado"));
        credito.setFechaOtorgamiento(parseDateRequired(params.get(4), "fecha_otorgamiento"));
        credito.setFechaVencimiento(parseDateRequired(params.get(5), "fecha_vencimiento"));
        credito.setInteres(parseBigDecimalRequired(params.get(6), "interes"));
        credito.setEstado(parseStringRequired(params.get(7), "estado"));
        credito.setCuotasTotal(parseIntRequired(params.get(8), "cuotas_total"));
        credito.setDiasMora(parseIntRequired(params.get(9), "dias_mora"));

        validarCredito(credito);
        validarRelaciones(credito);

        creditoDAO.create(credito);
    }

    public void actualizarCredito(List<String> params) throws SQLException {
        if (params.size() < 11) {
            throw new IllegalArgumentException(
                    "Cantidad de parametros invalida. Formato: credito modificar id,id_venta,monto_credito,monto_pendiente,monto_pagado,fecha_otorgamiento,fecha_vencimiento,interes,estado,cuotas_total,dias_mora"
            );
        }

        Credito credito = new Credito();

        credito.setId(parseIntRequired(params.get(0), "id"));
        credito.setIdVenta(parseIntRequired(params.get(1), "id_venta"));
        credito.setMontoCredito(parseBigDecimalRequired(params.get(2), "monto_credito"));
        credito.setMontoPendiente(parseBigDecimalRequired(params.get(3), "monto_pendiente"));
        credito.setMontoPagado(parseBigDecimalRequired(params.get(4), "monto_pagado"));
        credito.setFechaOtorgamiento(parseDateRequired(params.get(5), "fecha_otorgamiento"));
        credito.setFechaVencimiento(parseDateRequired(params.get(6), "fecha_vencimiento"));
        credito.setInteres(parseBigDecimalRequired(params.get(7), "interes"));
        credito.setEstado(parseStringRequired(params.get(8), "estado"));
        credito.setCuotasTotal(parseIntRequired(params.get(9), "cuotas_total"));
        credito.setDiasMora(parseIntRequired(params.get(10), "dias_mora"));

        if (!creditoDAO.existsById(credito.getId())) {
            throw new IllegalArgumentException("No existe un credito con id: " + credito.getId());
        }

        validarCredito(credito);
        validarRelaciones(credito);

        creditoDAO.update(credito);
    }

    public List<String[]> listarCreditos() throws SQLException {
        return creditoDAO.listAll();
    }

    public String[] obtenerCreditoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del credito debe ser mayor a 0");
        }

        return creditoDAO.readById(id);
    }

    public void eliminarCredito(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del credito debe ser mayor a 0");
        }

        if (!creditoDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe un credito con id: " + id);
        }

        creditoDAO.delete(id);
    }

    public String getComandosCredito() {
        return "Comandos Credito:\n" +
                "- credito listar\n" +
                "- credito agregar id_venta,monto_credito,monto_pendiente,monto_pagado,fecha_otorgamiento,fecha_vencimiento,interes,estado,cuotas_total,dias_mora\n" +
                "- credito modificar id,id_venta,monto_credito,monto_pendiente,monto_pagado,fecha_otorgamiento,fecha_vencimiento,interes,estado,cuotas_total,dias_mora\n" +
                "- credito eliminar id\n" +
                "- credito help";
    }

    private void validarRelaciones(Credito credito) throws SQLException {
        if (!ventaDAO.existsById(credito.getIdVenta())) {
            throw new IllegalArgumentException("No existe una venta con id: " + credito.getIdVenta());
        }
    }

    private void validarCredito(Credito credito) {
        if (credito.getIdVenta() <= 0) {
            throw new IllegalArgumentException("El id_venta debe ser mayor a 0");
        }

        if (credito.getMontoCredito() == null || credito.getMontoCredito().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto_credito no puede ser negativo");
        }

        if (credito.getMontoPendiente() == null || credito.getMontoPendiente().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto_pendiente no puede ser negativo");
        }

        if (credito.getMontoPagado() == null || credito.getMontoPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto_pagado no puede ser negativo");
        }

        if (credito.getInteres() == null || credito.getInteres().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El interes no puede ser negativo");
        }

        if (credito.getCuotasTotal() <= 0) {
            throw new IllegalArgumentException("cuotas_total debe ser mayor a 0");
        }

        if (credito.getDiasMora() < 0) {
            throw new IllegalArgumentException("dias_mora no puede ser negativo");
        }

        if (credito.getFechaOtorgamiento() == null) {
            throw new IllegalArgumentException("fecha_otorgamiento es obligatoria");
        }

        if (credito.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("fecha_vencimiento es obligatoria");
        }

        if (credito.getFechaVencimiento().before(credito.getFechaOtorgamiento())) {
            throw new IllegalArgumentException("fecha_vencimiento no puede ser menor a fecha_otorgamiento");
        }

        validarEstado(credito.getEstado());
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String value = estado.trim().toUpperCase();

        if (!value.equals("ACTIVO") &&
            !value.equals("PAGADO") &&
            !value.equals("VENCIDO") &&
            !value.equals("CANCELADO")) {
            throw new IllegalArgumentException("Estado no valido. Use: ACTIVO, PAGADO, VENCIDO o CANCELADO");
        }
    }

    private int parseIntRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un numero entero");
        }
    }

    private BigDecimal parseBigDecimalRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser decimal");
        }
    }

    private Date parseDateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe tener formato yyyy-MM-dd");
        }
    }

    private String parseStringRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        return value.trim();
    }
}