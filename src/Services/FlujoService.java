package Services;

import DAO.FlujoDAO;

import java.sql.SQLException;
import java.util.List;

public class FlujoService {

    private FlujoDAO flujoDAO;

    public FlujoService() {
        this.flujoDAO = new FlujoDAO();
    }

    public void avanzarFlujo(List<String> params) throws SQLException {
        // [id_tramite; etapa_actual; id_usuario_responsable]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: flujo avanzar [id_tramite;etapa_actual;id_usuario_responsable]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        String etapa = params.get(1).trim();
        int responsable = Integer.parseInt(params.get(2).trim());

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
