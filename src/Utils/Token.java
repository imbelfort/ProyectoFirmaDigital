package Utils;

public class Token {

    private int name;
    private int attribute;

    public static final int CU = 0;
    public static final int ACTION = 1;
    public static final int PARAMS = 2;
    public static final int END = 3;
    public static final int ERROR = 4;

    // Entidades del Vicerrectorado (100 - 199)
    public static final int USUARIO = 100;
    public static final int DELEGACION = 101;
    public static final int INDICADOR = 102;
    public static final int TRAMITE = 103;
    public static final int DOCUMENTO = 104;
    public static final int TIPO_TRAMITE = 105;
    public static final int FLUJO = 106;
    public static final int OBSERVACION = 107;
    public static final int BITACORA = 108;
    public static final int FIRMA = 109;
    public static final int DESPACHO = 110;
    public static final int CERTIFICADO = 111;
    public static final int PORTAL = 112;
    public static final int SLA = 113;
    public static final int ALERTA = 114;
    public static final int REPORTE = 115;

    // Acciones de Comandos (200 - 299)
    public static final int LISTAR = 200;
    public static final int AGREGAR = 201;
    public static final int MODIFICAR = 202;
    public static final int ELIMINAR = 203;
    public static final int HELP = 204;
    public static final int VALIDAR = 205;
    public static final int AVANZAR = 206;
    public static final int CONSOLIDADA = 207;
    public static final int REMOTA = 208;
    public static final int GENERAR = 209;
    public static final int CONSULTAR = 210;
    public static final int VERIFICAR = 211;
    public static final int TRAMITES = 212;
    public static final int FIRMAS = 213;

    public static final int ERROR_COMMAND = 300;
    public static final int ERROR_CHARACTER = 301;

    public static final String LEXEME_CU = "caso de uso";
    public static final String LEXEME_ACTION = "action";
    public static final String LEXEME_PARAMS = "params";
    public static final String LEXEME_END = "end";
    public static final String LEXEME_ERROR = "error";

    public static final String LEXEME_USUARIO = "usuario";
    public static final String LEXEME_DELEGACION = "delegacion";
    public static final String LEXEME_INDICADOR = "indicador";
    public static final String LEXEME_TRAMITE = "tramite";
    public static final String LEXEME_DOCUMENTO = "documento";
    public static final String LEXEME_TIPO_TRAMITE = "tipo_tramite";
    public static final String LEXEME_FLUJO = "flujo";
    public static final String LEXEME_OBSERVACION = "observacion";
    public static final String LEXEME_BITACORA = "bitacora";
    public static final String LEXEME_FIRMA = "firma";
    public static final String LEXEME_DESPACHO = "despacho";
    public static final String LEXEME_CERTIFICADO = "certificado";
    public static final String LEXEME_PORTAL = "portal";
    public static final String LEXEME_SLA = "sla";
    public static final String LEXEME_ALERTA = "alerta";
    public static final String LEXEME_REPORTE = "reporte";

    public static final String LEXEME_LISTAR = "listar";
    public static final String LEXEME_AGREGAR = "agregar";
    public static final String LEXEME_MODIFICAR = "modificar";
    public static final String LEXEME_ELIMINAR = "eliminar";
    public static final String LEXEME_HELP = "help";
    public static final String LEXEME_VALIDAR = "validar";
    public static final String LEXEME_AVANZAR = "avanzar";
    public static final String LEXEME_CONSOLIDADA = "consolidada";
    public static final String LEXEME_REMOTA = "remota";
    public static final String LEXEME_GENERAR = "generar";
    public static final String LEXEME_CONSULTAR = "consultar";
    public static final String LEXEME_VERIFICAR = "verificar";
    public static final String LEXEME_TRAMITES = "tramites";
    public static final String LEXEME_FIRMAS = "firmas";

    public static final String LEXEME_ERROR_COMMAND = "UNKNOWN COMMAND";
    public static final String LEXEME_ERROR_CHARACTER = "UNKNOWN CHARACTER";

    public Token() {
    }

    public Token(String token) {
        int id = findByLexeme(token);

        if (id != -1) {
            if (100 <= id && id < 200) {
                this.name = CU;
                this.attribute = id;
            } else if (200 <= id && id < 300) {
                this.name = ACTION;
                this.attribute = id;
            } else {
                this.name = ERROR;
                this.attribute = ERROR_COMMAND;
            }
        } else {
            this.name = ERROR;
            this.attribute = ERROR_COMMAND;
        }
    }

    public Token(int name) {
        this.name = name;
    }

    public Token(int name, int attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        if (0 <= name && name <= 1) {
            return "< " + getStringToken(name) + " , " + getStringToken(attribute) + ">";
        } else if (name == 2) {
            return "< " + getStringToken(name) + " , " + attribute + ">";
        } else if (name == 3) {
            return "< " + getStringToken(name) + " , _______ >";
        } else if (name == 4) {
            return "< " + getStringToken(name) + " , " + getStringToken(attribute) + ">";
        }
        return "< TOKEN , DESCONOCIDO>";
    }

