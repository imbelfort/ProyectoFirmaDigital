package Utils;

public class Token {

    private int name;
    private int attribute;

    public static final int CU = 0;
    public static final int ACTION = 1;
    public static final int PARAMS = 2;
    public static final int END = 3;
    public static final int ERROR = 4;

    public static final int USUARIO = 100;
    public static final int CLIENTE = 101;
    public static final int VENDEDOR = 102;
    public static final int PROPIETARIO = 103;

    public static final int PROVEEDOR = 104;
    public static final int CATEGORIA = 105;
    public static final int PRODUCTO = 106;
    public static final int PRODUCTO_VARIANTE = 107;

    public static final int TECNICA_INVENTARIO = 108;
    public static final int TECNICA_COSTO = 109;

    public static final int PROMOCION = 110;
    public static final int PROMOCION_CATEGORIA = 111;
    public static final int PROMOCION_PRODUCTO = 112;

    public static final int COMPRA = 113;
    public static final int DETALLE_COMPRA = 114;

    public static final int CARRITO = 115;
    public static final int CARRITO_DETALLE = 130;

    public static final int PEDIDO = 116;
    public static final int DETALLE_PEDIDO = 117;

    public static final int VENTA = 118;
    public static final int DETALLE_VENTA = 119;

    public static final int MOVIMIENTO_INVENTARIO = 120;

    public static final int METODO_PAGO = 121;
    public static final int PAGO_CONTADO = 122;
    public static final int CREDITO = 123;
    public static final int PAGO_CUOTA = 124;

    public static final int REPORTE = 125;

    public static final int LOTE_INVENTARIO = 126;
    public static final int SALIDA_LOTE = 127;

    public static final int ELIMINAR = 200;
    public static final int MODIFICAR = 201;
    public static final int LISTAR = 202;
    public static final int AGREGAR = 203;
    public static final int MOSTRAR = 204;
    public static final int GENERAR = 205;
    public static final int HELP = 206;
    public static final int CONSULTAR = 207;

    public static final int ERROR_COMMAND = 300;
    public static final int ERROR_CHARACTER = 301;

    public static final String LEXEME_CU = "caso de uso";
    public static final String LEXEME_ACTION = "action";
    public static final String LEXEME_PARAMS = "params";
    public static final String LEXEME_END = "end";
    public static final String LEXEME_ERROR = "error";

    public static final String LEXEME_USUARIO = "usuario";
    public static final String LEXEME_CLIENTE = "cliente";
    public static final String LEXEME_VENDEDOR = "vendedor";
    public static final String LEXEME_PROPIETARIO = "propietario";

    public static final String LEXEME_PROVEEDOR = "proveedor";
    public static final String LEXEME_CATEGORIA = "categoria";
    public static final String LEXEME_PRODUCTO = "producto";
    public static final String LEXEME_PRODUCTO_VARIANTE = "producto_variante";

    public static final String LEXEME_TECNICA_INVENTARIO = "tecnica_inventario";
    public static final String LEXEME_TECNICA_COSTO = "tecnica_costo";

    public static final String LEXEME_PROMOCION = "promocion";
    public static final String LEXEME_PROMOCION_CATEGORIA = "promocion_categoria";
    public static final String LEXEME_PROMOCION_PRODUCTO = "promocion_producto";

    public static final String LEXEME_COMPRA = "compra";
    public static final String LEXEME_DETALLE_COMPRA = "detalle_compra";

    public static final String LEXEME_CARRITO = "carrito";

    public static final String LEXEME_CARRITO_DETALLE = "carrito_detalle";
    public static final String LEXEME_PEDIDO = "pedido";
    public static final String LEXEME_DETALLE_PEDIDO = "pedido_detalle";

