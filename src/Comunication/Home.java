package Comunication;

import Interfaces.IEmailEventListener;

import Utils.Interpreter;
import Interfaces.ITokenCasoUsoEventListener;
import Utils.Token;
import Models.*;
import Services.CategoriaService;
import Services.CreditoService;
import Services.PedidoDetalleService;
import Services.DetalleVentaService;
import Services.CarritoDetalleService;
import Services.PedidoService;
import Services.ProductoService;
import Services.PromocionProductoService;
import Services.PromocionService;
import Services.ReporteService;
import Services.ClienteService;
import Services.PropietarioService;
import Services.UsuarioService;
import Services.VendedorService;
import Services.VentaService;
import Services.CompraService;
import Services.DetalleCompraService;
import Services.TecnicaInventarioService;
import Services.TecnicaCostoService;
import Services.MovimientoInventarioService;
import Services.PromocionCategoriaService;
import Services.MetodoPagoService;
import Services.PagoContadoService;
import Services.PagoContadoResultado;
import Services.PagoCuotaResultado;
import Services.PagoCuotaService;
import Services.ProductoVarianteService;
import Services.CarritoService;
import Services.LoteInventarioService;
import Services.SalidaLoteService;

import java.util.List;
import javax.swing.SwingUtilities;
import Services.ProveedorService;

import Utils.Email;
import proyectomailgrupo20sa.Principal;

public class Home {

    public static void iniciar() {
        MailVerificationThread mail = new MailVerificationThread();
        mail.setEmailEventListener(new IEmailEventListener() {
            @Override
            public void onReceiveEmailEvent(List<Email> emails) {
                for (Email email : emails) {
                    interprete(email);
                }
            }
        });
        Thread thread = new Thread(mail);
        thread.setName("Mail Verification Thread");
        thread.start();
    }

