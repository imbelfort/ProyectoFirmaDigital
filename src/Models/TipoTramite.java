package Models;

public class TipoTramite {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int tiempoSlaHoras;
    private double costoBs;
    private String estado;

    public TipoTramite() {
        this.tiempoSlaHoras = 72;
        this.costoBs = 0.0;
        this.estado = "ACTIVO";
    }

    public TipoTramite(int id, String codigo, String nombre, String descripcion, int tiempoSlaHoras, double costoBs, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoSlaHoras = tiempoSlaHoras;
        this.costoBs = costoBs;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getTiempoSlaHoras() { return tiempoSlaHoras; }
    public void setTiempoSlaHoras(int tiempoSlaHoras) { this.tiempoSlaHoras = tiempoSlaHoras; }
    public double getCostoBs() { return costoBs; }
    public void setCostoBs(double costoBs) { this.costoBs = costoBs; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
