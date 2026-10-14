package Models;

public class Propietario {
    private int idPropietario;
    private int idUsuario;
    private String nivelAcceso; // total, limitado

    public Propietario() {
        this.nivelAcceso = "total";
    }

    public Propietario(int idPropietario, int idUsuario, String nivelAcceso) {
        this.idPropietario = idPropietario;
        this.idUsuario = idUsuario;
        this.nivelAcceso = nivelAcceso;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String toString() {
        return "Propietario{" +
                "idPropietario=" + idPropietario +
                ", idUsuario=" + idUsuario +
                ", nivelAcceso='" + nivelAcceso + '\'' +
                '}';
    }
}
