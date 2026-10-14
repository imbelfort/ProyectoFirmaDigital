package Services;

import DAO.PortalDAO;
import Models.PortalSeguimiento;

import java.sql.SQLException;
import java.util.List;

public class PortalService {

    private PortalDAO portalDAO;

    public PortalService() {
        this.portalDAO = new PortalDAO();
    }

    public void habilitarPortal(List<String> params) throws SQLException {
        // [id_tramite; pin_seguimiento]
        if (params.size() < 2) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: portal habilitar [id_tramite;pin_seguimiento]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        String pin = params.get(1).trim();

        PortalSeguimiento p = new PortalSeguimiento();
        p.setIdTramite(idTramite);
        p.setPinSeguimiento(pin);
        p.setPortalActivo(true);
        p.setTotalVisitas(0);

        portalDAO.create(p);
    }

    public String[] consultarPorPin(String pin) throws SQLException {
        return portalDAO.consultarPorPin(pin.trim());
    }

    public String getComandosPortal() {
        return "Comandos Portal de Seguimiento Estudiantil (CU6):\n" +
                "- portal consultar [pin_seguimiento]";
    }
}
