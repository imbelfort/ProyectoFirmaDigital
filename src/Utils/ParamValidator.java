package Utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * Validador estricto y específico de parámetros para comandos de correo / GUI.
 * Proporciona mensajes de error claros, detallados y sin ambigüedad con códigos de error explícitos.
 */
public class ParamValidator {

    private static String getParamHint(List<String> params) {
        if (params == null || params.isEmpty()) {
            return "\n• No se proporcionaron parámetros. Los parámetros deben ir entre corchetes y separados por punto y coma: [param1;param2;...]";
        }
        if (params.size() == 1 && params.get(0).contains(" ")) {
            return "\n• AVISO DE FORMATO: Se recibió un único texto con espacios ('" + params.get(0) + "'). Los campos DEBEN separarse con punto y coma (;) entre corchetes [campo1;campo2;...], no con espacios.";
        }
        return "\n• Parámetros recibidos (" + params.size() + "): " + params + ". Faltan datos requeridos para completar este comando.";
    }

    /**
     * Sanitiza un texto eliminando caracteres de control y etiquetas peligrosas que rompen el flujo.
     */
    public static String sanitizar(String input) {
        if (input == null) return "";
        // Eliminar caracteres de control no imprimibles (ASCII 0..31 excepto \t, \n, \r)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 32 || c == '\t' || c == '\n' || c == '\r') {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }

    public static int requireInt(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        String val = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (val.isEmpty()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") está vacío.\n" +
                    "• Ingrese un valor numérico entero válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_001,
                    "El valor '" + val + "' para [" + paramName + "] (parámetro #" + (index + 1) + ") no es un número entero válido.\n" +
                    "• Error: Ingresó texto o símbolos en un campo que requiere solo números enteros (sin letras ni puntos).\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
    }

    public static double requireDouble(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        String val = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (val.isEmpty()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") está vacío.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        try {
            return Double.parseDouble(val.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_001,
                    "El valor '" + val + "' para [" + paramName + "] (parámetro #" + (index + 1) + ") no es un número decimal válido.\n" +
                    "• Ingrese un valor numérico (ej. 50.00 ó 100).\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
    }

    public static String requireString(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        String val = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (val.isEmpty()) {
            throw new CommandException(
                    ErrorCode.ERR_INC_002,
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") no puede estar en blanco.\n" +
                    "• Ingrese el dato correspondiente para este campo.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        // Validar que no contenga etiquetas HTML ni caracteres especiales que rompan el flujo
        if (val.contains("<") || val.contains(">") || val.toLowerCase().contains("javascript:") || val.contains("\0")) {
            throw new CommandException(
                    ErrorCode.ERR_CMD_004,
                    "El campo '" + paramName + "' contiene caracteres especiales no permitidos (<, >, etiquetas HTML o scripts).\n" +
                    "• Por seguridad institucional, no se admiten etiquetas ni inyecciones de código en los datos.",
                    example
            );
        }
        return val;
    }

    public static String requireEmail(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_005,
                    "El correo electrónico '" + val + "' ingresado para [" + paramName + "] no tiene un formato válido.\n" +
                    "• Debe contener '@' y un dominio válido (ejemplo: usuario@uagrm.edu.bo ó usuario@gmail.com).\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        return val.toLowerCase();
    }

    public static String requireTelefono(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^\\+?[0-9]{7,15}$")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_001,
                    "El teléfono '" + val + "' para [" + paramName + "] es inválido. Solo debe contener números (7 a 15 dígitos) y opcionalmente el prefijo '+'.",
                    example
            );
        }
        return val;
    }

    public static String requireRol(List<String> params, int index, String defaultValue, String commandSyntax, String example) {
        String val = optionalString(params, index, defaultValue).toUpperCase();
        if (!val.equals("ALUMNO") && !val.equals("DOCENTE") && !val.equals("AUTORIDAD")
                && !val.equals("VICERRECTOR") && !val.equals("ENCARGADO")
                && !val.equals("ADMINISTRATIVO") && !val.equals("ADMINISTRADOR")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_004,
                    "El rol '" + val + "' ingresado no es válido.\n" +
                    "• Roles institucionales permitidos: ALUMNO, DOCENTE, VICERRECTOR, ENCARGADO, ADMINISTRATIVO.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        return val;
    }

    public static String requirePrioridad(List<String> params, int index, String defaultValue, String commandSyntax, String example) {
        String val = optionalString(params, index, defaultValue).toUpperCase();
        if (!val.equals("BAJA") && !val.equals("NORMAL") && !val.equals("MEDIA")
                && !val.equals("ALTA") && !val.equals("URGENTE") && !val.equals("URGENTE_72H")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_004,
                    "La prioridad '" + val + "' no es válida.\n" +
                    "• Valores permitidos para prioridad: BAJA, NORMAL, MEDIA, ALTA, URGENTE.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        return val;
    }

    public static String optionalString(List<String> params, int index, String defaultValue) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String raw = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (raw.isEmpty()) {
            return defaultValue;
        }
        // Desinfectar caracteres que puedan romper tablas HTML
        if (raw.contains("<") || raw.contains(">")) {
            raw = raw.replace("<", "").replace(">", "");
        }
        return raw;
    }

    public static int optionalInt(List<String> params, int index, int defaultValue, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String val = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (val.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_001,
                    "El valor opcional '" + val + "' para [" + paramName + "] no es un número entero válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
    }

    public static double optionalDouble(List<String> params, int index, double defaultValue, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String val = params.get(index) != null ? sanitizar(params.get(index)) : "";
        if (val.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(val.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_001,
                    "El valor opcional '" + val + "' para [" + paramName + "] no es un número decimal válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
    }

    public static Date requireDate(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_002,
                    "Formato de fecha inválido para '" + val + "'. Se requiere el formato AAAA-MM-DD con guiones (ejemplo: 2026-10-15).\n" +
                    "• No use barras (/) ni día primero.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate localDate = LocalDate.parse(val, dtf);
            return Date.valueOf(localDate);
        } catch (Exception e) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_002,
                    "La fecha '" + val + "' no representa una fecha válida en el calendario (use formato AAAA-MM-DD).\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
    }

    public static String requireNumericCode(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^\\d+$")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_003,
                    "El código o registro ingresado para [" + paramName + "] ('" + val + "') contiene caracteres inválidos.\n" +
                    "• Regla institucional: Este campo debe ser estrictamente numérico (solo dígitos del 0 al 9).\n" +
                    "• Ejemplos válidos: 219064748 (alumno), 1045 (docente), 2026001 (trámite).\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        return val;
    }

    public static String requireCodigoTramite(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^[a-zA-Z0-9_-]+$")) {
            throw new CommandException(
                    ErrorCode.ERR_FMT_003,
                    "El código ingresado para [" + paramName + "] ('" + val + "') contiene caracteres inválidos.\n" +
                    "• Regla institucional: Solo debe contener caracteres alfanuméricos, guiones (-) o guiones bajos (_).\n" +
                    "• Ejemplos válidos: 2026001, TR-2026-001, TR-19347.\n" +
                    "• Sintaxis requerida: " + commandSyntax,
                    example
            );
        }
        return val;
    }
}
