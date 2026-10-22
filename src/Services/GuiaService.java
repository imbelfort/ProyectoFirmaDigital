package Services;

import DAO.GuiaDAO;
import Models.GuiaProcesal;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class GuiaService {

    private GuiaDAO guiaDAO;

    public GuiaService() {
        this.guiaDAO = new GuiaDAO();
    }

    public void crearGuia(List<String> params) throws SQLException {
        String sintaxis = "guia agregar [id_tipo_tramite;paso_numero;nombre_etapa;descripcion_instrucciones;rol_responsable;tiempo_estimado_horas]";
        String ejemplo = "guia agregar [1;1;Recepcion de Documentos;Verificar que los requisitos esten completos en PDF;ENCARGADO;24]";

        int idTipo = ParamValidator.requireInt(params, 0, "id_tipo_tramite", sintaxis, ejemplo);
        int paso = ParamValidator.requireInt(params, 1, "paso_numero", sintaxis, ejemplo);
        String etapa = ParamValidator.requireString(params, 2, "nombre_etapa", sintaxis, ejemplo);
        String desc = ParamValidator.requireString(params, 3, "descripcion_instrucciones", sintaxis, ejemplo);
        String rol = ParamValidator.requireString(params, 4, "rol_responsable", sintaxis, ejemplo).toUpperCase();
        int tiempo = ParamValidator.requireInt(params, 5, "tiempo_estimado_horas", sintaxis, ejemplo);

        GuiaProcesal g = new GuiaProcesal();
        g.setIdTipoTramite(idTipo);
        g.setPasoNumero(paso);
        g.setNombreEtapa(etapa);
        g.setDescripcionInstrucciones(desc);
        g.setRolResponsable(rol);
        g.setTiempoEstimadoHoras(tiempo);

        guiaDAO.create(g);
    }

    public List<String[]> listarGuias() throws SQLException {
        return guiaDAO.listAll();
    }

    public List<String[]> mostrarGuiaPorTipo(List<String> params) throws SQLException {
        String sintaxis = "guia mostrar [id_tipo_tramite]";
        String ejemplo = "guia mostrar [1]";
        int idTipo = ParamValidator.requireInt(params, 0, "id_tipo_tramite", sintaxis, ejemplo);
        return guiaDAO.listByTipoTramite(idTipo);
    }

    public String getComandosGuia() {
        return "Comandos Guia Procesal (CU4):\n" +
                "- guia listar\n" +
                "- guia mostrar [id_tipo_tramite]\n" +
                "- guia agregar [id_tipo_tramite;paso_numero;nombre_etapa;descripcion_instrucciones;rol_responsable;tiempo_estimado_horas]";
    }
}
