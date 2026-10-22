package Models;

public class GuiaProcesal {
    private int id;
    private int idTipoTramite;
    private int pasoNumero;
    private String nombreEtapa;
    private String descripcionInstrucciones;
    private String rolResponsable;
    private int tiempoEstimadoHoras;

    public GuiaProcesal() {
    }

    public GuiaProcesal(int id, int idTipoTramite, int pasoNumero, String nombreEtapa,
                        String descripcionInstrucciones, String rolResponsable, int tiempoEstimadoHoras) {
        this.id = id;
        this.idTipoTramite = idTipoTramite;
        this.pasoNumero = pasoNumero;
        this.nombreEtapa = nombreEtapa;
        this.descripcionInstrucciones = descripcionInstrucciones;
        this.rolResponsable = rolResponsable;
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdTipoTramite() { return idTipoTramite; }
    public void setIdTipoTramite(int idTipoTramite) { this.idTipoTramite = idTipoTramite; }
    public int getPasoNumero() { return pasoNumero; }
    public void setPasoNumero(int pasoNumero) { this.pasoNumero = pasoNumero; }
    public String getNombreEtapa() { return nombreEtapa; }
    public void setNombreEtapa(String nombreEtapa) { this.nombreEtapa = nombreEtapa; }
    public String getDescripcionInstrucciones() { return descripcionInstrucciones; }
    public void setDescripcionInstrucciones(String descripcionInstrucciones) { this.descripcionInstrucciones = descripcionInstrucciones; }
    public String getRolResponsable() { return rolResponsable; }
    public void setRolResponsable(String rolResponsable) { this.rolResponsable = rolResponsable; }
    public int getTiempoEstimadoHoras() { return tiempoEstimadoHoras; }
    public void setTiempoEstimadoHoras(int tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; }
}
