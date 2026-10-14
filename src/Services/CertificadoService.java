package Services;

import DAO.CertificadoDAO;
import Models.CertificadoOficial;
import Utils.ParamValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CertificadoService {

    private CertificadoDAO certDAO;

    public CertificadoService() {
        this.certDAO = new CertificadoDAO();
    }

    public void generarCertificado(List<String> params) throws SQLException {
        String sintaxis = "certificado generar [id_tramite;id_firma;titulo]";
        String ejemplo = "certificado generar [1;1;Certificado Oficial de Calificaciones]";

        int idTramite = ParamValidator.requireInt(params, 0, "id_tramite", sintaxis, ejemplo);
        int idFirma = ParamValidator.requireInt(params, 1, "id_firma", sintaxis, ejemplo);
        String titulo = ParamValidator.requireString(params, 2, "titulo", sintaxis, ejemplo);

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
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Falta el código de verificación del certificado.\n" +
                    "• Sintaxis requerida: certificado consultar [codigo_verificacion]\n" +
                    "• Ejemplo válido: certificado consultar [4f2a1b9c-8d3e-4f5a-b6c7-d8e9f0a1b2c3]"
            );
        }
        return certDAO.consultarPorCodigo(codigo.trim());
    }

    public String getComandosCertificado() {
        return "Comandos Certificado Oficial con QR (CU6):\n" +
                "- certificado listar\n" +
                "- certificado generar [id_tramite;id_firma;titulo]\n" +
                "- certificado consultar [codigo_verificacion]";
    }
}
