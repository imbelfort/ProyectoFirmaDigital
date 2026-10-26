package Utils;

import Database.Conexion;
import Database.InicializarDB;
import Services.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Suite de Pruebas Automatizada Integral (CU1 a CU8)
 * Ejecuta y valida todos los comandos del catálogo contra la base de datos PostgreSQL.
 */
public class TestRunner {

    private static int exitosos = 0;
    private static int fallidos = 0;
    private static final List<String> errores = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("========================================================");
        System.out.println("   SUITE DE PRUEBAS DE COMANDOS POR CORREO (CU1 - CU8)  ");
        System.out.println("   Sistema de Firma Digital del Vicerrectorado (UAGRM)  ");
        System.out.println("========================================================\n");

        // [PASO 1] Probar conexión a PostgreSQL
        System.out.println("[PASO 1] Probando conexion a PostgreSQL...");
        Connection conn = Conexion.getInstancia().EstablecerConexion();
        if (conn == null) {
            System.err.println(">>> [ERROR FATAL] No se pudo conectar a la base de datos PostgreSQL.");
            System.err.println("    Asegurese de que PostgreSQL este corriendo localmente o en Docker.");
            System.exit(1);
        }
        System.out.println(">>> [EXITO] Conexion establecida con la base de datos.\n");

        // [PASO 2] Verificando e inicializando tablas y datos base
        System.out.println("[PASO 2] Verificando tablas y datos iniciales...");
        try {
            InicializarDB.crearTablasSiNoExisten();
            InicializarDB.insertarDatosIniciales();
            System.out.println(">>> [EXITO] Tablas y datos iniciales listos.\n");
        } catch (Exception e) {
            System.err.println(">>> [WARN] " + e.getMessage());
        }

        // [PASO 3] Instanciar Servicios
        UsuarioService usuarioService = new UsuarioService();
        DelegacionService delegacionService = new DelegacionService();
        IndicadorService indicadorService = new IndicadorService();
        TipoTramiteService tipoTramiteService = new TipoTramiteService();
        TramiteService tramiteService = new TramiteService();
        DocumentoService documentoService = new DocumentoService();
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

        System.out.println("[PASO 3] Ejecutando interpretacion de cada comando del catalogo (CU1 - CU8)...");
        System.out.println("--------------------------------------------------------");

        // ==========================================
        // CU1: GESTIÓN DE USUARIOS
        // ==========================================
        probar("usuario help", () -> usuarioService.getComandosUsuario());
        probar("usuario listar", () -> usuarioService.listarUsuarios());
        probar("usuario agregar", () -> {
            try {
                usuarioService.crearUsuario(Arrays.asList(
                    "Carlos", "Mendoza Test", "8823456", "9901002",
                    "cmendoza_test@uagrm.edu.bo", "ENCARGADO", "71000002",
                    "Despacho Vicerrectorado", "Encargado de Firma"
                ));
            } catch (Exception e) {
                if (!e.getMessage().contains("Ya existe")) throw e;
            }
        });
        probar("usuario modificar", () -> {
            usuarioService.actualizarUsuario(Arrays.asList(
                "1", "Reinerio", "Vargas", "1234567", "1001",
                "vicerrector@uagrm.edu.bo", "VICERRECTOR", "71000001",
                "Vicerrectorado", "Vicerrector General", "ACTIVO"
            ));
        });

        // ==========================================
        // CU2: DELEGACIÓN E INDICADORES
        // ==========================================
        probar("delegacion help", () -> delegacionService.getComandosDelegacion());
        probar("delegacion listar", () -> delegacionService.listarDelegaciones());
        probar("delegacion agregar", () -> {
            delegacionService.crearDelegacion(Arrays.asList(
                "1", "2", "RES-VR-012/2026", "Viaje academico CEUB",
                "2026-10-15", "2026-10-25", "TOTAL"
            ));
        });
        probar("delegacion modificar", () -> {
            delegacionService.modificarDelegacion(Arrays.asList(
                "1", "RES-VR-012/2026", "Ampliacion comision",
                "2026-10-15", "2026-10-30", "TOTAL", "ACTIVA"
            ));
        });
        probar("indicador help", () -> indicadorService.getComandosIndicador());
        probar("indicador listar", () -> indicadorService.listarIndicadores());
        probar("indicador agregar", () -> {
            try {
                indicadorService.crearIndicador(Arrays.asList(
                    "IND-TEST-" + System.currentTimeMillis() % 10000,
                    "Cumplimiento SLA Test", "Metas 72h", "95.00", "0.0", "PORCENTAJE", "MENSUAL"
                ));
            } catch (Exception e) {
                if (!e.getMessage().contains("Ya existe")) throw e;
            }
        });

