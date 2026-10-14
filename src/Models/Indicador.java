package Models;

public class Indicador {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double metaEsperada;
    private double valorActual;
    private String unidadMedida;
    private String periodo;
    private String estado;

    public Indicador() {
        this.unidadMedida = "HORAS";
        this.periodo = "MENSUAL";
        this.estado = "ACTIVO";
    }

    public Indicador(int id, String codigo, String nombre, String descripcion,
                     double metaEsperada, double valorActual, String unidadMedida, String periodo, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.metaEsperada = metaEsperada;
        this.valorActual = valorActual;
        this.unidadMedida = unidadMedida;
        this.periodo = periodo;
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
    public double getMetaEsperada() { return metaEsperada; }
    public void setMetaEsperada(double metaEsperada) { this.metaEsperada = metaEsperada; }
    public double getValorActual() { return valorActual; }
    public void setValorActual(double valorActual) { this.valorActual = valorActual; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
