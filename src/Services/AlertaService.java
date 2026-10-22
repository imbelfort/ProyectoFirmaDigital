package Services;

import DAO.AlertaDAO;
import Models.AlertaSLA;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class AlertaService {

    private AlertaDAO alertaDAO;

    public AlertaService() {
        this.alertaDAO = new AlertaDAO();
    }

    public void crearAlerta(List<String> params) throws SQLException {
        String sintaxis = "alerta agregar [id_tramite;tipo_alerta;mensaje;notificado_a]";
        String ejemplo = "alerta agregar [1;ADVERTENCIA_24H;El tramite vence en menos de 24 horas habiles;encargado@uagrm.edu.bo]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        String tipo = ParamValidator.requireString(params, 1, "tipo_alerta", sintaxis, ejemplo).toUpperCase();
        String mensaje = ParamValidator.requireString(params, 2, "mensaje", sintaxis, ejemplo);
        String notificadoA = params.size() > 3 ? params.get(3).trim() : "ENCARGADO_DESPACHO";

        AlertaSLA a = new AlertaSLA();
        a.setIdTramite(idTramite);
        a.setTipoAlerta(tipo);
        a.setMensaje(mensaje);
        a.setNotificadoA(notificadoA);

        alertaDAO.create(a);
    }

    public void atenderAlerta(List<String> params) throws SQLException {
        String sintaxis = "alerta atender [id_alerta]";
        String ejemplo = "alerta atender [1]";
        int idAlerta = ParamValidator.requireInt(params, 0, "id_alerta", sintaxis, ejemplo);
        alertaDAO.marcarAtendida(idAlerta);
    }

    public List<String[]> listarAlertas() throws SQLException {
        return alertaDAO.listAll();
    }

    public String getComandosAlerta() {
        return "Comandos Alertas SLA (CU7):\n" +
                "- alerta listar\n" +
                "- alerta agregar [id_tramite;tipo_alerta;mensaje;notificado_a]\n" +
                "- alerta atender [id_alerta]";
    }
}
