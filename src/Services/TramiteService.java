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

    public Tramite crearTramite(List<String> params) throws SQLException {
        return crearTramite(params, null);
    }

    public Tramite crearTramite(List<String> params, String remitenteEmail) throws SQLException {
        String sintaxis = "tramite agregar [id_tipo;asunto]  o  tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]";
        String ejemplo = "tramite agregar [1;Solicitud de Diploma Academico]  o  tramite agregar [2026001;4;1;Solicitud de Certificado;NORMAL]";

        String codigo;
        int solicitante;
        int tipo;
        String asunto;
        String prioridad;

        // MODO SIMPLIFICADO INTELIGENTE: El alumno solo envía [id_tipo; asunto] o [id_tipo; asunto; prioridad]
        if (params != null && params.size() <= 3 && params.size() >= 2) {
            tipo = ParamValidator.requireInt(params, 0, "id_tipo_tramite", sintaxis, ejemplo);
            asunto = ParamValidator.requireString(params, 1, "asunto", sintaxis, ejemplo);
            prioridad = params.size() == 3 ? ParamValidator.requirePrioridad(params, 2, "NORMAL", sintaxis, ejemplo) : "NORMAL";

            // 1. Identificar o AUTOREGISTRAR al solicitante automáticamente por su correo
            solicitante = new DAO.UsuarioDAO().findOrCreateByEmail(remitenteEmail);

            // 2. Generar código de trámite correlativo automático con año (Ej. 2026105)
            codigo = String.valueOf(2026000 + (int) (System.currentTimeMillis() % 9000 + 1000));

        } else {
            // MODO COMPLETO (Compatibilidad retrospectiva)
            codigo = ParamValidator.requireCodigoTramite(params, 0, "codigo_tramite", sintaxis, ejemplo);
            solicitante = ParamValidator.requireInt(params, 1, "id_solicitante", sintaxis, ejemplo);
            tipo = ParamValidator.requireInt(params, 2, "id_tipo", sintaxis, ejemplo);
            asunto = ParamValidator.requireString(params, 3, "asunto", sintaxis, ejemplo);
            prioridad = ParamValidator.requirePrioridad(params, 4, "NORMAL", sintaxis, ejemplo);
        }

        Tramite t = new Tramite();
        t.setCodigoTramite(codigo);
        t.setIdUsuarioSolicitante(solicitante);
        t.setIdTipoTramite(tipo);
        t.setAsunto(asunto);
        t.setPrioridad(prioridad);
        t.setEstado("INICIADO");

        tramiteDAO.create(t);

        try {
            new DAO.SlaDAO().createDefaultSla(t.getId(), 72);
        } catch (Exception ignored) {
            // SLA ya inicializado o tolerante a fallas
        }

        return t;
    }

    public String[] consultarDetalle(int id) throws SQLException {
        return tramiteDAO.readDetalleById(id);
    }

    public List<String[]> listarTramites() throws SQLException {
        return tramiteDAO.listAll();
    }

    public void modificarTramite(List<String> params) throws SQLException {
        String sintaxis = "tramite modificar [id;asunto;prioridad;estado]";
        String ejemplo = "tramite modificar [1;Solicitud rectificada;ALTA;EN_PROCESO]";

        int id = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        if (!tramiteDAO.existsById(id)) {
            throw new SQLException("No existe ningún trámite registrado con el ID: " + id + ". Verifique los IDs válidos con 'tramite listar'.");
        }
        String asunto = ParamValidator.requireString(params, 1, "asunto", sintaxis, ejemplo);
        String prioridad = ParamValidator.requirePrioridad(params, 2, "NORMAL", sintaxis, ejemplo);
        String estado = ParamValidator.optionalString(params, 3, "INICIADO").toUpperCase();

        tramiteDAO.update(id, asunto, prioridad, estado);
    }

    public void eliminarTramite(int id) throws SQLException {
        if (!tramiteDAO.existsById(id)) {
            throw new SQLException("No existe ningún trámite registrado con el ID: " + id + ". Verifique los IDs válidos con 'tramite listar'.");
        }
        tramiteDAO.delete(id);
    }

    public String getComandosTramite() {
        return "Comandos Gestion de Tramites (CU3):\n" +
                "- tipo_tramite listar\n" +
                "  (Consulte primero los tipos de tramite disponibles y su id_tipo: 1=Diploma, 2=Titulo, etc.)\n" +
                "- tramite listar\n" +
                "- tramite agregar [id_tipo;asunto]\n" +
                "  (Ejemplo: tramite agregar [1;Solicitud de Diploma de Licenciatura])\n" +
                "- tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]\n" +
                "- tramite modificar [id;asunto;prioridad;estado]\n" +
                "- tramite eliminar [id]\n\n" +
                "Nota: En el modo simplificado el sistema asigna automaticamente el alumno solicitante y el codigo de tramite.";
    }
}
