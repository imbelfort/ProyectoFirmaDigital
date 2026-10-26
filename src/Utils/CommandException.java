package Utils;

/**
 * Excepción explícita para errores de validación de sintaxis, tipos y lógica de comandos.
 */
public class CommandException extends RuntimeException {

    private final String errorCode;
    private final String suggestion;

    public CommandException(String errorCode, String message, String suggestion) {
        super(message);
        this.errorCode = errorCode;
        this.suggestion = suggestion;
    }

    public CommandException(String errorCode, String message) {
        this(errorCode, message, "Consulte 'help' para ver el manual completo de comandos.");
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getSuggestion() {
        return suggestion;
    }
}
