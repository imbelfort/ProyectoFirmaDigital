package Services;

import DAO.MovimientoInventarioDAO;

import java.sql.SQLException;
import java.util.List;

public class MovimientoInventarioService {

    private final MovimientoInventarioDAO movimientoInventarioDAO;

    public MovimientoInventarioService() {
        this.movimientoInventarioDAO = new MovimientoInventarioDAO();
    }

    public List<String[]> listarMovimientosInventario() throws SQLException {
        return movimientoInventarioDAO.listAll();
    }

    public String getComandosMovimientoInventario() {
        return "Comandos Movimiento Inventario:\n" +
                "- movimiento_inventario listar\n" +
                "- movimiento_inventario help";
    }
}