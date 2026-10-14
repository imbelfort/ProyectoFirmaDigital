package Services;

import DAO.FlujoDAO;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class FlujoService {

    private FlujoDAO flujoDAO;

    public FlujoService() {
        this.flujoDAO = new FlujoDAO();
    }

    public void avanzarFlujo(List<String> params) throws SQLException {
        String sintaxis = "flujo avanzar [id_tramite;etapa_actual;id_usuario_responsable]";
        String ejemplo = "flujo avanzar [1;REVISION_LEGAL;2]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        String etapa = ParamValidator.requireString(params, 1, "etapa_actual", sintaxis, ejemplo).toUpperCase();
        int responsable = ParamValidator.requireInt(params, 2, "id_usuario_responsable", sintaxis, ejemplo);

        flujoDAO.avanzarEtapa(idTramite, etapa, responsable);
    }

    public List<String[]> listarFlujo() throws SQLException {
        return flujoDAO.listAll();
    }

    public String getComandosFlujo() {
        return "Comandos Enrutamiento de Flujo (CU4):\n" +
                "- flujo listar\n" +
                "- flujo avanzar [id_tramite;etapa_actual;id_usuario_responsable]";
    }
}
