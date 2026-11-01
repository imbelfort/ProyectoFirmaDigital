package test;

import Comunication.TokenEvent;
import Interfaces.ITokenCasoUsoEventListener;
import Utils.CommandException;
import Utils.ErrorCode;
import Utils.Interpreter;
import Utils.ParamValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PruebaValidacionesSeguridadYErrores {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("  TEST SUITE: VALIDACIONES DE SEGURIDAD, CARACTERES Y ERRORES CLAROS     ");
        System.out.println("  Sistema de Firma Digital del Vicerrectorado (UAGRM - Grupo 26 SA)      ");
        System.out.println("=========================================================================\n");

        testCaracteresPeligrosos();
        testCaracteresSegurosPermitidos();
        testErroresClarosYDiagnosticosTipeo();
        testValidacionesDeNegocioYFormatos();

        System.out.println("\n=========================================================================");
        System.out.println("  RESULTADOS: Total=" + totalTests + " | Exitosos=" + passedTests + " | Fallidos=" + failedTests);
        System.out.println("=========================================================================");

        if (failedTests == 0) {
            System.out.println("¡TODAS LAS PRUEBAS DE VALIDACIÓN Y MANEJO DE ERRORES PASARON CON ÉXITO!");
        } else {
            System.err.println("ALERTA: Algunos casos no pasaron la validacion.");
            System.exit(1);
        }
    }

    private static void testCaracteresPeligrosos() {
        System.out.println("--- 1. CARACTERES ESPECIALES PELIGROSOS (NO DEBEN ROMPER EL FLUJO) ---");

        assertInterpreterError(
            "Inyección HTML en comando",
            "usuario listar <script>alert('xss')</script>",
            ErrorCode.ERR_CMD_004
        );

        assertInterpreterError(
            "Intento de pipe command injection",
            "tramite listar | dir",
            ErrorCode.ERR_CMD_004
        );

        assertInterpreterError(
            "Caracteres extraños en comando (#)",
            "usuario #listar",
            ErrorCode.ERR_CMD_004
        );

        assertInterpreterError(
            "Caracteres extraños en comando ($)",
            "documento $listar",
            ErrorCode.ERR_CMD_004
        );

        assertInterpreterError(
            "Control char nulo en asunto",
            "usuario listar \u0000",
            ErrorCode.ERR_CMD_004
        );
    }

    private static void testCaracteresSegurosPermitidos() {
        System.out.println("\n--- 2. CARACTERES SEGUROS PERMITIDOS (NO DEBEN FALLAR EN ANALEX) ---");

        assertInterpreterNoError(
            "Comando con corchetes y punto y coma en parámetros",
            "usuario agregar [219064748;1045236;Juan;Perez;juan@uagrm.edu.bo;ALUMNO]"
        );

        assertInterpreterNoError(
            "Número con signo negativo",
            "usuario consultar [-1]"
        );

        assertInterpreterNoError(
            "Ruta con barras y puntos en archivo_url",
            "documento agregar [1;1;/expedientes/2026/titulo.pdf]"
        );

        assertInterpreterNoError(
            "Texto con comillas y paréntesis en asunto",
            "tramite agregar [1001;1;Solicitud de (Diploma) \"Urgente\";NORMAL]"
        );
    }

    private static void testErroresClarosYDiagnosticosTipeo() {
        System.out.println("\n--- 3. ERRORES CLAROS Y DIAGNÓSTICO ANTE ERRORES DE TIPEO ---");

        assertInterpreterError(
            "Typo en entidad: usuari -> sugiere usuario",
            "usuari listar",
            ErrorCode.ERR_CMD_002
        );

        assertInterpreterError(
            "Typo en entidad: tramit -> sugiere tramite",
            "tramit listar",
            ErrorCode.ERR_CMD_002
        );

        assertInterpreterError(
            "Typo en entidad: documeto -> sugiere documento",
            "documeto listar",
            ErrorCode.ERR_CMD_002
        );

        assertInterpreterError(
            "Typo en acción: listr -> sugiere listar",
            "usuario listr",
            ErrorCode.ERR_CMD_003
        );

        assertInterpreterError(
            "Typo en acción: agragar -> sugiere agregar",
            "tramite agragar [1;2;Asunto;NORMAL]",
            ErrorCode.ERR_CMD_003
        );

        assertInterpreterError(
            "Acción inexistente para la entidad: usuario cocinar",
            "usuario cocinar",
            ErrorCode.ERR_CMD_003
        );

        assertInterpreterError(
            "Orden invertido: listar usuario",
            "listar usuario",
            ErrorCode.ERR_ORD_001
        );

        assertInterpreterError(
            "Orden invertido: consultar tramite [1]",
            "consultar tramite [1]",
            ErrorCode.ERR_ORD_001
        );

        assertInterpreterError(
            "Parámetro antes de entidad: 1 usuario consultar",
            "1 usuario consultar",
            ErrorCode.ERR_ORD_002
        );

        assertInterpreterError(
            "Parámetro antes de acción: usuario 1 consultar",
            "usuario 1 consultar",
            ErrorCode.ERR_ORD_003
        );

        assertInterpreterError(
            "Orden incompleta: entidad sola (usuario)",
            "usuario",
            ErrorCode.ERR_INC_001
        );

        assertInterpreterError(
            "Orden incompleta: acción sola (listar)",
            "listar",
            ErrorCode.ERR_INC_001
        );
    }

    private static void testValidacionesDeNegocioYFormatos() {
        System.out.println("\n--- 4. VALIDACIONES DE NEGOCIO Y FORMATOS (PARAMVALIDATOR) ---");

        // 1. Email inválido
        totalTests++;
        try {
            List<String> params = Arrays.asList("sin_arroba_ni_dominio");
            ParamValidator.requireEmail(params, 0, "email", "usuario agregar [...]", "usuario@uagrm.edu.bo");
            System.err.println("  ✗ [FAIL] Email sin arroba -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_005.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Email sin arroba -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Email sin arroba -> Código esperado ERR-FMT-005, recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 2. Código con símbolos raros
        totalTests++;
        try {
            List<String> params = Arrays.asList("REG#99*");
            ParamValidator.requireNumericCode(params, 0, "registro", "usuario agregar [...]", "219064748");
            System.err.println("  ✗ [FAIL] Código con símbolos raros -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_003.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Código con símbolos raros -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Código con símbolos -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 3. Texto con etiquetas HTML
        totalTests++;
        try {
            List<String> params = Arrays.asList("Juan <script>alert(1)</script>");
            ParamValidator.requireString(params, 0, "nombre", "usuario agregar [...]", "Juan");
            System.err.println("  ✗ [FAIL] Texto con etiquetas HTML -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_CMD_004.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Texto con etiquetas HTML -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] HTML en texto -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 4. Fecha con barras en vez de guiones
        totalTests++;
        try {
            List<String> params = Arrays.asList("2026/10/25");
            ParamValidator.requireDate(params, 0, "fecha", "delegacion agregar [...]", "2026-10-25");
            System.err.println("  ✗ [FAIL] Fecha con barras -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_002.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Fecha con barras en vez de guiones -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Fecha con barras -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 5. Fecha inexistente 30 de febrero
        totalTests++;
        try {
            List<String> params = Arrays.asList("2026-02-30");
            ParamValidator.requireDate(params, 0, "fecha", "delegacion agregar [...]", "2026-02-28");
            System.err.println("  ✗ [FAIL] Fecha inexistente 30 de febrero -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_002.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Fecha inexistente 30 de febrero -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Fecha inexistente -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 6. Teléfono con letras
        totalTests++;
        try {
            List<String> params = Arrays.asList("+591-700-TELEFONO");
            ParamValidator.requireTelefono(params, 0, "telefono", "usuario agregar [...]", "+59170012345");
            System.err.println("  ✗ [FAIL] Teléfono con letras -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_001.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Teléfono con letras -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Teléfono con letras -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 7. ID no numérico
        totalTests++;
        try {
            List<String> params = Arrays.asList("abc");
            ParamValidator.requireInt(params, 0, "id_usuario", "usuario consultar [id]", "usuario consultar [1]");
            System.err.println("  ✗ [FAIL] ID no numérico -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_001.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] ID no numérico -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] ID no numérico -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }

        // 8. Rol fuera de catálogo
        totalTests++;
        try {
            List<String> params = Arrays.asList("SUPERHEROE");
            ParamValidator.requireRol(params, 0, "ALUMNO", "usuario agregar [...]", "ALUMNO");
            System.err.println("  ✗ [FAIL] Rol fuera del catálogo -> No arrojó CommandException.");
            failedTests++;
        } catch (CommandException e) {
            if (ErrorCode.ERR_FMT_004.equals(e.getErrorCode())) {
                System.out.println("  ✓ [OK] Rol fuera del catálogo -> [" + e.getErrorCode() + "] | Msg: " + trunc(e.getMessage()));
                passedTests++;
            } else {
                System.err.println("  ✗ [FAIL] Rol fuera del catálogo -> Código recibido: " + e.getErrorCode());
                failedTests++;
            }
        }
    }

    private static void assertInterpreterError(String testName, String command, String expectedErrorCode) {
        totalTests++;
        final List<String> capturedErrors = new ArrayList<>();
        final List<String> capturedSuggestions = new ArrayList<>();
        final List<String> capturedMsgs = new ArrayList<>();

        Interpreter interpreter = new Interpreter(command, "test@uagrm.edu.bo");
        interpreter.setListener(new DummyListener() {
            @Override
            public void error(TokenEvent event) {
                capturedErrors.add(event.getErrorCode());
                capturedSuggestions.add(event.getSuggestion());
                capturedMsgs.add(event.getParams().size() > 0 ? event.getParams().get(0) : "");
            }

            @Override
            public void posibleserrores(TokenEvent event) {
                capturedErrors.add(event.getErrorCode());
                capturedSuggestions.add(event.getSuggestion());
                capturedMsgs.add(event.getParams().size() > 0 ? event.getParams().get(0) : "");
            }
        });

        interpreter.run();

        if (!capturedErrors.isEmpty() && expectedErrorCode.equals(capturedErrors.get(0))) {
            System.out.println("  ✓ [OK] " + testName + " -> [" + capturedErrors.get(0) + "] | Msg: " + trunc(capturedMsgs.get(0)) + " | Sug: " + trunc(capturedSuggestions.get(0)));
            passedTests++;
        } else {
            String obt = capturedErrors.isEmpty() ? "Ninguno" : capturedErrors.get(0);
            System.err.println("  ✗ [FAIL] " + testName + " -> Esperaba: [" + expectedErrorCode + "], obtenido: [" + obt + "]");
            failedTests++;
        }
    }

    private static void assertInterpreterNoError(String testName, String command) {
        totalTests++;
        final List<String> capturedErrors = new ArrayList<>();

        Interpreter interpreter = new Interpreter(command, "test@uagrm.edu.bo");
        interpreter.setListener(new DummyListener() {
            @Override
            public void error(TokenEvent event) {
                capturedErrors.add(event.getErrorCode() != null ? event.getErrorCode() : "ERROR");
            }

            @Override
            public void posibleserrores(TokenEvent event) {
                capturedErrors.add(event.getErrorCode() != null ? event.getErrorCode() : "POSIBLES_ERRORES");
            }
        });

        interpreter.run();

        if (capturedErrors.isEmpty()) {
            System.out.println("  ✓ [OK] " + testName + " -> No rechazó caracteres válidos.");
            passedTests++;
        } else {
            System.err.println("  ✗ [FAIL] " + testName + " -> Rechazó caracteres válidos: " + capturedErrors);
            failedTests++;
        }
    }

    private static String trunc(String s) {
        if (s == null) return "null";
        s = s.replace("\n", " ").trim();
        return s.length() > 45 ? s.substring(0, 42) + "..." : s;
    }

    private static class DummyListener implements ITokenCasoUsoEventListener {
        @Override public void usuario(TokenEvent event) {}
        @Override public void delegacion(TokenEvent event) {}
        @Override public void indicador(TokenEvent event) {}
        @Override public void tramite(TokenEvent event) {}
        @Override public void documento(TokenEvent event) {}
        @Override public void tipoTramite(TokenEvent event) {}
        @Override public void flujo(TokenEvent event) {}
        @Override public void guia(TokenEvent event) {}
        @Override public void observacion(TokenEvent event) {}
        @Override public void bitacora(TokenEvent event) {}
        @Override public void firma(TokenEvent event) {}
        @Override public void despacho(TokenEvent event) {}
        @Override public void certificado(TokenEvent event) {}
        @Override public void portal(TokenEvent event) {}
        @Override public void sla(TokenEvent event) {}
        @Override public void alerta(TokenEvent event) {}
        @Override public void reporte(TokenEvent event) {}
        @Override public void posibleserrores(TokenEvent event) {}
        @Override public void error(TokenEvent event) {}
    }
}
