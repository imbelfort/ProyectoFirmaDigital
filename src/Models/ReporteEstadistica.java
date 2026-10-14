package Models;

import java.sql.Timestamp;

public class ReporteEstadistica {
    private int id;
    private int idUsuario;
    private String tipoReporte;
    private String parametros;
    private String resultadoResumen;
    private Timestamp fechaGeneracion;

    public ReporteEstadistica() {}

    public ReporteEstadistica(int id, int idUsuario, String tipoReporte, String parametros, String resultadoResumen, Timestamp fechaGeneracion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipoReporte = tipoReporte;
        this.parametros = parametros;
        this.resultadoResumen = resultadoResumen;
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }
    public String getResultadoResumen() { return resultadoResumen; }
    public void setResultadoResumen(String resultadoResumen) { this.resultadoResumen = resultadoResumen; }
    public Timestamp getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Timestamp fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}
