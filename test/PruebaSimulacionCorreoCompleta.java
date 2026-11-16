package test;

import Comunication.Home;
import Database.Conexion;
import Database.ConfigDB;
import Database.ConfigEmailServer;
import Database.InicializarDB;
import Utils.Email;
import Utils.Extractor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PruebaSimulacionCorreoCompleta {

    private static final String SMTP_HOST = ConfigEmailServer.getHost();
    private static final int SMTP_PORT = Integer.parseInt(ConfigEmailServer.getPortSmtp());
    private static final String POP_HOST = ConfigEmailServer.getHost();
    private static final int POP_PORT = Integer.parseInt(ConfigEmailServer.getPortPop());

    private static final String SERVER_USER = ConfigEmailServer.USER;
    private static final String SERVER_PASS = ConfigEmailServer.PASSWORD;
    private static final String SERVER_EMAIL = ConfigEmailServer.MAIL;

    private static final String SENDER_GMAIL = "estudiante.vicerrectorado@gmail.com";

    private static int totalPruebas = 0;
    private static int pruebasExitosas = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("   SUITE DE SIMULACION E2E: ENVIO DE CORREOS GMAIL Y RESPUESTAS DEL SISTEMA");
        System.out.println("   Sistema de Firma Digital del Vicerrectorado (UAGRM - Grupo 26 SA)     ");
        System.out.println("==========================================================================\n");

        System.out.println("[CONFIGURACION INICIAL]");
        System.out.println("  * Host Correo       : " + SMTP_HOST + " (SMTP:" + SMTP_PORT + " / POP3:" + POP_PORT + ")");
        System.out.println("  * Servidor Casilla  : " + SERVER_EMAIL);
        System.out.println("  * Remitente Simulado: " + SENDER_GMAIL);
        System.out.println("  * Base de Datos     : " + ConfigDB.getUrl() + ConfigDB.DB + " (Usuario: " + ConfigDB.USER + ")\n");

        System.out.print("  [1/2] Verificando conexion a PostgreSQL... ");
        Connection conn = Conexion.getInstancia().EstablecerConexion();
        if (conn == null) {
            System.out.println("FALLO. No se pudo conectar a PostgreSQL.");
            System.exit(1);
        }
        System.out.println("OK");

        System.out.print("  [2/2] Asegurando tablas y datos iniciales... ");
        try {
            InicializarDB.crearTablasSiNoExisten();
            InicializarDB.insertarDatosIniciales();
            System.out.println("OK\n");
        } catch (Exception e) {
            System.out.println("WARN: " + e.getMessage());
        }

        // Limpiar cualquier residuo previo en los buzones POP3
        limpiarBuzonPOP3(SERVER_USER, SERVER_PASS);
        limpiarBuzonPOP3(SENDER_GMAIL, SENDER_GMAIL);

        System.out.println("==========================================================================");
        System.out.println("                  INICIANDO ESCENARIOS DE PRUEBA                          ");
        System.out.println("==========================================================================\n");

        probarComandoE2E(
                "CU0. Ayuda General",
                "help",
                "Consulta de manual de comandos por correo",
                "Sistema de Firma Digital del Vicerrectorado - Grupo 26 SA"
        );

        probarComandoE2E(
                "CU1. Listado de Usuarios",
                "usuario listar",
                "Consulta del catálogo de usuarios registrados",
                "Lista de Usuarios"
        );

        probarComandoE2E(
                "CU3. Listado de Trámites",
                "tramite listar",
                "Consulta de trámites registrados en el sistema",
                "Lista de Tramites Academicos"
        );

        String randomSuffix = String.valueOf(System.currentTimeMillis() % 100000);
        String comandoNuevoUsuario = "usuario agregar [Fernanda;Salazar;88" + randomSuffix + ";2210" + randomSuffix + ";fsalazar" + randomSuffix + "@gmail.com;ALUMNO;72199999;Facultad de Tecnologia;Ingenieria de Sistemas]";
        probarComandoE2E(
                "CU1. Registro de Nuevo Usuario",
                comandoNuevoUsuario,
                "Alta de alumno solicitante",
                "Usuario agregado exitosamente"
        );

        probarComandoE2E(
                "CU3. Consulta de Tipos de Trámite",
                "tipo_tramite listar",
                "Consulta de tipos de trámite y requisitos arancelarios",
                "Catalogo de Tipos de Tramite"
        );

        probarComandoE2E(
                "CU4. Guía Procesal de Requisitos",
                "guia help",
                "Consulta de pasos y normativa de trámites",
                "Comandos Guia Procesal"
        );

        probarComandoE2E(
                "CU7. Monitoreo de Plazos SLA",
                "sla listar",
                "Supervisión del SLA de 72 horas hábiles",
                "Monitoreo de Tiempos"
        );

        probarComandoE2E(
                "CU8. Reportes Estadísticos",
                "reporte tramites",
                "Estadísticas consolidadas de trámites",
                "Reporte de Tramites por Estado"
        );

        probarComandoE2E(
                "VALIDACION. Diagnóstico de Error por Tipeo (Typo)",
                "usuario listr",
                "Manejo inteligente de error léxico con sugerencia",
                "listr"
        );

        probarComandoE2E(
                "SEGURIDAD. Detección de Inyección de Comandos",
                "usuario listar <script>alert(1)</script>",
                "Bloqueo de caracteres peligrosos y protección del sistema",
                "caracteres especiales no permitidos"
        );

        System.out.println("\n==========================================================================");
        System.out.println("   RESUMEN FINAL DE PRUEBAS DE SIMULACION E2E:                            ");
        System.out.println("   Total Pruebas:    " + totalPruebas);
        System.out.println("   Pruebas Exitosas: " + pruebasExitosas);
        System.out.println("   Pruebas Fallidas: " + (totalPruebas - pruebasExitosas));
        System.out.println("==========================================================================");

        if (pruebasExitosas == totalPruebas) {
            System.out.println(">>> [EXITO TOTAL] Todos los correos enviados desde Gmail fueron procesados");
            System.out.println("    correctamente por el servidor y las respuestas HTML fueron entregadas!");
        } else {
            System.out.println(">>> [FALLO] Hubo pruebas que no completaron el ciclo.");
            System.exit(1);
        }
    }

    private static void probarComandoE2E(String nombreCaso, String subjectComando, String descripcion, String palabraClaveEsperada) {
        totalPruebas++;
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("TEST #" + totalPruebas + ": [" + nombreCaso + "]");
        System.out.println("  Descripción : " + descripcion);
        System.out.println("  Comando en Asunto : '" + subjectComando + "'");
        System.out.println("  Flujo : Gmail (" + SENDER_GMAIL + ") -> " + SERVER_EMAIL + " -> Proceso -> Gmail");

        try {
            // Limpiar Gmail antes del envío
            limpiarBuzonPOP3(SENDER_GMAIL, SENDER_GMAIL);

            // Paso A: Inyectar correo via SMTP a la casilla del servidor
            inyectarCorreoSMTP(SENDER_GMAIL, SERVER_EMAIL, subjectComando, "Contenido de prueba");

            // Paso B: Leer el correo desde POP3 en la casilla del servidor (con DELE)
            List<Email> correosEntrantes = leerYVaciarPOP3(SERVER_USER, SERVER_PASS);
            if (correosEntrantes.isEmpty()) {
                System.out.println("  [ERROR] El correo no fue encontrado en el buzón POP3 del servidor.");
                return;
            }

            Email correo = correosEntrantes.get(0);

            // Paso C: Procesar en el núcleo del sistema (Home.interprete)
            Home.interprete(correo);

            // Paso D: Esperar y leer el buzón del usuario Gmail (reintentar hasta 10 segundos)
            List<String> respuestasGmail = leerYEsperarContenidoPOP3(SENDER_GMAIL, SENDER_GMAIL, 20);
            if (respuestasGmail.isEmpty()) {
                System.out.println("  [ERROR] No se recibió respuesta en la casilla de Gmail del usuario.");
                return;
            }

            String ultimaRespuesta = respuestasGmail.get(respuestasGmail.size() - 1);

            // Paso E: Validar que la respuesta contiene lo esperado
            if (ultimaRespuesta.contains(palabraClaveEsperada)) {
                System.out.println("  [OK] Respuesta recibida en Gmail exitosamente.");
                System.out.println("  [OK] Validación confirmada: Contiene '" + palabraClaveEsperada + "'.");
                pruebasExitosas++;
            } else {
                System.out.println("  [FALLO] La respuesta no contiene el texto esperado: '" + palabraClaveEsperada + "'");
                System.out.println("  Fragmento recibido: " + ultimaRespuesta.substring(0, Math.min(300, ultimaRespuesta.length())));
            }

        } catch (Exception e) {
            System.out.println("  [EXCEPCION] Error durante el flujo E2E: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inyectarCorreoSMTP(String from, String to, String subject, String body) throws Exception {
        Socket socket = new Socket(SMTP_HOST, SMTP_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        readSmtpResponse(reader); // Banner
        writer.writeBytes("HELO localhost\r\n");
        writer.flush();
        readSmtpResponse(reader); // HELO

        writer.writeBytes("MAIL FROM:<" + from + ">\r\n");
        writer.flush();
        readSmtpResponse(reader); // MAIL FROM

        writer.writeBytes("RCPT TO:<" + to + ">\r\n");
        writer.flush();
        readSmtpResponse(reader); // RCPT TO

        writer.writeBytes("DATA\r\n");
        writer.flush();
        readSmtpResponse(reader); // DATA

        StringBuilder msg = new StringBuilder();
        msg.append("From: <").append(from).append(">\r\n");
        msg.append("To: <").append(to).append(">\r\n");
        msg.append("Subject: ").append(subject).append("\r\n");
        msg.append("MIME-Version: 1.0\r\n");
        msg.append("Content-Type: text/plain; charset=UTF-8\r\n");
        msg.append("\r\n");
        msg.append(body).append("\r\n");
        msg.append(".\r\n");

        writer.writeBytes(msg.toString());
        writer.flush();
        readSmtpResponse(reader); // End of DATA

        writer.writeBytes("QUIT\r\n");
        writer.flush();
        readSmtpResponse(reader); // QUIT

        socket.close();
    }

    private static List<Email> leerYVaciarPOP3(String user, String pass) throws Exception {
        List<Email> emails = new ArrayList<>();
        Socket socket = new Socket(POP_HOST, POP_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        reader.readLine(); // Banner
        writer.writeBytes("USER " + user + "\r\n");
        writer.flush();
        reader.readLine();

        writer.writeBytes("PASS " + pass + "\r\n");
        writer.flush();
        reader.readLine();

        writer.writeBytes("STAT\r\n");
        writer.flush();
        String stat = reader.readLine();

        int count = 0;
        if (stat != null && stat.startsWith("+OK")) {
            String[] parts = stat.split("\\s+");
            if (parts.length >= 2) {
                count = Integer.parseInt(parts[1]);
            }
        }

        for (int i = 1; i <= count; i++) {
            writer.writeBytes("RETR " + i + "\r\n");
            writer.flush();
            reader.readLine(); // +OK

            StringBuilder content = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null || ".".equals(line)) {
                    break;
                }
                content.append(line).append("\n");
            }

            Email email = Extractor.getEmail(content.toString());
            emails.add(email);

            writer.writeBytes("DELE " + i + "\r\n");
            writer.flush();
            reader.readLine();
        }

        writer.writeBytes("QUIT\r\n");
        writer.flush();
        reader.readLine();

        socket.close();
        return emails;
    }

    private static List<String> leerYEsperarContenidoPOP3(String user, String pass, int maxRetries) throws Exception {
        for (int retry = 0; retry < maxRetries; retry++) {
            List<String> list = leerYVaciarContenidoPOP3(user, pass);
            if (!list.isEmpty()) {
                return list;
            }
            Thread.sleep(500);
        }
        return new ArrayList<>();
    }

    private static List<String> leerYVaciarContenidoPOP3(String user, String pass) throws Exception {
        List<String> list = new ArrayList<>();
        Socket socket = new Socket(POP_HOST, POP_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        reader.readLine(); // Banner
        writer.writeBytes("USER " + user + "\r\n");
        writer.flush();
        reader.readLine();

        writer.writeBytes("PASS " + pass + "\r\n");
        writer.flush();
        reader.readLine();

        writer.writeBytes("STAT\r\n");
        writer.flush();
        String stat = reader.readLine();

        int count = 0;
        if (stat != null && stat.startsWith("+OK")) {
            String[] parts = stat.split("\\s+");
            if (parts.length >= 2) {
                count = Integer.parseInt(parts[1]);
            }
        }

        for (int i = 1; i <= count; i++) {
            writer.writeBytes("RETR " + i + "\r\n");
            writer.flush();
            reader.readLine(); // +OK

            StringBuilder content = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null || ".".equals(line)) {
                    break;
                }
                content.append(line).append("\n");
            }
            list.add(content.toString());

            writer.writeBytes("DELE " + i + "\r\n");
            writer.flush();
            reader.readLine();
        }

        writer.writeBytes("QUIT\r\n");
        writer.flush();
        reader.readLine();

        socket.close();
        return list;
    }

    private static void limpiarBuzonPOP3(String user, String pass) {
        try {
            Socket socket = new Socket(POP_HOST, POP_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            reader.readLine(); // Banner
            writer.writeBytes("USER " + user + "\r\n");
            writer.flush();
            reader.readLine();

            writer.writeBytes("PASS " + pass + "\r\n");
            writer.flush();
            reader.readLine();

            writer.writeBytes("STAT\r\n");
            writer.flush();
            String stat = reader.readLine();

            int count = 0;
            if (stat != null && stat.startsWith("+OK")) {
                String[] parts = stat.split("\\s+");
                if (parts.length >= 2) {
                    count = Integer.parseInt(parts[1]);
                }
            }

            for (int i = 1; i <= count; i++) {
                writer.writeBytes("DELE " + i + "\r\n");
                writer.flush();
                reader.readLine();
            }

            writer.writeBytes("QUIT\r\n");
            writer.flush();
            reader.readLine();

            socket.close();
        } catch (Exception ignored) {
        }
    }

    private static String readSmtpResponse(BufferedReader reader) throws Exception {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            sb.append(line).append("\n");
            if (line.length() >= 4 && line.charAt(3) == ' ') {
                break;
            } else if (line.length() < 4) {
                break;
            }
        }
        return sb.toString().trim();
    }
}