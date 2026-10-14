package Services;

import DAO.DespachoDAO;
import Models.DespachoLegajo;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class DespachoService {

    private DespachoDAO despachoDAO;

    public DespachoService() {
        this.despachoDAO = new DespachoDAO();
    }

    public void registrarDespacho(List<String> params) throws SQLException {
        String sintaxis = "despacho agregar [id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]";
        String ejemplo = "despacho agregar [1;2;DESP-2026-001;DIGITAL_CORREO]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int despachador = ParamValidator.requireInt(params, 1, "id_usuario_despachador", sintaxis, ejemplo);
        String nro = ParamValidator.requireString(params, 2, "numero_despacho", sintaxis, ejemplo);
        String tipo = ParamValidator.optionalString(params, 3, "DIGITAL_CORREO").toUpperCase();

        DespachoLegajo d = new DespachoLegajo();
        d.setIdTramite(idTramite);
        d.setIdUsuarioDespachador(despachador);
        d.setNumeroDespacho(nro);
        d.setLegajoPrevisualizadoUrl("http://legajos.uagrm.edu.bo/doc/" + nro + ".pdf");
        d.setTipoEntrega(tipo);
        d.setEstado("DESPACHADO");

        despachoDAO.create(d);
    }

    public List<String[]> listarDespachos() throws SQLException {
        return despachoDAO.listAll();
    }

    public String getComandosDespacho() {
        return "Comandos Despacho de Legajos (CU5):\n" +
                "- despacho listar\n" +
                "- despacho agregar [id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]\n\n" +
                "Tipos de entrega: DIGITAL_CORREO, PORTAL_DESCARGA, VENTANILLA.";
    }
}