    public static void interprete(Email email) {
        SendEmail respuesta = new SendEmail();

        // InicializaciÃ³n de servicios
        UsuarioService usuarioService = new UsuarioService();
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService();
        ProductoVarianteService productoVarianteService = new ProductoVarianteService();
        PedidoService pedidoService = new PedidoService();
        PedidoDetalleService pedidoDetalleService = new PedidoDetalleService();
        DetalleVentaService detalleVentaService = new DetalleVentaService();
        CarritoDetalleService carritoDetalleService = new CarritoDetalleService();
        VendedorService vendedorService = new VendedorService();
        VentaService ventaService = new VentaService();
        CreditoService creditoService = new CreditoService();
        CarritoService carritoService = new CarritoService();
        PromocionService promocionService = new PromocionService();
        PromocionProductoService promocionProductoService = new PromocionProductoService();
        ReporteService reporteService = new ReporteService();
        ClienteService clienteService = new ClienteService();
        PropietarioService propietarioService = new PropietarioService();
        ProveedorService proveedorService = new ProveedorService();
        CompraService compraService = new CompraService();
        DetalleCompraService detalleCompraService = new DetalleCompraService();
        LoteInventarioService loteInventarioService = new LoteInventarioService();
        TecnicaInventarioService tecnicaInventarioService = new TecnicaInventarioService();
        TecnicaCostoService tecnicaCostoService = new TecnicaCostoService();
        MovimientoInventarioService movimientoInventarioService = new MovimientoInventarioService();
        PromocionCategoriaService promocionCategoriaService = new PromocionCategoriaService();
        MetodoPagoService metodoPagoService = new MetodoPagoService();
        PagoContadoService pagoContadoService = new PagoContadoService();
        PagoCuotaService pagoCuotaService = new PagoCuotaService();
        SalidaLoteService salidaLoteService = new SalidaLoteService();
        
        String subjectText = email.getSubject() != null ? email.getSubject().trim() : "";
            

        
        Interpreter interpreter = new Interpreter(subjectText, email.getFrom());
        interpreter.setListener(new ITokenCasoUsoEventListener() {

            // USUARIO HANDLER
            @Override
            public void usuario(TokenEvent event) {
                System.out.println("CU: USUARIO");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);
                            List<String[]> lista = usuarioService.listarUsuarios();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, email.getSubject(), HtmlBuilder.HEADERS_USER, lista);
                            break;

                        case Token.AGREGAR:
                            if (!validarParametrosRango(event, 6, 9)) {
                                return;
                            }
                            usuarioService.crearUsuario(event.getParams());
                            List<String[]> listaAdd = usuarioService.listarUsuarios();
                            EnviarRespuestaUsuario(email, respuesta, "Usuario agregado exitosamente", HtmlBuilder.HEADERS_USER, listaAdd);
                            break;

                        case Token.MODIFICAR:
                            if (!validarParametrosRango(event, 7, 11)) {
                                return;
                            }
                            usuarioService.actualizarUsuario(event.getParams());
                            List<String[]> listaEdit = usuarioService.listarUsuarios();
                            EnviarRespuestaUsuario(email, respuesta, "Usuario modificado exitosamente", HtmlBuilder.HEADERS_USER, listaEdit);
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);
                            int id = Integer.parseInt(event.getParams().get(0));
                            usuarioService.eliminarUsuario(id);
                            List<String[]> listaDel = usuarioService.listarUsuarios();
                            EnviarRespuestaUsuario(email, respuesta, "Usuario eliminado exitosamente", HtmlBuilder.HEADERS_USER, listaDel);
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, usuarioService.getComandosUsuario());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }
                } catch (IllegalArgumentException e) {
                    enviarErrorParametros(email.getFrom());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

@Override
public void carrito(TokenEvent event) {
    System.out.println("CU: CARRITO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = carritoService.listarCarritos();

                System.out.println("[OK] carrito listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Carritos",
                        HtmlBuilder.HEADERS_CARRITO,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 1 || event.getParams().size() > 3) {
                    throw new IllegalArgumentException(
                            "Cantidad de parametros invalida. Formato: carrito agregar id_cliente,fecha_creacion?,fecha_actualizacion?"
                    );
                }

                carritoService.crearCarrito(event.getParams());

                System.out.println("[OK] carrito creado exitosamente");

                List<String[]> listaAdd = carritoService.listarCarritos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Carrito agregado correctamente",
                        HtmlBuilder.HEADERS_CARRITO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 4);

                carritoService.actualizarCarrito(event.getParams());

                System.out.println("[OK] carrito modificado exitosamente");

                List<String[]> listaEdit = carritoService.listarCarritos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Carrito modificado correctamente",
                        HtmlBuilder.HEADERS_CARRITO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idCarritoEliminar = Integer.parseInt(event.getParams().get(0));

                carritoService.eliminarCarrito(idCarritoEliminar);

                System.out.println("[OK] carrito eliminado exitosamente");

                List<String[]> listaDel = carritoService.listarCarritos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Carrito eliminado correctamente",
                        HtmlBuilder.HEADERS_CARRITO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help carrito enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        carritoService.getComandosCarrito()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para carrito");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] carrito: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] carrito: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}


@Override
public void detalleCompra(TokenEvent event) {
    System.out.println("CU: DETALLE_COMPRA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = detalleCompraService.listarDetalleCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Detalles de Compra",
                        HtmlBuilder.HEADERS_DETALLE_COMPRA,
                        lista
                );
                break;

            case Token.AGREGAR:
                detalleCompraService.crearDetalleCompra(event.getParams());

                List<String[]> listaAdd = detalleCompraService.listarDetalleCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de compra agregado correctamente",
                        HtmlBuilder.HEADERS_DETALLE_COMPRA,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                detalleCompraService.actualizarDetalleCompra(event.getParams());

                List<String[]> listaEdit = detalleCompraService.listarDetalleCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de compra modificado correctamente",
                        HtmlBuilder.HEADERS_DETALLE_COMPRA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idDetalleEliminar = Integer.parseInt(event.getParams().get(0));

                detalleCompraService.eliminarDetalleCompra(idDetalleEliminar);

                List<String[]> listaDel = detalleCompraService.listarDetalleCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de compra eliminado correctamente",
                        HtmlBuilder.HEADERS_DETALLE_COMPRA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, detalleCompraService.getComandosDetalleCompra());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}          

@Override
public void promocionCategoria(TokenEvent event) {
    System.out.println("CU: PROMOCION_CATEGORIA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = promocionCategoriaService.listarPromocionCategorias();

                System.out.println("[OK] promocion_categoria listada correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Promociones por Categoria",
                        HtmlBuilder.HEADERS_PROMOCION_CATEGORIA,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 4);

                promocionCategoriaService.crearPromocionCategoria(event.getParams());

                System.out.println("[OK] promocion_categoria creada exitosamente");

                List<String[]> listaAdd = promocionCategoriaService.listarPromocionCategorias();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion asignada a categoria correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_CATEGORIA,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 4);

                promocionCategoriaService.actualizarPromocionCategoria(event.getParams());

                System.out.println("[OK] promocion_categoria modificada exitosamente");

                List<String[]> listaEdit = promocionCategoriaService.listarPromocionCategorias();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion categoria modificada correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_CATEGORIA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 2);

                int idPromocion = Integer.parseInt(event.getParams().get(0));
                int idCategoria = Integer.parseInt(event.getParams().get(1));

                promocionCategoriaService.eliminarPromocionCategoria(idPromocion, idCategoria);

                System.out.println("[OK] promocion_categoria eliminada exitosamente");

                List<String[]> listaDel = promocionCategoriaService.listarPromocionCategorias();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion categoria eliminada correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_CATEGORIA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help promocion_categoria enviado correctamente");

                mostrarYEnviarHELP(email, respuesta, promocionCategoriaService.getComandosPromocionCategoria());
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para promocion_categoria");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] promocion_categoria: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] promocion_categoria: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void metodoPago(TokenEvent event) {
    System.out.println("CU: METODO_PAGO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = metodoPagoService.listarMetodosPago();

                System.out.println("[OK] metodo_pago listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Metodos de Pago",
                        HtmlBuilder.HEADERS_METODO_PAGO,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 1 || event.getParams().size() > 2) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para metodo_pago agregar");
                }

                metodoPagoService.crearMetodoPago(event.getParams());

                System.out.println("[OK] metodo_pago creado exitosamente");

                List<String[]> listaAdd = metodoPagoService.listarMetodosPago();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Metodo de pago agregado correctamente",
                        HtmlBuilder.HEADERS_METODO_PAGO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                if (event.getParams().size() < 2 || event.getParams().size() > 3) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para metodo_pago modificar");
                }

                metodoPagoService.actualizarMetodoPago(event.getParams());

                System.out.println("[OK] metodo_pago modificado exitosamente");

                List<String[]> listaEdit = metodoPagoService.listarMetodosPago();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Metodo de pago modificado correctamente",
                        HtmlBuilder.HEADERS_METODO_PAGO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idMetodoPagoEliminar = Integer.parseInt(event.getParams().get(0));

                metodoPagoService.eliminarMetodoPago(idMetodoPagoEliminar);

                System.out.println("[OK] metodo_pago eliminado exitosamente");

                List<String[]> listaDel = metodoPagoService.listarMetodosPago();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Metodo de pago eliminado correctamente",
                        HtmlBuilder.HEADERS_METODO_PAGO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help metodo_pago enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        metodoPagoService.getComandosMetodoPago()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para metodo_pago");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] metodo_pago: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] metodo_pago: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void tecnicaInventario(TokenEvent event) {
    System.out.println("CU: TECNICA_INVENTARIO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = tecnicaInventarioService.listarTecnicasInventario();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Tecnicas de Inventario",
                        HtmlBuilder.HEADERS_TECNICA_INVENTARIO,
                        lista
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, tecnicaInventarioService.getComandosTecnicaInventario());
                break;

            case Token.AGREGAR:
            case Token.MODIFICAR:
            case Token.ELIMINAR:
                mostrarYEnviarHELP(email, respuesta, tecnicaInventarioService.getComandosTecnicaInventario());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void tecnicaCosto(TokenEvent event) {
    System.out.println("CU: TECNICA_COSTO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = tecnicaCostoService.listarTecnicasCosto();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Tecnicas de Costo",
                        HtmlBuilder.HEADERS_TECNICA_COSTO,
                        lista
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, tecnicaCostoService.getComandosTecnicaCosto());
                break;

            case Token.AGREGAR:
            case Token.MODIFICAR:
            case Token.ELIMINAR:
                mostrarYEnviarHELP(email, respuesta, tecnicaCostoService.getComandosTecnicaCosto());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void movimientoInventario(TokenEvent event) {
    System.out.println("CU: MOVIMIENTO_INVENTARIO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = movimientoInventarioService.listarMovimientosInventario();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Movimientos de Inventario",
                        HtmlBuilder.HEADERS_MOVIMIENTO_INVENTARIO,
                        lista
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, movimientoInventarioService.getComandosMovimientoInventario());
                break;

            case Token.AGREGAR:
            case Token.MODIFICAR:
            case Token.ELIMINAR:
                mostrarYEnviarHELP(email, respuesta, movimientoInventarioService.getComandosMovimientoInventario());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

            // CATEGORIA HANDLER
            @Override
            public void categoria(TokenEvent event) {
                System.out.println("CU: CATEGORIA");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);

                            List<String[]> categorias = categoriaService.listarCategorias();

                            EnviarCorreoDeGrupoAGmail(
                                    email,
                                    respuesta,
                                    "Lista de Categorías",
                                    HtmlBuilder.HEADERS_CATEGORIA,
                                    categorias
                            );
                            break;

                        case Token.AGREGAR:
                            validarParametros(event, 3);

                            categoriaService.crearCategoria(event.getParams(), construirImagenAdjunta(email));

                            List<String[]> categoriasAdd = categoriaService.listarCategorias();

                            EnviarCorreoDeGrupoAGmail(
                                    email,
                                    respuesta,
                                    "Lista de Categorías",
                                    HtmlBuilder.HEADERS_CATEGORIA,
                                    categoriasAdd
                            );
                            break;

                        case Token.MODIFICAR:
                            validarParametros(event, 4);

                            categoriaService.actualizarCategoria(event.getParams(), construirImagenAdjunta(email));

                            List<String[]> categoriasEdit = categoriaService.listarCategorias();

                            EnviarCorreoDeGrupoAGmail(
                                    email,
                                    respuesta,
                                    "Lista de Categorías",
                                    HtmlBuilder.HEADERS_CATEGORIA,
                                    categoriasEdit
                            );
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);

                            int id = Integer.parseInt(event.getParams().get(0));
                            categoriaService.eliminarCategoria(id);

                            List<String[]> categoriasDel = categoriaService.listarCategorias();

                            EnviarCorreoDeGrupoAGmail(
                                    email,
                                    respuesta,
                                    "Lista de Categorías",
                                    HtmlBuilder.HEADERS_CATEGORIA,
                                    categoriasDel
                            );
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, categoriaService.getComandosCategoria());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }

                } catch (IllegalArgumentException e) {
                    respuesta.responseUser(email, e.getMessage());
                    mostraRespuesta(e.getMessage());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

            

            private List<String[]> convertirUsuariosAStringArray(List<String[]> usuarios) {
                return usuarios.stream()
                        .map(u -> new String[]{
                            u.length > 0 ? u[0] : "",
                            u.length > 1 ? u[1] : "",
                            u.length > 2 ? u[2] : "",
                            u.length > 3 ? u[3] : "",
                            u.length > 4 ? u[4] : "",
                            u.length > 5 ? u[5] : "",
                            u.length > 6 ? u[6] : "",
                            u.length > 7 ? u[7] : "",
                            u.length > 8 ? u[8] : "",
                            u.length > 9 ? u[9] : "",
                            u.length > 10 ? u[10] : "",
                            u.length > 11 ? u[11] : "",
                            renderizarFotoPerfilUsuario(u.length > 12 ? u[12] : null)
                        })
                        .collect(java.util.stream.Collectors.toList());
            }

            private String renderizarFotoPerfilUsuario(String fotoPerfilBase64) {
                if (fotoPerfilBase64 == null || fotoPerfilBase64.isBlank()) {
                    return "Sin imagen";
                }

                String trimmed = fotoPerfilBase64.trim();
                if (trimmed.startsWith("data:")) {
                    trimmed = trimmed.substring(trimmed.indexOf(',') + 1);
                }

                if (!esBase64Posible(trimmed)) {
                    return "Sin imagen";
                }

                String mimeType = inferirMimeType(trimmed);
                String cid = "userimg-" + Integer.toHexString(trimmed.hashCode());
                return "<img src='cid:" + cid + "' data-inline-cid='" + cid + "' data-inline-type='" + mimeType + "' data-inline-base64='" + trimmed + "' style='max-width:90px; max-height:90px; object-fit:cover; border:1px solid #6A1B9A; border-radius:6px;' />";
            }

        
            private String[] clasificarAdjuntosProducto(Email email, String commandText) {
                String imagen = null;
                String qr = null;
                java.util.List<String> roles = extraerRolesAdjuntosProducto(commandText);

                if (email == null || email.getAttachments() == null || email.getAttachments().isEmpty()) {
                    return new String[]{null, null};
                }

                java.util.List<Email.Attachment> attachments = email.getAttachments();
                for (int i = 0; i < attachments.size(); i++) {
                    Email.Attachment attachment = attachments.get(i);
                    if (attachment == null) {
                        continue;
                    }

                    String dataUrl = construirDataUrlAdjunto(attachment);
                    if (dataUrl == null) {
                        continue;
                    }

                    if (i < roles.size()) {
                        String role = roles.get(i);
                        if ("imagen".equals(role) && imagen == null) {
                            imagen = dataUrl;
                            continue;
                        }
                        if ("qr".equals(role) && qr == null) {
                            qr = dataUrl;
                            continue;
                        }
                    }

                    String nombre = attachment.getFileName() != null ? attachment.getFileName().toLowerCase() : "";
                    if (esAdjuntoQr(nombre)) {
                        if (qr == null) {
                            qr = dataUrl;
                        }
                    } else if (esAdjuntoImagenProducto(nombre)) {
                        if (imagen == null) {
                            imagen = dataUrl;
                        }
                    } else if (imagen == null) {
                        imagen = dataUrl;
                    } else if (qr == null) {
                        qr = dataUrl;
                    }
                }

                if (imagen == null && qr != null && email.getAttachments().size() == 1) {
                    imagen = qr;
                    qr = null;
                }

                return new String[]{imagen, qr};
            }

            private java.util.List<String> extraerRolesAdjuntosProducto(String commandText) {
                java.util.List<String> roles = new java.util.ArrayList<>();
                if (commandText == null || commandText.isBlank()) {
                    return roles;
                }

                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\[(.*?)\\]").matcher(commandText.toLowerCase());
                while (matcher.find()) {
                    String token = matcher.group(1).trim();
                    if (token.contains("adjunto_imagen") || token.contains("imagen") || token.contains("foto") || token.contains("producto") || token.contains("image")) {
                        roles.add("imagen");
                    } else if (token.contains("adjunto_qr") || token.contains("qr") || token.contains("codigo") || token.contains("barcode") || token.contains("bidi")) {
                        roles.add("qr");
                    }
                }
                return roles;
            }

            private boolean esAdjuntoQr(String nombreArchivo) {
                return nombreArchivo.contains("qr") || nombreArchivo.contains("codigo") || nombreArchivo.contains("barcode") || nombreArchivo.contains("bidi");
            }

            private boolean esAdjuntoImagenProducto(String nombreArchivo) {
                return nombreArchivo.contains("imagen") || nombreArchivo.contains("foto") || nombreArchivo.contains("producto") || nombreArchivo.contains("image");
            }

            private String construirDataUrlAdjunto(Email.Attachment attachment) {
                if (attachment == null) {
                    return null;
                }

                String base64 = attachment.getContentBase64();
                if (base64 == null || base64.isBlank()) {
                    return null;
                }

                String contentType = attachment.getContentType();
                String mimeType = (contentType != null && !contentType.isBlank()) ? contentType.split(";")[0].trim() : inferirMimeType(base64.trim());
                return "data:" + mimeType + ";base64," + base64.trim();
            }

            private boolean esBase64Posible(String value) {
                return value.matches("^[A-Za-z0-9+/=\\r\\n]+$") && value.length() > 16;
            }

            private String inferirMimeType(String base64) {
                if (base64.startsWith("iVBORw0KGgo")) {
                    return "image/png";
                }
                if (base64.startsWith("/9j/")) {
                    return "image/jpeg";
                }
                if (base64.startsWith("R0lGOD")) {
                    return "image/gif";
                }
                if (base64.startsWith("UklGR")) {
                    return "image/webp";
                }
                return "image/jpeg";
            }

            private String construirImagenAdjunta(Email email) {
                return construirImagenAdjunta(email, 0);
            }

            private String construirImagenAdjunta(Email email, int attachmentIndex) {
                if (email == null) {
                    return null;
                }

                Email.Attachment attachment = email.getAttachment(attachmentIndex);
                if (attachment == null) {
                    return null;
                }

                String base64 = attachment.getContentBase64();
                if (base64 == null || base64.isBlank()) {
                    return null;
                }

                String contentType = attachment.getContentType();
                String mimeType = (contentType != null && !contentType.isBlank()) ? contentType.split(";")[0].trim() : inferirMimeType(base64.trim());
                return "data:" + mimeType + ";base64," + base64.trim();
            }

            
@Override
public void compra(TokenEvent event) {
    System.out.println("CU: COMPRA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = compraService.listarCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Compras",
                        HtmlBuilder.HEADERS_COMPRA,
                        lista
                );
                break;

            case Token.AGREGAR:
                compraService.crearCompra(event.getParams());

                List<String[]> listaAdd = compraService.listarCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Compra agregada correctamente",
                        HtmlBuilder.HEADERS_COMPRA,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                compraService.actualizarCompra(event.getParams());

                List<String[]> listaEdit = compraService.listarCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Compra modificada correctamente",
                        HtmlBuilder.HEADERS_COMPRA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idCompraEliminar = Integer.parseInt(event.getParams().get(0));

                compraService.eliminarCompra(idCompraEliminar);

                List<String[]> listaDel = compraService.listarCompras();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Compra eliminada correctamente",
                        HtmlBuilder.HEADERS_COMPRA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, compraService.getComandosCompra());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}    


