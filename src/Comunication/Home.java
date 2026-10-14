package Comunication;

import Interfaces.IEmailEventListener;
import Interfaces.ITokenCasoUsoEventListener;
import Services.*;
import Utils.Email;
import Utils.Interpreter;
import Utils.Token;
import proyectomailgrupo20sa.Principal;

import javax.swing.SwingUtilities;
import java.util.List;

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

        // Inicialización de Servicios del Vicerrectorado
        UsuarioService usuarioService = new UsuarioService();
        DelegacionService delegacionService = new DelegacionService();
        IndicadorService indicadorService = new IndicadorService();
        TramiteService tramiteService = new TramiteService();
        DocumentoService documentoService = new DocumentoService();
        TipoTramiteService tipoTramiteService = new TipoTramiteService();
        FlujoService flujoService = new FlujoService();
        FirmaService firmaService = new FirmaService();
        DespachoService despachoService = new DespachoService();
        CertificadoService certificadoService = new CertificadoService();
        PortalService portalService = new PortalService();
        SlaService slaService = new SlaService();
        ReporteService reporteService = new ReporteService();

        String subjectText = email.getSubject() != null ? email.getSubject().trim() : "";

        Interpreter interpreter = new Interpreter(subjectText, email.getFrom());
        interpreter.setListener(new ITokenCasoUsoEventListener() {

            // CU1. USUARIO
            @Override
            public void usuario(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            List<String[]> lista = usuarioService.listarUsuarios();
                            enviarRespuesta(email, respuesta, "Lista de Usuarios", HtmlBuilder.HEADERS_USER, lista);
                            break;
                        case Token.AGREGAR:
                            usuarioService.crearUsuario(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Usuario agregado exitosamente (Contraseña = CI)", "Lista de Usuarios", HtmlBuilder.HEADERS_USER, usuarioService.listarUsuarios());
                            break;
                        case Token.MODIFICAR:
                            usuarioService.actualizarUsuario(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Usuario modificado exitosamente", "Lista de Usuarios", HtmlBuilder.HEADERS_USER, usuarioService.listarUsuarios());
                            break;
                        case Token.ELIMINAR:
                            usuarioService.eliminarUsuario(Integer.parseInt(event.getParams().get(0).trim()));
                            enviarMensajeYTabla(email, respuesta, "Usuario eliminado exitosamente", "Lista de Usuarios", HtmlBuilder.HEADERS_USER, usuarioService.listarUsuarios());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Gestion de Usuarios (CU1)", usuarioService.getComandosUsuario());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU2. DELEGACION E INDICADOR
            @Override
            public void delegacion(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Lista de Delegaciones de Autoridad", HtmlBuilder.HEADERS_DELEGACION, delegacionService.listarDelegaciones());
                            break;
                        case Token.AGREGAR:
                            delegacionService.crearDelegacion(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Delegacion registrada exitosamente", "Lista de Delegaciones", HtmlBuilder.HEADERS_DELEGACION, delegacionService.listarDelegaciones());
                            break;
                        case Token.MODIFICAR:
                            delegacionService.modificarDelegacion(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Delegacion actualizada exitosamente", "Lista de Delegaciones", HtmlBuilder.HEADERS_DELEGACION, delegacionService.listarDelegaciones());
                            break;
                        case Token.ELIMINAR:
                            delegacionService.eliminarDelegacion(Integer.parseInt(event.getParams().get(0).trim()));
                            enviarMensajeYTabla(email, respuesta, "Delegacion eliminada exitosamente", "Lista de Delegaciones", HtmlBuilder.HEADERS_DELEGACION, delegacionService.listarDelegaciones());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Delegacion (CU2)", delegacionService.getComandosDelegacion());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void indicador(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Indicadores de Gestion Vicerrectoral", HtmlBuilder.HEADERS_INDICADOR, indicadorService.listarIndicadores());
                            break;
                        case Token.AGREGAR:
                            indicadorService.crearIndicador(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Indicador registrado", "Indicadores de Gestion", HtmlBuilder.HEADERS_INDICADOR, indicadorService.listarIndicadores());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Indicadores (CU2)", indicadorService.getComandosIndicador());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU3. TRAMITES, DOCUMENTOS Y TIPOS
            @Override
            public void tramite(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Lista de Tramites Academicos", HtmlBuilder.HEADERS_TRAMITE, tramiteService.listarTramites());
                            break;
                        case Token.AGREGAR:
                            tramiteService.crearTramite(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Tramite iniciado exitosamente", "Lista de Tramites", HtmlBuilder.HEADERS_TRAMITE, tramiteService.listarTramites());
                            break;
                        case Token.MODIFICAR:
                            tramiteService.modificarTramite(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Tramite modificado exitosamente", "Lista de Tramites", HtmlBuilder.HEADERS_TRAMITE, tramiteService.listarTramites());
                            break;
                        case Token.ELIMINAR:
                            tramiteService.eliminarTramite(Integer.parseInt(event.getParams().get(0).trim()));
                            enviarMensajeYTabla(email, respuesta, "Tramite eliminado exitosamente", "Lista de Tramites", HtmlBuilder.HEADERS_TRAMITE, tramiteService.listarTramites());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Tramites (CU3)", tramiteService.getComandosTramite());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void documento(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Documentos del Expediente", HtmlBuilder.HEADERS_DOCUMENTO, documentoService.listarDocumentos());
                            break;
                        case Token.AGREGAR:
                            documentoService.crearDocumento(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Documento anexado al expediente", "Documentos del Expediente", HtmlBuilder.HEADERS_DOCUMENTO, documentoService.listarDocumentos());
                            break;
                        case Token.VALIDAR:
                            documentoService.validarDocumento(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Documento validado correctamente", "Documentos del Expediente", HtmlBuilder.HEADERS_DOCUMENTO, documentoService.listarDocumentos());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Documentos (CU3)", documentoService.getComandosDocumento());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void tipoTramite(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Catalogo de Tipos de Tramite", HtmlBuilder.HEADERS_TIPO_TRAMITE, tipoTramiteService.listarTipos());
                            break;
                        case Token.AGREGAR:
                            tipoTramiteService.crearTipo(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Tipo de tramite registrado", "Catalogo de Tramites", HtmlBuilder.HEADERS_TIPO_TRAMITE, tipoTramiteService.listarTipos());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Tipos de Tramite (CU3)", tipoTramiteService.getComandosTipoTramite());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU4. FLUJO
            @Override
            public void flujo(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Historial de Flujo de Tramites", HtmlBuilder.HEADERS_FLUJO, flujoService.listarFlujo());
                            break;
                        case Token.AVANZAR:
                            flujoService.avanzarFlujo(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Etapa del tramite actualizada", "Flujo de Tramites", HtmlBuilder.HEADERS_FLUJO, flujoService.listarFlujo());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Flujo (CU4)", flujoService.getComandosFlujo());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU5. FIRMA Y DESPACHO
            @Override
            public void firma(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Registro de Firmas Digitales Emitidas", HtmlBuilder.HEADERS_FIRMA, firmaService.listarFirmas());
                            break;
                        case Token.CONSOLIDADA:
                            firmaService.ejecutarFirma(event.getParams(), "CONSOLIDADA");
                            enviarMensajeYTabla(email, respuesta, "Firma digital consolidada ejecutada exitosamente", "Firmas Digitales", HtmlBuilder.HEADERS_FIRMA, firmaService.listarFirmas());
                            break;
                        case Token.REMOTA:
                            firmaService.ejecutarFirma(event.getParams(), "REMOTA");
                            enviarMensajeYTabla(email, respuesta, "Firma digital remota vicerrectoral ejecutada exitosamente", "Firmas Digitales", HtmlBuilder.HEADERS_FIRMA, firmaService.listarFirmas());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Firma Digital (CU5)", firmaService.getComandosFirma());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void despacho(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Legajos Despachados Oficialmente", HtmlBuilder.HEADERS_DESPACHO, despachoService.listarDespachos());
                            break;
                        case Token.AGREGAR:
                            despachoService.registrarDespacho(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Legajo despachado oficialmente", "Despachos Oficiales", HtmlBuilder.HEADERS_DESPACHO, despachoService.listarDespachos());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Despacho (CU5)", despachoService.getComandosDespacho());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU6. CERTIFICADO Y PORTAL
            @Override
            public void certificado(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Certificados Oficiales Emitidos con QR", HtmlBuilder.HEADERS_CERTIFICADO, certificadoService.listarCertificados());
                            break;
                        case Token.GENERAR:
                            certificadoService.generarCertificado(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Certificado Oficial con QR generado exitosamente", "Certificados Emitidos", HtmlBuilder.HEADERS_CERTIFICADO, certificadoService.listarCertificados());
                            break;
                        case Token.CONSULTAR:
                            String[] cert = certificadoService.consultarCertificado(event.getParams().get(0).trim());
                            if (cert != null) {
                                enviarRespuesta(email, respuesta, "Certificado Oficial Valido", HtmlBuilder.HEADERS_CERTIFICADO, List.of(cert));
                            } else {
                                enviarMensaje(email, respuesta, "No se encontro ningun certificado con el codigo de verificacion indicado.");
                            }
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Certificado QR (CU6)", certificadoService.getComandosCertificado());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void portal(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.CONSULTAR:
                            String[] portalInfo = portalService.consultarPorPin(event.getParams().get(0).trim());
                            if (portalInfo != null) {
                                enviarRespuesta(email, respuesta, "Seguimiento de Tramite Estudiantil", HtmlBuilder.HEADERS_PORTAL, List.of(portalInfo));
                            } else {
                                enviarMensaje(email, respuesta, "PIN de seguimiento no valido o no registrado.");
                            }
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Portal (CU6)", portalService.getComandosPortal());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU7. MONITOREO SLA
            @Override
            public void sla(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Monitoreo de Tiempos y Alertas SLA (72 Horas)", HtmlBuilder.HEADERS_SLA, slaService.listarMonitoreo());
                            break;
                        case Token.VERIFICAR:
                            int idTramite = Integer.parseInt(event.getParams().get(0).trim());
                            slaService.verificarSla(idTramite);
                            enviarMensajeYTabla(email, respuesta, "SLA verificado y actualizado", "Monitoreo SLA", HtmlBuilder.HEADERS_SLA, slaService.listarMonitoreo());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Monitoreo SLA (CU7)", slaService.getComandosSla());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            // CU8. REPORTES
            @Override
            public void reporte(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.TRAMITES:
                            enviarRespuesta(email, respuesta, "Reporte de Tramites por Estado", HtmlBuilder.HEADERS_REPORTE_ESTADO, reporteService.reporteTramitesPorEstado());
                            break;
                        case Token.SLA:
                            enviarRespuesta(email, respuesta, "Reporte de Tasa de Cumplimiento SLA 72 Horas", HtmlBuilder.HEADERS_REPORTE_SLA, reporteService.reporteCumplimientoSla());
                            break;
                        case Token.FIRMAS:
                            enviarRespuesta(email, respuesta, "Reporte de Firmas por Autoridad Vicerrectoral", HtmlBuilder.HEADERS_REPORTE_FIRMAS, reporteService.reporteFirmasPorVicerrector());
                            break;
                        case Token.HELP:
                        default:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Reportes y Estadisticas (CU8)", reporteService.getComandosReporte());
                            break;
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex);
                }
            }

            @Override
            public void error(TokenEvent event) {
                mostrarYEnviarHELPGeneral(email, respuesta);
            }

            @Override
            public void posibleserrores(TokenEvent event) {
                mostrarYEnviarHELPGeneral(email, respuesta);
            }
        });

        Thread thread = new Thread(interpreter);
        thread.setName("Interpreter Thread");
        thread.start();
    }

    private static void enviarRespuesta(Email email, SendEmail respuesta, String titulo, String[] cabecera, List<String[]> lista) {
        String s = HtmlBuilder.generateTable(titulo, cabecera, lista);
        mostraRespuesta(s);
        respuesta.responseUser(email, s);
    }

    private static void enviarMensajeYTabla(Email email, SendEmail respuesta, String mensaje, String titulo, String[] cabecera, List<String[]> lista) {
        String s = HtmlBuilder.generateMessageAndTable(mensaje, titulo, cabecera, lista);
        mostraRespuesta(s);
        respuesta.responseUser(email, s);
    }

    private static void enviarMensaje(Email email, SendEmail respuesta, String mensaje) {
        String html = "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h3 style='color: #0275d8;'>" + mensaje + "</h3>" +
                "</body></html>";
        mostraRespuesta(html);
        respuesta.responseUser(email, html);
    }

    private static void manejarExcepcion(Email email, SendEmail respuesta, Exception ex) {
        String html = "<html><body style='font-family: Arial; padding: 20px;'>" +
                "<h3 style='color: #d9534f;'>Error al procesar comando</h3>" +
                "<p>" + ex.getMessage() + "</p>" +
                "<p>Use <b>help</b> para ver la sintaxis de los comandos disponibles.</p>" +
                "</body></html>";
        mostraRespuesta(html);
        respuesta.responseUser(email, html);
    }

    public static void mostraRespuesta(String textoHtml) {
        SwingUtilities.invokeLater(() -> {
            Principal.ta_respuesta.setContentType("text/html");
            Principal.ta_respuesta.setText(textoHtml);
            Principal.ta_respuesta.setCaretPosition(0);
        });
    }

    public static void mostrarYEnviarHELP(Email email, SendEmail respuesta, String titulo, String comandos) {
        String htmlTabla = HtmlBuilder.generateHelpTable(titulo, comandos);
        respuesta.responseUser(email, htmlTabla);
        mostraRespuesta(htmlTabla);
    }

    public static void mostrarYEnviarHELPGeneral(Email email, SendEmail respuesta) {
        String titulo = "Sistema de Firma Digital del Vicerrectorado - Grupo 20 SA";
        String comandos =
                "REGLA: Para Alumnos, Docentes y Administrativos, su login es su Registro numerico y su CI es su contraseña inicial.\n\n" +
                "CU1. Gestion de Usuarios:\n" +
                "  usuario help | usuario listar\n\n" +
                "CU2. Delegacion de Autoridad e Indicadores:\n" +
                "  delegacion help | delegacion listar\n" +
                "  indicador help | indicador listar\n\n" +
                "CU3. Tramites, Documentos y Expediente:\n" +
                "  tramite help | tramite listar\n" +
                "  documento help | documento listar\n" +
                "  tipo_tramite help | tipo_tramite listar\n\n" +
                "CU4. Enrutamiento de Flujo:\n" +
                "  flujo help | flujo listar\n\n" +
                "CU5. Firma Digital y Despacho:\n" +
                "  firma help | firma listar\n" +
                "  despacho help | despacho listar\n\n" +
                "CU6. Certificado Oficial con QR y Seguimiento:\n" +
                "  certificado help | certificado listar\n" +
                "  portal help\n\n" +
                "CU7. Monitoreo SLA (72 Horas Habiles):\n" +
                "  sla help | sla listar\n\n" +
                "CU8. Reportes y Estadisticas:\n" +
                "  reporte help | reporte tramites | reporte sla | reporte firmas";

        String htmlTabla = HtmlBuilder.generateHelpTable(titulo, comandos);
        respuesta.responseUser(email, htmlTabla);
        mostraRespuesta(htmlTabla);
    }
}
