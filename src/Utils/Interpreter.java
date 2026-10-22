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
        tokenEvent.addParams(command);
        tokenEvent.addParams(error);
        listener.error(tokenEvent);
    }

    @Override
    public void run() {
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