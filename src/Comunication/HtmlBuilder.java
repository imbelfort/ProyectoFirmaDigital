package Comunication;

import java.util.List;

public class HtmlBuilder {

    private static final String HTML_OPEN = "<html>";
    private static final String HTML_CLOSE = "</html>";
    private static final String BODY_OPEN = "<body>";
    private static final String BODY_CLOSE = "</body>";

    // ============================================================
    // HEADERS GENERALES
    // ============================================================

    public static final String[] HEADERS_USER = {
        "ID",
        "REGISTRO",
        "CI",
        "NOMBRE",
        "APELLIDOS",
        "EMAIL",
        "TELEFONO",
        "ROL",
        "FACULTAD_O_UNIDAD",
        "CARRERA_O_CARGO",
        "ESTADO"
    };

    public static final String[] HEADERS_DELEGACION = {
        "ID", "TITULAR", "DELEGADO", "RESOLUCION", "MOTIVO", "FECHA_INICIO", "FECHA_FIN", "ALCANCE", "ESTADO"
    };

    public static final String[] HEADERS_INDICADOR = {
        "ID", "CODIGO", "NOMBRE", "META", "VALOR_ACTUAL", "UNIDAD", "PERIODO", "ESTADO"
    };

    public static final String[] HEADERS_TIPO_TRAMITE = {
        "ID", "CODIGO", "NOMBRE", "DESCRIPCION", "SLA_HORAS", "COSTO_BS", "ESTADO"
    };

    public static final String[] HEADERS_TRAMITE = {
        "ID", "CODIGO", "REGISTRO", "SOLICITANTE", "TIPO_TRAMITE", "ASUNTO", "ESTADO", "PRIORIDAD", "FECHA"
    };

    public static final String[] HEADERS_DOCUMENTO = {
        "ID", "COD_TRAMITE", "DOCUMENTO", "TIPO", "ARCHIVO_URL", "ESTADO_VALIDACION", "FECHA_VALIDACION"
    };

    public static final String[] HEADERS_FLUJO = {
        "ID", "COD_TRAMITE", "ETAPA_ACTUAL", "RESPONSABLE", "FECHA_INICIO", "FECHA_FIN", "ESTADO"
    };

    public static final String[] HEADERS_FIRMA = {
        "ID", "COD_TRAMITE", "FIRMANTE", "TIPO_FIRMA", "ALGORITMO", "CERTIFICADO", "FECHA_FIRMA", "VALIDEZ"
    };

    public static final String[] HEADERS_DESPACHO = {
        "ID", "COD_TRAMITE", "DESPACHADOR", "NRO_DESPACHO", "TIPO_ENTREGA", "FECHA_DESPACHO", "ESTADO"
    };

    public static final String[] HEADERS_CERTIFICADO = {
        "ID", "COD_TRAMITE", "COD_VERIFICACION", "TITULO", "PORTAL_URL", "FECHA_EMISION", "ESTADO"
    };

    public static final String[] HEADERS_PORTAL = {
        "ID", "COD_TRAMITE", "REGISTRO", "ESTUDIANTE", "TRAMITE", "ASUNTO", "ESTADO", "PRIORIDAD", "FECHA", "VISITAS"
    };

    public static final String[] HEADERS_SLA = {
        "ID", "COD_TRAMITE", "LIMITE_HORAS", "HORAS_TRANSCURRIDAS", "ESTADO_SLA", "INICIO", "LIMITE", "ALERTAS"
    };

    public static final String[] HEADERS_REPORTE_ESTADO = {
        "ESTADO", "TOTAL_TRAMITES", "PORCENTAJE"
    };

    public static final String[] HEADERS_REPORTE_SLA = {
        "ESTADO_SLA", "CANTIDAD", "TASA_CUMPLIMIENTO"
    };

    public static final String[] HEADERS_REPORTE_FIRMAS = {
        "FIRMANTE", "TIPO_FIRMA", "TOTAL_FIRMADOS"
    };


    // ============================================================
    // GENERAR TABLA GENERAL
    // ============================================================

