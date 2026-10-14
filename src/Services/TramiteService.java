package Services;

import DAO.TramiteDAO;
import Models.Tramite;

import java.sql.SQLException;
import java.util.List;

public class TramiteService {

    private TramiteDAO tramiteDAO;

    public TramiteService() {
        this.tramiteDAO = new TramiteDAO();
    }

    public void crearTramite(List<String> params) throws SQLException {
        // [codigo_tramite; id_solicitante; id_tipo; asunto; prioridad]
        if (params.size() < 4) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: tramite agregar [codigo_tramite;id_solicitante;id_tipo;asunto;prioridad]");
        }
        String codigo = params.get(0).trim();
        int solicitante = Integer.parseInt(params.get(1).trim());
        int tipo = Integer.parseInt(params.get(2).trim());
        String asunto = params.get(3).trim();
        String prioridad = params.size() > 4 ? params.get(4).trim().toUpperCase() : "NORMAL";

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
        // [id; asunto; prioridad; estado]
        if (params.size() < 4) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: tramite modificar [id;asunto;prioridad;estado]");
        }
        int id = Integer.parseInt(params.get(0).trim());
        String asunto = params.get(1).trim();
        String prioridad = params.get(2).trim();
        String estado = params.get(3).trim();

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
                "- tramite eliminar [id]";
    }
}
