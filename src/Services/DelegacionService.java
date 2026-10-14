package Services;

import DAO.DelegacionDAO;
import Models.Delegacion;
import Utils.ParamValidator;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class DelegacionService {

    private DelegacionDAO delegacionDAO;

    public DelegacionService() {
        this.delegacionDAO = new DelegacionDAO();
    }

    public void crearDelegacion(List<String> params) throws SQLException {
        String sintaxis = "delegacion agregar [id_titular;id_delegado;resolucion;motivo;fecha_inicio;fecha_fin;alcance]";
        String ejemplo = "delegacion agregar [1;2;R-VR-012/2026;Viaje academico;2026-10-15;2026-10-25;TOTAL]";

        int titular = ParamValidator.requireInt(params, 0, "id_titular", sintaxis, ejemplo);
        int delegado = ParamValidator.requireInt(params, 1, "id_delegado", sintaxis, ejemplo);
        String res = ParamValidator.requireString(params, 2, "resolucion", sintaxis, ejemplo);
        String motivo = ParamValidator.requireString(params, 3, "motivo", sintaxis, ejemplo);
        Date fi = ParamValidator.requireDate(params, 4, "fecha_inicio", sintaxis, ejemplo);
        Date ff = ParamValidator.requireDate(params, 5, "fecha_fin", sintaxis, ejemplo);
        String alcance = ParamValidator.optionalString(params, 6, "TOTAL");

        if (fi.after(ff)) {
            throw new IllegalArgumentException(
                    "Inconsistencia en fechas: La fecha_inicio (" + fi + ") no puede ser posterior a la fecha_fin (" + ff + ").\n" +
                    "• Sintaxis requerida: " + sintaxis + "\n" +
                    "• Ejemplo válido: " + ejemplo
            );
        }

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
        String sintaxis = "delegacion modificar [id;resolucion;motivo;fecha_inicio;fecha_fin;alcance;estado]";
        String ejemplo = "delegacion modificar [1;R-VR-012/2026;Viaje extendido;2026-10-15;2026-10-30;TOTAL;ACTIVA]";

        int id = ParamValidator.requireInt(params, 0, "id_delegacion", sintaxis, ejemplo);
        String res = ParamValidator.requireString(params, 1, "resolucion", sintaxis, ejemplo);
        String motivo = ParamValidator.requireString(params, 2, "motivo", sintaxis, ejemplo);
        Date fi = ParamValidator.requireDate(params, 3, "fecha_inicio", sintaxis, ejemplo);
        Date ff = ParamValidator.requireDate(params, 4, "fecha_fin", sintaxis, ejemplo);
        String alcance = ParamValidator.optionalString(params, 5, "TOTAL");
        String estado = ParamValidator.optionalString(params, 6, "ACTIVA");

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