    // ============================================================
    // CONSTRUCTOR DE TABLA PRINCIPAL (ESTILO EJECUTIVO MODERNO)
    // ============================================================

    private static String buildTableHtml(String title, String[] headers, List<String[]> data) {
        StringBuilder tableHeadersHtml = new StringBuilder();

        for (String header : headers) {
            tableHeadersHtml.append("<th style=\"background-color: #1E3A8A; color: #FFFFFF; font-size: 11px; ")
                    .append("font-weight: bold; text-transform: uppercase; letter-spacing: 0.5px; ")
                    .append("padding: 10px 8px; border: 1px solid #1E3A8A; text-align: center;\">")
                    .append(escapeHtml(header))
                    .append("</th>");
        }

        StringBuilder tableBodyHtml = new StringBuilder();

        if (data == null || data.isEmpty()) {
            tableBodyHtml.append("<tr>")
                    .append("<td colspan=\"")
                    .append(headers.length)
                    .append("\" style=\"border: 1px solid #CBD5E1; padding: 24px 10px; ")
                    .append("color: #64748B; font-size: 13px; text-align: center; font-style: italic; background-color: #F8FAFC;\">")
                    .append("No existen registros para mostrar")
                    .append("</td>")
                    .append("</tr>");
        } else {
            for (int r = 0; r < data.size(); r++) {
                String[] element = data.get(r);
                String bgColor = (r % 2 == 0) ? "#FFFFFF" : "#F8FAFC";

                tableBodyHtml.append("<tr style=\"background-color: ").append(bgColor).append(";\">");

                for (int i = 0; i < headers.length; i++) {
                    String value = "";

                    if (element != null && i < element.length && element[i] != null) {
                        value = element[i];
                    }

                    String header = headers[i];

                    tableBodyHtml.append("<td style=\"border: 1px solid #CBD5E1; padding: 8px 10px; ")
                            .append("color: #334155; font-size: 12px; text-align: center; vertical-align: middle;\">")
                            .append(formatValueByHeader(header, value))
                            .append("</td>");
                }

                tableBodyHtml.append("</tr>");
            }
        }

        StringBuilder html = new StringBuilder();
        if (title != null && !title.trim().isEmpty()) {
            html.append("<div align=\"center\" style=\"margin-bottom: 14px;\">")
                .append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: auto;\">")
                .append("<tr><td align=\"center\" style=\"padding-bottom: 4px;\">")
                .append("<span style=\"background-color: #EFF6FF; color: #1D4ED8; border: 1px solid #BFDBFE; ")
                .append("font-size: 11px; font-weight: bold; padding: 3px 12px; text-transform: uppercase; letter-spacing: 0.5px;\">")
                .append("Sistema de Firma Digital")
                .append("</span></td></tr>")
                .append("<tr><td align=\"center\">")
                .append("<h2 style=\"color: #0F172A; font-family: Arial, sans-serif; font-size: 17px; font-weight: bold; margin: 4px 0 0 0;\">")
                .append(escapeHtml(title))
                .append("</h2></td></tr></table></div>");
        }

        html.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; ")
            .append("border: 1px solid #CBD5E1; width: 96%; margin: auto; font-family: Arial, Helvetica, sans-serif; font-size: 12px;\">")
            .append("<thead><tr>")
            .append(tableHeadersHtml)
            .append("</tr></thead>")
            .append("<tbody>")
            .append(tableBodyHtml)
            .append("</tbody>")
            .append("</table>");

