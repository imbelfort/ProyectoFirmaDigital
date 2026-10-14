package Services;

public class PagoCuotaResultado {

    private boolean qrGenerado;
    private String htmlRespuesta;

    public boolean isQrGenerado() {
        return qrGenerado;
    }

    public void setQrGenerado(boolean qrGenerado) {
        this.qrGenerado = qrGenerado;
    }

    public String getHtmlRespuesta() {
        return htmlRespuesta;
    }

    public void setHtmlRespuesta(String htmlRespuesta) {
        this.htmlRespuesta = htmlRespuesta;
    }
}