    public String getStringToken(int token) {
        switch (token) {
            case CU: return LEXEME_CU;
            case ACTION: return LEXEME_ACTION;
            case PARAMS: return LEXEME_PARAMS;
            case END: return LEXEME_END;
            case ERROR: return LEXEME_ERROR;

            case USUARIO: return LEXEME_USUARIO;
            case DELEGACION: return LEXEME_DELEGACION;
            case INDICADOR: return LEXEME_INDICADOR;
            case TRAMITE: return LEXEME_TRAMITE;
            case DOCUMENTO: return LEXEME_DOCUMENTO;
            case TIPO_TRAMITE: return LEXEME_TIPO_TRAMITE;
            case FLUJO: return LEXEME_FLUJO;
            case OBSERVACION: return LEXEME_OBSERVACION;
            case BITACORA: return LEXEME_BITACORA;
            case FIRMA: return LEXEME_FIRMA;
            case DESPACHO: return LEXEME_DESPACHO;
            case CERTIFICADO: return LEXEME_CERTIFICADO;
            case PORTAL: return LEXEME_PORTAL;
            case SLA: return LEXEME_SLA;
            case ALERTA: return LEXEME_ALERTA;
            case REPORTE: return LEXEME_REPORTE;

            case LISTAR: return LEXEME_LISTAR;
            case AGREGAR: return LEXEME_AGREGAR;
            case MODIFICAR: return LEXEME_MODIFICAR;
            case ELIMINAR: return LEXEME_ELIMINAR;
            case HELP: return LEXEME_HELP;
            case VALIDAR: return LEXEME_VALIDAR;
            case AVANZAR: return LEXEME_AVANZAR;
            case CONSOLIDADA: return LEXEME_CONSOLIDADA;
            case REMOTA: return LEXEME_REMOTA;
            case GENERAR: return LEXEME_GENERAR;
            case CONSULTAR: return LEXEME_CONSULTAR;
            case VERIFICAR: return LEXEME_VERIFICAR;
            case TRAMITES: return LEXEME_TRAMITES;
            case FIRMAS: return LEXEME_FIRMAS;

            case ERROR_COMMAND: return LEXEME_ERROR_COMMAND;
            case ERROR_CHARACTER: return LEXEME_ERROR_CHARACTER;
            default: return "N: " + token;
        }
    }

    private int findByLexeme(String lexeme) {
        if (lexeme == null) {
            return -1;
        }

        String normalized = lexeme.trim().toLowerCase();

        switch (normalized) {
            case LEXEME_CU: return CU;
            case LEXEME_ACTION: return ACTION;
            case LEXEME_PARAMS: return PARAMS;
            case LEXEME_END: return END;
            case LEXEME_ERROR: return ERROR;

            case LEXEME_USUARIO: return USUARIO;
            case LEXEME_DELEGACION: return DELEGACION;
            case LEXEME_INDICADOR: return INDICADOR;
            case LEXEME_TRAMITE: return TRAMITE;
            case LEXEME_DOCUMENTO: return DOCUMENTO;
            case LEXEME_TIPO_TRAMITE: return TIPO_TRAMITE;
            case LEXEME_FLUJO: return FLUJO;
            case LEXEME_OBSERVACION: return OBSERVACION;
            case LEXEME_BITACORA: return BITACORA;
            case LEXEME_FIRMA: return FIRMA;
            case LEXEME_DESPACHO: return DESPACHO;
            case LEXEME_CERTIFICADO: return CERTIFICADO;
            case LEXEME_PORTAL: return PORTAL;
            case LEXEME_SLA: return SLA;
            case LEXEME_ALERTA: return ALERTA;
            case LEXEME_REPORTE: return REPORTE;

            case LEXEME_LISTAR: return LISTAR;
            case LEXEME_AGREGAR: return AGREGAR;
            case LEXEME_MODIFICAR: return MODIFICAR;
            case LEXEME_ELIMINAR: return ELIMINAR;
            case LEXEME_HELP: return HELP;
            case LEXEME_VALIDAR: return VALIDAR;
            case LEXEME_AVANZAR: return AVANZAR;
            case LEXEME_CONSOLIDADA: return CONSOLIDADA;
            case LEXEME_REMOTA: return REMOTA;
            case LEXEME_GENERAR: return GENERAR;
            case LEXEME_CONSULTAR: return CONSULTAR;
            case LEXEME_VERIFICAR: return VERIFICAR;
            case LEXEME_TRAMITES: return TRAMITES;
            case LEXEME_FIRMAS: return FIRMAS;

            default: return -1;
        }
    }
}