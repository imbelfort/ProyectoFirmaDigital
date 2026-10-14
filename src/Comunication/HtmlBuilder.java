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

public static String generateTable(String title, String[] headers, List<String[]> data) {
    StringBuilder tableHeadersHtml = new StringBuilder();

    for (String header : headers) {
        tableHeadersHtml.append("<th style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                .append("background-color: #6A1B9A; color: white; font-size: 12px;\">")
                .append(escapeHtml(header))
                .append("</th>");
    }

    StringBuilder tableBodyHtml = new StringBuilder();

    if (data == null || data.isEmpty()) {
        tableBodyHtml.append("<tr>")
                .append("<td colspan=\"")
                .append(headers.length)
                .append("\" style=\"border: 1px solid #6A1B9A; padding: 8px; ")
                .append("color: #4A148C; font-size: 12px; text-align: center;\">")
                .append("No existen registros para mostrar")
                .append("</td>")
                .append("</tr>");
    } else {
        for (String[] element : data) {
            tableBodyHtml.append("<tr style=\"border: 1px solid #6A1B9A; background-color: #F3E5F5;\">");

            for (int i = 0; i < headers.length; i++) {
                String value = "";

                if (element != null && i < element.length && element[i] != null) {
                    value = element[i];
                }

                String header = headers[i];

                tableBodyHtml.append("<td style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                        .append("color: #4A148C; font-size: 12px; text-align:center; vertical-align:middle;\">")
                        .append(formatValueByHeader(header, value))
                        .append("</td>");
            }

            tableBodyHtml.append("</tr>");
        }
    }

    String html = "<center><h3 style=\"color: #4A148C; font-size: 16px;\">"
            + escapeHtml(title)
            + "</h3></center>"
            + "<table style=\"border-collapse: collapse; border: 1px solid #6A1B9A; "
            + "width: 95%; margin: auto; font-family: Arial; font-size: 12px;\">"
            + "<thead><tr>"
            + tableHeadersHtml
            + "</tr></thead>"
            + "<tbody>"
            + tableBodyHtml
            + "</tbody>"
            + "</table>";

    return insertInHtml(html);
}

public static String generateTableContentSinHtml(String title, String[] headers, List<String[]> data) {
    StringBuilder tableHeadersHtml = new StringBuilder();

    for (String header : headers) {
        tableHeadersHtml.append("<th style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                .append("background-color: #6A1B9A; color: white; font-size: 12px;\">")
                .append(escapeHtml(header))
                .append("</th>");
    }

    StringBuilder tableBodyHtml = new StringBuilder();

    if (data == null || data.isEmpty()) {
        tableBodyHtml.append("<tr>")
                .append("<td colspan=\"")
                .append(headers.length)
                .append("\" style=\"border: 1px solid #6A1B9A; padding: 8px; ")
                .append("color: #4A148C; font-size: 12px; text-align: center;\">")
                .append("No existen registros para mostrar")
                .append("</td>")
                .append("</tr>");
    } else {
        for (String[] element : data) {
            tableBodyHtml.append("<tr style=\"border: 1px solid #6A1B9A; background-color: #F3E5F5;\">");

            for (int i = 0; i < headers.length; i++) {
                String value = "";

                if (element != null && i < element.length && element[i] != null) {
                    value = element[i];
                }

                String header = headers[i];

                tableBodyHtml.append("<td style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                        .append("color: #4A148C; font-size: 12px; text-align:center; vertical-align:middle;\">")
                        .append(formatValueByHeader(header, value))
                        .append("</td>");
            }

            tableBodyHtml.append("</tr>");
        }
    }

    return "<center><h3 style=\"color: #4A148C; font-size: 16px;\">"
            + escapeHtml(title)
            + "</h3></center>"
            + "<table style=\"border-collapse: collapse; border: 1px solid #6A1B9A; "
            + "width: 95%; margin: auto; font-family: Arial; font-size: 12px;\">"
            + "<thead><tr>"
            + tableHeadersHtml
            + "</tr></thead>"
            + "<tbody>"
            + tableBodyHtml
            + "</tbody>"
            + "</table>";
}
    // ============================================================
    // GENERAR MENSAJE + TABLA
    // ============================================================

    public static String generateMessageAndTable(String mensaje, String tituloTabla, String[] headers, List<String[]> data) {
        String htmlMensaje = "<center><h3 style=\"color: #4A148C; font-size: 16px;\">"
                + escapeHtml(mensaje)
                + "</h3></center>";

        String tabla = generateTableContent(tituloTabla, headers, data);

        return insertInHtml(htmlMensaje + tabla);
    }

private static String generateTableContent(String title, String[] headers, List<String[]> data) {
    StringBuilder tableHeadersHtml = new StringBuilder();

    for (String header : headers) {
        tableHeadersHtml.append("<th style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                .append("background-color: #6A1B9A; color: white; font-size: 12px;\">")
                .append(escapeHtml(header))
                .append("</th>");
    }

    StringBuilder tableBodyHtml = new StringBuilder();

    if (data == null || data.isEmpty()) {
        tableBodyHtml.append("<tr>")
                .append("<td colspan=\"")
                .append(headers.length)
                .append("\" style=\"border: 1px solid #6A1B9A; padding: 8px; ")
                .append("color: #4A148C; font-size: 12px; text-align: center;\">")
                .append("No existen registros para mostrar")
                .append("</td>")
                .append("</tr>");
    } else {
        for (String[] element : data) {
            tableBodyHtml.append("<tr style=\"border: 1px solid #6A1B9A; background-color: #F3E5F5;\">");

            for (int i = 0; i < headers.length; i++) {
                String value = "";

                if (element != null && i < element.length && element[i] != null) {
                    value = element[i];
                }

                String header = headers[i];

                tableBodyHtml.append("<td style=\"border: 1px solid #6A1B9A; padding: 4px; ")
                        .append("color: #4A148C; font-size: 12px; text-align:center; vertical-align:middle;\">")
                        .append(formatValueByHeader(header, value))
                        .append("</td>");
            }

            tableBodyHtml.append("</tr>");
        }
    }

    return "<center><h3 style=\"color: #4A148C; font-size: 16px;\">"
            + escapeHtml(title)
            + "</h3></center>"
            + "<table style=\"border-collapse: collapse; border: 1px solid #6A1B9A; "
            + "width: 95%; margin: auto; font-family: Arial; font-size: 12px;\">"
            + "<thead><tr>"
            + tableHeadersHtml
            + "</tr></thead>"
            + "<tbody>"
            + tableBodyHtml
            + "</tbody>"
            + "</table>";
}


    // ============================================================
    // GENERAR TEXTO
    // ============================================================

    public static String generateText(String[] args) {
        if (args == null || args.length == 0) {
            return insertInHtml("<center><h3>Sin contenido</h3></center>");
        }

        StringBuilder acumulative = new StringBuilder();

        acumulative.append("<center><h2>")
                .append(escapeHtml(args[0]))
                .append("</h2></center>");

        for (int i = 1; i < args.length; i++) {
            acumulative.append("<center><h3>")
                    .append(escapeHtml(args[i]))
                    .append("</h3></center>");
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

            acumulative.append("<tr>")
                .append("<td style=\"border: 1px solid #6A1B9A; padding: 6px; ")
                .append("background-color: #6A1B9A; color: white; font-weight: bold;\">")
                .append(escapeHtml(headers[i]))
                .append("</td>")
                .append("<td style=\"border: 1px solid #6A1B9A; padding: 6px; color: #4A148C;\">")
                .append(formatValueByHeader(headers[i], value))
                .append("</td>")
                .append("</tr>");
        }

        String table = "<div align=\"center\">"
                + "<h2 style=\"color: #4A148C;\">"
                + escapeHtml(title)
                + "</h2>"
                + "</div>"
                + "<table style=\"border-collapse: collapse; width: 70%; margin: auto; "
                + "font-family: Arial; font-size: 13px;\">"
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
                    "<center><h3>No existen comandos para mostrar</h3></center>"
            );
        }

        String[] lineas = comandosTexto.split("\\r?\\n");
        StringBuilder rowsHtml = new StringBuilder();

        int startIndex = 0;

        if (lineas.length > 0
                && lineas[0].toLowerCase().contains("comandos")) {
            startIndex = 1;
        }

        for (int i = startIndex; i < lineas.length; i++) {
            String linea = lineas[i];

            if (linea == null) {
                continue;
            }

            linea = linea.trim();

            /*
            * Ignora líneas vacías y caracteres sueltos
            * que no representan comandos.
            */
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

            rowsHtml.append("<tr>")
                    .append("<td style='")
                    .append("border:1px solid #4A148C;")
                    .append("padding:8px;")
                    .append("color:#4A148C;")
                    .append("vertical-align:middle;")
                    .append("'>")
                    .append(escapeHtml(linea))
                    .append("</td>")

                    .append("<td style='")
                    .append("border:1px solid #4A148C;")
                    .append("padding:8px;")
                    .append("color:#4A148C;")
                    .append("vertical-align:middle;")
                    .append("'>")
                    .append(escapeHtml(descripcion))
                    .append("</td>")
                    .append("</tr>");
        }

        String html =
                "<center>"
                + "<h3 style='color:#4A148C;'>"
                + escapeHtml(titulo)
                + "</h3>"
                + "</center>"

                + "<table style='"
                + "border-collapse:collapse;"
                + "width:90%;"
                + "margin:auto;"
                + "font-family:Arial,sans-serif;"
                + "font-size:14px;"
                + "'>"

                + "<thead>"
                + "<tr>"

                + "<th style='"
                + "border:1px solid #4A148C;"
                + "background-color:#6A1B9A;"
                + "color:white;"
                + "padding:8px;"
                + "'>"
                + "Comando"
                + "</th>"

                + "<th style='"
                + "border:1px solid #4A148C;"
                + "background-color:#6A1B9A;"
                + "color:white;"
                + "padding:8px;"
                + "'>"
                + "Descripción"
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
        return HTML_OPEN + BODY_OPEN + data + BODY_CLOSE + HTML_CLOSE;
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

    // Seguridad: si por error llega "Sin imagen" a una columna que NO es imagen,
    // no lo mostramos.
    if (value.equalsIgnoreCase("Sin imagen")) {
        return "";
    }

    return escapeHtml(value);
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
                return "Sin imagen";
        }

        value = value.trim();

        if (value.startsWith("http://") || value.startsWith("https://")) {
                return "<img src=\"" + escapeHtml(value) + "\" alt=\"imagen\" "
                        + "style=\"width:75px; height:75px; object-fit:cover; border-radius:6px; "
                        + "border:1px solid #6A1B9A;\">";
        }

        if (value.startsWith("cid:")) {
                return "<img src=\"" + escapeHtml(value) + "\" alt=\"imagen\" "
                        + "style=\"width:75px; height:75px; object-fit:cover; border-radius:6px; "
                        + "border:1px solid #6A1B9A;\">";
        }

        if (value.startsWith("data:image")) {
                return "<img src=\"" + value + "\" alt=\"imagen\" "
                        + "style=\"width:90px; height:90px; object-fit:cover; border-radius:8px; "
                        + "border:1px solid #6A1B9A; display:block; margin:auto;\">";
        }

        // Si viene base64 puro desde la BD
        if (value.length() > 100) {
                String mime = detectarMimeBase64(value);

                return "<img src=\"data:" + mime + ";base64," + value + "\" alt=\"imagen\" "
                        + "style=\"width:75px; height:75px; object-fit:cover; border-radius:6px; "
                        + "border:1px solid #6A1B9A;\">";
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
        sb.append("<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 15px auto; border: 2px solid #D32F2F; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>");

        // Header
        sb.append("<div style='background-color: #D32F2F; color: white; padding: 12px 18px;'>");
        sb.append("<h2 style='margin: 0; font-size: 18px; font-weight: bold;'>[ERROR EN COMANDO] — ").append(escapeHtml(causaPrincipal)).append("</h2>");
        sb.append("<span style='font-size: 12px; opacity: 0.9;'>Sistema de Firma Digital del Vicerrectorado (Grupo 20 SA)</span>");
        sb.append("</div>");

        // Body
        sb.append("<div style='background-color: #FFFFFF; padding: 18px;'>");

        // Comando recibido
        if (comandoRecibido != null && !comandoRecibido.trim().isEmpty()) {
            sb.append("<p style='margin: 0 0 4px 0; font-weight: bold; color: #424242; font-size: 13px;'>Comando recibido:</p>");
            sb.append("<div style='background: #FFEBEE; color: #C62828; padding: 10px 14px; border-radius: 6px; font-family: Consolas, monospace; font-size: 13px; margin-bottom: 14px; border-left: 4px solid #D32F2F;'>");
            sb.append(escapeHtml(comandoRecibido));
            sb.append("</div>");
        }

        // Detalle del fallo
        sb.append("<p style='margin: 0 0 4px 0; font-weight: bold; color: #D32F2F; font-size: 13px;'>¿Qué falló exactamente?:</p>");
        sb.append("<div style='background: #FFF8E1; color: #E65100; padding: 10px 14px; border-radius: 6px; font-size: 13px; line-height: 1.5; margin-bottom: 14px; border-left: 4px solid #FFA000; white-space: pre-wrap;'>");
        sb.append(escapeHtml(detalleFallo));
        sb.append("</div>");

        // Sintaxis esperada
        if (sintaxisCorrecta != null && !sintaxisCorrecta.trim().isEmpty()) {
            sb.append("<p style='margin: 0 0 4px 0; font-weight: bold; color: #2E7D32; font-size: 13px;'>Sintaxis requerida:</p>");
            sb.append("<div style='background: #E8F5E9; color: #2E7D32; padding: 10px 14px; border-radius: 6px; font-family: Consolas, monospace; font-size: 13px; margin-bottom: 14px; border-left: 4px solid #4CAF50;'>");
            sb.append(escapeHtml(sintaxisCorrecta));
            sb.append("</div>");
        }

        // Ejemplo válido
        if (ejemploValido != null && !ejemploValido.trim().isEmpty()) {
            sb.append("<p style='margin: 0 0 4px 0; font-weight: bold; color: #1565C0; font-size: 13px;'>Ejemplo de cómo debe enviarse:</p>");
            sb.append("<div style='background: #E3F2FD; color: #0D47A1; padding: 10px 14px; border-radius: 6px; font-family: Consolas, monospace; font-size: 13px; margin-bottom: 14px; border-left: 4px solid #1E88E5;'>");
            sb.append(escapeHtml(ejemploValido));
            sb.append("</div>");
        }

        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #757575; text-align: center;'>");
        sb.append("Envie <b>help</b> para consultar la lista de todos los comandos disponibles.");
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