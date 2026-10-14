package Services;

import DAO.IndicadorDAO;
import Models.Indicador;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class IndicadorService {

    private IndicadorDAO indicadorDAO;

    public IndicadorService() {
        this.indicadorDAO = new IndicadorDAO();
    }

    public void crearIndicador(List<String> params) throws SQLException {
        String sintaxis = "indicador agregar [codigo;nombre;descripcion;meta;unidad;periodo]";
        String ejemplo = "indicador agregar [IND-01;Tasa Tramites 72h;Porcentaje de tramites en tiempo;95.00;PORCENTAJE;MENSUAL]";

        String codigo = ParamValidator.requireString(params, 0, "codigo", sintaxis, ejemplo);
        String nombre = ParamValidator.requireString(params, 1, "nombre", sintaxis, ejemplo);
        String desc = ParamValidator.requireString(params, 2, "descripcion", sintaxis, ejemplo);
        double meta = ParamValidator.requireDouble(params, 3, "meta", sintaxis, ejemplo);
        String unidad = ParamValidator.requireString(params, 4, "unidad", sintaxis, ejemplo);
        String periodo = ParamValidator.optionalString(params, 5, "MENSUAL");

        Indicador ind = new Indicador();
        ind.setCodigo(codigo);
        ind.setNombre(nombre);
        ind.setDescripcion(desc);
        ind.setMetaEsperada(meta);
        ind.setUnidadMedida(unidad);
        ind.setPeriodo(periodo);
        ind.setEstado("ACTIVO");

        indicadorDAO.create(ind);
    }

    public void modificarIndicador(List<String> params) throws SQLException {
        String sintaxis = "indicador modificar [id;meta_esperada;valor_actual;estado]";
        String ejemplo = "indicador modificar [1;98.00;92.50;ACTIVO]";

        int id = ParamValidator.requireInt(params, 0, "id_indicador", sintaxis, ejemplo);
        double meta = ParamValidator.requireDouble(params, 1, "meta_esperada", sintaxis, ejemplo);
        double actual = ParamValidator.requireDouble(params, 2, "valor_actual", sintaxis, ejemplo);
        String estado = ParamValidator.optionalString(params, 3, "ACTIVO").toUpperCase();

        indicadorDAO.update(id, meta, actual, estado);
    }

    public List<String[]> listarIndicadores() throws SQLException {
        return indicadorDAO.listAll();
    }

    public String getComandosIndicador() {
        return "Comandos Indicadores de Gestion (CU2):\n" +
                "- indicador listar\n" +
                "- indicador agregar [codigo;nombre;descripcion;meta;unidad;periodo]\n" +
                "- indicador modificar [id;meta_esperada;valor_actual;estado]";
    }
}
