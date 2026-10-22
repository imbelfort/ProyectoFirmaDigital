package Services;

import DAO.BitacoraDAO;
import Models.BitacoraTramite;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;

public class BitacoraService {

    private BitacoraDAO bitacoraDAO;

    public BitacoraService() {
        this.bitacoraDAO = new BitacoraDAO();
    }

    public void registrarEvento(int idTramite, Integer idUsuario, String accion, String detalle, String ipOrigen) throws SQLException {
        BitacoraTramite b = new BitacoraTramite();
        b.setIdTramite(idTramite);
        b.setIdUsuario(idUsuario);
        b.setAccion(accion);
        b.setDetalle(detalle);
        b.setIpOrigen(ipOrigen);
        bitacoraDAO.create(b);
    }

    public void registrarEventoManual(List<String> params) throws SQLException {
        String sintaxis = "bitacora agregar [id_tramite;id_usuario;accion;detalle]";
        String ejemplo = "bitacora agregar [1;1001;REVISION_MANUAL;Verificacion fisica de documentos en kardex]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int idUsuario = ParamValidator.requireInt(params, 1, "id_usuario", sintaxis, ejemplo);
        String accion = ParamValidator.requireString(params, 2, "accion", sintaxis, ejemplo).toUpperCase();
        String detalle = ParamValidator.requireString(params, 3, "detalle", sintaxis, ejemplo);

        registrarEvento(idTramite, idUsuario, accion, detalle, "127.0.0.1");
    }

    public List<String[]> listarBitacora() throws SQLException {
        return bitacoraDAO.listAll();
    }

    public List<String[]> consultarPorTramite(List<String> params) throws SQLException {
        String sintaxis = "bitacora consultar [id_tramite]";
        String ejemplo = "bitacora consultar [1]";
        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        return bitacoraDAO.listByTramite(idTramite);
    }

    public String getComandosBitacora() {
        return "Comandos Bitacora de Auditoria (CU4):\n" +
                "- bitacora listar\n" +
                "- bitacora consultar [id_tramite]\n" +
                "- bitacora agregar [id_tramite;id_usuario;accion;detalle]";
    }
}
