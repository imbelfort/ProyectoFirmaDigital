package Services;

import DAO.ReporteDAO;

import java.sql.SQLException;
import java.util.List;

public class ReporteService {

    private final ReporteDAO reporteDAO;

    public ReporteService() {
        this.reporteDAO = new ReporteDAO();
    }

    public List<String[]> reporteVentasUltimoMes() throws SQLException {
        return reporteDAO.ventasUltimoMes();
    }

    public List<String[]> reporteVentasUltimoTrimestre() throws SQLException {
        return reporteDAO.ventasUltimoTrimestre();
    }

    public List<String[]> reporteVentasUltimoAnio() throws SQLException {
        return reporteDAO.ventasUltimoAnio();
    }

    public List<String[]> reporteClientesUltimoMes() throws SQLException {
        return reporteDAO.clientesRegistradosUltimoMes();
    }

    public List<String[]> reporteVentasUltimoAnioPorMetodoPago() throws SQLException {
        return reporteDAO.ventasUltimoAnioPorMetodoPago();
    }

    public String getComandosReporte() {
        return "Comandos Reporte:\n" +
                "- reporte listar ventas_mes\n" +
                "- reporte listar ventas_trimestre\n" +
                "- reporte listar ventas_anio\n" +
                "- reporte listar clientes_mes\n" +
                "- reporte listar ventas_anio_metodo_pago\n" +
                "- reporte help";
    }
}