    public static final String LEXEME_VENTA = "venta";
    public static final String LEXEME_DETALLE_VENTA = "detalle_venta";
    public static final String LEXEME_SALIDA_LOTE = "salida_lote";
    public static final String LEXEME_MOVIMIENTO_INVENTARIO = "movimiento_inventario";
    public static final String LEXEME_LOTE_INVENTARIO = "lote_inventario";
    public static final String LEXEME_METODO_PAGO = "metodo_pago";
    public static final String LEXEME_PAGO_CONTADO = "pago_contado";
    public static final String LEXEME_CREDITO = "credito";
    public static final String LEXEME_PAGO_CUOTA = "pago_cuota";

    public static final String LEXEME_REPORTE = "reporte";

    public static final String LEXEME_ELIMINAR = "eliminar";
    public static final String LEXEME_MODIFICAR = "modificar";
    public static final String LEXEME_LISTAR = "listar";
    public static final String LEXEME_AGREGAR = "agregar";
    public static final String LEXEME_MOSTRAR = "mostrar";
    public static final String LEXEME_GENERAR = "generar";
    public static final String LEXEME_CONSULTAR = "consultar";
    public static final String LEXEME_HELP = "help";

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
            case CU:
                return LEXEME_CU;
            case ACTION:
                return LEXEME_ACTION;
            case PARAMS:
                return LEXEME_PARAMS;
            case END:
                return LEXEME_END;
            case ERROR:
                return LEXEME_ERROR;

            case USUARIO:
                return LEXEME_USUARIO;
            case CLIENTE:
                return LEXEME_CLIENTE;
            case VENDEDOR:
                return LEXEME_VENDEDOR;
            case PROPIETARIO:
                return LEXEME_PROPIETARIO;

            case PROVEEDOR:
                return LEXEME_PROVEEDOR;
            case CATEGORIA:
                return LEXEME_CATEGORIA;
            case PRODUCTO:
                return LEXEME_PRODUCTO;
            case PRODUCTO_VARIANTE:
                return LEXEME_PRODUCTO_VARIANTE;

            
            case LOTE_INVENTARIO:
                return LEXEME_LOTE_INVENTARIO; 

            case TECNICA_INVENTARIO:
                return LEXEME_TECNICA_INVENTARIO;
            case TECNICA_COSTO:
                return LEXEME_TECNICA_COSTO;

            case PROMOCION:
                return LEXEME_PROMOCION;
            case PROMOCION_CATEGORIA:
                return LEXEME_PROMOCION_CATEGORIA;
            case PROMOCION_PRODUCTO:
                return LEXEME_PROMOCION_PRODUCTO;

            case COMPRA:
                return LEXEME_COMPRA;
            case DETALLE_COMPRA:
                return LEXEME_DETALLE_COMPRA;

            case CARRITO:
                return LEXEME_CARRITO;
            
            case CARRITO_DETALLE:
                return LEXEME_CARRITO_DETALLE;


            case PEDIDO:
                return LEXEME_PEDIDO;
            case DETALLE_PEDIDO:
                return LEXEME_DETALLE_PEDIDO;

            case VENTA:
                return LEXEME_VENTA;
            case DETALLE_VENTA:
                return LEXEME_DETALLE_VENTA;

            case SALIDA_LOTE:
                return LEXEME_SALIDA_LOTE;

            case MOVIMIENTO_INVENTARIO:
                return LEXEME_MOVIMIENTO_INVENTARIO;

            case METODO_PAGO:
                return LEXEME_METODO_PAGO;
            case PAGO_CONTADO:
                return LEXEME_PAGO_CONTADO;
            case CREDITO:
                return LEXEME_CREDITO;
            case PAGO_CUOTA:
                return LEXEME_PAGO_CUOTA;

            case REPORTE:
                return LEXEME_REPORTE;

            case AGREGAR:
                return LEXEME_AGREGAR;
            case ELIMINAR:
                return LEXEME_ELIMINAR;
            case MODIFICAR:
                return LEXEME_MODIFICAR;
            case LISTAR:
                return LEXEME_LISTAR;
            case MOSTRAR:
                return LEXEME_MOSTRAR;
            case GENERAR:
                return LEXEME_GENERAR;
            case CONSULTAR:
                return LEXEME_CONSULTAR;
            case HELP:
                return LEXEME_HELP;

