package Services;

import DAO.TecnicaCostoDAO;

import java.sql.SQLException;
import java.util.List;

public class TecnicaCostoService {

    private final TecnicaCostoDAO tecnicaCostoDAO;

    public TecnicaCostoService() {
        this.tecnicaCostoDAO = new TecnicaCostoDAO();
    }

    public List<String[]> listarTecnicasCosto() throws SQLException {
        return tecnicaCostoDAO.listAll();
    }

    public String getComandosTecnicaCosto() {
        return "Comandos Tecnica Costo:\n" +
                "- tecnica_costo listar\n" +
                "- tecnica_costo help\n\n" +
                "Nota: Las tecnicas de costo son datos base del sistema.\n" +
                "1. Costo promedio ponderado\n" +
                "2. FIFO de costo";
    }
}