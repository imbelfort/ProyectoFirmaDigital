package Services;

import DAO.ClienteDAO;
import Models.Cliente;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ClienteService {
    private ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public void crearCliente(List<String> params) throws SQLException {
        if (params.size() < 2) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Cliente: id_usuario, tipo_cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(Integer.parseInt(params.get(0)));
        cliente.setTipoCliente(params.get(1));
        if (params.size() > 2) {
            cliente.setLimiteCredito(new BigDecimal(params.get(2)));
        }
        if (params.size() > 3) {
            cliente.setCreditoDisponible(new BigDecimal(params.get(3)));
        }
        if (params.size() > 4) {
            cliente.setCreditoUtilizado(new BigDecimal(params.get(4)));
        }

        clienteDAO.create(cliente);
    }

    public Cliente obtenerClientePorId(int id) throws SQLException {
        return clienteDAO.readById(id);
    }

    public Cliente obtenerClientePorUsuario(int idUsuario) throws SQLException {
        return clienteDAO.readByUsuarioId(idUsuario);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listAll();
    }

    public List<Cliente> listarClientesPorTipo(String tipoCliente) throws SQLException {
        return clienteDAO.listByTipo(tipoCliente);
    }

    public void actualizarCliente(List<String> params) throws SQLException {
        if (params.size() < 3) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Cliente: id, id_usuario, tipo_cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setIdCliente(Integer.parseInt(params.get(0)));
        cliente.setIdUsuario(Integer.parseInt(params.get(1)));
        cliente.setTipoCliente(params.get(2));
        if (params.size() > 3) {
            cliente.setLimiteCredito(new BigDecimal(params.get(3)));
        }
        if (params.size() > 4) {
            cliente.setCreditoDisponible(new BigDecimal(params.get(4)));
        }
        if (params.size() > 5) {
            cliente.setCreditoUtilizado(new BigDecimal(params.get(5)));
        }

        clienteDAO.update(cliente);
    }

    public void eliminarCliente(int id) throws SQLException {
        clienteDAO.delete(id);
    }

    public String getComandosCliente() {
        return "Comandos Cliente:\n" +
               "- cliente listar\n" +
               "- cliente listar_tipo minorista, mayorista\n" +
               "- cliente agregar id_usuario,tipo_cliente,limite_credito?,credito_disponible?,credito_utilizado?\n" +
               "- cliente modificar id,id_usuario,tipo_cliente,limite_credito?,credito_disponible?,credito_utilizado?\n" +
               "- cliente eliminar id";
    }
}
