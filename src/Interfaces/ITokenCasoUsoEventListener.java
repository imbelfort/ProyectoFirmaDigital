package Interfaces;

import Comunication.TokenEvent;

public interface ITokenCasoUsoEventListener {

    void usuario(TokenEvent event);

    void cliente(TokenEvent event);

    void vendedor(TokenEvent event);

    void propietario(TokenEvent event);

    void proveedor(TokenEvent event);

    void categoria(TokenEvent event);

    void producto(TokenEvent event);

    void productoVariante(TokenEvent event);

    void tecnicaInventario(TokenEvent event);

    void tecnicaCosto(TokenEvent event);

    void promocion(TokenEvent event);

    void promocionCategoria(TokenEvent event);

    void promocionProducto(TokenEvent event);

    void compra(TokenEvent event);

    void detalleCompra(TokenEvent event);

    void loteInventario(TokenEvent event);

    void carrito(TokenEvent event);

    void carritoDetalle(TokenEvent event);

    void pedido(TokenEvent event);

    void pedidoDetalle(TokenEvent event);

    void venta(TokenEvent event);

    void detalleVenta(TokenEvent event);

    void movimientoInventario(TokenEvent event);

    void salidaLote(TokenEvent event);

    void metodoPago(TokenEvent event);

    void pagoContado(TokenEvent event);

    void credito(TokenEvent event);

    void pagoCuota(TokenEvent event);

    void reporte(TokenEvent event);

    void error(TokenEvent event);

    void posibleserrores(TokenEvent event);
}