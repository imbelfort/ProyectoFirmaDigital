package Services;

import DAO.TramiteDAO;
import Models.Tramite;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class TramiteService {

    private TramiteDAO tramiteDAO;

    public TramiteService() {
        this.tramiteDAO = new TramiteDAO();
    }

    public void crearTramite(List<String> params) throws SQLException {
        String sintaxis = "tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]";
        String ejemplo = "tramite agregar [2026001;4;1;Solicitud de Certificado de Notas;NORMAL]";

        String codigo = ParamValidator.requireNumericCode(params, 0, "codigo_tramite", sintaxis, ejemplo);
        int solicitante = ParamValidator.requireInt(params, 1, "id_solicitante", sintaxis, ejemplo);
        int tipo = ParamValidator.requireInt(params, 2, "id_tipo", sintaxis, ejemplo);
        String asunto = ParamValidator.requireString(params, 3, "asunto", sintaxis, ejemplo);
        String prioridad = ParamValidator.optionalString(params, 4, "NORMAL").toUpperCase();

        Tramite t = new Tramite();
        t.setCodigoTramite(codigo);
        t.setIdUsuarioSolicitante(solicitante);
        t.setIdTipoTramite(tipo);
        t.setAsunto(asunto);
        t.setPrioridad(prioridad);
        t.setEstado("INICIADO");

        tramiteDAO.create(t);
    }

    public List<String[]> listarTramites() throws SQLException {
        return tramiteDAO.listAll();
    }

    public void modificarTramite(List<String> params) throws SQLException {
        String sintaxis = "tramite modificar [id;asunto;prioridad;estado]";
        String ejemplo = "tramite modificar [1;Solicitud rectificada;ALTA;EN_PROCESO]";

        int id = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        String asunto = ParamValidator.requireString(params, 1, "asunto", sintaxis, ejemplo);
        String prioridad = ParamValidator.optionalString(params, 2, "NORMAL").toUpperCase();
        String estado = ParamValidator.optionalString(params, 3, "INICIADO").toUpperCase();

        tramiteDAO.update(id, asunto, prioridad, estado);
    }

    public void eliminarTramite(int id) throws SQLException {
        tramiteDAO.delete(id);
    }

    public String getComandosTramite() {
        return "Comandos Gestion de Tramites (CU3):\n" +
                "- tramite listar\n" +
                "- tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]\n" +
                "- tramite modificar [id;asunto;prioridad;estado]\n" +
                "- tramite eliminar [id]\n\n" +
                "Nota: El código de trámite es numérico (ej. 2026001).";
    }
}
