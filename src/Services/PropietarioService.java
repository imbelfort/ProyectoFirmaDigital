package Services;

import DAO.PropietarioDAO;
import Models.Propietario;
import java.sql.SQLException;
import java.util.List;

public class PropietarioService {
    private PropietarioDAO propietarioDAO;

    public PropietarioService() {
        this.propietarioDAO = new PropietarioDAO();
    }

    public void crearPropietario(List<String> params) throws SQLException {
        if (params.size() < 1) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Propietario: id_usuario");
        }

        Propietario propietario = new Propietario();
        propietario.setIdUsuario(Integer.parseInt(params.get(0)));
        if (params.size() > 1) {
            propietario.setNivelAcceso(params.get(1));
        }

        propietarioDAO.create(propietario);
    }

    public Propietario obtenerPropietarioPorId(int id) throws SQLException {
        return propietarioDAO.readById(id);
    }

    public Propietario obtenerPropietarioPorUsuario(int idUsuario) throws SQLException {
        return propietarioDAO.readByUsuarioId(idUsuario);
    }

    public List<Propietario> listarPropietarios() throws SQLException {
        return propietarioDAO.listAll();
    }

    public void actualizarPropietario(List<String> params) throws SQLException {
        if (params.size() < 2) {
            throw new IllegalArgumentException("ParÃ¡metros insuficientes para Propietario: id, id_usuario");
        }

        Propietario propietario = new Propietario();
        propietario.setIdPropietario(Integer.parseInt(params.get(0)));
        propietario.setIdUsuario(Integer.parseInt(params.get(1)));
        if (params.size() > 2) {
            propietario.setNivelAcceso(params.get(2));
        }

        propietarioDAO.update(propietario);
    }

    public void eliminarPropietario(int id) throws SQLException {
        propietarioDAO.delete(id);
    }

    public String getComandosPropietario() {
        return "Comandos Propietario:\n" +
               "- propietario listar\n" +
               "- propietario agregar id_usuario,nivel_acceso?\n" +
               "- propietario modificar id,id_usuario,nivel_acceso\n" +
               "- propietario eliminar id\n" +
               "Niveles de acceso vÃ¡lidos: total, limitado";
    }
}
