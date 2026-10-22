package Services;

import DAO.ObservacionDAO;
import Models.ObservacionTramite;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class ObservacionService {

    private ObservacionDAO observacionDAO;

    public ObservacionService() {
        this.observacionDAO = new ObservacionDAO();
    }

    public void crearObservacion(List<String> params) throws SQLException {
        String sintaxis = "observacion agregar [id_tramite;id_usuario;tipo_observacion;descripcion]";
        String ejemplo = "observacion agregar [1;2;FALTA_REQUISITO;Falta la fotocopia legalizada del carnet de identidad]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int idUsuario = ParamValidator.requireInt(params, 1, "id_usuario", sintaxis, ejemplo);
        String tipo = ParamValidator.requireString(params, 2, "tipo_observacion", sintaxis, ejemplo).toUpperCase();
        String desc = ParamValidator.requireString(params, 3, "descripcion", sintaxis, ejemplo);

        ObservacionTramite o = new ObservacionTramite();
        o.setIdTramite(idTramite);
        o.setIdUsuario(idUsuario);
        o.setTipoObservacion(tipo);
        o.setDescripcion(desc);

        observacionDAO.create(o);
    }

    public void subsanarObservacion(List<String> params) throws SQLException {
        String sintaxis = "observacion subsanar [id_observacion;descripcion_subsanacion]";
        String ejemplo = "observacion subsanar [1;Se adjunto la fotocopia solicitada en formato PDF]";

        int idObs = ParamValidator.requireInt(params, 0, "id_observacion", sintaxis, ejemplo);
        String detalle = params.size() > 1 ? params.get(1) : "Subsanado correctamente";

        observacionDAO.subsanar(idObs, detalle);
    }

    public List<String[]> listarObservaciones() throws SQLException {
        return observacionDAO.listAll();
    }

    public String getComandosObservacion() {
        return "Comandos Observaciones (CU4):\n" +
                "- observacion listar\n" +
                "- observacion agregar [id_tramite;id_usuario;tipo_observacion;descripcion]\n" +
                "- observacion subsanar [id_observacion;descripcion_subsanacion]";
    }
}
