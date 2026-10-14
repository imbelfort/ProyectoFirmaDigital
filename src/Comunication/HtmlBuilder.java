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
        "UNIDAD_ACADEMICA",
        "CARGO",
        "ESTADO"
    };

    public static final String[] HEADERS_CLIENTE = {
            "ID_CLIENTE", "TIPO_CLIENTE", "LIMITE_CREDITO",
            "CREDITO_DISPONIBLE", "CREDITO_UTILIZADO"
    };

    public static final String[] HEADERS_VENDEDOR = {
            "ID_VENDEDOR", "COMISION_PORCENTAJE", "TOTAL_VENTAS"
    };

    public static final String[] HEADERS_PROPIETARIO = {
            "ID_PROPIETARIO", "NIVEL_ACCESO"
    };

    public static final String[] HEADERS_PROVEEDOR = {
            "ID", "NOMBRE", "NIT", "TELEFONO", "EMAIL", "DIRECCION",
            "ESTADO", "FECHA_CREACION", "FECHA_MODIFICACION"
    };

    public static final String[] HEADERS_CATEGORIA = {
            "ID", "ID_PROPIETARIO", "NOMBRE", "DESCRIPCION", "IMAGEN",
            "FECHA_CREACION", "FECHA_MODIFICACION"
    };

    public static final String[] HEADERS_PRODUCTO = {
            "ID",
            "ID_CATEGORIA",
            "NOMBRE",
            "CODIGO",
            "IMAGEN",
            "QR",
            "DESCRIPCION",
            "MARCA",
            "GENERO",
            "PRECIO_VENTA_BASE",
            "PRECIO_VENTA_MAYORISTA_BASE",
            "ESTADO",
            "FECHA_CREACION",
            "FECHA_MODIFICACION"
    };

    
    public static final String[] HEADERS_PRODUCTO_VARIANTE = {
            "ID",
            "ID_PRODUCTO",
            "SKU",
            "TALLA",
            "COLOR",
            "PRECIO_COMPRA",
            "PRECIO_VENTA",
            "PRECIO_VENTA_MAYORISTA",
            "STOCK_ACTUAL",
            "STOCK_MINIMO",
            "PUNTO_REORDEN",
            "COSTO_PROMEDIO",
            "ID_TECNICA_INVENTARIO",
            "ID_TECNICA_COSTO",
            "ESTADO",
            "FECHA_CREACION",
            "FECHA_MODIFICACION"
    };


    public static final String[] HEADERS_TECNICA_INVENTARIO = {
            "ID", "NOMBRE", "CODIGO", "DESCRIPCION", "ESTADO"
    };

    public static final String[] HEADERS_TECNICA_COSTO = {
            "ID", "NOMBRE", "CODIGO", "DESCRIPCION", "ESTADO"
    };

    public static final String[] HEADERS_PROMOCION = {
            "ID", "ID_PROPIETARIO", "NOMBRE", "DESCRIPCION",
            "PORCENTAJE_DESCUENTO", "FECHA_INICIO", "FECHA_FIN",
            "ESTADO", "FECHA_CREACION", "FECHA_MODIFICACION"
    };

    public static final String[] HEADERS_PROMOCION_CATEGORIA = {
            "ID_PROMOCION", "ID_CATEGORIA", "APLICA_MAYORISTA", "APLICA_MINORISTA"
    };

    public static final String[] HEADERS_PROMOCION_PRODUCTO = {
            "ID_PROMOCION", "ID_PRODUCTO", "APLICA_MAYORISTA", "APLICA_MINORISTA"
    };

    public static final String[] HEADERS_COMPRA = {
            "ID", "ID_PROPIETARIO", "ID_PROVEEDOR", "NUM_COMPRA",
            "PRECIO_TOTAL", "FECHA_COMPRA"
    };

    public static final String[] HEADERS_DETALLE_COMPRA = {
            "ID_DETALLE", "ID_COMPRA", "ID_PRODUCTO_VARIANTE",
            "CANTIDAD", "PRECIO", "DESCUENTO", "SUBTOTAL"
    };

    public static final String[] HEADERS_LOTE_INVENTARIO = {
            "ID", "ID_PRODUCTO_VARIANTE", "ID_DETALLE_COMPRA",
            "FECHA_INGRESO", "CANTIDAD_INICIAL", "CANTIDAD_DISPONIBLE",
            "COSTO_UNITARIO", "ESTADO"
    };

    public static final String[] HEADERS_CARRITO = {
            "ID", "ID_CLIENTE", "FECHA_CREACION", "FECHA_ACTUALIZACION"
    };

    public static final String[] HEADERS_CARRITO_DETALLE = {
            "ID_CARRITO", "ID_PRODUCTO_VARIANTE", "CANTIDAD",
            "PRECIO_UNITARIO", "DESCUENTO"
    };

    public static final String[] HEADERS_PEDIDO = {
            "ID", "ID_CLIENTE", "ID_CARRITO", "NUMERO_PEDIDO",
            "FECHA_PEDIDO", "FECHA_PAGO", "FECHA_ENVIO", "FECHA_ENTREGA",
            "FECHA_RECIBIDO_CONFORME", "TIPO_VENTA", "SUBTOTAL",
            "DESCUENTO", "TOTAL", "ESTADO", "DIRECCION_ENTREGA", "OBSERVACIONES"
    };

    public static final String[] HEADERS_PEDIDO_DETALLE = {
            "ID_DETALLE", "ID_PEDIDO", "ID_PRODUCTO_VARIANTE",
            "CANTIDAD", "PRECIO_UNITARIO", "SUBTOTAL", "DESCUENTO"
    };

        public static final String[] HEADERS_VENTA = {
                "ID", "ID_VENDEDOR", "ID_CLIENTE", "ID_PEDIDO",
                "NUMERO_VENTA", "TIPO", "ORIGEN", "TIPO_PAGO",
                "TOTAL", "ESTADO", "OBSERVACIONES", "FECHA_VENTA"
        };

        public static final String[] HEADERS_DETALLE_VENTA = {
                "ID_DETALLE",
                "ID_VENTA",
                "ID_PRODUCTO_VARIANTE",
                "CANTIDAD",
                "PRECIO_UNITARIO",
                "SUBTOTAL",
                "DESCUENTO"
        };

        public static final String[] HEADERS_SALIDA_LOTE = {
                "ID",
                "ID_DETALLE_VENTA",
                "ID_LOTE",
                "CANTIDAD",
                "COSTO_UNITARIO_APLICADO",
                "COSTO_TOTAL",
                "FECHA_SALIDA"
        };

        public static final String[] HEADERS_MOVIMIENTO_INVENTARIO = {
                "ID", "ID_PRODUCTO_VARIANTE", "ID_TECNICA_INVENTARIO",
                "ID_TECNICA_COSTO", "TIPO_MOVIMIENTO", "MOTIVO",
                "CANTIDAD", "COSTO_UNITARIO", "COSTO_TOTAL",
                "STOCK_ANTERIOR", "STOCK_RESULTANTE", "FECHA", "OBSERVACION"
        };

        public static final String[] HEADERS_METODO_PAGO = {
                "ID",
                "NOMBRE",
                "DESCRIPCION"
        };


        public static final String[] HEADERS_PAGO_CONTADO = {
                "ID", "ID_VENTA", "ID_METODO_PAGO", "FECHA",
                "MONTO_PAGO", "INTERES_MORA_COBRADO", "RECARGO_EXTRA", "OBSERVACIONES", "ESTADO"
        };

        public static final String[] HEADERS_CREDITO = {
                "ID", "ID_VENTA", "MONTO_CREDITO", "MONTO_PENDIENTE",
                "MONTO_PAGADO", "FECHA_OTORGAMIENTO", "FECHA_VENCIMIENTO",
                "INTERES", "ESTADO", "CUOTAS_TOTAL", "DIAS_MORA",
                "FECHA_CREACION", "FECHA_ACTUALIZACION"
        };

        public static final String[] HEADERS_PAGO_CUOTA = {
                "ID", "ID_CREDITO", "ID_METODO_PAGO", "NUMERO_CUOTA",
                "INTERES_CUOTA", "FECHA_VENCIMIENTO", "FECHA_PAGO",
                "MONTO_PAGADO", "DIAS_MORA", "MONTO_PENDIENTE",
                "ESTADO", "FECHA_CREACION", "FECHA_ACTUALIZACION"
        };

        public static final String[] HEADERS_REPORTE = {
                "ID", "ID_USUARIO", "TIPO_REPORTE", "FECHA_GENERACION",
                "PARAMETROS", "FECHA_CREACION", "FECHA_ACTUALIZACION"
        };

        public static final String[] HEADERS_NOTIFICACION = {
                "ID", "ID_USUARIO", "TIPO", "TITULO", "MENSAJE",
                "FECHA_CREACION", "ESTADO"
        };

        public static final String[] HEADERS_REPORTE_VENTAS_RESUMEN = {
                "PERIODO",
                "CANTIDAD_VENTAS",
                "MONTO_TOTAL"
        };

        public static final String[] HEADERS_REPORTE_CLIENTES_MES = {
                "PERIODO",
                "CANTIDAD_CLIENTES"
        };

        public static final String[] HEADERS_REPORTE_VENTAS_METODO_PAGO = {
                "METODO_PAGO",
                "CANTIDAD_PAGOS",
                "MONTO_TOTAL"
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

        if (comando.contains("listar_por_producto")) {
            return "Lista las variantes pertenecientes a un producto especi­fico";
        }

        if (comando.contains("listar")) {
            return "Lista todos los registros";
        }

        if (comando.contains("ver")) {
            return "Muestra un registro especi­fico por ID";
        }

        if (comando.contains("agregar")) {
            return "Agrega un nuevo registro con los parametros especificados";
        }

        if (comando.contains("modificar")) {
            return "Modifica un registro existente";
        }

        if (comando.contains("eliminar")) {
            return "Elimina un registro por su ID";
        }

        return "Comando disponible";
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