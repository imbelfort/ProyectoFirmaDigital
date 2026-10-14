package Models;

public class PromocionCategoria {

    private int idPromocion;
    private int idCategoria;
    private boolean aplicaMayorista;
    private boolean aplicaMinorista;

    public PromocionCategoria() {
    }

    public PromocionCategoria(int idPromocion, int idCategoria, boolean aplicaMayorista, boolean aplicaMinorista) {
        this.idPromocion = idPromocion;
        this.idCategoria = idCategoria;
        this.aplicaMayorista = aplicaMayorista;
        this.aplicaMinorista = aplicaMinorista;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isAplicaMayorista() {
        return aplicaMayorista;
    }

    public void setAplicaMayorista(boolean aplicaMayorista) {
        this.aplicaMayorista = aplicaMayorista;
    }

    public boolean isAplicaMinorista() {
        return aplicaMinorista;
    }

    public void setAplicaMinorista(boolean aplicaMinorista) {
        this.aplicaMinorista = aplicaMinorista;
    }

    @Override
    public String toString() {
        return "PromocionCategoria{" +
                "idPromocion=" + idPromocion +
                ", idCategoria=" + idCategoria +
                ", aplicaMayorista=" + aplicaMayorista +
                ", aplicaMinorista=" + aplicaMinorista +
                '}';
    }
}