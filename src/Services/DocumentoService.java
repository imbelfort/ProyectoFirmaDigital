package Services;

import DAO.DocumentoDAO;
import Models.DocumentoExpediente;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class DocumentoService {

    private DocumentoDAO documentoDAO;

    public DocumentoService() {
        this.documentoDAO = new DocumentoDAO();
    }

    public void crearDocumento(List<String> params) throws SQLException {
        crearDocumento(params, null, null);
    }

    public void crearDocumento(List<String> params, String adjuntoNombre, String adjuntoBase64) throws SQLException {
        String sintaxis = "documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]  o  adjuntar archivo al correo";
        String ejemplo = "documento agregar [1;1;Cedula_Identidad.pdf;PDF;http://docs.uagrm.edu.bo/1/ci.pdf]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int idReq = ParamValidator.optionalInt(params, 1, 0, "id_requisito", sintaxis, ejemplo);

        String nombre;
        String tipo;
        String url;

        if (params != null && params.size() > 2 && !params.get(2).trim().isEmpty()) {
            nombre = params.get(2).trim();
            tipo = ParamValidator.optionalString(params, 3, "PDF");
            url = ParamValidator.optionalString(params, 4, "");
        } else if (adjuntoNombre != null && !adjuntoNombre.trim().isEmpty()) {
            nombre = adjuntoNombre.trim();
            int dot = nombre.lastIndexOf('.');
            tipo = dot >= 0 ? nombre.substring(dot + 1).toUpperCase() : "ADJUNTO";
            url = "(Adjunto en correo)";
        } else {
            nombre = ParamValidator.requireString(params, 2, "nombre_documento", sintaxis, ejemplo);
            tipo = ParamValidator.optionalString(params, 3, "PDF");
            url = ParamValidator.optionalString(params, 4, "");
        }

        String hashCalculado;
        if (adjuntoBase64 != null && !adjuntoBase64.trim().isEmpty()) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] decoded = java.util.Base64.getDecoder().decode(adjuntoBase64.replaceAll("\\s+", ""));
                byte[] digest = md.digest(decoded);
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b));
                }
                hashCalculado = "SHA256:" + sb.toString();
            } catch (Exception e) {
                hashCalculado = "SHA256-" + System.currentTimeMillis();
            }
        } else {
            hashCalculado = "SHA256-" + System.currentTimeMillis();
        }

        DocumentoExpediente doc = new DocumentoExpediente();
        doc.setIdTramite(idTramite);
        doc.setIdRequisito(idReq > 0 ? idReq : 0);
        doc.setNombreDocumento(nombre);
        doc.setTipoDocumento(tipo);
        doc.setArchivoUrl(url);
        doc.setHashDocumento(hashCalculado);
        doc.setEstadoValidacion("PENDIENTE");

        documentoDAO.create(doc);
    }

    public List<String[]> listarDocumentos() throws SQLException {
        return documentoDAO.listAll();
    }

    public void validarDocumento(List<String> params) throws SQLException {
        String sintaxis = "documento validar [id_documento;estado_validacion;observacion]";
        String ejemplo = "documento validar [1;APROBADO;Documento legible y verificado]";

        int id = ParamValidator.requireInt(params, 0, "id_documento", sintaxis, ejemplo);
        String estado = ParamValidator.requireString(params, 1, "estado_validacion", sintaxis, ejemplo).toUpperCase();
        String obs = ParamValidator.optionalString(params, 2, "Sin observaciones");

        if (!estado.equals("APROBADO") && !estado.equals("OBSERVADO") && !estado.equals("RECHAZADO") && !estado.equals("PENDIENTE")) {
            throw new IllegalArgumentException(
                    "Estado de validación inválido: '" + estado + "'.\n" +
                    "• Estados permitidos: APROBADO, OBSERVADO, RECHAZADO, PENDIENTE.\n" +
                    "• Sintaxis requerida: " + sintaxis + "\n" +
                    "• Ejemplo válido: " + ejemplo
            );
        }

        documentoDAO.validarDocumento(id, estado, obs);
    }

    public String getComandosDocumento() {
        return "Comandos Expediente y Documentos (CU3):\n" +
                "- documento listar\n" +
                "- documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]\n" +
                "- documento agregar [id_tramite;id_requisito] (Con archivo adjunto en el correo)\n" +
                "- documento validar [id_documento;estado_validacion;observacion]\n\n" +
                "Estados de validacion: APROBADO, OBSERVADO, RECHAZADO.\n" +
                "Nota: Puede adjuntar directamente archivos (PDF, imagenes) en el correo y el sistema extraera su nombre y calculara su hash SHA-256.";
    }
}
