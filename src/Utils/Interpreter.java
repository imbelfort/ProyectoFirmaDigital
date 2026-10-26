package Utils;

import Interfaces.ITokenCasoUsoEventListener;
import Comunication.TokenEvent;

public class Interpreter implements Runnable {

    private ITokenCasoUsoEventListener listener;
    private Analex analex;

    private String command;
    private String sender;

    public Interpreter(String command, String sender) {
        this.command = command;
        this.sender = sender;
    }

    public ITokenCasoUsoEventListener getListener() {
        return listener;
    }

    public void setListener(ITokenCasoUsoEventListener listener) {
        this.listener = listener;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private void filterEvent(TokenCommand tokenCommand) {
        TokenEvent tokenEvent = new TokenEvent(this, sender);
        tokenEvent.setAction(tokenCommand.getAction());

        int countParams = tokenCommand.countParams();

        for (int i = 0; i < countParams; i++) {
            int pos = tokenCommand.getParams(i);
            tokenEvent.addParams(analex.getParam(pos));
        }

        switch (tokenCommand.getName()) {
            case Token.USUARIO:
                listener.usuario(tokenEvent);
                break;
            case Token.DELEGACION:
                listener.delegacion(tokenEvent);
                break;
            case Token.INDICADOR:
                listener.indicador(tokenEvent);
                break;
            case Token.TRAMITE:
                listener.tramite(tokenEvent);
                break;
            case Token.DOCUMENTO:
                listener.documento(tokenEvent);
                break;
            case Token.TIPO_TRAMITE:
                listener.tipoTramite(tokenEvent);
                break;
            case Token.FLUJO:
                listener.flujo(tokenEvent);
                break;
            case Token.GUIA:
                listener.guia(tokenEvent);
                break;
            case Token.OBSERVACION:
                listener.observacion(tokenEvent);
                break;
            case Token.BITACORA:
                listener.bitacora(tokenEvent);
                break;
            case Token.FIRMA:
                listener.firma(tokenEvent);
                break;
            case Token.DESPACHO:
                listener.despacho(tokenEvent);
                break;
            case Token.CERTIFICADO:
                listener.certificado(tokenEvent);
                break;
            case Token.PORTAL:
                listener.portal(tokenEvent);
                break;
            case Token.SLA:
                listener.sla(tokenEvent);
                break;
            case Token.ALERTA:
                listener.alerta(tokenEvent);
                break;
            case Token.REPORTE:
                listener.reporte(tokenEvent);
                break;
            default:
                listener.posibleserrores(tokenEvent);
                break;
        }
    }

    private void tokenError(Token token, String error) {
        TokenEvent tokenEvent = new TokenEvent(this, sender);
        tokenEvent.setAction(token.getAttribute());
        tokenEvent.addParams(error != null && !error.isEmpty() ? error : "Término no reconocido");

        String lexeme = analex.lexeme();
        if (token.getAttribute() == Token.ERROR_CHARACTER || (lexeme != null && lexeme.matches(".*[^a-zA-Z0-9_,;\\[\\]\\s\\.@-].*"))) {
            tokenEvent.setErrorCode(ErrorCode.ERR_CMD_004);
            tokenEvent.setSuggestion("Utilice solo caracteres alfanuméricos y corchetes para los parámetros [param1;param2]. Ejemplo: usuario listar");
        } else {
            tokenEvent.setErrorCode(ErrorCode.ERR_CMD_002);
            tokenEvent.setSuggestion("help (para ver el manual completo de comandos)");
        }
        listener.error(tokenEvent);
    }

    @Override
    public void run() {
        if (command == null || command.trim().isEmpty()) {
            TokenEvent errorEvent = new TokenEvent(this, sender);
            errorEvent.setErrorCode(ErrorCode.ERR_CMD_001);
            errorEvent.setAction(Token.ERROR_COMMAND);
            errorEvent.addParams("El asunto (Subject) del correo electrónico está vacío. Por favor ingrese el comando en el asunto.");
            errorEvent.setSuggestion("help (para ver el manual completo de comandos)");
            listener.error(errorEvent);
            return;
        }

        // Detección temprana de caracteres de control no imprimibles (ASCII 0..31 y 127)
        for (int i = 0; i < command.length(); i++) {
            char ch = command.charAt(i);
            if ((ch < 32 && ch != '\t' && ch != '\n' && ch != '\r') || ch == 127) {
                TokenEvent errorEvent = new TokenEvent(this, sender);
                errorEvent.setErrorCode(ErrorCode.ERR_CMD_004);
                errorEvent.setAction(Token.ERROR_CHARACTER);
                errorEvent.addParams("El comando contiene un carácter de control no permitido (código ASCII " + (int) ch + ").");
                errorEvent.setSuggestion("Utilice solo texto y caracteres alfanuméricos legibles. Ejemplo: usuario listar");
                listener.error(errorEvent);
                return;
            }
        }

        String rawCommand = command.trim();

        // Detección de caracteres peligrosos fuera de parámetros
        if (rawCommand.contains("<") || rawCommand.contains(">") || rawCommand.contains("|") || rawCommand.contains("#") || rawCommand.contains("$")) {
            TokenEvent errorEvent = new TokenEvent(this, sender);
            errorEvent.setErrorCode(ErrorCode.ERR_CMD_004);
            errorEvent.setAction(Token.ERROR_CHARACTER);
            errorEvent.addParams("El comando contiene caracteres especiales no permitidos (<, >, |, #, $). Por seguridad se rechaza la solicitud.");
            errorEvent.setSuggestion("Utilice solo caracteres alfanuméricos y corchetes para los parámetros [param1;param2]. Ejemplo: usuario listar");
            listener.error(errorEvent);
            return;
        }

        // Análisis sintáctico estructural previo (detección de orden y completitud)
        String cleanCommand = rawCommand;
        String[] words = cleanCommand.split("\\s+");
        String firstWord = words[0].replaceAll("[,;\\]\\[]", "").trim();
        String secondWord = words.length > 1 ? words[1].replaceAll("[,;\\]\\[]", "").trim() : null;

        // Comando help / ayuda global
        if ("help".equalsIgnoreCase(firstWord) || "ayuda".equalsIgnoreCase(firstWord)) {
            // Se procesará normalmente
        }
        // 1. Caso Orden Invertido: Acción escrita antes de Entidad (ej: "listar usuario")
        else if (Token.isAction(firstWord)) {
            if (secondWord != null && Token.isCU(secondWord)) {
                TokenEvent errorEvent = new TokenEvent(this, sender);
                errorEvent.setErrorCode(ErrorCode.ERR_ORD_001);
                errorEvent.setAction(Token.ERROR_COMMAND);
                errorEvent.addParams("Orden sintáctico invertido: Se escribió la acción '" + firstWord + "' antes de la entidad '" + secondWord + "'.");
                errorEvent.setSuggestion(secondWord + " " + firstWord);
                listener.error(errorEvent);
                return;
            }
            if (words.length == 1) {
                TokenEvent errorEvent = new TokenEvent(this, sender);
                errorEvent.setErrorCode(ErrorCode.ERR_INC_001);
                errorEvent.setAction(Token.ERROR_COMMAND);
                errorEvent.addParams("Orden incompleta: Se especificó la acción '" + firstWord + "' pero falta indicar sobre qué entidad aplicarla.");
                errorEvent.setSuggestion("usuario " + firstWord + "  (o consulte 'help' para ver entidades disponibles)");
                listener.error(errorEvent);
                return;
            }
        }
        // 2. Caso Parámetro colocado al inicio antes de la entidad (ej: "1 usuario consultar" o "[1] tramite consultar")
        else if (firstWord.matches("^\\d+$") && secondWord != null && Token.isCU(secondWord)) {
            TokenEvent errorEvent = new TokenEvent(this, sender);
            errorEvent.setErrorCode(ErrorCode.ERR_ORD_002);
            errorEvent.setAction(Token.ERROR_COMMAND);
            String accionEsperada = words.length > 2 ? words[2].replaceAll("[,;\\]\\[]", "") : "consultar";
            errorEvent.addParams("Se detectó un parámetro numérico ('" + firstWord + "') antes del nombre de la entidad o acción.");
            errorEvent.setSuggestion(secondWord + " " + accionEsperada + " [" + firstWord + "]");
            listener.error(errorEvent);
            return;
        }
        // 3. Comprobar si la primera palabra es una ENTIDAD conocida
        else if (Token.isCU(firstWord)) {
            // Caso de Orden Incompleta: Solo se escribió la entidad sin acción
            if (words.length == 1) {
                TokenEvent errorEvent = new TokenEvent(this, sender);
                errorEvent.setErrorCode(ErrorCode.ERR_INC_001);
                errorEvent.setAction(Token.ERROR_COMMAND);
                errorEvent.addParams("Orden incompleta: Ingresó la entidad '" + firstWord + "' pero no indicó la acción a realizar.");
                errorEvent.setSuggestion(sugerirAccionesParaEntidad(firstWord));
                listener.error(errorEvent);
                return;
            }

            // Si hay segunda palabra pero no es una acción válida
            if (secondWord != null && !Token.isAction(secondWord)) {
                // Caso: ID colocado antes de la acción (ej: 'usuario 1 consultar')
                if (secondWord.matches("^\\d+$") && words.length > 2 && Token.isAction(words[2])) {
                    TokenEvent errorEvent = new TokenEvent(this, sender);
                    errorEvent.setErrorCode(ErrorCode.ERR_ORD_003);
                    errorEvent.setAction(Token.ERROR_COMMAND);
                    errorEvent.addParams("Parámetros mal ordenados: Se colocó el ID '" + secondWord + "' antes de la acción '" + words[2] + "'.");
                    errorEvent.setSuggestion(firstWord + " " + words[2] + " [" + secondWord + "]");
                    listener.error(errorEvent);
                    return;
                }

                // Acción no reconocida para la entidad (Typo o Incompatible)
                TokenEvent errorEvent = new TokenEvent(this, sender);
                errorEvent.setErrorCode(ErrorCode.ERR_CMD_003);
                errorEvent.setAction(Token.ERROR_COMMAND);
                String sugerida = Token.sugerirAccionCercana(secondWord);
                String msg = "Acción '" + secondWord + "' no reconocida para la entidad '" + firstWord + "'.";
                if (sugerida != null) {
                    msg += " ¿Quizás quiso decir '" + sugerida + "'?";
                }
                errorEvent.addParams(msg);
                errorEvent.setSuggestion(sugerirAccionesParaEntidad(firstWord));
                listener.error(errorEvent);
                return;
            }
        }
        // 4. La primera palabra NO es ni CU ni Action conocida (Typo en Entidad)
        else {
            TokenEvent errorEvent = new TokenEvent(this, sender);
            errorEvent.setErrorCode(ErrorCode.ERR_CMD_002);
            errorEvent.setAction(Token.ERROR_COMMAND);
            String sugerido = Token.sugerirCUCercano(firstWord);
            if (sugerido != null) {
                errorEvent.addParams("Entidad o comando '" + firstWord + "' no reconocido. ¿Quizás quiso decir '" + sugerido + "'?");
                String segunda = secondWord != null && Token.isAction(secondWord) ? secondWord : "listar";
                errorEvent.setSuggestion(sugerido + " " + segunda);
            } else {
                errorEvent.addParams("Entidad o comando '" + firstWord + "' no reconocido por el sistema.");
                errorEvent.setSuggestion("help (para consultar la lista de entidades y comandos disponibles)");
            }
            listener.error(errorEvent);
            return;
        }

        // Si pasó los filtros sintácticos previos, proceder con el Analizador Léxico Analex
        analex = new Analex(stripAttachmentMarkers(command));
        TokenCommand tokenCommand = new TokenCommand();
        Token token;

        boolean cuSet = false;
        while ((token = analex.Preanalisis()).getName() != Token.END && token.getName() != Token.ERROR) {
            if (token.getName() == Token.CU) {
                if (!cuSet) {
                    tokenCommand.setName(token.getAttribute());
                    cuSet = true;
                } else {
                    tokenCommand.setAction(token.getAttribute());
                }
            } else if (token.getName() == Token.ACTION) {
                tokenCommand.setAction(token.getAttribute());
            } else if (token.getName() == Token.PARAMS) {
                tokenCommand.addParams(token.getAttribute());
            }

            analex.next();
        }

        if (token.getName() == Token.END) {
            filterEvent(tokenCommand);
        } else if (token.getName() == Token.ERROR) {
            tokenError(token, analex.lexeme());
        }
    }

    public static String sugerirAccionesParaEntidad(String entidad) {
        if (entidad == null) return "Consulte 'help' para ver el catálogo de comandos.";
        String e = entidad.toLowerCase();

        if (e.contains("usuari")) {
            return "Acciones para usuario:\n" +
                   "- usuario listar\n" +
                   "- usuario consultar [id]\n" +
                   "- usuario agregar [registro;ci;nombre;apellidos;email;telefono;rol;facultad;carrera]\n" +
                   "- usuario modificar [id;registro;ci;nombre;apellidos;email;telefono;rol;facultad;carrera;estado]\n" +
                   "- usuario eliminar [id]";
        }
        if (e.contains("tramit")) {
            return "Acciones para tramite:\n" +
                   "- tramite listar\n" +
                   "- tramite consultar [id]\n" +
                   "- tramite agregar [codigo;id_solicitante;id_tipo;asunto;prioridad]\n" +
                   "- tramite modificar [id;asunto;prioridad;estado]\n" +
                   "- tramite eliminar [id]";
        }
        if (e.contains("documen")) {
            return "Acciones para documento:\n" +
                   "- documento listar [id_tramite]\n" +
                   "- documento consultar [id]\n" +
                   "- documento agregar [id_tramite;id_requisito;archivo_url]\n" +
                   "- documento validar [id_doc;APROBADO/RECHAZADO;observacion]\n" +
                   "- documento eliminar [id]";
        }
        if (e.contains("tipo") || e.contains("catalogo")) {
            return "Acciones para tipo_tramite:\n" +
                   "- tipo_tramite listar\n" +
                   "- tipo_tramite consultar [id]\n" +
                   "- tipo_tramite agregar [codigo;nombre;descripcion;sla_horas;costo_bs]\n" +
                   "- tipo_tramite modificar [id;nombre;sla_horas;costo_bs;estado]";
        }
        if (e.contains("flujo")) {
            return "Acciones para flujo:\n" +
                   "- flujo listar [id_tramite]\n" +
                   "- flujo consultar [id]\n" +
                   "- flujo avanzar [id_tramite;nueva_etapa;id_responsable]";
        }
        if (e.contains("guia")) {
            return "Acciones para guia:\n" +
                   "- guia listar [id_tipo_tramite]\n" +
                   "- guia consultar [id]\n" +
                   "- guia agregar [id_tipo;paso;etapa;instrucciones;rol;tiempo]";
        }
        if (e.contains("observa")) {
            return "Acciones para observacion:\n" +
                   "- observacion listar [id_tramite]\n" +
                   "- observacion agregar [id_tramite;id_usuario;tipo;descripcion]\n" +
                   "- observacion subsanar [id_obs;detalle_subsanacion]";
        }
        if (e.contains("bitacora")) {
            return "Acciones para bitacora:\n" +
                   "- bitacora listar [id_tramite]\n" +
                   "- bitacora consultar [id]";
        }
        if (e.contains("firma")) {
            return "Acciones para firma digital:\n" +
                   "- firma listar\n" +
                   "- firma consolidada [id_tramite;id_firmante]\n" +
                   "- firma remota [id_tramite;id_firmante]";
        }
        if (e.contains("despacho")) {
            return "Acciones para despacho:\n" +
                   "- despacho listar\n" +
                   "- despacho consultar [id]\n" +
                   "- despacho agregar [id_tramite;id_despachador;nro_despacho;tipo_entrega]";
        }
        if (e.contains("certificad")) {
            return "Acciones para certificado:\n" +
                   "- certificado listar\n" +
                   "- certificado consultar [id_tramite]\n" +
                   "- certificado verificar [codigo_verificacion]\n" +
                   "- certificado generar [id_tramite;titulo]";
        }
        if (e.contains("portal")) {
            return "Acciones para portal de seguimiento:\n" +
                   "- portal consultar [id_tramite;pin]\n" +
                   "- portal habilitar [id_tramite;pin]";
        }
        if (e.contains("sla")) {
            return "Acciones para sla:\n" +
                   "- sla listar\n" +
                   "- sla verificar [id_tramite]";
        }
        if (e.contains("alerta")) {
            return "Acciones para alerta:\n" +
                   "- alerta listar\n" +
                   "- alerta agregar [id_tramite;tipo_alerta;mensaje;notificado_a]\n" +
                   "- alerta atender [id_alerta]";
        }
        if (e.contains("report")) {
            return "Acciones para reporte:\n" +
                   "- reporte tramites\n" +
                   "- reporte sla\n" +
                   "- reporte firmas";
        }
        if (e.contains("delega")) {
            return "Acciones para delegacion:\n" +
                   "- delegacion listar\n" +
                   "- delegacion agregar [id_titular;id_delegado;resolucion;motivo;fecha_inicio;fecha_fin;alcance]\n" +
                   "- delegacion eliminar [id]";
        }
        if (e.contains("indica")) {
            return "Acciones para indicador:\n" +
                   "- indicador listar\n" +
                   "- indicador agregar [codigo;nombre;meta;valor_actual;unidad;periodo]";
        }

        return "Consulte 'help' para ver el manual completo de comandos de Firma Digital.";
    }

    private String stripAttachmentMarkers(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String result = input.trim();
        result = result.replace("[", " , ").replace("]", " , ");
        result = result.replace(";", ",");
        return result.replaceAll("\\s+", " ").trim();
    }
}