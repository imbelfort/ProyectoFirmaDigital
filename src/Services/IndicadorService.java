package Services;

import DAO.IndicadorDAO;
import Models.Indicador;

import java.sql.SQLException;
import java.util.List;

public class IndicadorService {

    private IndicadorDAO indicadorDAO;

    public IndicadorService() {
        this.indicadorDAO = new IndicadorDAO();
    }

    public void crearIndicador(List<String> params) throws SQLException {
        // [codigo; nombre; descripcion; meta; unidad; periodo]
        if (params.size() < 5) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: indicador agregar [codigo;nombre;descripcion;meta;unidad;periodo]");
        }
        String codigo = params.get(0).trim();
        String nombre = params.get(1).trim();
        String desc = params.get(2).trim();
        double meta = Double.parseDouble(params.get(3).trim());
        String unidad = params.get(4).trim();
        String periodo = params.size() > 5 ? params.get(5).trim() : "MENSUAL";

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

    public List<String[]> listarIndicadores() throws SQLException {
        return indicadorDAO.listAll();
    }

    public String getComandosIndicador() {
        return "Comandos Indicadores de Gestion (CU2):\n" +
                "- indicador listar\n" +
                "- indicador agregar [codigo;nombre;descripcion;meta;unidad;periodo]";
    }
}