// PROVEEDOR HANDLER
@Override
public void proveedor(TokenEvent event) {
    System.out.println("CU: PROVEEDOR");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = proveedorService.listarProveedores();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Proveedores",
                        HtmlBuilder.HEADERS_PROVEEDOR,
                        lista
                );
                break;

            case Token.AGREGAR:
                proveedorService.crearProveedor(event.getParams());

                List<String[]> listaAdd = proveedorService.listarProveedores();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Proveedor agregado correctamente",
                        HtmlBuilder.HEADERS_PROVEEDOR,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                proveedorService.actualizarProveedor(event.getParams());

                List<String[]> listaEdit = proveedorService.listarProveedores();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Proveedor modificado correctamente",
                        HtmlBuilder.HEADERS_PROVEEDOR,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idProveedorEliminar = Integer.parseInt(event.getParams().get(0));

                proveedorService.eliminarProveedor(idProveedorEliminar);

                List<String[]> listaDel = proveedorService.listarProveedores();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Proveedor eliminado correctamente",
                        HtmlBuilder.HEADERS_PROVEEDOR,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, proveedorService.getComandosProveedor());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

            @Override
            public void vendedor(TokenEvent event) {
                System.out.println("CU: VENDEDOR");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);
                            List<String[]> lista = vendedorService.listarVendedores();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Vendedores", HtmlBuilder.HEADERS_VENDEDOR, lista);
                            break;

                        case Token.AGREGAR:
                            vendedorService.crearVendedor(event.getParams());
                            List<String[]> listaAdd = vendedorService.listarVendedores();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Vendedores", HtmlBuilder.HEADERS_VENDEDOR, listaAdd);
                            break;

                        case Token.MODIFICAR:
                            vendedorService.actualizarVendedor(event.getParams());
                            List<String[]> listaEdit = vendedorService.listarVendedores();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Vendedores", HtmlBuilder.HEADERS_VENDEDOR, listaEdit);
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);
                            int id = Integer.parseInt(event.getParams().get(0));
                            vendedorService.eliminarVendedor(id);
                            List<String[]> listaDel = vendedorService.listarVendedores();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Vendedores", HtmlBuilder.HEADERS_VENDEDOR, listaDel);
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, vendedorService.getComandosVendedor());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }
                } catch (IllegalArgumentException e) {
                    enviarErrorParametros(email.getFrom());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

            // CLIENTE HANDLER
            @Override
            public void cliente(TokenEvent event) {
                System.out.println("CU: CLIENTE");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);
                            List<Models.Cliente> clientes = clienteService.listarClientes();
                            List<String[]> lista = new java.util.ArrayList<>();
                            for (Models.Cliente c : clientes) {
                                lista.add(new String[]{String.valueOf(c.getIdCliente()), c.getTipoCliente(), String.valueOf(c.getLimiteCredito()), String.valueOf(c.getCreditoDisponible()), String.valueOf(c.getCreditoUtilizado())});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Clientes", HtmlBuilder.HEADERS_CLIENTE, lista);
                            break;

                        case Token.AGREGAR:
                            if (!validarParametrosRango(event, 2, 5)) {
                                return;
                            }
                            clienteService.crearCliente(event.getParams());
                            List<Models.Cliente> listaAdd = clienteService.listarClientes();
                            List<String[]> listaAddStr = new java.util.ArrayList<>();
                            for (Models.Cliente c : listaAdd) {
                                listaAddStr.add(new String[]{String.valueOf(c.getIdCliente()), String.valueOf(c.getIdUsuario()), c.getTipoCliente(), String.valueOf(c.getLimiteCredito()), String.valueOf(c.getCreditoDisponible()), String.valueOf(c.getCreditoUtilizado())});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Clientes", HtmlBuilder.HEADERS_CLIENTE, listaAddStr);
                            break;

                        case Token.MODIFICAR:
                            if (!validarParametrosRango(event, 3, 6)) {
                                return;
                            }
                            clienteService.actualizarCliente(event.getParams());
                            List<Models.Cliente> listaEdit = clienteService.listarClientes();
                            List<String[]> listaEditStr = new java.util.ArrayList<>();
                            for (Models.Cliente c : listaEdit) {
                                listaEditStr.add(new String[]{String.valueOf(c.getIdCliente()), String.valueOf(c.getIdUsuario()), c.getTipoCliente(), String.valueOf(c.getLimiteCredito()), String.valueOf(c.getCreditoDisponible()), String.valueOf(c.getCreditoUtilizado())});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Clientes", HtmlBuilder.HEADERS_CLIENTE, listaEditStr);
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);
                            int id = Integer.parseInt(event.getParams().get(0));
                            clienteService.eliminarCliente(id);
                            List<Models.Cliente> listaDel = clienteService.listarClientes();
                            List<String[]> listaDelStr = new java.util.ArrayList<>();
                            for (Models.Cliente c : listaDel) {
                                listaDelStr.add(new String[]{String.valueOf(c.getIdCliente()), String.valueOf(c.getIdUsuario()), c.getTipoCliente(), String.valueOf(c.getLimiteCredito()), String.valueOf(c.getCreditoDisponible()), String.valueOf(c.getCreditoUtilizado())});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Clientes", HtmlBuilder.HEADERS_CLIENTE, listaDelStr);
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, clienteService.getComandosCliente());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }
                } catch (IllegalArgumentException e) {
                    enviarErrorParametros(email.getFrom());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

@Override
public void producto(TokenEvent event) {
    System.out.println("CU: PRODUCTO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = productoService.listarProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Productos",
                        HtmlBuilder.HEADERS_PRODUCTO,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 9);

                String[] adjuntosProducto = clasificarAdjuntosProducto(email, email.getSubject());

                productoService.crearProducto(
                        event.getParams(),
                        adjuntosProducto[0],
                        adjuntosProducto[1]
                );

                System.out.println("[OK] producto creado exitosamente");

                List<String[]> listaAdd = productoService.listarProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Producto agregado correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 10);

                String[] adjuntosProductoEdit = clasificarAdjuntosProducto(email, email.getSubject());

                productoService.actualizarProducto(
                        event.getParams(),
                        adjuntosProductoEdit[0],
                        adjuntosProductoEdit[1]
                );

                System.out.println("[OK] producto modificado exitosamente");

                List<String[]> listaEdit = productoService.listarProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Producto modificado correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idProductoEliminar = Integer.parseInt(event.getParams().get(0));

                productoService.eliminarProducto(idProductoEliminar);

                System.out.println("[OK] producto eliminado exitosamente");

                List<String[]> listaDel = productoService.listarProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Producto eliminado correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        productoService.getComandosProducto()
                );
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] producto: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] producto: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void productoVariante(TokenEvent event) {
    System.out.println("CU: PRODUCTO_VARIANTE");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = productoVarianteService.listarProductoVariantes();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Variantes de Producto",
                        HtmlBuilder.HEADERS_PRODUCTO_VARIANTE,
                        lista
                );
                break;

            case Token.AGREGAR:
                productoVarianteService.crearProductoVariante(event.getParams());

                List<String[]> listaAdd = productoVarianteService.listarProductoVariantes();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Variante agregada correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO_VARIANTE,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                productoVarianteService.actualizarProductoVariante(event.getParams());

                List<String[]> listaEdit = productoVarianteService.listarProductoVariantes();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Variante modificada correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO_VARIANTE,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);
                int idVarianteEliminar = Integer.parseInt(event.getParams().get(0));

                productoVarianteService.eliminarProductoVariante(idVarianteEliminar);

                List<String[]> listaDel = productoVarianteService.listarProductoVariantes();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Variante eliminada correctamente",
                        HtmlBuilder.HEADERS_PRODUCTO_VARIANTE,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);
                mostrarYEnviarHELP(email, respuesta, productoVarianteService.getComandosProductoVariante());
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}


