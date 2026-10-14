package Utils;

import java.sql.Date;
import java.util.List;

/**
 * Validador estricto y específico de parámetros para comandos de correo / GUI.
 * Proporciona mensajes de error claros, detallados y sin ambigüedad.
 */
public class ParamValidator {

    public static int requireInt(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "].\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] está vacío.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor '" + val + "' para [" + paramName + "] no es un número entero válido.\n" +
                    "• Se requiere un número entero positivo (sin letras ni puntos).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static double requireDouble(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "].\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] está vacío.\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        try {
            return Double.parseDouble(val.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El valor '" + val + "' para [" + paramName + "] no es un número decimal válido.\n" +
                    "• Ingrese un valor numérico (ej. 50.00 ó 100).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static String requireString(List<String> params, int index, String paramName, String commandSyntax, String example) {
        if (params == null || index >= params.size()) {
            throw new IllegalArgumentException(
                    "Falta el parámetro obligatorio [" + paramName + "].\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        String val = params.get(index) != null ? params.get(index).trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(
                    "El parámetro [" + paramName + "] no puede estar en blanco.\n" +
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
                    "• Ingrese año-mes-día (ejemplo: 2026-10-14).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
    }

    public static String requireNumericCode(List<String> params, int index, String paramName, String commandSyntax, String example) {
        String val = requireString(params, index, paramName, commandSyntax, example);
        if (!val.matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "El código o registro ingresado para [" + paramName + "] ('" + val + "') contiene letras o caracteres inválidos.\n" +
                    "• Regla del Vicerrectorado: Códigos puramente numéricos (ej. 219064748 para alumno, 1045 para docente, 1001 para autoridad).\n" +
                    "• Sintaxis requerida: " + commandSyntax + "\n" +
                    "• Ejemplo válido: " + example
            );
        }
        return val;
    }
}
