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

            case Token.CLIENTE:
                listener.cliente(tokenEvent);
                break;

            case Token.VENDEDOR:
                listener.vendedor(tokenEvent);
                break;

            case Token.PROPIETARIO:
                listener.propietario(tokenEvent);
                break;

            case Token.PROVEEDOR:
                listener.proveedor(tokenEvent);
                break;

            case Token.CATEGORIA:
                listener.categoria(tokenEvent);
                break;

            case Token.PRODUCTO:
                listener.producto(tokenEvent);
                break;

            case Token.PRODUCTO_VARIANTE:
                listener.productoVariante(tokenEvent);
                break;

            case Token.TECNICA_INVENTARIO:
                listener.tecnicaInventario(tokenEvent);
                break;

            case Token.TECNICA_COSTO:
                listener.tecnicaCosto(tokenEvent);
                break;

            case Token.PROMOCION:
                listener.promocion(tokenEvent);
                break;

            case Token.PROMOCION_CATEGORIA:
                listener.promocionCategoria(tokenEvent);
                break;

            case Token.PROMOCION_PRODUCTO:
                listener.promocionProducto(tokenEvent);
                break;

            case Token.COMPRA:
                listener.compra(tokenEvent);
                break;

            case Token.DETALLE_COMPRA:
                listener.detalleCompra(tokenEvent);
                break;

            case Token.CARRITO:
                listener.carrito(tokenEvent);
                break;

            case Token.CARRITO_DETALLE:
                listener.carritoDetalle(tokenEvent);
                break;

            case Token.LOTE_INVENTARIO:
                listener.loteInventario(tokenEvent);
                break;

            case Token.PEDIDO:
                listener.pedido(tokenEvent);
                break;

            case Token.DETALLE_PEDIDO:
                listener.pedidoDetalle(tokenEvent);
                break;

            case Token.VENTA:
                listener.venta(tokenEvent);
                break;

            case Token.DETALLE_VENTA:
                listener.detalleVenta(tokenEvent);
                break;

            case Token.SALIDA_LOTE:
                listener.salidaLote(tokenEvent);
                break;

            case Token.MOVIMIENTO_INVENTARIO:
                listener.movimientoInventario(tokenEvent);
                break;

            case Token.METODO_PAGO:
                listener.metodoPago(tokenEvent);
                break;

            case Token.PAGO_CONTADO:
                listener.pagoContado(tokenEvent);
                break;

            case Token.CREDITO:
                listener.credito(tokenEvent);
                break;

            case Token.PAGO_CUOTA:
                listener.pagoCuota(tokenEvent);
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

        while ((token = analex.Preanalisis()).getName() != Token.END && token.getName() != Token.ERROR) {
            if (token.getName() == Token.CU) {
                tokenCommand.setName(token.getAttribute());
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