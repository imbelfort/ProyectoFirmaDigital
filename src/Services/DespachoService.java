package Services;

import DAO.DespachoDAO;
import Models.DespachoLegajo;

import java.sql.SQLException;
import java.util.List;

public class DespachoService {

    private DespachoDAO despachoDAO;

    public DespachoService() {
        this.despachoDAO = new DespachoDAO();
    }

    public void registrarDespacho(List<String> params) throws SQLException {
        // [id_tramite; id_usuario_despachador; numero_despacho; tipo_entrega]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: despacho agregar [id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        int despachador = Integer.parseInt(params.get(1).trim());
        String nro = params.get(2).trim();
        String tipo = params.size() > 3 ? params.get(3).trim().toUpperCase() : "DIGITAL_CORREO";

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
                "- despacho agregar [id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]";
    }
}
