/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Comunication;

//import com.juanvladimir13.template.MaryTemplate;
import Utils.Email;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class SendEmail {

    private final static int PORT_SMTP = 25;
    private final static String SERVER = "mail.tecnoweb.org.bo";
    private final static String user_emisor = "grupo20sa@tecnoweb.org.bo";
    private static String user_receptor;

    public SendEmail() {
        user_receptor = "";

    }

    public String getUser_receptor() {
        return user_receptor;
    }

    public void setUser_receptor(String user_receptor) {
        SendEmail.user_receptor = user_receptor;
    }

    public void responseUser(Email email, String mensaje) {
        // Estableciendo variables
        BufferedReader reader;
        DataOutputStream writer;
        String command;
        System.out.println("Enviando correo de respuesta a: " + email.getFrom());

        try {
            // Estableciendo Conexion Socket
            Socket socket = new Socket(SERVER, PORT_SMTP);
            socket.setSoTimeout(15000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new DataOutputStream(socket.getOutputStream());

            if (socket != null && reader != null && writer != null) {
                System.out.println("SMTP: " + reader.readLine());

                // Saludar al servidor
                command = "HELO " + SERVER + "\r\n";
                writer.writeBytes(command);
                writer.flush();
                System.out.println("SMTP: " + reader.readLine());

                command = "MAIL FROM:" + user_emisor + "\r\n";
                command = "MAIL FROM:<" + user_emisor.trim() + ">\r\n";
                writer.writeBytes(command);
                writer.flush();
                System.out.println("SMTP: " + reader.readLine());

                String origen = email.getFrom();
                if (esEmailValido(origen)) {
                    command = "RCPT TO:<" + origen.trim() + ">\r\n";
                    writer.writeBytes(command);
                    writer.flush();
                    System.out.println("SMTP: " + reader.readLine());
                } else {
                    System.out.println("Correo de respuesta no enviado: no hay destinatario válido");
                    writer.close();
                    reader.close();
                    socket.close();
                    return;
                }

                // Escribir Mensaje
                // Comenzar DATA
                command = "DATA\r\n";
                writer.writeBytes(command);
                writer.flush();
                System.out.println("SMTP: " + reader.readLine());

                // Construir cabeceras mÃ­nimas vÃ¡lidas (From, To, Subject, MIME)
                StringBuilder data = new StringBuilder();
                data.append("From: <").append(user_emisor).append(">\r\n");

                // Reconstruir lista To a partir de los RCPT enviados (usar getEmailsReceptores + remitente)
                List<String> toList = new ArrayList<>();
                if (esEmailValido(email.getFrom())) toList.add(email.getFrom().trim());

                if (!toList.isEmpty()) {
                    data.append("To: ");
                    for (int i = 0; i < toList.size(); i++) {
                        if (i > 0) data.append(", ");
                        data.append("<").append(toList.get(i)).append(">");
                    }
                    data.append("\r\n");
                }

                // Sanitize subject: remove or replace any internal newlines to avoid header injection
                String subject = "";
                if (email.getSubject() != null) {
                    // Use \R to replace any Unicode linebreak sequences with a single space
                    subject = email.getSubject().toString().replaceAll("\\R+", " ").trim();
                }
                data.append("Subject: ").append(subject).append("\r\n");

                // Cuerpo: normalizar saltos de lÃ­nea a CRLF y aplicar dot-stuffing solo al cuerpo
                InlineEmailContent inlineContent = prepararContenidoInline(mensaje);

                String cuerpoSeguridad = normalizeForSmtp(inlineContent.htmlSanitizado)
                        .replace("\r\n.", "\r\n..");
                data.append("MIME-Version: 1.0\r\n");

                if (inlineContent.hasImages()) {
                    data.append("Content-Type: multipart/related; boundary=related-boundary-1\r\n");
                } else {
                    data.append("Content-Type: text/html; charset=\"UTF-8\"\r\n");
                }

                data.append("\r\n"); 

                if (inlineContent.hasImages()) {
                    data.append("--related-boundary-1\r\n");
                    data.append("Content-Type: text/html; charset=\"UTF-8\"\r\n");
                    data.append("Content-Transfer-Encoding: 8bit\r\n\r\n");
                    data.append(cuerpoSeguridad);
                    data.append("\r\n");

                    for (InlineImage inlineImage : inlineContent.imagenes) {
                        data.append("--related-boundary-1\r\n");
                        data.append("Content-Type: ").append(inlineImage.contentType).append("\r\n");
                        data.append("Content-Transfer-Encoding: base64\r\n");
                        data.append("Content-ID: <").append(inlineImage.cid).append(">\r\n");
                        data.append("Content-Disposition: inline; filename=\"").append(inlineImage.cid).append("\"\r\n\r\n");
                        data.append(inlineImage.base64).append("\r\n");
                    }
                    data.append("--related-boundary-1--\r\n");
                } else {
                    data.append(cuerpoSeguridad);
                    if (!cuerpoSeguridad.endsWith("\r\n")) data.append("\r\n");
                }

                data.append(".\r\n");

                String finalData = data.toString();

                writer.writeBytes(finalData);
                writer.flush();
                String dataResponse = reader.readLine();
                if (dataResponse != null) {
                    System.out.println("SMTP: " + dataResponse);
                } else {
                    System.out.println("Correo de respuesta no enviado: el servidor no respondiÃ³ despuÃ©s del cuerpo");
                }

                command = "QUIT\r\n";
                writer.writeBytes(command);
                writer.flush();
                String quitResponse = reader.readLine();
                if (quitResponse != null) {
                    System.out.println("SMTP: " + quitResponse);
                }

                System.out.println("Correo de respuesta enviado exitosamente a: " + email.getFrom());

            }

            // Cerrar Conexion
            writer.close();
            reader.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Correo de respuesta no enviado: no se pudo conectar con el servidor indicado");
        } catch (IOException ex) {
            System.out.println("Correo de respuesta no enviado: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Correo de respuesta no enviado: " + ex.getMessage());
        }
    }

    static protected String getMultiline(BufferedReader in) throws IOException {
        String lines = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Server closed connection
                throw new IOException(" S : Server unawares closed the connection.");
            }
            if (line.charAt(3) == ' ') {
                lines = lines + "\n" + line;
                // No more lines in the server response
                break;
            }
            // Add read line to the list of lines
            lines = lines + "\n" + line;
        }
        return lines;
    }
