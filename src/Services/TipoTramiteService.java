package Services;

import DAO.TipoTramiteDAO;
import Models.TipoTramite;

import java.sql.SQLException;
import java.util.List;

public class TipoTramiteService {

    private TipoTramiteDAO tipoDAO;

    public TipoTramiteService() {
        this.tipoDAO = new TipoTramiteDAO();
    }

    public void crearTipo(List<String> params) throws SQLException {
        // [codigo; nombre; descripcion; tiempo_sla; costo]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: tipo_tramite agregar [codigo;nombre;descripcion;tiempo_sla;costo]");
        }
        String codigo = params.get(0).trim();
        String nombre = params.get(1).trim();
        String desc = params.get(2).trim();
        int sla = params.size() > 3 ? Integer.parseInt(params.get(3).trim()) : 72;
        double costo = params.size() > 4 ? Double.parseDouble(params.get(4).trim()) : 0.0;

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
