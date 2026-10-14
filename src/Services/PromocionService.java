package Services;

import DAO.PromocionDAO;
import Models.Promocion;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PromocionService {

    private final PromocionDAO promocionDAO;

    public PromocionService() {
        this.promocionDAO = new PromocionDAO();
    }

    public void crearPromocion(List<String> params) throws SQLException {
        if (params.size() < 6) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para Promocion. " +
                    "Formato: promocion agregar id_propietario,nombre,descripcion?,porcentaje_descuento,fecha_inicio,fecha_fin,estado?"
            );
        }

        Promocion promocion = new Promocion();

        promocion.setIdPropietario(parseIntRequired(params.get(0), "id_propietario"));
        promocion.setNombre(parseStringRequired(params.get(1), "nombre"));
        promocion.setDescripcion(getParam(params, 2));
        promocion.setPorcentajeDescuento(parseBigDecimalRequired(params.get(3), "porcentaje_descuento"));
        promocion.setFechaInicio(parseDateRequired(params.get(4), "fecha_inicio"));
        promocion.setFechaFin(parseDateRequired(params.get(5), "fecha_fin"));
        promocion.setEstado(getParamOrDefault(params, 6, "ACTIVA"));

        validarPromocion(promocion);

        promocionDAO.create(promocion);
    }

    public String[] obtenerPromocionPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la promocion debe ser mayor a 0");
        }

        return promocionDAO.readById(id);
    }

    public List<String[]> listarPromociones() throws SQLException {
        return promocionDAO.listAll();
    }

    public void actualizarPromocion(List<String> params) throws SQLException {
        if (params.size() < 8) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes para modificar Promocion. " +
                    "Formato: promocion modificar id,id_propietario,nombre,descripcion,porcentaje_descuento,fecha_inicio,fecha_fin,estado"
            );
        }

        Promocion promocion = new Promocion();

        promocion.setId(parseIntRequired(params.get(0), "id"));
        promocion.setIdPropietario(parseIntRequired(params.get(1), "id_propietario"));
        promocion.setNombre(parseStringRequired(params.get(2), "nombre"));
        promocion.setDescripcion(getParam(params, 3));
        promocion.setPorcentajeDescuento(parseBigDecimalRequired(params.get(4), "porcentaje_descuento"));
        promocion.setFechaInicio(parseDateRequired(params.get(5), "fecha_inicio"));
        promocion.setFechaFin(parseDateRequired(params.get(6), "fecha_fin"));
        promocion.setEstado(getParamOrDefault(params, 7, "ACTIVA"));

        if (!promocionDAO.existsById(promocion.getId())) {
            throw new IllegalArgumentException("No existe una promocion con id: " + promocion.getId());
        }

        validarPromocion(promocion);

        promocionDAO.update(promocion);
    }

    public void eliminarPromocion(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de la promocion debe ser mayor a 0");
        }

        if (!promocionDAO.existsById(id)) {
            throw new IllegalArgumentException("No existe una promocion con id: " + id);
        }

        promocionDAO.delete(id);
    }

    public String getComandosPromocion() {
        return "Comandos Promocion:\n" +
                "- promocion listar\n" +
                "- promocion agregar id_propietario,nombre,descripcion?,porcentaje_descuento,fecha_inicio,fecha_fin,estado?\n" +
                "- promocion modificar id,id_propietario,nombre,descripcion,porcentaje_descuento,fecha_inicio,fecha_fin,estado\n" +
                "- promocion eliminar id\n\n" +
                "Estados permitidos:\n" +
                "ACTIVA, INACTIVA, FINALIZADA";
    }

    private void validarPromocion(Promocion promocion) {
        if (promocion.getIdPropietario() <= 0) {
            throw new IllegalArgumentException("El id_propietario debe ser mayor a 0");
        }

        if (promocion.getNombre() == null || promocion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la promocion es obligatorio");
        }

        if (promocion.getPorcentajeDescuento() == null ||
                promocion.getPorcentajeDescuento().compareTo(BigDecimal.ZERO) < 0 ||
                promocion.getPorcentajeDescuento().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El porcentaje_descuento debe estar entre 0 y 100");
        }

        if (promocion.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha_inicio es obligatoria");
        }

        if (promocion.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha_fin es obligatoria");
        }

        if (promocion.getFechaFin().before(promocion.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha_fin no puede ser menor a la fecha_inicio");
        }

        validarEstado(promocion.getEstado());
    }

    private void validarEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        String normalizado = estado.trim().toUpperCase();

        if (!normalizado.equals("ACTIVA") &&
            !normalizado.equals("INACTIVA") &&
            !normalizado.equals("FINALIZADA")) {
            throw new IllegalArgumentException("Estado no valido. Use: ACTIVA, INACTIVA o FINALIZADA");
        }
    }

    private String getParam(List<String> params, int index) {
        if (params.size() <= index) {
            return null;
        }

        String value = params.get(index);

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    private String getParamOrDefault(List<String> params, int index, String defaultValue) {
        String value = getParam(params, index);
        return value != null ? value : defaultValue;
    }

    private int parseIntRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un numero entero");
        }
    }

    private String parseStringRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        return value.trim();
    }

    private BigDecimal parseBigDecimalRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser decimal");
        }
    }

    private Date parseDateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio");
        }

        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe tener formato yyyy-MM-dd");
        }
    }
}