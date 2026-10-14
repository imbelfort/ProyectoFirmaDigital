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
        String sintaxis = "documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]";
        String ejemplo = "documento agregar [1;1;Cedula_Identidad.pdf;PDF;http://docs.uagrm.edu.bo/1/ci.pdf]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int idReq = ParamValidator.optionalInt(params, 1, 0, "id_requisito", sintaxis, ejemplo);
        String nombre = ParamValidator.requireString(params, 2, "nombre_documento", sintaxis, ejemplo);
        String tipo = ParamValidator.optionalString(params, 3, "PDF");
        String url = ParamValidator.optionalString(params, 4, "");

        DocumentoExpediente doc = new DocumentoExpediente();
        doc.setIdTramite(idTramite);
        doc.setIdRequisito(idReq > 0 ? idReq : 0);
        doc.setNombreDocumento(nombre);
        doc.setTipoDocumento(tipo);
        doc.setArchivoUrl(url);
        doc.setHashDocumento("SHA256-" + System.currentTimeMillis());
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
                "- documento validar [id_documento;estado_validacion;observacion]\n\n" +
                "Estados de validación: APROBADO, OBSERVADO, RECHAZADO.";
    }
}