@Override
public void pedido(TokenEvent event) {
    System.out.println("CU: PEDIDO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = pedidoService.listarPedidos();

                System.out.println("[OK] pedido listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Pedidos",
                        HtmlBuilder.HEADERS_PEDIDO,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 13 || event.getParams().size() > 15) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para pedido agregar");
                }

                pedidoService.crearPedido(event.getParams());

                System.out.println("[OK] pedido creado exitosamente");

                List<String[]> listaAdd = pedidoService.listarPedidos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pedido agregado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                if (event.getParams().size() < 14 || event.getParams().size() > 16) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para pedido modificar");
                }

                pedidoService.actualizarPedido(event.getParams());

                System.out.println("[OK] pedido modificado exitosamente");

                List<String[]> listaEdit = pedidoService.listarPedidos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pedido modificado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idPedidoEliminar = Integer.parseInt(event.getParams().get(0));

                pedidoService.eliminarPedido(idPedidoEliminar);

                System.out.println("[OK] pedido eliminado exitosamente");

                List<String[]> listaDel = pedidoService.listarPedidos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pedido eliminado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help pedido enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        pedidoService.getComandosPedido()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para pedido");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] pedido: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] pedido: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}


@Override
public void pedidoDetalle(TokenEvent event) {
    System.out.println("CU: PEDIDO_DETALLE");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = pedidoDetalleService.listarPedidoDetalles();

                System.out.println("[OK] pedido_detalle listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Detalles de Pedido",
                        HtmlBuilder.HEADERS_PEDIDO_DETALLE,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 4 || event.getParams().size() > 5) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para pedido_detalle agregar");
                }

                pedidoDetalleService.crearPedidoDetalle(event.getParams());

                System.out.println("[OK] pedido_detalle creado exitosamente");

                List<String[]> listaAdd = pedidoDetalleService.listarPedidoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de pedido agregado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO_DETALLE,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                if (event.getParams().size() < 5 || event.getParams().size() > 6) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para pedido_detalle modificar");
                }

                pedidoDetalleService.actualizarPedidoDetalle(event.getParams());

                System.out.println("[OK] pedido_detalle modificado exitosamente");

                List<String[]> listaEdit = pedidoDetalleService.listarPedidoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de pedido modificado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO_DETALLE,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idDetalleEliminar = Integer.parseInt(event.getParams().get(0));

                pedidoDetalleService.eliminarPedidoDetalle(idDetalleEliminar);

                System.out.println("[OK] pedido_detalle eliminado exitosamente");

                List<String[]> listaDel = pedidoDetalleService.listarPedidoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de pedido eliminado correctamente",
                        HtmlBuilder.HEADERS_PEDIDO_DETALLE,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help pedido_detalle enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        pedidoDetalleService.getComandosPedidoDetalle()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para pedido_detalle");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] pedido_detalle: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] pedido_detalle: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void detalleVenta(TokenEvent event) {
    System.out.println("CU: DETALLE_VENTA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = detalleVentaService.listarDetalleVentas();

                System.out.println("[OK] detalle_venta listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Detalles de Venta",
                        HtmlBuilder.HEADERS_DETALLE_VENTA,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 4
                        || event.getParams().size() > 5) {

                    throw new IllegalArgumentException(
                            "Cantidad de parametros invalida "
                                    + "para detalle_venta agregar"
                    );
                }

                String alertaReorden =
                        detalleVentaService.crearDetalleVenta(
                                event.getParams()
                        );

                System.out.println(
                        "[OK] detalle_venta creado exitosamente"
                );

                List<String[]> listaAdd =
                        detalleVentaService.listarDetalleVentas();

                if (alertaReorden != null
                        && !alertaReorden.trim().isEmpty()) {

                    String tablaDetalleVenta =
                            HtmlBuilder.generateTableContentSinHtml(
                                    "Lista de Detalles de Venta",
                                    HtmlBuilder.HEADERS_DETALLE_VENTA,
                                    listaAdd
                            );

                    String htmlRespuesta =
                            "<html>"
                            + "<body style=\""
                            + "font-family:Arial;"
                            + "padding:20px;"
                            + "\">"

                            + "<h2 style=\""
                            + "text-align:center;"
                            + "color:#4A148C;"
                            + "\">"
                            + "Detalle de venta agregado correctamente"
                            + "</h2>"

                            + alertaReorden

                            + tablaDetalleVenta

                            + "</body>"
                            + "</html>";

                    enviarHtmlDirecto(
                            email,
                            respuesta,
                            htmlRespuesta
                    );

                } else {
                    EnviarCorreoDeGrupoAGmail(
                            email,
                            respuesta,
                            "Detalle de venta agregado correctamente",
                            HtmlBuilder.HEADERS_DETALLE_VENTA,
                            listaAdd
                    );
                }

                break;

            case Token.MODIFICAR:
                if (event.getParams().size() < 5 || event.getParams().size() > 6) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para detalle_venta modificar");
                }

                detalleVentaService.actualizarDetalleVenta(event.getParams());

                System.out.println("[OK] detalle_venta modificado exitosamente");

                List<String[]> listaEdit = detalleVentaService.listarDetalleVentas();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de venta modificado correctamente",
                        HtmlBuilder.HEADERS_DETALLE_VENTA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idDetalleEliminar = Integer.parseInt(event.getParams().get(0));

                detalleVentaService.eliminarDetalleVenta(idDetalleEliminar);

                System.out.println("[OK] detalle_venta eliminado exitosamente");

                List<String[]> listaDel = detalleVentaService.listarDetalleVentas();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de venta eliminado correctamente",
                        HtmlBuilder.HEADERS_DETALLE_VENTA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help detalle_venta enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        detalleVentaService.getComandosDetalleVenta()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para detalle_venta");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] detalle_venta: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] detalle_venta: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void salidaLote(TokenEvent event) {
    System.out.println("CU: SALIDA_LOTE");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = salidaLoteService.listarSalidasLote();

                System.out.println("[OK] salida_lote listada correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Salidas de Lote",
                        HtmlBuilder.HEADERS_SALIDA_LOTE,
                        lista
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help salida_lote enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        salidaLoteService.getComandosSalidaLote()
                );
                break;

            case Token.AGREGAR:
            case Token.MODIFICAR:
            case Token.ELIMINAR:
                System.out.println("[ERROR] salida_lote no permite agregar, modificar ni eliminar por correo");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        salidaLoteService.getComandosSalidaLote()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para salida_lote");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] salida_lote: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] salida_lote: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void loteInventario(TokenEvent event) {
    System.out.println("CU: LOTE_INVENTARIO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = loteInventarioService.listarLotesInventario();

                System.out.println("[OK] lote_inventario listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Lotes de Inventario",
                        HtmlBuilder.HEADERS_LOTE_INVENTARIO,
                        lista
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help lote_inventario enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        loteInventarioService.getComandosLoteInventario()
                );
                break;

            case Token.AGREGAR:
            case Token.MODIFICAR:
            case Token.ELIMINAR:
                System.out.println("[ERROR] lote_inventario no permite agregar, modificar ni eliminar por correo");
                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        loteInventarioService.getComandosLoteInventario()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para lote_inventario");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] lote_inventario: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] lote_inventario: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void carritoDetalle(TokenEvent event) {
    System.out.println("CU: CARRITO_DETALLE");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = carritoDetalleService.listarCarritoDetalles();

                System.out.println("[OK] carrito_detalle listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Detalles de Carrito",
                        HtmlBuilder.HEADERS_CARRITO_DETALLE,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 5);

                carritoDetalleService.crearCarritoDetalle(event.getParams());

                System.out.println("[OK] carrito_detalle creado exitosamente");

                List<String[]> listaAdd = carritoDetalleService.listarCarritoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de carrito agregado correctamente",
                        HtmlBuilder.HEADERS_CARRITO_DETALLE,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 5);

                carritoDetalleService.actualizarCarritoDetalle(event.getParams());

                System.out.println("[OK] carrito_detalle modificado exitosamente");

                List<String[]> listaEdit = carritoDetalleService.listarCarritoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de carrito modificado correctamente",
                        HtmlBuilder.HEADERS_CARRITO_DETALLE,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 2);

                int idCarrito = Integer.parseInt(event.getParams().get(0));
                int idProductoVariante = Integer.parseInt(event.getParams().get(1));

                carritoDetalleService.eliminarCarritoDetalle(idCarrito, idProductoVariante);

                System.out.println("[OK] carrito_detalle eliminado exitosamente");

                List<String[]> listaDel = carritoDetalleService.listarCarritoDetalles();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Detalle de carrito eliminado correctamente",
                        HtmlBuilder.HEADERS_CARRITO_DETALLE,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help carrito_detalle enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        carritoDetalleService.getComandosCarritoDetalle()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para carrito_detalle");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] carrito_detalle: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] carrito_detalle: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void venta(TokenEvent event) {
    System.out.println("CU: VENTA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = ventaService.listarVentas();

                System.out.println("[OK] venta listada correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Ventas",
                        HtmlBuilder.HEADERS_VENTA,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 10 || event.getParams().size() > 11) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para venta agregar");
                }

                ventaService.crearVenta(event.getParams());

                System.out.println("[OK] venta creada exitosamente");

                List<String[]> listaAdd = ventaService.listarVentas();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Venta agregada correctamente",
                        HtmlBuilder.HEADERS_VENTA,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                if (event.getParams().size() < 11 || event.getParams().size() > 12) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para venta modificar");
                }

                ventaService.actualizarVenta(event.getParams());

                System.out.println("[OK] venta modificada exitosamente");

                List<String[]> listaEdit = ventaService.listarVentas();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Venta modificada correctamente",
                        HtmlBuilder.HEADERS_VENTA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idVentaEliminar = Integer.parseInt(event.getParams().get(0));

                ventaService.eliminarVenta(idVentaEliminar);

                System.out.println("[OK] venta eliminada exitosamente");

                List<String[]> listaDel = ventaService.listarVentas();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Venta eliminada correctamente",
                        HtmlBuilder.HEADERS_VENTA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help venta enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        ventaService.getComandosVenta()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para venta");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] venta: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] venta: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void credito(TokenEvent event) {
    System.out.println("CU: CREDITO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = creditoService.listarCreditos();

                System.out.println("[OK] credito listado correctamente");

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Creditos",
                        HtmlBuilder.HEADERS_CREDITO,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 10);

                creditoService.crearCredito(event.getParams());

                System.out.println("[OK] credito creado exitosamente");

                List<String[]> listaAdd = creditoService.listarCreditos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Credito agregado correctamente",
                        HtmlBuilder.HEADERS_CREDITO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 11);

                creditoService.actualizarCredito(event.getParams());

                System.out.println("[OK] credito modificado exitosamente");

                List<String[]> listaEdit = creditoService.listarCreditos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Credito modificado correctamente",
                        HtmlBuilder.HEADERS_CREDITO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idCreditoEliminar = Integer.parseInt(event.getParams().get(0));

                creditoService.eliminarCredito(idCreditoEliminar);

                System.out.println("[OK] credito eliminado exitosamente");

                List<String[]> listaDel = creditoService.listarCreditos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Credito eliminado correctamente",
                        HtmlBuilder.HEADERS_CREDITO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                System.out.println("[OK] help credito enviado correctamente");

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        creditoService.getComandosCredito()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para credito");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] credito: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] credito: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}