private boolean esEmailValido(String email) {
    return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
}

    ///////////////////////////////////////////////////////////////
    public void responseEmail(String emailReceptor, String codigoHTML) {
        try {
            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            p.put("mail.smtp.port", "587");
            p.put("mail.smtp.auth", "true");
            Session s = Session.getDefaultInstance(p);

            MimeMessage mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress("medinytalimber.123@gmail.com"));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceptor));
            mensaje.setSubject("NOTIFICACION");
            //mensaje.setText("informacion");
            mensaje.setContent(codigoHTML, "text/html");//
            Transport t = s.getTransport("smtp");
            t.connect("medinytalimber.123@gmail.com", "xgyxhcqqwxjkmydj");
            System.out.println("enviando...");
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();
            System.out.println("mensaje enviado");
        } catch (MessagingException ex) {
            Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("correo no enviado");
        }
    }

  public void sendmail(String receptor, String subject) {
    try {
        // ConfiguraciÃ³n SMTP
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.auth", "true");

        Session s = Session.getDefaultInstance(p);

        MimeMessage mensaje = new MimeMessage(s);
        mensaje.setFrom(new InternetAddress("medinytalimber.123@gmail.com"));
        mensaje.setSubject(subject);
        mensaje.setText("enviado");

        if (esEmailValido(receptor)) {
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor.trim()));
        } else {
            System.out.println("Email inválido: " + receptor);
            return;
        }

        // EnvÃ­o del mensaje
        Transport t = s.getTransport("smtp");
        t.connect("medinytalimber.123@gmail.com", "xgyxhcqqwxjkmydj");
        System.out.println("Enviando...");
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
        System.out.println("Mensaje enviado");

    } catch (MessagingException ex) {
        Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Correo no enviado");
    }
}


    public String mensajeHtml(String mensaje) {
        String html = "<h2>%s</h2>";
        return String.format(html, mensaje);
    }

    public String cargarHtml(String s) {
        String html = "<center>\n"
                + "  <br>\n" + s + "  <br>\n"
                + "</center>";
        return html;
    }

    public String[] getEmailsReceptores() {
        return new String[0];
    }

        // Normaliza saltos de lÃ­nea a CRLF y aplica dot-stuffing para DATA SMTP
        private String normalizeForSmtp(String s) {
            if (s == null) return "";
            // Normalize various line endings to LF first
            String tmp = s.replace("\r\n", "\n").replace("\r", "\n");
            // Convert LF to CRLF
            String crlf = tmp.replace("\n", "\r\n");
            // Dot-stuffing: lines that start with a dot must be prefixed with another dot
            if (crlf.startsWith(".")) crlf = "." + crlf;
            crlf = crlf.replace("\r\n.", "\r\n..");
            return crlf;
        }

        private InlineEmailContent prepararContenidoInline(String html) {
            InlineEmailContent content = new InlineEmailContent();

            if (html == null || html.isBlank()) {
                content.htmlSanitizado = "";
                return content;
            }

            Pattern pattern = Pattern.compile(
                    "<img([^>]*)src=[\"']data:(image/[^;]+);base64,([^\"']+)[\"']([^>]*)>",
                    Pattern.CASE_INSENSITIVE
            );

            Matcher matcher = pattern.matcher(html);
            StringBuffer sanitized = new StringBuffer();

            int contador = 0;

            while (matcher.find()) {
                String antesSrc = matcher.group(1);
                String contentType = matcher.group(2);
                String base64 = matcher.group(3);
                String despuesSrc = matcher.group(4);

                base64 = limpiarBase64(base64);

                String cid = "imagen_inline_" + contador;

                content.imagenes.add(new InlineImage(cid, contentType, cortarBase64(base64)));

                String replacement =
                        "<img" + antesSrc +
                        "src=\"cid:" + cid + "\"" +
                        despuesSrc +
                        ">";

                matcher.appendReplacement(sanitized, Matcher.quoteReplacement(replacement));

                contador++;
            }

            matcher.appendTail(sanitized);
            content.htmlSanitizado = sanitized.toString();

            return content;
        }

        private String limpiarBase64(String base64) {
            if (base64 == null) {
                return "";
            }

            return base64
                    .replace("\r", "")
                    .replace("\n", "")
                    .replace(" ", "")
                    .trim();
        }

        private String cortarBase64(String base64) {
            if (base64 == null || base64.isEmpty()) {
                return "";
            }

            String limpio = limpiarBase64(base64);
            StringBuilder sb = new StringBuilder();

            int limite = 76;

            for (int i = 0; i < limpio.length(); i += limite) {
                int fin = Math.min(i + limite, limpio.length());
                sb.append(limpio, i, fin).append("\r\n");
            }

            return sb.toString();
        }

        private static class InlineImage {
            private final String cid;
            private final String contentType;
            private final String base64;

            private InlineImage(String cid, String contentType, String base64) {
                this.cid = cid;
                this.contentType = contentType;
                this.base64 = base64;
            }
        }

        private static class InlineEmailContent {
            private String htmlSanitizado = "";
            private final List<InlineImage> imagenes = new ArrayList<>();

            private boolean hasImages() {
                return !imagenes.isEmpty();
            }
        }