        // ==========================================
        // CU3: TRÁMITES Y EXPEDIENTE
        // ==========================================
        probar("tipo_tramite help", () -> tipoTramiteService.getComandosTipoTramite());
        probar("tipo_tramite listar", () -> tipoTramiteService.listarTipos());
        probar("tipo_tramite agregar", () -> {
            try {
                tipoTramiteService.crearTipo(Arrays.asList(
                    "TRAM_TEST_" + (System.currentTimeMillis() % 1000),
                    "Diploma Test", "Diploma de Grado", "72", "350.00"
                ));
            } catch (Exception e) {
                if (!e.getMessage().contains("Ya existe")) throw e;
            }
        });
        probar("tramite help", () -> tramiteService.getComandosTramite());
        probar("tramite listar", () -> tramiteService.listarTramites());
        final String codTramite = "TR-" + (System.currentTimeMillis() % 100000);
        probar("tramite agregar", () -> {
            tramiteService.crearTramite(Arrays.asList(
                codTramite, "4", "1", "Solicitud Diploma Academico Licenciatura", "NORMAL"
            ));
        });
        probar("tramite modificar", () -> {
            tramiteService.modificarTramite(Arrays.asList(
                "1", "Solicitud Diploma Academico Modificado", "URGENTE", "INICIADO"
            ));
        });
        probar("documento help", () -> documentoService.getComandosDocumento());
        probar("documento listar", () -> documentoService.listarDocumentos());
        probar("documento agregar", () -> {
            documentoService.crearDocumento(Arrays.asList(
                "1", "1", "Cedula_Identidad.pdf", "PDF", "http://docs.uagrm.edu.bo/1/ci.pdf"
            ));
        });
        probar("documento validar", () -> {
            List<String[]> docs = documentoService.listarDocumentos();
            String docId = docs.isEmpty() ? "1" : docs.get(0)[0];
            documentoService.validarDocumento(Arrays.asList(
                docId, "APROBADO", "Documento legalizado conforme"
            ));
        });

        // ==========================================
        // CU4: FLUJO, GUÍA, OBSERVACIÓN Y BITÁCORA
        // ==========================================
        probar("flujo help", () -> flujoService.getComandosFlujo());
        probar("flujo listar", () -> flujoService.listarFlujo());
        probar("flujo avanzar", () -> {
            flujoService.avanzarFlujo(Arrays.asList(
                "1", "REVISION_LEGAL", "2"
            ));
        });
        probar("guia help", () -> guiaService.getComandosGuia());
        probar("guia listar", () -> guiaService.listarGuias());
        probar("guia mostrar", () -> guiaService.mostrarGuiaPorTipo(Collections.singletonList("1")));
        probar("guia agregar", () -> {
            guiaService.crearGuia(Arrays.asList(
                "1", "1", "Recepcion", "Verificar documentos completos en PDF", "ENCARGADO", "12"
            ));
        });
        probar("observacion help", () -> observacionService.getComandosObservacion());
        probar("observacion listar", () -> observacionService.listarObservaciones());
        probar("observacion agregar", () -> {
            observacionService.crearObservacion(Arrays.asList(
                "1", "2", "FALTA_REQUISITO", "Falta fotocopia legalizada de CI"
            ));
        });
        probar("observacion subsanar", () -> {
            List<String[]> obsList = observacionService.listarObservaciones();
            String obsId = obsList.isEmpty() ? "1" : obsList.get(0)[0];
            observacionService.subsanarObservacion(Arrays.asList(
                obsId, "Se adjunto la fotocopia requerida en PDF"
            ));
        });
        probar("bitacora help", () -> bitacoraService.getComandosBitacora());
        probar("bitacora listar", () -> bitacoraService.listarBitacora());
        probar("bitacora consultar", () -> bitacoraService.consultarPorTramite(Collections.singletonList("1")));