@Override
public void pagoCuota(TokenEvent event) {
    System.out.println("CU: PAGO_CUOTA");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = pagoCuotaService.listarPagosCuota();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Pagos de Cuota",
                        HtmlBuilder.HEADERS_PAGO_CUOTA,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 9);

                PagoCuotaResultado resultado = pagoCuotaService.crearPagoCuota(
                        event.getParams(),
                        email.getFrom()
                );

                if (resultado.isQrGenerado()) {
                    System.out.println("[OK] solicitud de pago QR para cuota generada exitosamente");
                    enviarHtmlDirecto(email, respuesta, resultado.getHtmlRespuesta());
                } else {
                    System.out.println("[OK] pago_cuota creado exitosamente");

                    List<String[]> listaAdd = pagoCuotaService.listarPagosCuota();

                    EnviarCorreoDeGrupoAGmail(
                            email,
                            respuesta,
                            "Pago de cuota agregado correctamente",
                            HtmlBuilder.HEADERS_PAGO_CUOTA,
                            listaAdd
                    );
                }
                break;

            case Token.MODIFICAR:
                validarParametros(event, 11);

                pagoCuotaService.actualizarPagoCuota(event.getParams());

                List<String[]> listaEdit = pagoCuotaService.listarPagosCuota();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pago de cuota modificado correctamente",
                        HtmlBuilder.HEADERS_PAGO_CUOTA,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idPagoCuotaEliminar = Integer.parseInt(event.getParams().get(0));

                pagoCuotaService.eliminarPagoCuota(idPagoCuotaEliminar);

                List<String[]> listaDel = pagoCuotaService.listarPagosCuota();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pago de cuota eliminado correctamente",
                        HtmlBuilder.HEADERS_PAGO_CUOTA,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        pagoCuotaService.getComandosPagoCuota()
                );
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] pago_cuota: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] pago_cuota: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

            // PROMOCION HANDLER
            @Override
            public void promocion(TokenEvent event) {
                System.out.println("CU: PROMOCION");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);
                            List<String[]> lista = promocionService.listarPromociones();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Promociones", HtmlBuilder.HEADERS_PROMOCION, lista);
                            break;

                        case Token.AGREGAR:
                            promocionService.crearPromocion(event.getParams());
                            List<String[]> listaAdd = promocionService.listarPromociones();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Promociones", HtmlBuilder.HEADERS_PROMOCION, listaAdd);
                            break;

                        case Token.MODIFICAR:
                            promocionService.actualizarPromocion(event.getParams());
                            List<String[]> listaEdit = promocionService.listarPromociones();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Promociones", HtmlBuilder.HEADERS_PROMOCION, listaEdit);
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);
                            int id = Integer.parseInt(event.getParams().get(0));
                            promocionService.eliminarPromocion(id);
                            List<String[]> listaDel = promocionService.listarPromociones();
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Promociones", HtmlBuilder.HEADERS_PROMOCION, listaDel);
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, promocionService.getComandosPromocion());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }
                } catch (IllegalArgumentException e) {
                    enviarErrorParametros(email.getFrom());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

            // PROMOCION PRODUCTO HANDLER
@Override
public void promocionProducto(TokenEvent event) {
    System.out.println("CU: PROMOCION_PRODUCTO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = promocionProductoService.listarPromocionProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Promociones por Producto",
                        HtmlBuilder.HEADERS_PROMOCION_PRODUCTO,
                        lista
                );
                break;

            case Token.AGREGAR:
                validarParametros(event, 4);

                promocionProductoService.crearPromocionProducto(event.getParams());

                List<String[]> listaAdd = promocionProductoService.listarPromocionProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion asignada al producto correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_PRODUCTO,
                        listaAdd
                );
                break;

            case Token.MODIFICAR:
                validarParametros(event, 4);

                promocionProductoService.actualizarPromocionProducto(event.getParams());

                List<String[]> listaEdit = promocionProductoService.listarPromocionProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion producto modificada correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_PRODUCTO,
                        listaEdit
                );
                break;

            case Token.ELIMINAR:
                validarParametros(event, 2);

                int idPromocion = Integer.parseInt(event.getParams().get(0));
                int idProducto = Integer.parseInt(event.getParams().get(1));

                promocionProductoService.eliminarPromocionProducto(idPromocion, idProducto);

                List<String[]> listaDel = promocionProductoService.listarPromocionProductos();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Promocion producto eliminada correctamente",
                        HtmlBuilder.HEADERS_PROMOCION_PRODUCTO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        promocionProductoService.getComandosPromocionProducto()
                );
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        enviarErrorParametros(email.getFrom());
    } catch (Exception ex) {
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

@Override
public void pagoContado(TokenEvent event) {
    System.out.println("CU: PAGO_CONTADO");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                validarParametros(event, 0);

                List<String[]> lista = pagoContadoService.listarPagosContado();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Lista de Pagos Contado",
                        HtmlBuilder.HEADERS_PAGO_CONTADO,
                        lista
                );
                break;

            case Token.AGREGAR:
                if (event.getParams().size() < 6 || event.getParams().size() > 7) {
                    throw new IllegalArgumentException("Cantidad de parametros invalida para pago_contado agregar");
                }

                PagoContadoResultado resultado = pagoContadoService.crearPagoContado(
                        event.getParams(),
                        email.getFrom()
                );

                if (resultado.isQrGenerado()) {
                    System.out.println("[OK] solicitud de pago QR generada exitosamente");
                    enviarHtmlDirecto(email, respuesta, resultado.getHtmlRespuesta());
                } else {
                    System.out.println("[OK] pago_contado creado exitosamente");

                    List<String[]> listaAdd = pagoContadoService.listarPagosContado();

                    EnviarCorreoDeGrupoAGmail(
                            email,
                            respuesta,
                            "Pago contado agregado correctamente",
                            HtmlBuilder.HEADERS_PAGO_CONTADO,
                            listaAdd
                    );
                }
                break;

            case Token.ELIMINAR:
                validarParametros(event, 1);

                int idPagoEliminar = Integer.parseInt(event.getParams().get(0));

                pagoContadoService.eliminarPagoContado(idPagoEliminar);

                List<String[]> listaDel = pagoContadoService.listarPagosContado();

                EnviarCorreoDeGrupoAGmail(
                        email,
                        respuesta,
                        "Pago contado eliminado correctamente",
                        HtmlBuilder.HEADERS_PAGO_CONTADO,
                        listaDel
                );
                break;

            case Token.HELP:
                validarParametros(event, 0);

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        pagoContadoService.getComandosPagoContado()
                );
                break;

            default:
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] pago_contado: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] pago_contado: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}

