package Services;

import DAO.DelegacionDAO;
import Models.Delegacion;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class DelegacionService {

    private DelegacionDAO delegacionDAO;

    public DelegacionService() {
        this.delegacionDAO = new DelegacionDAO();
    }

    public void crearDelegacion(List<String> params) throws SQLException {
        // [id_titular; id_delegado; resolucion; motivo; fecha_inicio; fecha_fin; alcance]
        if (params.size() < 6) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: delegacion agregar [id_titular;id_delegado;resolucion;motivo;fecha_inicio;fecha_fin;alcance]");
        }
        int titular = Integer.parseInt(params.get(0).trim());
        int delegado = Integer.parseInt(params.get(1).trim());
        String res = params.get(2).trim();
        String motivo = params.get(3).trim();
        Date fi = Date.valueOf(params.get(4).trim());
        Date ff = Date.valueOf(params.get(5).trim());
        String alcance = params.size() > 6 ? params.get(6).trim() : "TOTAL";

        Delegacion d = new Delegacion();
        d.setIdAutoridadTitular(titular);
        d.setIdAutoridadDelegada(delegado);
        d.setResolucion(res);
        d.setMotivo(motivo);
        d.setFechaInicio(fi);
        d.setFechaFin(ff);
        d.setAlcanceFirma(alcance);
        d.setEstado("ACTIVA");

        delegacionDAO.create(d);
    }

    public List<String[]> listarDelegaciones() throws SQLException {
        return delegacionDAO.listAll();
    }

    public void modificarDelegacion(List<String> params) throws SQLException {
        if (params.size() < 7) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: delegacion modificar [id;resolucion;motivo;fecha_inicio;fecha_fin;alcance;estado]");
        }
        int id = Integer.parseInt(params.get(0).trim());
        String res = params.get(1).trim();
        String motivo = params.get(2).trim();
        Date fi = Date.valueOf(params.get(3).trim());
        Date ff = Date.valueOf(params.get(4).trim());
        String alcance = params.get(5).trim();
        String estado = params.get(6).trim();

        Delegacion d = new Delegacion();
        d.setId(id);
        d.setResolucion(res);
        d.setMotivo(motivo);
        d.setFechaInicio(fi);
        d.setFechaFin(ff);
        d.setAlcanceFirma(alcance);
        d.setEstado(estado);

        delegacionDAO.update(d);
    }

    public void eliminarDelegacion(int id) throws SQLException {
        delegacionDAO.delete(id);
    }

    public String getComandosDelegacion() {
        return "Comandos Delegacion de Autoridad (CU2):\n" +
                "- delegacion listar\n" +
                "- delegacion agregar [id_titular;id_delegado;resolucion;motivo;fecha_inicio;fecha_fin;alcance]\n" +
                "- delegacion modificar [id;resolucion;motivo;fecha_inicio;fecha_fin;alcance;estado]\n" +
                "- delegacion eliminar [id]";
    }
}
