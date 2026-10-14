package Services;

import DAO.VendedorDAO;
import Models.Vendedor;

import java.math.BigDecimal;
 
import java.sql.SQLException;
import java.util.List;

public class VendedorService {
    private VendedorDAO vendedorDAO;

    public VendedorService() {
        this.vendedorDAO = new VendedorDAO();
    }

    public void crearVendedor(List<String> params) throws SQLException {
        if (params.size() < 1) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Vendedor: id_usuario");
        }

        Vendedor vendedor = new Vendedor();
        vendedor.setIdUsuario(Integer.parseInt(params.get(0)));
        if (params.size() > 1) {
            vendedor.setComisionPorcentaje(new BigDecimal(params.get(1)));
        }
        if (params.size() > 2) {
            vendedor.setTotalVentas(new BigDecimal(params.get(2)));
        }

        vendedorDAO.create(vendedor);
    }

    public String[] obtenerVendedorPorId(int id) throws SQLException {
        return vendedorDAO.readById(id);
    }

    public List<String[]> listarVendedores() throws SQLException {
        return vendedorDAO.listAll();
    }

    public void actualizarVendedor(List<String> params) throws SQLException {
        if (params.size() < 2) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Vendedor: id, id_usuario");
        }

        Vendedor vendedor = new Vendedor();
        vendedor.setIdVendedor(Integer.parseInt(params.get(0)));
        vendedor.setIdUsuario(Integer.parseInt(params.get(1)));
        if (params.size() > 2) {
            vendedor.setComisionPorcentaje(new BigDecimal(params.get(2)));
        }
        if (params.size() > 3) {
            vendedor.setTotalVentas(new BigDecimal(params.get(3)));
        }

        vendedorDAO.update(vendedor);
    }

    public void eliminarVendedor(int id) throws SQLException {
        vendedorDAO.delete(id);
    }

    public String getComandosVendedor() {
         return "Comandos Vendedor:\n" +
             "- vendedor listar\n" +
             "- vendedor agregar id_usuario, comision_porcentaje?, total_ventas?\n" +
             "- vendedor modificar id, id_usuario, comision_porcentaje?, total_ventas?\n" +
             "- vendedor eliminar id";
    }
}
