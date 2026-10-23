package Comunication;

import Interfaces.IEmailEventListener;
import Interfaces.ITokenCasoUsoEventListener;
import Services.*;
import Utils.Email;
import Utils.Interpreter;
import Utils.ParamValidator;
import Utils.Token;
import java.sql.SQLException;
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
        GuiaService guiaService = new GuiaService();
        ObservacionService observacionService = new ObservacionService();
        BitacoraService bitacoraService = new BitacoraService();
        FirmaService firmaService = new FirmaService();
        DespachoService despachoService = new DespachoService();
        CertificadoService certificadoService = new CertificadoService();
        PortalService portalService = new PortalService();
        SlaService slaService = new SlaService();
        AlertaService alertaService = new AlertaService();
        ReporteService reporteService = new ReporteService();

        String subjectText = email.getSubject() != null ? email.getSubject().trim() : "";

        // Si el asunto está completamente vacío
        if (subjectText.isEmpty()) {
            String htmlError = HtmlBuilder.generateDetailedError(
                    "(Asunto vacío)",
                    "Comando Vacío o No Proporcionado",
                    "El correo o mensaje recibido no contiene ningún comando en el Asunto (Subject).\n" +
                    "• El sistema requiere que el comando se escriba en el Asunto del correo.\n" +
                    "• Ingrese un comando como 'help' para consultar la guía completa o 'usuario listar' para consultar registros.",
                    "<entidad> <accion> [parametros]",
                    "usuario listar  ó  tramite listar  ó  help"
            );
            mostraRespuesta(htmlError);
            respuesta.responseUser(email, htmlError);
            return;
        }

        // Si es directamente help o ayuda general
        if (subjectText.equalsIgnoreCase("help") || subjectText.equalsIgnoreCase("ayuda")) {
            mostrarYEnviarHELPGeneral(email, respuesta);
            return;
        }

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
                            int id = ParamValidator.requireInt(event.getParams(), 0, "id_usuario", "usuario eliminar [id_usuario]", "usuario eliminar [4]");
                            usuarioService.eliminarUsuario(id);
                            enviarMensajeYTabla(email, respuesta, "Usuario con ID " + id + " eliminado exitosamente", "Lista de Usuarios", HtmlBuilder.HEADERS_USER, usuarioService.listarUsuarios());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Gestion de Usuarios (CU1)", usuarioService.getComandosUsuario());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'usuario'.\n" +
                                "• Acciones válidas: listar, agregar, modificar, eliminar, help.\n" +
                                "• Envie 'usuario help' para ver la sintaxis y ejemplos de cada acción."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "usuario");
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
                            int id = ParamValidator.requireInt(event.getParams(), 0, "id_delegacion", "delegacion eliminar [id_delegacion]", "delegacion eliminar [1]");
                            delegacionService.eliminarDelegacion(id);
                            enviarMensajeYTabla(email, respuesta, "Delegacion con ID " + id + " eliminada exitosamente", "Lista de Delegaciones", HtmlBuilder.HEADERS_DELEGACION, delegacionService.listarDelegaciones());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Delegacion (CU2)", delegacionService.getComandosDelegacion());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'delegacion'.\n" +
                                "• Acciones válidas: listar, agregar, modificar, eliminar, help.\n" +
                                "• Envie 'delegacion help' para ver la sintaxis y ejemplos de cada acción."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "delegacion");
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
                            enviarMensajeYTabla(email, respuesta, "Indicador registrado exitosamente", "Indicadores de Gestion", HtmlBuilder.HEADERS_INDICADOR, indicadorService.listarIndicadores());
                            break;
                        case Token.MODIFICAR:
                            indicadorService.modificarIndicador(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Indicador actualizado exitosamente", "Indicadores de Gestion", HtmlBuilder.HEADERS_INDICADOR, indicadorService.listarIndicadores());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Indicadores (CU2)", indicadorService.getComandosIndicador());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'indicador'.\n" +
                                "• Acciones válidas: listar, agregar, modificar, help.\n" +
                                "• Envie 'indicador help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "indicador");
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
                            int id = ParamValidator.requireInt(event.getParams(), 0, "id_tramite", "tramite eliminar [id_tramite]", "tramite eliminar [1]");
                            tramiteService.eliminarTramite(id);
                            enviarMensajeYTabla(email, respuesta, "Tramite con ID " + id + " eliminado exitosamente", "Lista de Tramites", HtmlBuilder.HEADERS_TRAMITE, tramiteService.listarTramites());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Tramites (CU3)", tramiteService.getComandosTramite());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'tramite'.\n" +
                                "• Acciones válidas: listar, agregar, modificar, eliminar, help.\n" +
                                "• Envie 'tramite help' para ver la sintaxis y ejemplos de cada acción."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "tramite");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Documentos (CU3)", documentoService.getComandosDocumento());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'documento'.\n" +
                                "• Acciones válidas: listar, agregar, validar, help.\n" +
                                "• Envie 'documento help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "documento");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Tipos de Tramite (CU3)", tipoTramiteService.getComandosTipoTramite());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'tipo_tramite'.\n" +
                                "• Acciones válidas: listar, agregar, help.\n" +
                                "• Envie 'tipo_tramite help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "tipo_tramite");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Flujo (CU4)", flujoService.getComandosFlujo());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'flujo'.\n" +
                                "• Acciones válidas: listar, avanzar, help.\n" +
                                "• Envie 'flujo help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "flujo");
                }
            }

            @Override
            public void guia(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Catalogo de Guia Procesal", HtmlBuilder.HEADERS_GUIA, guiaService.listarGuias());
                            break;
                        case Token.MOSTRAR:
                            enviarRespuesta(email, respuesta, "Instrucciones de la Guia Procesal", HtmlBuilder.HEADERS_GUIA, guiaService.mostrarGuiaPorTipo(event.getParams()));
                            break;
                        case Token.AGREGAR:
                            guiaService.crearGuia(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Paso de guia procesal registrado exitosamente", "Guia Procesal", HtmlBuilder.HEADERS_GUIA, guiaService.listarGuias());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Guia Procesal (CU4)", guiaService.getComandosGuia());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'guia'.\n" +
                                "• Acciones válidas: listar, mostrar, agregar, help.\n" +
                                "• Envie 'guia help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "guia");
                }
            }

            @Override
            public void observacion(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Historial de Observaciones de Tramites", HtmlBuilder.HEADERS_OBSERVACION, observacionService.listarObservaciones());
                            break;
                        case Token.AGREGAR:
                            observacionService.crearObservacion(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Observacion registrada exitosamente", "Observaciones de Tramite", HtmlBuilder.HEADERS_OBSERVACION, observacionService.listarObservaciones());
                            break;
                        case Token.SUBSANAR:
                            observacionService.subsanarObservacion(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Observacion subsanada exitosamente", "Observaciones de Tramite", HtmlBuilder.HEADERS_OBSERVACION, observacionService.listarObservaciones());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Observaciones (CU4)", observacionService.getComandosObservacion());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'observacion'.\n" +
                                "• Acciones válidas: listar, agregar, subsanar, help.\n" +
                                "• Envie 'observacion help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "observacion");
                }
            }

            @Override
            public void bitacora(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Bitacora General de Auditoria de Tramites", HtmlBuilder.HEADERS_BITACORA, bitacoraService.listarBitacora());
                            break;
                        case Token.CONSULTAR:
                            enviarRespuesta(email, respuesta, "Bitacora de Auditoria del Tramite", HtmlBuilder.HEADERS_BITACORA, bitacoraService.consultarPorTramite(event.getParams()));
                            break;
                        case Token.AGREGAR:
                            bitacoraService.registrarEventoManual(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Evento registrado en bitacora exitosamente", "Bitacora de Auditoria", HtmlBuilder.HEADERS_BITACORA, bitacoraService.listarBitacora());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Bitacora (CU4)", bitacoraService.getComandosBitacora());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'bitacora'.\n" +
                                "• Acciones válidas: listar, consultar, agregar, help.\n" +
                                "• Envie 'bitacora help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "bitacora");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Firma Digital (CU5)", firmaService.getComandosFirma());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'firma'.\n" +
                                "• Acciones válidas: listar, consolidada, remota, help.\n" +
                                "• Envie 'firma help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "firma");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Despacho (CU5)", despachoService.getComandosDespacho());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'despacho'.\n" +
                                "• Acciones válidas: listar, agregar, help.\n" +
                                "• Envie 'despacho help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "despacho");
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
                            String cod = ParamValidator.requireString(event.getParams(), 0, "codigo_verificacion", "certificado consultar [codigo_verificacion]", "certificado consultar [4f2a1b9c-8d3e-4f5a-b6c7-d8e9f0a1b2c3]");
                            String[] cert = certificadoService.consultarCertificado(cod);
                            if (cert != null) {
                                enviarRespuesta(email, respuesta, "Certificado Oficial Valido", HtmlBuilder.HEADERS_CERTIFICADO, java.util.Collections.singletonList(cert));
                            } else {
                                throw new SQLException("No se encontró ningún certificado con el código de verificación: '" + cod + "'. Verifique el código.");
                            }
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Certificado QR (CU6)", certificadoService.getComandosCertificado());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'certificado'.\n" +
                                "• Acciones válidas: listar, generar, consultar, help.\n" +
                                "• Envie 'certificado help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "certificado");
                }
            }

            @Override
            public void portal(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Portales de Seguimiento Estudiantil Activos", HtmlBuilder.HEADERS_PORTAL, portalService.listarPortales());
                            break;
                        case Token.AGREGAR:
                        case Token.HABILITAR:
                            portalService.habilitarPortal(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Portal de seguimiento habilitado exitosamente", "Portales de Seguimiento", HtmlBuilder.HEADERS_PORTAL, portalService.listarPortales());
                            break;
                        case Token.CONSULTAR:
                            String pin = ParamValidator.requireString(event.getParams(), 0, "pin_seguimiento", "portal consultar [pin_seguimiento]", "portal consultar [PIN-8841]");
                            String[] portalInfo = portalService.consultarPorPin(pin);
                            if (portalInfo != null) {
                                enviarRespuesta(email, respuesta, "Seguimiento de Tramite Estudiantil", HtmlBuilder.HEADERS_PORTAL, java.util.Collections.singletonList(portalInfo));
                            } else {
                                throw new SQLException("No se encontró ningún trámite con el PIN de seguimiento: '" + pin + "'. Verifique el código.");
                            }
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Portal (CU6)", portalService.getComandosPortal());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'portal'.\n" +
                                "• Acciones válidas: listar, agregar, consultar, help.\n" +
                                "• Envie 'portal help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "portal");
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
                            slaService.verificarSla(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "SLA verificado y actualizado exitosamente", "Monitoreo SLA", HtmlBuilder.HEADERS_SLA, slaService.listarMonitoreo());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Monitoreo SLA (CU7)", slaService.getComandosSla());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'sla'.\n" +
                                "• Acciones válidas: listar, verificar, help.\n" +
                                "• Envie 'sla help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "sla");
                }
            }

            @Override
            public void alerta(TokenEvent event) {
                try {
                    switch (event.getAction()) {
                        case Token.LISTAR:
                            enviarRespuesta(email, respuesta, "Historial de Alertas de Vencimiento SLA", HtmlBuilder.HEADERS_ALERTA, alertaService.listarAlertas());
                            break;
                        case Token.AGREGAR:
                            alertaService.crearAlerta(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Alerta generada y registrada exitosamente", "Alertas SLA", HtmlBuilder.HEADERS_ALERTA, alertaService.listarAlertas());
                            break;
                        case Token.ATENDER:
                            alertaService.atenderAlerta(event.getParams());
                            enviarMensajeYTabla(email, respuesta, "Alerta marcada como atendida exitosamente", "Alertas SLA", HtmlBuilder.HEADERS_ALERTA, alertaService.listarAlertas());
                            break;
                        case Token.HELP:
                            mostrarYEnviarHELP(email, respuesta, "Comandos Alertas SLA (CU7)", alertaService.getComandosAlerta());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Acción no reconocida para 'alerta'.\n" +
                                "• Acciones válidas: listar, agregar, atender, help.\n" +
                                "• Envie 'alerta help' para ver la sintaxis y ejemplos."
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "alerta");
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
                            mostrarYEnviarHELP(email, respuesta, "Comandos Reportes y Estadisticas (CU8)", reporteService.getComandosReporte());
                            break;
                        default:
                            throw new IllegalArgumentException(
                                "Reporte o acción no reconocida para 'reporte'.\n" +
                                "• Reportes disponibles: reporte tramites, reporte sla, reporte firmas, reporte help.\n" +
                                "• Ejemplo: reporte tramites"
                            );
                    }
                } catch (Exception ex) {
                    manejarExcepcion(email, respuesta, ex, subjectText, "reporte");
                }
            }

            @Override
            public void error(TokenEvent event) {
                String cmd = event.getParams().size() > 0 ? event.getParams().get(0) : subjectText;
                String lexeme = event.getParams().size() > 1 ? event.getParams().get(1) : "término desconocido";

                String htmlError = HtmlBuilder.generateDetailedError(
                        cmd,
                        "Error de Sintaxis o Carácter Inválido en Comando",
                        "El término, carácter o palabra '" + lexeme + "' no es reconocido por el analizador del sistema.\n"
                        + "• Verifique que la entidad y la acción estén escritas en minúsculas y sin acentos (ej. 'tramite listar').\n"
                        + "• Recuerde que los parámetros deben ir entre corchetes [ ] y separados con punto y coma (;).\n"
                        + "• No use caracteres especiales ni signos extraños fuera de los corchetes.",
                        "<entidad> <accion> [parametro1;parametro2;...]",
                        "usuario listar  ó  tramite listar  ó  help"
                );
                mostraRespuesta(htmlError);
                respuesta.responseUser(email, htmlError);
            }

            @Override
            public void posibleserrores(TokenEvent event) {
                if (subjectText.equalsIgnoreCase("help") || subjectText.equalsIgnoreCase("ayuda")) {
                    mostrarYEnviarHELPGeneral(email, respuesta);
                    return;
                }

                String htmlError = HtmlBuilder.generateDetailedError(
                        subjectText,
                        "Entidad o Comando Principal No Reconocido",
                        "No se reconoce la entidad o comando principal: '" + subjectText + "'.\n"
                        + "• Entidades válidas del sistema:\n"
                        + "  - usuario (gestión de usuarios, alumnos, docentes, autoridades)\n"
                        + "  - tramite (iniciar, modificar, listar, eliminar)\n"
                        + "  - documento (anexar y validar documentos del expediente)\n"
                        + "  - delegacion (delegaciones de firma de autoridades)\n"
                        + "  - indicador (indicadores de gestión vicerrectoral)\n"
                        + "  - tipo_tramite (catálogo oficial de trámites)\n"
                        + "  - flujo (enrutamiento y avance de etapas)\n"
                        + "  - firma (emisión de firma consolidada o remota)\n"
                        + "  - despacho (registro de despachos oficiales)\n"
                        + "  - certificado (emisión y consulta de certificados con QR)\n"
                        + "  - portal (seguimiento público de trámites con PIN)\n"
                        + "  - sla (monitoreo de 72 horas hábiles y alertas)\n"
                        + "  - reporte (estadísticas de trámites, firmas y SLA)\n"
                        + "• Envie únicamente la palabra 'help' en el Asunto para recibir el manual general.",
                        "<entidad> <accion> [parametros]",
                        "usuario listar  ó  tramite agregar [...]  ó  help"
                );
                mostraRespuesta(htmlError);
                respuesta.responseUser(email, htmlError);
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

    private static void manejarExcepcion(Email email, SendEmail respuesta, Exception ex, String comandoOriginal, String casoUso) {
        String causaPrincipal;
        String detalleFallo;
        String sintaxisCorrecta = obtenerSintaxisCasoUso(casoUso);
        String ejemploValido = obtenerEjemploCasoUso(casoUso);

        if (ex instanceof IllegalArgumentException) {
            causaPrincipal = "Parámetros Incorrectos o Faltantes en el Comando";
            detalleFallo = ex.getMessage();
        } else if (ex instanceof SQLException) {
            String sqlMsg = ex.getMessage() != null ? ex.getMessage() : "";

            if (sqlMsg.startsWith("No se encontró") || sqlMsg.startsWith("No existe")) {
                causaPrincipal = "Registro No Encontrado en Base de Datos";
                detalleFallo = sqlMsg;
            } else if (sqlMsg.startsWith("Ya existe")) {
                causaPrincipal = "Registro Duplicado en Base de Datos";
                detalleFallo = sqlMsg;
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_tramite_solicitante")) {
                causaPrincipal = "Usuario Solicitante No Encontrado";
                detalleFallo = "El ID de solicitante ingresado no existe en la base de datos de usuarios.\n"
                        + "• Debe ingresar el ID de un usuario previamente registrado.\n"
                        + "• Consulte los usuarios registrados mediante 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_tramite_tipo")) {
                causaPrincipal = "Tipo de Trámite No Encontrado";
                detalleFallo = "El ID de tipo de trámite ingresado no existe en el catálogo.\n"
                        + "• Verifique los tipos disponibles mediante 'tipo_tramite listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_delegacion_titular")) {
                causaPrincipal = "Autoridad Titular No Encontrada";
                detalleFallo = "El ID de autoridad titular no existe en la base de datos de usuarios. Verifique con 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_delegacion_delegado")) {
                causaPrincipal = "Autoridad Delegada No Encontrada";
                detalleFallo = "El ID de autoridad delegada no existe en la base de datos de usuarios. Verifique con 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && (sqlMsg.contains("fk_doc_tramite") || sqlMsg.contains("fk_flujo_tramite") || sqlMsg.contains("fk_firma_tramite") || sqlMsg.contains("fk_despacho_tramite") || sqlMsg.contains("fk_cert_tramite") || sqlMsg.contains("fk_portal_tramite") || sqlMsg.contains("fk_sla_tramite") || sqlMsg.contains("fk_obs_tramite") || sqlMsg.contains("fk_bitacora_tramite"))) {
                causaPrincipal = "Trámite No Encontrado en el Sistema";
                detalleFallo = "El ID de trámite especificado no existe en el sistema.\n"
                        + "• Debe asociar la operación a un trámite existente.\n"
                        + "• Verifique los trámites registrados mediante 'tramite listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_doc_requisito")) {
                causaPrincipal = "Requisito No Encontrado";
                detalleFallo = "El ID de requisito especificado no existe en el catálogo de requisitos.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_flujo_responsable")) {
                causaPrincipal = "Usuario Responsable No Encontrado";
                detalleFallo = "El ID de usuario responsable para la etapa de flujo no existe en la base de datos de usuarios.\n"
                        + "• Verifique los IDs disponibles mediante 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_firma_firmante")) {
                causaPrincipal = "Usuario Firmante No Encontrado";
                detalleFallo = "El usuario firmante especificado no existe en la base de datos de usuarios. Verifique con 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_despacho_usuario")) {
                causaPrincipal = "Usuario Despachador No Encontrado";
                detalleFallo = "El usuario despachador especificado no existe en la base de datos de usuarios. Verifique con 'usuario listar'.";
            } else if (sqlMsg.contains("violates foreign key constraint") && sqlMsg.contains("fk_cert_firma")) {
                causaPrincipal = "Firma Digital No Encontrada";
                detalleFallo = "El ID de firma digital especificado no existe en el registro de firmas.\n"
                        + "• El trámite debe ser firmado digitalmente antes de generar el certificado oficial.\n"
                        + "• Verifique las firmas disponibles con 'firma listar'.";
            } else if (sqlMsg.contains("is still referenced from table")) {
                causaPrincipal = "No Se Puede Eliminar: Registro en Uso Activo";
                detalleFallo = "No es posible eliminar este registro porque ya cuenta con trámites, firmas o expedientes asociados en el sistema.\n"
                        + "• Por normativa y trazabilidad institucional, los registros con historial no pueden borrarse físicamente.\n"
                        + "• Puede cambiar su estado a 'INACTIVO' mediante el comando modificar.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("ci")) {
                causaPrincipal = "C.I. Ya Registrado en el Sistema";
                detalleFallo = "El C.I. ingresado ya pertenece a otro usuario registrado. Cada usuario debe poseer un C.I. único.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("registro")) {
                causaPrincipal = "Registro/Código Universitario Ya Existe";
                detalleFallo = "El Registro o Código numérico ya pertenece a otro usuario en el sistema.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("email")) {
                causaPrincipal = "Correo Electrónico Ya Registrado";
                detalleFallo = "El correo electrónico ingresado ya se encuentra asociado a otra cuenta de usuario.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("codigo_tramite")) {
                causaPrincipal = "Código de Trámite Duplicado";
                detalleFallo = "El código de trámite ya existe en la base de datos. Cada trámite debe tener un código numérico único.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("numero_despacho")) {
                causaPrincipal = "Número de Despacho Duplicado";
                detalleFallo = "El número de despacho oficial ya fue registrado anteriormente.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("codigo_verificacion")) {
                causaPrincipal = "Código de Verificación Duplicado";
                detalleFallo = "El código de verificación del certificado ya existe en el sistema.";
            } else if (sqlMsg.contains("violates unique constraint") && sqlMsg.contains("id_tramite")) {
                causaPrincipal = "Trámite Ya Cuenta con Este Registro";
                detalleFallo = "Este trámite ya tiene habilitado su portal de seguimiento o su monitoreo SLA.";
            } else if (sqlMsg.contains("invalid input syntax for type integer")) {
                causaPrincipal = "Tipo de Dato Inválido (Se Esperaba un Número Entero)";
                detalleFallo = "Se ingresó texto, letras o caracteres en un campo donde la base de datos requiere exclusivamente un número entero (ID o código).\n"
                        + "• Revise el orden y posición de los parámetros.";
            } else if (sqlMsg.contains("date/time field value out of range")) {
                causaPrincipal = "Formato de Fecha Inválido";
                detalleFallo = "El valor de fecha ingresado no es válido. Debe usar estrictamente el formato AAAA-MM-DD (ejemplo: 2026-10-15).";
            } else {
                causaPrincipal = "Restricción de Base de Datos";
                detalleFallo = sqlMsg;
            }
        } else {
            causaPrincipal = "Error Inesperado Durante el Procesamiento";
            detalleFallo = ex.getMessage() != null ? ex.getMessage() : ex.toString();
        }

        String htmlError = HtmlBuilder.generateDetailedError(
                comandoOriginal,
                causaPrincipal,
                detalleFallo,
                sintaxisCorrecta,
                ejemploValido
        );

        mostraRespuesta(htmlError);
        respuesta.responseUser(email, htmlError);
    }

    private static String obtenerSintaxisCasoUso(String casoUso) {
        if (casoUso == null) return "<caso_de_uso> <accion> [parametros]";
        switch (casoUso.toLowerCase()) {
            case "usuario":
                return "usuario agregar [nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo]\n"
                     + "usuario modificar [id;nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo;estado]\n"
                     + "usuario eliminar [id]\n"
                     + "usuario listar";
            case "delegacion":
                return "delegacion agregar [id_titular;id_delegado;resolucion;motivo;fecha_inicio;fecha_fin;alcance]\n"
                     + "delegacion modificar [id;resolucion;motivo;fecha_inicio;fecha_fin;alcance;estado]\n"
                     + "delegacion eliminar [id]\n"
                     + "delegacion listar";
            case "indicador":
                return "indicador agregar [codigo;nombre;descripcion;meta;unidad;periodo]\n"
                     + "indicador modificar [id;meta;valor_actual;estado]\n"
                     + "indicador listar";
            case "tramite":
                return "tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]\n"
                     + "tramite modificar [id;asunto;prioridad;estado]\n"
                     + "tramite eliminar [id]\n"
                     + "tramite listar";
            case "documento":
                return "documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]\n"
                     + "documento validar [id_documento;estado_validacion;observacion]\n"
                     + "documento listar";
            case "tipo_tramite":
                return "tipo_tramite agregar [codigo;nombre;descripcion;tiempo_sla;costo]\n"
                     + "tipo_tramite listar";
            case "flujo":
                return "flujo avanzar [id_tramite;etapa_actual;id_usuario_responsable]\n"
                     + "flujo listar";
            case "guia":
                return "guia mostrar [id_tipo_tramite]\n"
                     + "guia agregar [id_tipo_tramite;paso_numero;nombre_etapa;descripcion;rol;horas]\n"
                     + "guia listar";
            case "observacion":
                return "observacion agregar [id_tramite;id_usuario;tipo_observacion;descripcion]\n"
                     + "observacion subsanar [id_observacion;descripcion_subsanacion]\n"
                     + "observacion listar";
            case "bitacora":
                return "bitacora consultar [id_tramite]\n"
                     + "bitacora agregar [id_tramite;id_usuario;accion;detalle]\n"
                     + "bitacora listar";
            case "firma":
                return "firma consolidada [id_tramite;id_firmante;numero_certificado]\n"
                     + "firma remota [id_tramite;id_firmante;numero_certificado]\n"
                     + "firma listar";
            case "despacho":
                return "despacho agregar [id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]\n"
                     + "despacho listar";
            case "certificado":
                return "certificado generar [id_tramite;id_firma;titulo]\n"
                     + "certificado consultar [codigo_verificacion]\n"
                     + "certificado listar";
            case "portal":
                return "portal consultar [pin_seguimiento]\n"
                     + "portal habilitar [id_tramite;pin_seguimiento]\n"
                     + "portal listar";
            case "sla":
                return "sla verificar [id_tramite]\n"
                     + "sla listar";
            case "alerta":
                return "alerta agregar [id_tramite;tipo_alerta;mensaje;notificado_a]\n"
                     + "alerta atender [id_alerta]\n"
                     + "alerta listar";
            case "reporte":
                return "reporte tramites | reporte sla | reporte firmas";
            default:
                return "<caso_de_uso> <accion> [parametros]";
        }
    }

    private static String obtenerEjemploCasoUso(String casoUso) {
        if (casoUso == null) return "help";
        switch (casoUso.toLowerCase()) {
            case "usuario":
                return "usuario agregar [Juan;Perez;4567890;219064748;juan@alu.uagrm.edu.bo;ALUMNO;71000004;FICCT;Ingenieria Informatica]";
            case "delegacion":
                return "delegacion agregar [1;2;R-VR-012/2026;Viaje academico;2026-10-15;2026-10-25;TOTAL]";
            case "indicador":
                return "indicador agregar [IND-01;Tasa Tramites 72h;Porcentaje cumplido;95.00;PORCENTAJE;MENSUAL]";
            case "tramite":
                return "tramite agregar [2026001;4;1;Solicitud de Certificado de Notas;NORMAL]";
            case "documento":
                return "documento agregar [1;1;Cedula_Identidad.pdf;PDF;http://docs.uagrm.edu.bo/1/ci.pdf]";
            case "tipo_tramite":
                return "tipo_tramite agregar [TT-01;Certificado de Calificaciones;Emision oficial firmada;72;50.00]";
            case "flujo":
                return "flujo avanzar [1;REVISION_LEGAL;2]";
            case "guia":
                return "guia mostrar [1]";
            case "observacion":
                return "observacion agregar [1;2;FALTA_REQUISITO;Falta fotocopia legalizada de CI]";
            case "bitacora":
                return "bitacora consultar [1]";
            case "firma":
                return "firma consolidada [1;1;CERT-ADSIB-2026-991]";
            case "despacho":
                return "despacho agregar [1;2;DESP-2026-001;DIGITAL_CORREO]";
            case "certificado":
                return "certificado generar [1;1;Certificado Oficial de Calificaciones]";
            case "portal":
                return "portal consultar [PIN-8841]";
            case "sla":
                return "sla verificar [1]";
            case "alerta":
                return "alerta agregar [1;ADVERTENCIA_24H;Vence en menos de 24 horas;encargado@uagrm.edu.bo]";
            case "reporte":
                return "reporte tramites";
            default:
                return "help";
        }
    }

    public static void mostraRespuesta(String textoHtml) {
        System.out.println("Respuesta generada:");
        System.out.println(textoHtml);
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
                "CU4. Enrutamiento de Flujo, Guia, Observaciones y Bitacora:\n" +
                "  flujo help | flujo listar | guia help | observacion help | bitacora help\n\n" +
                "CU5. Firma Digital y Despacho:\n" +
                "  firma help | firma listar\n" +
                "  despacho help | despacho listar\n\n" +
                "CU6. Certificado Oficial con QR y Seguimiento:\n" +
                "  certificado help | certificado listar\n" +
                "  portal help | portal listar\n\n" +
                "CU7. Monitoreo SLA (72 Horas Habiles) y Alertas:\n" +
                "  sla help | sla listar | alerta help | alerta listar\n\n" +
                "CU8. Reportes y Estadisticas:\n" +
                "  reporte help | reporte tramites | reporte sla | reporte firmas";

        String htmlTabla = HtmlBuilder.generateHelpTable(titulo, comandos);
        respuesta.responseUser(email, htmlTabla);
        mostraRespuesta(htmlTabla);
    }
}