            case ERROR_COMMAND:
                return LEXEME_ERROR_COMMAND;
            case ERROR_CHARACTER:
                return LEXEME_ERROR_CHARACTER;
            default:
                return "N: " + token;
        }
    }

    private int findByLexeme(String lexeme) {
        if (lexeme == null) {
            return -1;
        }

        String normalized = lexeme.trim().toLowerCase();

        switch (normalized) {
            case LEXEME_CU:
                return CU;
            case LEXEME_ACTION:
                return ACTION;
            case LEXEME_PARAMS:
                return PARAMS;
            case LEXEME_END:
                return END;
            case LEXEME_ERROR:
                return ERROR;

            case LEXEME_USUARIO:
                return USUARIO;
            case LEXEME_CLIENTE:
                return CLIENTE;
            case LEXEME_VENDEDOR:
                return VENDEDOR;
            case LEXEME_PROPIETARIO:
                return PROPIETARIO;

            case LEXEME_PROVEEDOR:
                return PROVEEDOR;
            case LEXEME_CATEGORIA:
                return CATEGORIA;
            case LEXEME_PRODUCTO:
                return PRODUCTO;
            case LEXEME_PRODUCTO_VARIANTE:
                return PRODUCTO_VARIANTE;

            case LEXEME_TECNICA_INVENTARIO:
                return TECNICA_INVENTARIO;
            case LEXEME_TECNICA_COSTO:
                return TECNICA_COSTO;

            case LEXEME_PROMOCION:
                return PROMOCION;
            case LEXEME_PROMOCION_CATEGORIA:
                return PROMOCION_CATEGORIA;
            case LEXEME_PROMOCION_PRODUCTO:
                return PROMOCION_PRODUCTO;

            case LEXEME_COMPRA:
                return COMPRA;
            case LEXEME_DETALLE_COMPRA:
                return DETALLE_COMPRA;

            case LEXEME_LOTE_INVENTARIO:
                return LOTE_INVENTARIO;

            case LEXEME_CARRITO:
                return CARRITO;

            case LEXEME_CARRITO_DETALLE:
                return CARRITO_DETALLE;

            case LEXEME_PEDIDO:
                return PEDIDO;

            case LEXEME_DETALLE_PEDIDO:
                return DETALLE_PEDIDO;

            case LEXEME_VENTA:
                return VENTA;

            case LEXEME_DETALLE_VENTA:
                return DETALLE_VENTA;

            case LEXEME_SALIDA_LOTE:
                return SALIDA_LOTE;

            case LEXEME_MOVIMIENTO_INVENTARIO:
                return MOVIMIENTO_INVENTARIO;

            case LEXEME_METODO_PAGO:
                return METODO_PAGO;

            case LEXEME_PAGO_CONTADO:
                return PAGO_CONTADO;

            case LEXEME_CREDITO:
                return CREDITO;

            case LEXEME_PAGO_CUOTA:
                return PAGO_CUOTA;

            case LEXEME_REPORTE:
                return REPORTE;

            case LEXEME_ELIMINAR:
                return ELIMINAR;

            case LEXEME_MODIFICAR:
                return MODIFICAR;

            case LEXEME_LISTAR:
                return LISTAR;

            case LEXEME_AGREGAR:
                return AGREGAR;

            case LEXEME_MOSTRAR:
                return MOSTRAR;

            case LEXEME_GENERAR:
                return GENERAR;

            case LEXEME_CONSULTAR:
                return CONSULTAR;

            case LEXEME_HELP:
                return HELP;

            case LEXEME_ERROR_COMMAND:
                return ERROR_COMMAND;

            case LEXEME_ERROR_CHARACTER:
                return ERROR_CHARACTER;
            default:
                return -1;
        }
    }
}