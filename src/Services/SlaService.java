package Services;

import DAO.SlaDAO;
import Models.MonitoreoSLA;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SlaService {

    private SlaDAO slaDAO;

    public SlaService() {
        this.slaDAO = new SlaDAO();
    }

    public void iniciarMonitoreo(int idTramite, int horasLimite) throws SQLException {
        Timestamp inicio = new Timestamp(System.currentTimeMillis());
        Timestamp fin = new Timestamp(System.currentTimeMillis() + (horasLimite * 3600L * 1000L));

        MonitoreoSLA m = new MonitoreoSLA();
        m.setIdTramite(idTramite);
        m.setHorasSlaLimite(horasLimite);
        m.setFechaInicioSla(inicio);
        m.setFechaLimiteSla(fin);
        m.setHorasTranscurridas(0.0);
        m.setEstadoSla("EN_TIEMPO");

        slaDAO.create(m);
    }

    public List<String[]> listarMonitoreo() throws SQLException {
        return slaDAO.listAll();
    }

    public void verificarSla(List<String> params) throws SQLException {
        String sintaxis = "sla verificar [id_tramite]";
        String ejemplo = "sla verificar [1]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        slaDAO.verificarYActualizarSla(idTramite);
    }

    public String getComandosSla() {
        return "Comandos Monitoreo SLA 72 Horas Habiles (CU7):\n" +
                "- sla listar\n" +
                "- sla verificar [id_tramite]\n\n" +
                "El sistema calcula las 72 horas hábiles y genera alertas de vencimiento.";
    }
}
