package Comunication;

import Interfaces.IEmailEventListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.sasl.AuthenticationException;
import Utils.Command;
import Utils.Email;
import Utils.Extractor;

public class MailVerificationThread implements Runnable {

    private final static int PORT_POP = 110;
    private final static String HOST = "mail.tecnoweb.org.bo";
    private final static String USER = "grupo20sa";
    private final static String PASSWORD = "grup020grup020*";

    private final static int CONNECT_TIMEOUT_MS = 5000;
    private final static int LOOP_DELAY_MS = 5000;

    /*
     * Cambia a true solo cuando quieras depurar.
     * En false no imprimirÃ¡ conexion establecida, conexion cerrada,
     * cantidad de correos ni estado del hilo a cada rato.
     */
    private final static boolean DEBUG = false;

    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;

    private IEmailEventListener emailEventListener;

    public IEmailEventListener getEmailEventListener() {
        return emailEventListener;
    }

    public void setEmailEventListener(IEmailEventListener emailEventListener) {
        this.emailEventListener = emailEventListener;
    }

    public MailVerificationThread() {
        socket = null;
        input = null;
        output = null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<Email> emails = null;

                socket = openSocket(HOST, PORT_POP, CONNECT_TIMEOUT_MS);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());

                debug("Conexion POP3 establecida");

                authUser(USER, PASSWORD);

                int count = getEmailCount();

                if (count > 0) {
                    emails = getEmails(count);
                    debug("Correos encontrados: " + count);
                    deleteEmails(count);
                }

                output.writeBytes(Command.quit());
                input.readLine();

                cerrarConexionPop3();

                debug("Conexion POP3 cerrada");

                if (count > 0 && emailEventListener != null) {
                    emailEventListener.onReceiveEmailEvent(emails);
                }

                Thread.sleep(LOOP_DELAY_MS);

            } catch (IOException ex) {
                Logger.getLogger(MailVerificationThread.class.getName()).log(Level.SEVERE, null, ex);
                cerrarConexionPop3();
                safeSleep(LOOP_DELAY_MS);

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                debug("Hilo interrumpido");
                break;
            }
        }
    }

    private Socket openSocket(String host, int port, int timeoutMs) throws IOException {
        Socket s = new Socket();
        s.connect(new InetSocketAddress(host, port), timeoutMs);
        return s;
    }

    private void authUser(String email, String password) throws IOException {
        if (socket != null && input != null && output != null) {
            input.readLine();

            output.writeBytes(Command.user(email));
            input.readLine();

            output.writeBytes(Command.pass(password));
            String message = input.readLine();

            if (message.contains("-ERR")) {
                throw new AuthenticationException();
            }
        }
    }

    private int getEmailCount() throws IOException {
        output.writeBytes(Command.stat());

        String line = input.readLine();
        String[] data = line.split(" ");

        return Integer.parseInt(data[1]);
    }

    private List<Email> getEmails(int count) throws IOException {
        List<Email> emails = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            output.writeBytes(Command.retr(i));

            String text = readMultiline();
            emails.add(Extractor.getEmail(text));
        }

        return emails;
    }

    private void deleteEmails(int emails) throws IOException {
        for (int i = 1; i <= emails; i++) {
            output.writeBytes(Command.dele(i));
        }
    }

    private String readMultiline() throws IOException {
        StringBuilder lines = new StringBuilder();

        boolean firstLine = true;

        while (true) {
            String line = input.readLine();

            if (line == null) {
                throw new IOException("Servidor no responde. Ocurrio un error al abrir el correo.");
            }

            if (line.equals(".")) {
                break;
            }

            if (!firstLine) {
                lines.append('\n');
            }

            lines.append(line);
            firstLine = false;
        }

        return lines.toString();
    }

    private void cerrarConexionPop3() {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ignored) {
        }

        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ignored) {
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }

        input = null;
        output = null;
        socket = null;
    }

    private void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private void debug(String mensaje) {
        if (DEBUG) {
            System.out.println(mensaje);
        }
    }
}