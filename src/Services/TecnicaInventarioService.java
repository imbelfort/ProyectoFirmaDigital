package Services;

import DAO.TecnicaInventarioDAO;

import java.sql.SQLException;
import java.util.List;

public class TecnicaInventarioService {

    private final TecnicaInventarioDAO tecnicaInventarioDAO;

    public TecnicaInventarioService() {
        this.tecnicaInventarioDAO = new TecnicaInventarioDAO();
    }

    public List<String[]> listarTecnicasInventario() throws SQLException {
        return tecnicaInventarioDAO.listAll();
    }

    public String getComandosTecnicaInventario() {
        return "Comandos Tecnica Inventario:\n" +
                "- tecnica_inventario listar\n" +
                "- tecnica_inventario help\n\n" +
                "Tecnicas cargadas automaticamente:\n" +
                "1. FIFO\n" +
                "2. Stock minimo y punto de reorden";
    }
}