        return html.toString();
    }

    // ============================================================
    // GENERAR TABLA GENERAL
    // ============================================================

    public static String generateTable(String title, String[] headers, List<String[]> data) {
        return insertInHtml(buildTableHtml(title, headers, data));
    }

    public static String generateTableContentSinHtml(String title, String[] headers, List<String[]> data) {
        return buildTableHtml(title, headers, data);
    }

    // ============================================================
    // GENERAR MENSAJE + TABLA
    // ============================================================

    public static String generateMessageAndTable(String mensaje, String tituloTabla, String[] headers, List<String[]> data) {
        String htmlMensaje = "<div align=\"center\" style=\"margin-bottom: 16px;\">"
                + "<div style=\"background-color: #ECFDF5; border: 1px solid #A7F3D0; border-left: 5px solid #10B981; "
                + "padding: 10px 16px; width: 92%; margin: auto; text-align: center;\">"
                + "<span style=\"color: #065F46; font-size: 13px; font-weight: bold; font-family: Arial, sans-serif;\">"
                + "&#10003; " + escapeHtml(mensaje)
                + "</span>"
                + "</div></div>";

        String tabla = buildTableHtml(tituloTabla, headers, data);

        return insertInHtml(htmlMensaje + tabla);
    }

    private static String generateTableContent(String title, String[] headers, List<String[]> data) {
        return buildTableHtml(title, headers, data);
    }

    // ============================================================
    // GENERAR TEXTO
    // ============================================================

    public static String generateText(String[] args) {
        if (args == null || args.length == 0) {
            return insertInHtml("<center><h3 style=\"color: #64748B;\">Sin contenido</h3></center>");
        }

        StringBuilder acumulative = new StringBuilder();

        acumulative.append("<div align=\"center\" style=\"margin-bottom: 12px;\">")
                .append("<h2 style=\"color: #0F172A; font-family: Arial, sans-serif; font-size: 18px;\">")
                .append(escapeHtml(args[0]))
                .append("</h2></div>");

        for (int i = 1; i < args.length; i++) {
            acumulative.append("<div align=\"center\" style=\"margin-bottom: 8px;\">")
                    .append("<h3 style=\"color: #334155; font-family: Arial, sans-serif; font-size: 14px;\">")
                    .append(escapeHtml(args[i]))
                    .append("</h3></div>");
        }

        return insertInHtml(acumulative.toString());
    }

    // ============================================================
    // GENERAR TABLA DE UN SOLO REGISTRO
    // ============================================================

    public static String generateTableForSimpleData(String title, String[] headers, String[] data) {
        StringBuilder acumulative = new StringBuilder();

        for (int i = 0; i < headers.length; i++) {
            String value = "";

            if (data != null && i < data.length && data[i] != null) {
                value = data[i];
            }

            String bgColor = (i % 2 == 0) ? "#FFFFFF" : "#F8FAFC";

            acumulative.append("<tr style=\"background-color: ").append(bgColor).append(";\">")
                .append("<td style=\"border: 1px solid #CBD5E1; padding: 9px 14px; ")
                .append("background-color: #F1F5F9; color: #1E3A8A; font-weight: bold; width: 35%; ")
                .append("text-transform: uppercase; font-size: 11px; letter-spacing: 0.3px;\">")
                .append(escapeHtml(headers[i]))
                .append("</td>")
                .append("<td style=\"border: 1px solid #CBD5E1; padding: 9px 14px; color: #334155; font-size: 12px;\">")
                .append(formatValueByHeader(headers[i], value))
                .append("</td>")
                .append("</tr>");
        }

        String table = "<div align=\"center\" style=\"margin-bottom: 14px;\">"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: auto;\">"
                + "<tr><td align=\"center\" style=\"padding-bottom: 4px;\">"
                + "<span style=\"background-color: #EFF6FF; color: #1D4ED8; border: 1px solid #BFDBFE; "
                + "font-size: 11px; font-weight: bold; padding: 3px 12px; text-transform: uppercase; letter-spacing: 0.5px;\">"
                + "Detalle de Registro"
                + "</span></td></tr>"
                + "<tr><td align=\"center\">"
                + "<h2 style=\"color: #0F172A; font-family: Arial, sans-serif; font-size: 17px; margin: 4px 0 0 0;\">"
                + escapeHtml(title)
                + "</h2></td></tr></table></div>"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; width: 80%; margin: auto; "
                + "font-family: Arial, sans-serif; font-size: 12px; border: 1px solid #CBD5E1;\">"
                + acumulative
                + "</table>";

        return insertInHtml(table);
    }

    // ============================================================
    // GENERAR AYUDA DE COMANDOS
    // ============================================================

    public static String generateHelpTable(
            String titulo,
            String comandosTexto
    ) {
        if (comandosTexto == null || comandosTexto.trim().isEmpty()) {
            return insertInHtml(
                    "<center><h3 style=\"color: #64748B;\">No existen comandos para mostrar</h3></center>"
            );
        }

        String[] lineas = comandosTexto.split("\\r?\\n");
        StringBuilder rowsHtml = new StringBuilder();

        int startIndex = 0;

        if (lineas.length > 0
                && lineas[0].toLowerCase().contains("comandos")) {
            startIndex = 1;
        }

        int rowCount = 0;
        for (int i = startIndex; i < lineas.length; i++) {
            String linea = lineas[i];

            if (linea == null) {
                continue;
            }

            linea = linea.trim();

            if (linea.isEmpty()
                    || linea.equals("!")
                    || linea.equals("-")) {
                continue;
            }

            if (linea.startsWith("-")) {
                linea = linea.substring(1).trim();
            }

            if (linea.isEmpty()) {
                continue;
            }

            String descripcion = getDescripcionComando(linea);
            String bgColor = (rowCount % 2 == 0) ? "#FFFFFF" : "#F8FAFC";
            rowCount++;

            rowsHtml.append("<tr style=\"background-color: ").append(bgColor).append(";\">")
                    .append("<td style=\"border: 1px solid #CBD5E1; padding: 9px 12px; vertical-align: middle; width: 35%;\">")
                    .append("<span style=\"background-color: #EFF6FF; color: #1D4ED8; border: 1px solid #BFDBFE; ")
                    .append("font-family: Consolas, monospace; font-size: 12px; font-weight: bold; padding: 3px 8px;\">")
                    .append(escapeHtml(linea))
                    .append("</span>")
                    .append("</td>")
                    .append("<td style=\"border: 1px solid #CBD5E1; padding: 9px 12px; color: #334155; font-size: 12px; vertical-align: middle;\">")
                    .append(escapeHtml(descripcion))
                    .append("</td>")
                    .append("</tr>");
        }

        String html =
                "<div align=\"center\" style=\"margin-bottom: 14px;\">"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: auto;\">"
                + "<tr><td align=\"center\" style=\"padding-bottom: 4px;\">"
                + "<span style=\"background-color: #EFF6FF; color: #1D4ED8; border: 1px solid #BFDBFE; "
                + "font-size: 11px; font-weight: bold; padding: 3px 12px; text-transform: uppercase; letter-spacing: 0.5px;\">"
                + "Gu&iacute;a de Comandos"
                + "</span></td></tr>"
                + "<tr><td align=\"center\">"
                + "<h2 style=\"color: #0F172A; font-family: Arial, sans-serif; font-size: 17px; margin: 4px 0 0 0;\">"
                + escapeHtml(titulo)
                + "</h2></td></tr></table></div>"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\""
                + "border-collapse: collapse; "
                + "width: 94%; "
                + "margin: auto; "
                + "font-family: Arial, sans-serif; "
                + "font-size: 12px; "
                + "border: 1px solid #CBD5E1;"
                + "\">"
                + "<thead>"
                + "<tr>"
                + "<th style=\"border: 1px solid #1E3A8A; background-color: #1E3A8A; color: white; padding: 10px 12px; text-align: left; font-size: 11px; font-weight: bold; text-transform: uppercase; width: 35%;\">"
                + "Comando"
                + "</th>"
                + "<th style=\"border: 1px solid #1E3A8A; background-color: #1E3A8A; color: white; padding: 10px 12px; text-align: left; font-size: 11px; font-weight: bold; text-transform: uppercase;\">"
                + "Descripci&oacute;n"
                + "</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + rowsHtml
                + "</tbody>"
                + "</table>";

        return insertInHtml(html);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private static String insertInHtml(String data) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
                + "</head>"
                + "<body style=\"margin: 0; padding: 15px; background-color: #F8FAFC; font-family: Arial, Helvetica, sans-serif; color: #1E293B;\">"
                + "<div style=\"max-width: 960px; margin: 0 auto; background-color: #FFFFFF; border: 1px solid #CBD5E1; padding: 20px 24px;\">"
                + data
                + "<div style=\"margin-top: 24px; padding-top: 12px; border-top: 1px solid #E2E8F0; text-align: center; color: #64748B; font-size: 11px; font-family: Arial, sans-serif;\">"
                + "Sistema de Firma Digital del Vicerrectorado &bull; Universidad Aut&oacute;noma Gabriel Ren&eacute; Moreno"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private static String getDescripcionComando(String linea) {
        String comando = linea.toLowerCase();

        if (comando.contains("firmar")) {
            return "Ejecuta la firma digital consolidada o remota";
        }

        if (comando.contains("despachar")) {
            return "Registra el despacho oficial del legajo o expediente";
        }

        if (comando.contains("compilar")) {
            return "Compila y valida los documentos del expediente digital";
        }

        if (comando.contains("validar")) {
            return "Valida los requisitos y estado de un documento";
        }

        if (comando.contains("observar")) {
            return "Registra una observacion o bloqueo en la bitacora del tramite";
        }

        if (comando.contains("avanzar")) {
            return "Avanza el flujo procesal a la siguiente etapa";
        }

        if (comando.contains("monitorear")) {
            return "Monitorea los tiempos de atencion y cumplimiento de SLA (72h)";
        }

        if (comando.contains("alerta")) {
            return "Genera o gestiona alertas de vencimiento de tramite";
        }

        if (comando.contains("reporte")) {
            return "Genera reporte consolidado o estadistico del vicerrectorado";
        }

        if (comando.contains("listar")) {
            return "Lista todos los registros";
        }

        if (comando.contains("ver")) {
            return "Muestra un registro especifico por ID o codigo";
        }

        if (comando.contains("agregar") || comando.contains("iniciar")) {
            return "Agrega o inicia un nuevo registro con los parametros especificados";
        }

        if (comando.contains("modificar")) {
            return "Modifica un registro existente";
        }

        if (comando.contains("eliminar")) {
            return "Elimina un registro por su ID";
        }

        return "Comando de gestion disponible";
    }

    private static String formatValueByHeader(String header, String value) {
        if (value == null) {
            value = "";
        }

        value = value.trim();

        if (isImageHeader(header)) {
            return formatImageValue(value);
        }

        if (value.equalsIgnoreCase("Sin imagen")) {
            return "";
        }

        if (isStatusHeader(header)) {
            return formatStatusBadge(value);
        }

        return escapeHtml(value);
    }

    private static boolean isStatusHeader(String header) {
        if (header == null) {
            return false;
        }
        String h = header.trim().toUpperCase();
        return h.equals("ESTADO")
                || h.equals("ESTADO_SLA")
                || h.equals("ESTADO_VALIDACION")
                || h.equals("PRIORIDAD")
                || h.equals("VALIDEZ");
    }

    private static String formatStatusBadge(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        String v = value.trim().toUpperCase();

        // Estados positivos / activos
        if (v.equals("ACTIVO") || v.equals("VALIDO") || v.equals("VALIDADO")
                || v.equals("COMPLETADO") || v.equals("FIRMADO") || v.equals("DESPACHADO")
                || v.equals("CUMPLIDO") || v.equals("APROBADO") || v.equals("EN TIEMPO")
                || v.equals("EXITOSO")) {
            return "<span style=\"background-color: #DEF7EC; color: #03543F; border: 1px solid #BCF0DA; "
                    + "padding: 2px 7px; font-size: 11px; font-weight: bold;\">"
                    + escapeHtml(value)
                    + "</span>";
        }

        // Estados pendientes / advertencia / alerta
        if (v.equals("PENDIENTE") || v.equals("EN PROCESO") || v.equals("EN REVISION")
                || v.equals("INICIADO") || v.equals("REGISTRADO") || v.equals("ALERTA")
                || v.equals("POR VENCER") || v.equals("MEDIA")) {
            return "<span style=\"background-color: #FEF3C7; color: #92400E; border: 1px solid #FDE68A; "
                    + "padding: 2px 7px; font-size: 11px; font-weight: bold;\">"
                    + escapeHtml(value)
                    + "</span>";
        }

        // Estados críticos / negativos / rechazados / vencidos
        if (v.equals("INACTIVO") || v.equals("RECHAZADO") || v.equals("VENCIDO")
                || v.equals("ANULADO") || v.equals("OBSERVADO") || v.equals("BLOQUEADO")
                || v.equals("ALTA") || v.equals("URGENTE") || v.equals("CRITICO")
                || v.equals("REVOCADO") || v.equals("NO VALIDO") || v.equals("FUERA DE TIEMPO")) {
            return "<span style=\"background-color: #FDE8E8; color: #9B1C1C; border: 1px solid #F8B4B4; "
                    + "padding: 2px 7px; font-size: 11px; font-weight: bold;\">"
                    + escapeHtml(value)
                    + "</span>";
        }

        // Por defecto / neutro / baja prioridad
        return "<span style=\"background-color: #EFF6FF; color: #1E40AF; border: 1px solid #BFDBFE; "
                + "padding: 2px 7px; font-size: 11px; font-weight: bold;\">"
                + escapeHtml(value)
                + "</span>";
    }

    private static boolean isImageHeader(String header) {
        if (header == null) {
            return false;
        }

        String h = header.trim().toUpperCase();

        return h.equals("FOTO")
                || h.equals("IMAGEN")
                || h.equals("FOTO_PERFIL")
                || h.equals("QR");
    }

    private static String formatImageValue(String value) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return "<span style=\"color: #94A3B8; font-size: 11px; font-style: italic;\">Sin imagen</span>";
        }

        value = value.trim();

        if (value.startsWith("http://") || value.startsWith("https://")) {
            return "<img src=\"" + escapeHtml(value) + "\" alt=\"imagen\" "
                    + "style=\"width:70px; height:70px; object-fit:cover; border:1px solid #CBD5E1;\">";
        }

        if (value.startsWith("cid:")) {
            return "<img src=\"" + escapeHtml(value) + "\" alt=\"imagen\" "
                    + "style=\"width:70px; height:70px; object-fit:cover; border:1px solid #CBD5E1;\">";
        }

        if (value.startsWith("data:image")) {
            return "<img src=\"" + value + "\" alt=\"imagen\" "
                    + "style=\"width:85px; height:85px; object-fit:cover; border:1px solid #CBD5E1; display:block; margin:auto;\">";
        }

        // Si viene base64 puro desde la BD
        if (value.length() > 100) {
            String mime = detectarMimeBase64(value);

            return "<img src=\"data:" + mime + ";base64," + value + "\" alt=\"imagen\" "
                    + "style=\"width:70px; height:70px; object-fit:cover; border:1px solid #CBD5E1;\">";
        }

        // Si llega un nombre o ruta que no puede abrirse desde el correo
        return escapeHtml(value);
    }

