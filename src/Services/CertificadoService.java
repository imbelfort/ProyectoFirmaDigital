package Services;

import DAO.CertificadoDAO;
import Models.CertificadoOficial;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CertificadoService {

    private CertificadoDAO certDAO;

    public CertificadoService() {
        this.certDAO = new CertificadoDAO();
    }

    public void generarCertificado(List<String> params) throws SQLException {
        // [id_tramite; id_firma; titulo]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: certificado generar [id_tramite;id_firma;titulo]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        int idFirma = Integer.parseInt(params.get(1).trim());
        String titulo = params.get(2).trim();

        String uuid = UUID.randomUUID().toString();
        String portalUrl = "http://validar.uagrm.edu.bo/cert/" + uuid;
        String qrData = "UAGRM-CERT-OFICIAL|" + uuid + "|" + idTramite;

        CertificadoOficial c = new CertificadoOficial();
        c.setIdTramite(idTramite);
        c.setIdFirmaDigital(idFirma);
        c.setCodigoVerificacion(uuid);
        c.setQrData(qrData);
        c.setTituloDocumento(titulo);
        c.setPortalUrl(portalUrl);
        c.setEstado("EMITIDO");

        certDAO.create(c);
    }

    public List<String[]> listarCertificados() throws SQLException {
        return certDAO.listAll();
    }

    public String[] consultarCertificado(String codigo) throws SQLException {
        return certDAO.consultarPorCodigo(codigo.trim());
    }

    public String getComandosCertificado() {
        return "Comandos Certificado Oficial con QR (CU6):\n" +
                "- certificado listar\n" +
                "- certificado generar [id_tramite;id_firma;titulo]\n" +
                "- certificado consultar [codigo_verificacion]";
    }
}
