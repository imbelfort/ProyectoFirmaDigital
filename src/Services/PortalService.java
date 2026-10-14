package Services;

import DAO.PortalDAO;
import Models.PortalSeguimiento;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class PortalService {

    private PortalDAO portalDAO;

    public PortalService() {
        this.portalDAO = new PortalDAO();
    }

    public void habilitarPortal(List<String> params) throws SQLException {
        String sintaxis = "portal habilitar [id_tramite;pin_seguimiento]";
        String ejemplo = "portal habilitar [1;PIN-8841]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        String pin = ParamValidator.requireString(params, 1, "pin_seguimiento", sintaxis, ejemplo);

        PortalSeguimiento p = new PortalSeguimiento();
        p.setIdTramite(idTramite);
        p.setPinSeguimiento(pin);
        p.setPortalActivo(true);
        p.setTotalVisitas(0);

        portalDAO.create(p);
    }

    public List<String[]> listarPortales() throws SQLException {
        return portalDAO.listAll();
    }

    public String[] consultarPorPin(String pin) throws SQLException {
        if (pin == null || pin.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Falta el PIN de seguimiento del portal estudiantil.\n" +
                    "• Sintaxis requerida: portal consultar [pin_seguimiento]\n" +
                    "• Ejemplo válido: portal consultar [PIN-8841]"
            );
        }
        return portalDAO.consultarPorPin(pin.trim());
    }

    public String getComandosPortal() {
        return "Comandos Portal de Seguimiento Estudiantil (CU6):\n" +
                "- portal listar\n" +
                "- portal habilitar [id_tramite;pin_seguimiento]\n" +
                "- portal consultar [pin_seguimiento]";
    }
}