private static void enviarHtmlDirecto(Email email, SendEmail respuesta, String html) {
    try {
        Home.mostraRespuesta(html);

        Email emailRespuesta = new Email(
                email.getFrom(),
                "Solicitud de pago generada exitosamente"
        );

        respuesta.responseUser(emailRespuesta, html);

    } catch (Exception e) {
        System.out.println("[ERROR] No se pudo enviar HTML directo: " + e.getMessage());
    }
}


            // PROPIETARIO HANDLER
            @Override
            public void propietario(TokenEvent event) {
                System.out.println("CU: PROPIETARIO");
                System.out.println(event);

                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            validarParametros(event, 0);
                            List<Models.Propietario> lista = propietarioService.listarPropietarios();
                            List<String[]> listaStr = new java.util.ArrayList<>();
                            for (Models.Propietario p : lista) {
                                listaStr.add(new String[]{String.valueOf(p.getIdPropietario()), String.valueOf(p.getIdUsuario()), p.getNivelAcceso()});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Propietarios", HtmlBuilder.HEADERS_PROPIETARIO, listaStr);
                            break;

                        case Token.AGREGAR:
                            validarParametros(event, 1);
                            propietarioService.crearPropietario(event.getParams());
                            List<Models.Propietario> listaAdd = propietarioService.listarPropietarios();
                            List<String[]> listaAddStr = new java.util.ArrayList<>();
                            for (Models.Propietario p : listaAdd) {
                                listaAddStr.add(new String[]{String.valueOf(p.getIdPropietario()), String.valueOf(p.getIdUsuario()), p.getNivelAcceso()});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Propietarios", HtmlBuilder.HEADERS_PROPIETARIO, listaAddStr);
                            break;

                        case Token.MODIFICAR:
                            validarParametros(event, 2);
                            propietarioService.actualizarPropietario(event.getParams());
                            List<Models.Propietario> listaEdit = propietarioService.listarPropietarios();
                            List<String[]> listaEditStr = new java.util.ArrayList<>();
                            for (Models.Propietario p : listaEdit) {
                                listaEditStr.add(new String[]{String.valueOf(p.getIdPropietario()), String.valueOf(p.getIdUsuario()), p.getNivelAcceso()});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Propietarios", HtmlBuilder.HEADERS_PROPIETARIO, listaEditStr);
                            break;

                        case Token.ELIMINAR:
                            validarParametros(event, 1);
                            int id = Integer.parseInt(event.getParams().get(0));
                            propietarioService.eliminarPropietario(id);
                            List<Models.Propietario> listaDel = propietarioService.listarPropietarios();
                            List<String[]> listaDelStr = new java.util.ArrayList<>();
                            for (Models.Propietario p : listaDel) {
                                listaDelStr.add(new String[]{String.valueOf(p.getIdPropietario()), String.valueOf(p.getIdUsuario()), p.getNivelAcceso()});
                            }
                            EnviarCorreoDeGrupoAGmail(email, respuesta, "Lista de Propietarios", HtmlBuilder.HEADERS_PROPIETARIO, listaDelStr);
                            break;

                        case Token.HELP:
                            validarParametros(event, 0);
                            mostrarYEnviarHELP(email, respuesta, propietarioService.getComandosPropietario());
                            break;

                        default:
                            comandoDesconocido(email.getFrom());
                    }
                } catch (IllegalArgumentException e) {
                    enviarErrorParametros(email.getFrom());
                } catch (Exception ex) {
                    manejarExcepcionSQL(email.getFrom(), ex);
                }
            }

@Override
public void reporte(TokenEvent event) {
    System.out.println("CU: REPORTE");
    System.out.println(event);

    try {
        switch (event.getAction()) {
            case Token.LISTAR:
                if (event.getParams().size() != 1) {
                    throw new IllegalArgumentException("Debe indicar el tipo de reporte");
                }

                String tipoReporte = event.getParams().get(0).trim().toLowerCase();

                switch (tipoReporte) {
                    case "ventas_mes": {
                        List<String[]> lista = reporteService.reporteVentasUltimoMes();

                        EnviarCorreoDeGrupoAGmail(
                                email,
                                respuesta,
                                "Reporte de Ventas del Ultimo Mes",
                                HtmlBuilder.HEADERS_REPORTE_VENTAS_RESUMEN,
                                lista
                        );
                        break;
                    }

                    case "ventas_trimestre": {
                        List<String[]> lista = reporteService.reporteVentasUltimoTrimestre();

                        EnviarCorreoDeGrupoAGmail(
                                email,
                                respuesta,
                                "Reporte de Ventas del Ultimo Trimestre",
                                HtmlBuilder.HEADERS_REPORTE_VENTAS_RESUMEN,
                                lista
                        );
                        break;
                    }

                    case "ventas_anio": {
                        List<String[]> lista = reporteService.reporteVentasUltimoAnio();

                        EnviarCorreoDeGrupoAGmail(
                                email,
                                respuesta,
                                "Reporte de Ventas del Ultimo Anio",
                                HtmlBuilder.HEADERS_REPORTE_VENTAS_RESUMEN,
                                lista
                        );
                        break;
                    }

                    case "clientes_mes": {
                        List<String[]> lista = reporteService.reporteClientesUltimoMes();

                        EnviarCorreoDeGrupoAGmail(
                                email,
                                respuesta,
                                "Reporte de Clientes Registrados en el Ultimo Mes",
                                HtmlBuilder.HEADERS_REPORTE_CLIENTES_MES,
                                lista
                        );
                        break;
                    }

                    case "ventas_anio_metodo_pago": {
                        List<String[]> lista = reporteService.reporteVentasUltimoAnioPorMetodoPago();

                        EnviarCorreoDeGrupoAGmail(
                                email,
                                respuesta,
                                "Reporte de Ventas del Ultimo Anio por Metodo de Pago",
                                HtmlBuilder.HEADERS_REPORTE_VENTAS_METODO_PAGO,
                                lista
                        );
                        break;
                    }

                    default:
                        throw new IllegalArgumentException("Tipo de reporte no reconocido: " + tipoReporte);
                }

                System.out.println("[OK] reporte generado correctamente");
                break;

            case Token.HELP:
                validarParametros(event, 0);

                mostrarYEnviarHELP(
                        email,
                        respuesta,
                        reporteService.getComandosReporte()
                );
                break;

            default:
                System.out.println("[ERROR] Accion no reconocida para reporte");
                comandoDesconocido(email.getFrom());
                break;
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] reporte: " + e.getMessage());
        enviarErrorParametros(email.getFrom());

    } catch (Exception ex) {
        System.out.println("[ERROR] reporte: " + ex.getMessage());
        manejarExcepcionSQL(email.getFrom(), ex);
    }
}


@Override
public void error(TokenEvent event) {
    System.out.println("CU: ERROR / HELP GENERAL");
    System.out.println(event);

    try {
        String subject = email.getSubject() != null ? email.getSubject().trim().toLowerCase() : "";

        if (subject.equals("help")) {
            mostrarYEnviarHELPGeneral(email, respuesta);
            return;
        }

        comandoDesconocido(email.getFrom());

    } catch (Exception e) {
        manejarExcepcionSQL(email.getFrom(), e);
    }
}

@Override
public void posibleserrores(TokenEvent event) {
    System.out.println("CU: POSIBLES_ERRORES / HELP GENERAL");
    System.out.println(event);

    try {
        String subject = email.getSubject() != null ? email.getSubject().trim().toLowerCase() : "";

        if (subject.equals("help")) {
            mostrarYEnviarHELPGeneral(email, respuesta);
            return;
        }

        comandoDesconocido(email.getFrom());

    } catch (Exception e) {
        manejarExcepcionSQL(email.getFrom(), e);
    }
}

            // Metodos auxiliares para validaciones y respuestas
            private void validarParametros(TokenEvent event, int expectedCount) {
                if (event.getParams().size() != expectedCount) {
                    String errorMsg = "ERROR EN LA CANTIDAD DE PARAMETROS";
                    respuesta.responseUser(email, errorMsg);
                    mostraRespuesta(errorMsg);
                // throw new IllegalArgumentException("ERROR EN LA CANTIDAD DE PARAMETROS");
                }
            }

            private boolean validarParametrosRango(TokenEvent event, int minExpected, int maxExpected) {
                int size = event.getParams().size();
                if (size < minExpected || size > maxExpected) {
                    String errorMsg = "ERROR EN LA CANTIDAD DE PARAMETROS";
                    respuesta.responseUser(email, errorMsg);
                    mostraRespuesta(errorMsg);
                    return false;
                }
                return true;
            }

            private void enviarErrorParametros(String destinatario) {
                System.out.println("Error en la cantidad de parÃ¡metros");
                 mostrarYEnviarHELP(email, respuesta, "ERROR EN LA CANTIDAD DE PARAMETROS");
            }

            private void comandoDesconocido(String destinatario) {
                System.out.println("COMANDO DESCONOCIDO");

                String mensaje = "<html><body style='font-family: Arial; padding: 20px;'>" +
                        "<h2 style='color:#d32f2f;'>Comando desconocido</h2>" +
                        "<p>El comando enviado no fue reconocido por el sistema.</p>" +
                        "<p>Para ver todos los comandos disponibles envie:</p>" +
                        "<h3 style='color:#2E7D32;'>help</h3>" +
                        "<p>Ejemplos:</p>" +
                        "<ul>" +
                        "<li>producto help</li>" +
                        "<li>venta help</li>" +
                        "<li>pago_contado help</li>" +
                        "<li>reporte help</li>" +
                        "</ul>" +
                        "</body></html>";

                mostraRespuesta(mensaje);
                respuesta.responseUser(email, mensaje);
            }

            private void manejarExcepcionSQL(String destinatario, Exception ex) {
                System.out.println("Mensaje SQL: " + ex.getMessage());

                String mensaje = "<html><body style='font-family: Arial; padding: 20px;'>" +
                        "<h2 style='color:#d32f2f;'>Error al procesar el comando</h2>" +
                        "<p><b>Detalle:</b> " + ex.getMessage() + "</p>" +
                        "<p>Revise el formato del comando enviado o use <b>help</b> para ver los comandos disponibles.</p>" +
                        "</body></html>";

                mostraRespuesta(mensaje);
                respuesta.responseUser(email, mensaje);
            }
        });

        Thread thread = new Thread(interpreter);
        thread.setName("Interpreter Thread");
        thread.start();
    }

    private static void EnviarCorreoDeGrupoAGmail(Email email, SendEmail respuesta, String titulo, String[] cabecera, List<String[]> lista) {
        String s = HtmlBuilder.generateTable(titulo, cabecera, lista);
        mostraRespuesta(s);
     
        respuesta.responseUser(email, s);
    }

    private static void EnviarRespuestaUsuario(Email email, SendEmail respuesta, String mensaje, String[] cabecera, List<String[]> lista) {
        String contenido = HtmlBuilder.generateMessageAndTable(mensaje, "Lista de Usuarios", cabecera, lista);
        mostraRespuesta(contenido);

        respuesta.responseUser(email, contenido);
    }

    public static void mostraRespuesta(String textoHtml) {
        SwingUtilities.invokeLater(() -> {
            Principal.ta_respuesta.setContentType("text/html");
            Principal.ta_respuesta.setText(textoHtml);
            Principal.ta_respuesta.setCaretPosition(0); // para mostrar desde el inicio
        });
    }

    public static void mostrarYEnviarHELP(Email email, SendEmail respuesta, String comandos) {
        String subject = email.getSubject() != null ? email.getSubject().toLowerCase().trim() : "";
        String titulo = "Comandos Disponibles";

        if (subject.contains("producto_variante")) {
            titulo = "Comandos disponibles para el modulo Producto Variante";
        } else if (subject.contains("promocion_producto")) {
            titulo = "Comandos disponibles para el modulo Promocion Producto";
        } else if (subject.contains("promocion_categoria")) {
            titulo = "Comandos disponibles para el modulo Promocion Categoria";
        } else if (subject.contains("carrito_detalle")) {
            titulo = "Comandos disponibles para el modulo Carrito Detalle";
        } else if (subject.contains("pedido_detalle")) {
            titulo = "Comandos disponibles para el modulo Pedido Detalle";
        } else if (subject.contains("detalle_venta")) {
            titulo = "Comandos disponibles para el modulo Detalle Venta";
        } else if (subject.contains("detalle_compra")) {
            titulo = "Comandos disponibles para el modulo Detalle Compra";
        } else if (subject.contains("tecnica_inventario")) {
            titulo = "Comandos disponibles para el modulo Tecnica Inventario";
        } else if (subject.contains("tecnica_costo")) {
            titulo = "Comandos disponibles para el modulo Tecnica Costo";
        } else if (subject.contains("movimiento_inventario")) {
            titulo = "Comandos disponibles para el modulo Movimiento Inventario";
        } else if (subject.contains("lote_inventario")) {
            titulo = "Comandos disponibles para el modulo Lote Inventario";
        } else if (subject.contains("salida_lote")) {
            titulo = "Comandos disponibles para el modulo Salida Lote";
        } else if (subject.contains("metodo_pago")) {
            titulo = "Comandos disponibles para el modulo Metodo Pago";
        } else if (subject.contains("pago_contado")) {
            titulo = "Comandos disponibles para el modulo Pago Contado";
        } else if (subject.contains("pago_cuota")) {
            titulo = "Comandos disponibles para el modulo Pago Cuota";
        } else if (subject.contains("usuario")) {
            titulo = "Comandos disponibles para el modulo Usuario";
        } else if (subject.contains("propietario")) {
            titulo = "Comandos disponibles para el modulo Propietario";
        } else if (subject.contains("vendedor")) {
            titulo = "Comandos disponibles para el modulo Vendedor";
        } else if (subject.contains("cliente")) {
            titulo = "Comandos disponibles para el modulo Cliente";
        } else if (subject.contains("categoria")) {
            titulo = "Comandos disponibles para el modulo Categoria";
        } else if (subject.contains("producto")) {
            titulo = "Comandos disponibles para el modulo Producto";
        } else if (subject.contains("proveedor")) {
            titulo = "Comandos disponibles para el modulo Proveedor";
        } else if (subject.contains("compra")) {
            titulo = "Comandos disponibles para el modulo Compra";
        } else if (subject.contains("promocion")) {
            titulo = "Comandos disponibles para el modulo Promocion";
        } else if (subject.contains("carrito")) {
            titulo = "Comandos disponibles para el modulo Carrito";
        } else if (subject.contains("pedido")) {
            titulo = "Comandos disponibles para el modulo Pedido";
        } else if (subject.contains("venta")) {
            titulo = "Comandos disponibles para el modulo Venta";
        } else if (subject.contains("credito")) {
            titulo = "Comandos disponibles para el modulo Credito";
        } else if (subject.contains("reporte")) {
            titulo = "Comandos disponibles para el modulo Reporte";
        } else if (subject.contains("notificacion")) {
            titulo = "Comandos disponibles para el modulo Notificacion";
        }

        String htmlTabla = HtmlBuilder.generateHelpTable(titulo, comandos);

        respuesta.responseUser(email, htmlTabla);
        mostraRespuesta(htmlTabla);
    }

    public static void mostrarYEnviarHELPGeneral(Email email, SendEmail respuesta) {
        String titulo = "Comandos generales disponibles del sistema - Vicerrectorado Firma Digital (Grupo 20 SA)";

        String comandos =
                "REGLA: Para Alumnos, Docentes y Administrativos, su login es su Registro y su CI es su contraseña inicial.\n\n" +
                "CU1. Gestion de Usuarios\n" +
                "usuario help\n" +
                "usuario listar\n\n" +

                "CU2. Gestion de Delegacion de Autoridad e Indicadores de Gestion\n" +
                "delegacion help\n" +
                "delegacion listar\n" +
                "indicador help\n" +
                "indicador listar\n\n" +

                "CU3. Gestion de Tramites, Seleccionar Documentos, Validar Requisitos y Compilar Expediente\n" +
                "tramite help\n" +
                "tramite listar\n" +
                "documento help\n" +
                "documento listar\n" +
                "tipo_tramite help\n" +
                "tipo_tramite listar\n\n" +

                "CU4. Gestion de Enrostrar Flujo, Visualizar Guia Procesal, Gestionar Observaciones y Bitacora\n" +
                "flujo help\n" +
                "flujo listar\n" +
                "guia help\n" +
                "guia listar\n" +
                "observacion help\n" +
                "observacion listar\n" +
                "bitacora help\n" +
                "bitacora listar\n\n" +

                "CU5. Gestion de Despachar, Previsualizar Legajos y Ejecutar Firma Digital Consolidada o Remota\n" +
                "firma help\n" +
                "firma listar\n" +
                "despacho help\n" +
                "despacho listar\n\n" +

                "CU6. Gestion de Certificado Oficial con QR y Habilitar Portal de Seguimiento Estudiantil\n" +
                "certificado help\n" +
                "certificado listar\n" +
                "portal help\n\n" +

                "CU7. Gestion de Monitoreo y Tiempos de Atencion y Alertas SLA (72 Horas Habiles)\n" +
                "sla help\n" +
                "sla listar\n" +
                "alerta help\n" +
                "alerta listar\n\n" +

                "CU8. Reportes y Estadisticas\n" +
                "reporte help\n" +
                "reporte tramites\n" +
                "reporte sla";

        String htmlTabla = HtmlBuilder.generateHelpTable(titulo, comandos);

        respuesta.responseUser(email, htmlTabla);
        mostraRespuesta(htmlTabla);
    }
}
