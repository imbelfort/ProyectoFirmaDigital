package Services;

import DAO.SalidaLoteDAO;

import java.sql.SQLException;
import java.util.List;

public class SalidaLoteService {

    private final SalidaLoteDAO salidaLoteDAO;

    public SalidaLoteService() {
        this.salidaLoteDAO = new SalidaLoteDAO();
    }

    public List<String[]> listarSalidasLote() throws SQLException {
        return salidaLoteDAO.listAll();
    }

    public String getComandosSalidaLote() {
        return "Comandos Salida Lote:\n" +
                "- salida_lote listar\n" +
                "- salida_lote help";
    }
}