        // ==========================================
        // CU5: FIRMA DIGITAL Y DESPACHO
        // ==========================================
        probar("firma help", () -> firmaService.getComandosFirma());
        probar("firma listar", () -> firmaService.listarFirmas());
        probar("firma consolidada", () -> {
            firmaService.ejecutarFirma(Arrays.asList("1", "1", "CERT-ADSIB-2026-991"), "CONSOLIDADA");
        });
        probar("despacho help", () -> despachoService.getComandosDespacho());
        probar("despacho listar", () -> despachoService.listarDespachos());
        probar("despacho agregar", () -> {
            despachoService.registrarDespacho(Arrays.asList(
                "1", "2", "DESP-" + (System.currentTimeMillis() % 10000), "DIGITAL_CORREO"
            ));
        });

        // ==========================================
        // CU6: CERTIFICADO QR Y PORTAL
        // ==========================================
        probar("certificado help", () -> certificadoService.getComandosCertificado());
        probar("certificado listar", () -> certificadoService.listarCertificados());
        probar("certificado generar", () -> {
            List<String[]> firmas = firmaService.listarFirmas();
            String firmaId = firmas.isEmpty() ? "1" : firmas.get(0)[0];
            certificadoService.generarCertificado(Arrays.asList("1", firmaId, "Diploma Academico Oficial"));
        });
        probar("portal help", () -> portalService.getComandosPortal());
        probar("portal listar", () -> portalService.listarPortales());
        probar("portal habilitar", () -> {
            portalService.habilitarPortal(Arrays.asList("1", "PIN-8841"));
        });
        probar("portal consultar", () -> {
            portalService.consultarPorPin("PIN-8841");
        });

        // ==========================================
        // CU7: MONITOREO SLA 72H Y ALERTAS
        // ==========================================
        probar("sla help", () -> slaService.getComandosSla());
        probar("sla listar", () -> slaService.listarMonitoreo());
        probar("sla verificar", () -> {
            slaService.verificarSla(Arrays.asList("1"));
        });
        probar("alerta help", () -> alertaService.getComandosAlerta());
        probar("alerta listar", () -> alertaService.listarAlertas());
        probar("alerta agregar", () -> {
            alertaService.crearAlerta(Arrays.asList(
                "1", "ADVERTENCIA_24H", "El tramite vence en 24h habiles", "encargado@uagrm.edu.bo"
            ));
        });
        probar("alerta atender", () -> {
            alertaService.atenderAlerta(Arrays.asList("1"));
        });

        // ==========================================
        // CU8: REPORTES Y ESTADÍSTICAS
        // ==========================================
        probar("reporte help", () -> reporteService.getComandosReporte());
        probar("reporte tramites", () -> reporteService.reporteTramitesPorEstado());
        probar("reporte sla", () -> reporteService.reporteCumplimientoSla());
        probar("reporte firmas", () -> reporteService.reporteFirmasPorVicerrector());

        System.out.println("--------------------------------------------------------");
        System.out.println("========================================================");
        System.out.println("   RESUMEN FINAL DE PRUEBAS DE COMANDOS:");
        System.out.println("   Exitosos: " + exitosos + " / " + (exitosos + fallidos));
        System.out.println("   Fallidos: " + fallidos);
        System.out.println("========================================================");

        if (fallidos > 0) {
            System.err.println("\nDETALLE DE FALLAS ENCONTRADAS:");
            for (String err : errores) {
                System.err.println(" • " + err);
            }
            System.exit(1);
        } else {
            System.out.println("¡TODOS LOS COMANDOS Y CASOS DE USO (CU1-CU8) FUNCIONAN CORRECTAMENTE!");
        }
    }

    private static void probar(String nombreComando, EjecutorPrueba ejecutor) {
        try {
            ejecutor.ejecutar();
            System.out.println("  ✓ [OK] " + nombreComando);
            exitosos++;
        } catch (Throwable t) {
            System.err.println("  ✗ [FAIL] " + nombreComando + " -> " + t.getMessage());
            errores.add(nombreComando + " (" + t.getClass().getSimpleName() + ": " + t.getMessage() + ")");
            fallidos++;
        }
    }

    @FunctionalInterface
    private interface EjecutorPrueba {
        void ejecutar() throws Throwable;
    }
}