public String comandosHelp() {
    return "<html><body style='font-family: Arial, sans-serif; padding: 20px;'>" +
            "<h2 style='color:#2E7D32;'>Comandos disponibles del sistema</h2>" +

            "<h3>CU1. Gestion de Usuarios</h3>" +
            "<ul>" +
            "<li>usuario help</li>" +
            "<li>propietario help</li>" +
            "<li>vendedor help</li>" +
            "<li>cliente help</li>" +
            "</ul>" +

            "<h3>CU2. Gestion de Productos</h3>" +
            "<ul>" +
            "<li>categoria help</li>" +
            "<li>producto help</li>" +
            "<li>producto_variante help</li>" +
            "</ul>" +

            "<h3>CU3. Gestion de Compras</h3>" +
            "<ul>" +
            "<li>proveedor help</li>" +
            "<li>compra help</li>" +
            "<li>detalle_compra help</li>" +
            "</ul>" +

            "<h3>CU4. Gestion de Inventarios</h3>" +
            "<ul>" +
            "<li>tecnica_inventario help</li>" +
            "<li>tecnica_costo help</li>" +
            "<li>movimiento_inventario help</li>" +
            "<li>lote_inventario help</li>" +
            "<li>salida_lote help</li>" +
            "</ul>" +

            "<h3>CU5. Gestion de Promociones</h3>" +
            "<ul>" +
            "<li>promocion help</li>" +
            "<li>promocion_producto help</li>" +
            "<li>promocion_categoria help</li>" +
            "</ul>" +

            "<h3>CU6. Gestion de Ventas</h3>" +
            "<ul>" +
            "<li>carrito help</li>" +
            "<li>carrito_detalle help</li>" +
            "<li>pedido help</li>" +
            "<li>pedido_detalle help</li>" +
            "<li>venta help</li>" +
            "<li>detalle_venta help</li>" +
            "<li>credito help</li>" +
            "</ul>" +

            "<h3>CU7. Gestion de Pagos</h3>" +
            "<ul>" +
            "<li>metodo_pago help</li>" +
            "<li>pago_contado help</li>" +
            "<li>pago_cuota help</li>" +
            "</ul>" +

            "<h3>CU8. Reportes y Estadisticas</h3>" +
            "<ul>" +
            "<li>reporte help</li>" +
            "<li>notificacion help</li>" +
            "</ul>" +

            "<p style='margin-top: 25px; color:#555;'>" +
            "Para ver los comandos especificos de cada modulo, envie el comando help correspondiente. " +
            "Ejemplo: <b>producto help</b>" +
            "</p>" +

            "</body></html>";
}


    public String mensajeComandos(String mensajeHtml) {
        String[] partes = mensajeHtml.split("<br>");
        String mensaje = "";
        for (int i = 0; i < partes.length; i++) {
            mensaje = mensaje + partes[i] + "\r\n";
        }
        return mensaje;
    }

}
