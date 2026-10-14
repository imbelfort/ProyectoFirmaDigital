package Services;

import DAO.FirmaDAO;
import Models.FirmaDigital;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

public class FirmaService {

    private FirmaDAO firmaDAO;

    public FirmaService() {
        this.firmaDAO = new FirmaDAO();
    }

    public void ejecutarFirma(List<String> params, String tipoFirma) throws SQLException {
        // [id_tramite; id_firmante; numero_certificado]
        if (params.size() < 3) {
            throw new IllegalArgumentException("Parametros insuficientes. Formato: firma " + tipoFirma.toLowerCase() + " [id_tramite;id_firmante;numero_certificado]");
        }
        int idTramite = Integer.parseInt(params.get(0).trim());
        int idFirmante = Integer.parseInt(params.get(1).trim());
        String cert = params.get(2).trim();

        String hash = Long.toHexString(System.currentTimeMillis()) + "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String valorFirma = Base64.getEncoder().encodeToString(("DIGITAL-SIGNATURE-" + idTramite + "-" + idFirmante + "-" + System.currentTimeMillis()).getBytes());

        FirmaDigital f = new FirmaDigital();
        f.setIdTramite(idTramite);
        f.setIdFirmante(idFirmante);
        f.setTipoFirma(tipoFirma.toUpperCase());
        f.setAlgoritmo("SHA256withRSA");
        f.setHashDocumento(hash);
        f.setFirmaDigitalValor(valorFirma);
        f.setNumeroCertificadoDigital(cert);
        f.setValido(true);

        firmaDAO.create(f);
    }

    public List<String[]> listarFirmas() throws SQLException {
        return firmaDAO.listAll();
    }

    public String getComandosFirma() {
        return "Comandos Firma Digital (CU5):\n" +
                "- firma listar\n" +
                "- firma consolidada [id_tramite;id_firmante;numero_certificado]\n" +
                "- firma remota [id_tramite;id_firmante;numero_certificado]";
    }
}