private static String detectarMimeBase64(String base64) {
    if (base64 == null) {
        return "image/png";
    }

    String b = base64.trim();

    if (b.startsWith("/9j/")) {
        return "image/jpeg";
    }

    if (b.startsWith("iVBOR")) {
        return "image/png";
    }

    if (b.startsWith("R0lG")) {
        return "image/gif";
    }

    if (b.startsWith("UklGR")) {
        return "image/webp";
    }

    return "image/png";
}

    
    public static String generateDetailedError(
            String comandoRecibido,
            String causaPrincipal,
            String detalleFallo,
            String sintaxisCorrecta,
            String ejemploValido
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family: Arial, sans-serif; max-width: 850px; margin: 15px auto; border: 2px solid #DC2626; border-radius: 8px; overflow: hidden; background-color: #FFFFFF;'>");

        // Header rojo institucional
        sb.append("<div style='background-color: #DC2626; color: #FFFFFF; padding: 14px 18px;'>");
        sb.append("<h2 style='margin: 0; font-size: 17px; font-weight: bold; color: #FFFFFF;'>[ERROR EN COMANDO] &mdash; ").append(escapeHtml(causaPrincipal)).append("</h2>");
        sb.append("<span style='font-size: 11px; color: #FEE2E2;'>Sistema de Firma Digital del Vicerrectorado &bull; Grupo 20 SA</span>");
        sb.append("</div>");

        // Body
        sb.append("<div style='padding: 18px 20px; background-color: #FFFFFF;'>");

        // 1. Comando recibido
        if (comandoRecibido != null && !comandoRecibido.trim().isEmpty()) {
            sb.append("<div style='margin-bottom: 14px;'>");
            sb.append("<p style='margin: 0 0 5px 0; font-weight: bold; color: #374151; font-size: 12px; text-transform: uppercase;'>1. Comando que usted envi&oacute;:</p>");
            sb.append("<div style='background-color: #FEF2F2; color: #991B1B; padding: 10px 14px; border: 1px solid #FCA5A5; border-left: 5px solid #DC2626; font-family: Consolas, monospace; font-size: 13px; font-weight: bold;'>");
            sb.append(escapeHtml(comandoRecibido));
            sb.append("</div></div>");
        }

        // 2. Detalle del fallo
        sb.append("<div style='margin-bottom: 14px;'>");
        sb.append("<p style='margin: 0 0 5px 0; font-weight: bold; color: #DC2626; font-size: 12px; text-transform: uppercase;'>2. &iquest;En qu&eacute; se equivoc&oacute; o qu&eacute; datos faltan?:</p>");
        sb.append("<div style='background-color: #FFFBEB; color: #92400E; padding: 12px 14px; border: 1px solid #FCD34D; border-left: 5px solid #F59E0B; font-size: 13px; line-height: 1.6;'>");
        sb.append(escapeHtml(detalleFallo).replace("\n", "<br>"));
        sb.append("</div></div>");

        // 3. Sintaxis requerida
        if (sintaxisCorrecta != null && !sintaxisCorrecta.trim().isEmpty()) {
            sb.append("<div style='margin-bottom: 14px;'>");
            sb.append("<p style='margin: 0 0 5px 0; font-weight: bold; color: #166534; font-size: 12px; text-transform: uppercase;'>3. Estructura y sintaxis correcta requerida:</p>");
            sb.append("<div style='background-color: #F0FDF4; color: #166534; padding: 10px 14px; border: 1px solid #86EFAC; border-left: 5px solid #16A34A; font-family: Consolas, monospace; font-size: 12px; line-height: 1.5;'>");
            sb.append(escapeHtml(sintaxisCorrecta).replace("\n", "<br>"));
            sb.append("</div></div>");
        }

        // 4. Ejemplo válido
        if (ejemploValido != null && !ejemploValido.trim().isEmpty()) {
            sb.append("<div style='margin-bottom: 14px;'>");
            sb.append("<p style='margin: 0 0 5px 0; font-weight: bold; color: #1E40AF; font-size: 12px; text-transform: uppercase;'>4. Ejemplo v&aacute;lido (listo para copiar y enviar):</p>");
            sb.append("<div style='background-color: #EFF6FF; color: #1E40AF; padding: 10px 14px; border: 1px solid #93C5FD; border-left: 5px solid #2563EB; font-family: Consolas, monospace; font-size: 13px; font-weight: bold;'>");
            sb.append(escapeHtml(ejemploValido).replace("\n", "<br>"));
            sb.append("</div></div>");
        }

        sb.append("<p style='margin: 15px 0 0 0; font-size: 12px; color: #64748B; text-align: center; border-top: 1px solid #E2E8F0; padding-top: 10px;'>");
        sb.append("Puede enviar el comando <b>help</b> en el Asunto del correo para consultar la gu&iacute;a de todos los comandos.");
        sb.append("</p>");

        sb.append("</div>");
        sb.append("</div>");

        return insertInHtml(sb.toString());
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}