package Services;

import DAO.DocumentoDAO;
import Models.DocumentoExpediente;

import java.sql.SQLException;
import java.util.List;

public class DocumentoService {

    private DocumentoDAO documentoDAO;

    public DocumentoService() {
        this.documentoDAO = new DocumentoDAO();
    }

    public void crearDocumento(List<String> params) throws SQLException {
        // [id_tramite; id_requisito; nombre_documento; tipo; url]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        int idReq = params.size() > 1 && !params.get(1).trim().isEmpty() ? Integer.parseInt(params.get(1).trim()) : 0;
        String nombre = params.get(2).trim();
        String tipo = params.size() > 3 ? params.get(3).trim() : "PDF";
        String url = params.size() > 4 ? params.get(4).trim() : "";

        DocumentoExpediente doc = new DocumentoExpediente();
        doc.setIdTramite(idTramite);
        doc.setIdRequisito(idReq);
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
        // [id_documento; estado_validacion; observacion]
        if (params.size() < 2) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: documento validar [id_documento;estado_validacion;observacion]");
        }
        int id = Integer.parseInt(params.get(0).trim());
        String estado = params.get(1).trim().toUpperCase();
        String obs = params.size() > 2 ? params.get(2).trim() : "";

        documentoDAO.validarDocumento(id, estado, obs);
    }

    public String getComandosDocumento() {
        return "Comandos Expediente y Documentos (CU3):\n" +
                "- documento listar\n" +
                "- documento agregar [id_tramite;id_requisito;nombre_documento;tipo;url]\n" +
                "- documento validar [id_documento;estado_validacion;observacion]";
    }
}
