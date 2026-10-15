package Utils;

import java.sql.Date;
import java.util.List;

/**
 * Validador estricto y específico de parámetros para comandos de correo / GUI.
 * Proporciona mensajes de error claros, detallados y sin ambigüedad.
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

    public static int requireInt(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") está vacío.\n" +
                    "• Ingrese un valor numérico entero válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor '" + val + "' para [" + paramName + "] (parámetro #" + (index + 1) + ") no es un número entero válido.\n" +
                    "• Error: Ingresó texto o símbolos en un campo que requiere solo números enteros (sin letras ni puntos).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static double requireDouble(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") está vacío.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        try {
            return Double.parseDouble(val.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor '" + val + "' para [" + paramName + "] (parámetro #" + (index + 1) + ") no es un número decimal válido.\n" +
                    "• Ingrese un valor numérico (ej. 50.00 ó 100).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static String requireString(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "] (parámetro #" + (index + 1) + ")." + getParamHint(params) + "\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] (parámetro #" + (index + 1) + ") no puede estar en blanco.\n" +
                    "• Ingrese el dato correspondiente para este campo.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val;
    }

    public static String requireEmail(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.contains("@") || !val.contains(".") || val.contains(" ")) {
            throw new IllegalArgumentException(
                    "El correo electrónico '" + val + "' ingresado para [" + paramName + "] no tiene un formato válido.\n" +
                    "• Debe contener '@' y un dominio válido sin espacios (ejemplo: usuario@uagrm.edu.bo ó usuario@gmail.com).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val.toLowerCase();
    }

    public static String requireRol(List<String> params, int index, String defaultValue, String commandSyntax, String example) {
        String val = optionalString(params, index, defaultValue).toUpperCase();
        if (!val.equals("ALUMNO") && !val.equals("DOCENTE") && !val.equals("AUTORIDAD")
                && !val.equals("VICERRECTOR") && !val.equals("ENCARGADO")
                && !val.equals("ADMINISTRATIVO") && !val.equals("ADMINISTRADOR")) {
            throw new IllegalArgumentException(
                    "El rol '" + val + "' ingresado no es válido.\n" +
                    "• Roles institucionales permitidos: ALUMNO, DOCENTE, VICERRECTOR, ENCARGADO, ADMINISTRATIVO.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val;
    }

    public static String requirePrioridad(List<String> params, int index, String defaultValue, String commandSyntax, String example) {
        String val = optionalString(params, index, defaultValue).toUpperCase();
        if (!val.equals("BAJA") && !val.equals("NORMAL") && !val.equals("MEDIA")
                && !val.equals("ALTA") && !val.equals("URGENTE")) {
            throw new IllegalArgumentException(
                    "La prioridad '" + val + "' no es válida.\n" +
                    "• Valores permitidos para prioridad: BAJA, NORMAL, MEDIA, ALTA, URGENTE.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val;
    }

    public static String optionalString(List<String> params, int index, String defaultValue) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        return val.isEmpty() ? defaultValue : val;
    }

    public static int optionalInt(List<String> params, int index, int defaultValue, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor opcional '" + val + "' para [" + paramName + "] no es un número entero válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static double optionalDouble(List<String> params, int index, double defaultValue, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            return defaultValue;
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(val.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor opcional '" + val + "' para [" + paramName + "] no es un número decimal válido.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static Date requireDate(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        try {
            return Date.valueOf(val);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "La fecha '" + val + "' para [" + paramName + "] no tiene el formato válido AAAA-MM-DD.\n" +
                    "• Ingrese año-mes-día con guiones (ejemplo: 2026-10-15).\n" +
                    "• No use barras (/) ni día primero.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static String requireNumericCode(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "El código o registro ingresado para [" + paramName + "] ('" + val + "') contiene caracteres inválidos.\n" +
                    "• Regla institucional: Este campo debe ser estrictamente numérico (solo dígitos del 0 al 9).\n" +
                    "• Ejemplos válidos: 219064748 (alumno), 1045 (docente), 2026001 (trámite).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val;
    }
}
