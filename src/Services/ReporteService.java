package Services;

import DAO.ReporteDAO;

import java.sql.SQLException;
import java.util.List;

public class ReporteService {

    private ReporteDAO reporteDAO;

    public ReporteService() {
        this.reporteDAO = new ReporteDAO();
    }

    public List<String[]> reporteTramitesPorEstado() throws SQLException {
        return reporteDAO.reporteTramitesPorEstado();
    }

    public List<String[]> reporteCumplimientoSla() throws SQLException {
        return reporteDAO.reporteCumplimientoSla();
    }

    public List<String[]> reporteFirmasPorVicerrector() throws SQLException {
        return reporteDAO.reporteFirmasPorVicerrector();
    }

    public String getComandosReporte() {
        return "Comandos Reportes y Estadisticas (CU8):\n" +
                "- reporte tramites\n" +
                "- reporte sla\n" +
                "- reporte firmas";
    }
}