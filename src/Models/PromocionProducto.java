package Models;

public class PromocionProducto {

    private int idPromocion;
    private int idProducto;
    private boolean aplicaMayorista;
    private boolean aplicaMinorista;

    public PromocionProducto() {
    }

    public PromocionProducto(int idPromocion, int idProducto, boolean aplicaMayorista, boolean aplicaMinorista) {
        this.idPromocion = idPromocion;
        this.idProducto = idProducto;
        this.aplicaMayorista = aplicaMayorista;
        this.aplicaMinorista = aplicaMinorista;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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
        return "PromocionProducto{" +
                "idPromocion=" + idPromocion +
                ", idProducto=" + idProducto +
                ", aplicaMayorista=" + aplicaMayorista +
                ", aplicaMinorista=" + aplicaMinorista +
                '}';
    }
}