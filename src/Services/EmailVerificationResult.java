package Services;

public class EmailVerificationResult {

    private boolean valido;
    private boolean formatoValido;
    private boolean dominioValido;
    private boolean entregable;
    private boolean desechable;
    private String razon;

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public boolean isFormatoValido() {
        return formatoValido;
    }

    public void setFormatoValido(boolean formatoValido) {
        this.formatoValido = formatoValido;
    }

    public boolean isDominioValido() {
        return dominioValido;
    }

    public void setDominioValido(boolean dominioValido) {
        this.dominioValido = dominioValido;
    }

    public boolean isEntregable() {
        return entregable;
    }

    public void setEntregable(boolean entregable) {
        this.entregable = entregable;
    }

    public boolean isDesechable() {
        return desechable;
    }

    public void setDesechable(boolean desechable) {
        this.desechable = desechable;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }
}