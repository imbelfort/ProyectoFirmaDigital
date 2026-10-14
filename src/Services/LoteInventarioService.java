package Services;

import DAO.LoteInventarioDAO;

import java.sql.SQLException;
import java.util.List;

public class LoteInventarioService {

    private final LoteInventarioDAO loteInventarioDAO;

    public LoteInventarioService() {
        this.loteInventarioDAO = new LoteInventarioDAO();
    }

    public List<String[]> listarLotesInventario() throws SQLException {
        return loteInventarioDAO.listAll();
    }

    public String getComandosLoteInventario() {
        return "Comandos Lote Inventario:\n" +
                "- lote_inventario listar\n" +
                "- lote_inventario help";
    }
}