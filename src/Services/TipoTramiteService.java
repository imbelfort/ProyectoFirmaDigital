package Services;

import DAO.TipoTramiteDAO;
import Models.TipoTramite;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class TipoTramiteService {

    private TipoTramiteDAO tipoDAO;

    public TipoTramiteService() {
        this.tipoDAO = new TipoTramiteDAO();
    }

    public void crearTipo(List<String> params) throws SQLException {
        String sintaxis = "tipo_tramite agregar [codigo;nombre;descripcion;tiempo_sla;costo]";
        String ejemplo = "tipo_tramite agregar [TT-01;Certificado de Calificaciones;Emision oficial firmada;72;50.00]";

        String codigo = ParamValidator.requireString(params, 0, "codigo", sintaxis, ejemplo);
        String nombre = ParamValidator.requireString(params, 1, "nombre", sintaxis, ejemplo);
        String desc = ParamValidator.requireString(params, 2, "descripcion", sintaxis, ejemplo);
        int sla = ParamValidator.optionalInt(params, 3, 72, "tiempo_sla", sintaxis, ejemplo);
        double costo = ParamValidator.optionalDouble(params, 4, 0.0, "costo", sintaxis, ejemplo);

        TipoTramite t = new TipoTramite();
        t.setCodigo(codigo);
        t.setNombre(nombre);
        t.setDescripcion(desc);
        t.setTiempoSlaHoras(sla);
        t.setCostoBs(costo);
        t.setEstado("ACTIVO");

        tipoDAO.create(t);
    }

    public List<String[]> listarTipos() throws SQLException {
        return tipoDAO.listAll();
    }

    public String getComandosTipoTramite() {
        return "Comandos Tipos de Tramite (CU3):\n" +
                "- tipo_tramite listar\n" +
                "- tipo_tramite agregar [codigo;nombre;descripcion;tiempo_sla;costo]";
    }
}
