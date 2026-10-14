package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializarDB {

    private static void ejecutarSentencias(Statement stmt, String... sentencias) throws SQLException {
        for (String sentencia : sentencias) {
            stmt.executeUpdate(sentencia);
        }
    }

    private static void cerrarRecursos(Statement stmt, Connection con) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexion: " + e.getMessage());
        }
    }

    public static void eliminarTodasLasTablas() {
        Connection con = null;
        Statement stmt = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();

            if (con == null) {
                System.err.println("No hay conexion disponible. No se eliminaron tablas.");
                return;
            }

            stmt = con.createStatement();

            ejecutarSentencias(stmt,
                    "DROP TABLE IF EXISTS reporte_estadistica CASCADE",
                    "DROP TABLE IF EXISTS alerta_sla CASCADE",
                    "DROP TABLE IF EXISTS monitoreo_sla CASCADE",
                    "DROP TABLE IF EXISTS portal_seguimiento CASCADE",
                    "DROP TABLE IF EXISTS certificado_oficial CASCADE",
                    "DROP TABLE IF EXISTS despacho_legajo CASCADE",
                    "DROP TABLE IF EXISTS firma_digital CASCADE",
                    "DROP TABLE IF EXISTS bitacora_tramite CASCADE",
                    "DROP TABLE IF EXISTS observacion_tramite CASCADE",
                    "DROP TABLE IF EXISTS flujo_tramite CASCADE",
                    "DROP TABLE IF EXISTS guia_procesal CASCADE",
                    "DROP TABLE IF EXISTS documento_expediente CASCADE",
                    "DROP TABLE IF EXISTS requisito CASCADE",
                    "DROP TABLE IF EXISTS tramite CASCADE",
                    "DROP TABLE IF EXISTS tipo_tramite CASCADE",
                    "DROP TABLE IF EXISTS indicador_gestion CASCADE",
                    "DROP TABLE IF EXISTS delegacion_autoridad CASCADE",
                    "DROP TABLE IF EXISTS autoridad CASCADE",
                    "DROP TABLE IF EXISTS docente CASCADE",
                    "DROP TABLE IF EXISTS alumno CASCADE",
                    "DROP TABLE IF EXISTS usuario CASCADE"
            );

            System.out.println("[OK] Todas las tablas del Vicerrectorado fueron eliminadas correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar tablas: " + e.getMessage());
        } finally {
            cerrarRecursos(stmt, con);
        }
    }

    public static void crearTablasSiNoExisten() {
        Connection con = null;
        Statement stmt = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();

            if (con == null) {
                System.err.println("No hay conexion disponible a PostgreSQL. Se omite creacion de tablas.");
                return;
            }

            stmt = con.createStatement();

            String createUsuario =
                    "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id_usuario SERIAL PRIMARY KEY," +
                    "registro VARCHAR(50) UNIQUE NOT NULL," +
                    "ci VARCHAR(30) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "nombre VARCHAR(100) NOT NULL," +
                    "apellidos VARCHAR(100) NOT NULL," +
                    "email VARCHAR(150) UNIQUE NOT NULL," +
                    "telefono VARCHAR(30)," +
                    "rol VARCHAR(50) NOT NULL," +
                    "unidad_academica VARCHAR(150)," +
                    "cargo VARCHAR(100)," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO'," +
                    "fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            String createAlumno =
                    "CREATE TABLE IF NOT EXISTS alumno (" +
                    "id_alumno INTEGER PRIMARY KEY," +
                    "carrera VARCHAR(120) NOT NULL," +
                    "facultad VARCHAR(120) NOT NULL," +
                    "semestre INTEGER DEFAULT 1," +
                    "estado_academico VARCHAR(50) DEFAULT 'REGULAR'," +
                    "CONSTRAINT fk_alumno_usuario FOREIGN KEY (id_alumno) REFERENCES usuario(id_usuario) ON DELETE CASCADE" +
                    ");";

            String createDocente =
                    "CREATE TABLE IF NOT EXISTS docente (" +
                    "id_docente INTEGER PRIMARY KEY," +
                    "facultad VARCHAR(120) NOT NULL," +
                    "departamento VARCHAR(120)," +
                    "categoria_docente VARCHAR(50) DEFAULT 'TIEMPO_COMPLETO'," +
                    "CONSTRAINT fk_docente_usuario FOREIGN KEY (id_docente) REFERENCES usuario(id_usuario) ON DELETE CASCADE" +
                    ");";

            String createAutoridad =
                    "CREATE TABLE IF NOT EXISTS autoridad (" +
                    "id_autoridad INTEGER PRIMARY KEY," +
                    "tipo_autoridad VARCHAR(50) NOT NULL," +
                    "resolucion_nombramiento VARCHAR(100)," +
                    "fecha_posesion DATE," +
                    "CONSTRAINT fk_autoridad_usuario FOREIGN KEY (id_autoridad) REFERENCES usuario(id_usuario) ON DELETE CASCADE" +
                    ");";

            String createDelegacionAutoridad =
                    "CREATE TABLE IF NOT EXISTS delegacion_autoridad (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_autoridad_titular INTEGER NOT NULL," +
                    "id_autoridad_delegada INTEGER NOT NULL," +
                    "resolucion VARCHAR(100) NOT NULL," +
                    "motivo TEXT NOT NULL," +
                    "fecha_inicio DATE NOT NULL," +
                    "fecha_fin DATE NOT NULL," +
                    "alcance_firma VARCHAR(100) DEFAULT 'TOTAL'," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA'," +
                    "fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "CONSTRAINT fk_delegacion_titular FOREIGN KEY (id_autoridad_titular) REFERENCES usuario(id_usuario)," +
                    "CONSTRAINT fk_delegacion_delegado FOREIGN KEY (id_autoridad_delegada) REFERENCES usuario(id_usuario)" +
                    ");";

            String createIndicadorGestion =
                    "CREATE TABLE IF NOT EXISTS indicador_gestion (" +
                    "id SERIAL PRIMARY KEY," +
                    "codigo VARCHAR(50) UNIQUE NOT NULL," +
                    "nombre VARCHAR(150) NOT NULL," +
                    "descripcion TEXT," +
                    "meta_esperada NUMERIC(10,2) NOT NULL," +
                    "valor_actual NUMERIC(10,2) DEFAULT 0," +
                    "unidad_medida VARCHAR(30) NOT NULL," +
                    "periodo VARCHAR(50) DEFAULT 'MENSUAL'," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO'," +
                    "fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            String createTipoTramite =
                    "CREATE TABLE IF NOT EXISTS tipo_tramite (" +
                    "id SERIAL PRIMARY KEY," +
                    "codigo VARCHAR(50) UNIQUE NOT NULL," +
                    "nombre VARCHAR(150) NOT NULL," +
                    "descripcion TEXT," +
                    "tiempo_sla_horas INTEGER NOT NULL DEFAULT 72," +
                    "costo_bs NUMERIC(10,2) DEFAULT 0.00," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO'" +
                    ");";

            String createRequisito =
                    "CREATE TABLE IF NOT EXISTS requisito (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tipo_tramite INTEGER NOT NULL," +
                    "nombre VARCHAR(150) NOT NULL," +
                    "descripcion TEXT," +
                    "es_obligatorio BOOLEAN NOT NULL DEFAULT TRUE," +
                    "formato_permitido VARCHAR(50) DEFAULT 'PDF'," +
                    "CONSTRAINT fk_requisito_tipo_tramite FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id) ON DELETE CASCADE" +
                    ");";

            String createTramite =
                    "CREATE TABLE IF NOT EXISTS tramite (" +
                    "id SERIAL PRIMARY KEY," +
                    "codigo_tramite VARCHAR(50) UNIQUE NOT NULL," +
                    "id_usuario_solicitante INTEGER NOT NULL," +
                    "id_tipo_tramite INTEGER NOT NULL," +
                    "asunto VARCHAR(200) NOT NULL," +
                    "estado VARCHAR(50) NOT NULL DEFAULT 'INICIADO'," +
                    "prioridad VARCHAR(30) NOT NULL DEFAULT 'NORMAL'," +
                    "fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_finalizacion TIMESTAMP," +
                    "CONSTRAINT fk_tramite_solicitante FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario)," +
                    "CONSTRAINT fk_tramite_tipo FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id)" +
                    ");";

            String createDocumentoExpediente =
                    "CREATE TABLE IF NOT EXISTS documento_expediente (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_requisito INTEGER," +
                    "nombre_documento VARCHAR(200) NOT NULL," +
                    "tipo_documento VARCHAR(50) DEFAULT 'PDF'," +
                    "archivo_url TEXT," +
                    "hash_documento VARCHAR(128)," +
                    "estado_validacion VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE'," +
                    "observacion_validacion TEXT," +
                    "fecha_validacion TIMESTAMP," +
                    "CONSTRAINT fk_doc_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_doc_requisito FOREIGN KEY (id_requisito) REFERENCES requisito(id)" +
                    ");";

            String createGuiaProcesal =
                    "CREATE TABLE IF NOT EXISTS guia_procesal (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tipo_tramite INTEGER NOT NULL," +
                    "paso_numero INTEGER NOT NULL," +
                    "nombre_etapa VARCHAR(150) NOT NULL," +
                    "descripcion_instrucciones TEXT NOT NULL," +
                    "rol_responsable VARCHAR(50) NOT NULL," +
                    "tiempo_estimado_horas INTEGER DEFAULT 24," +
                    "CONSTRAINT fk_guia_tipo_tramite FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id) ON DELETE CASCADE" +
                    ");";

            String createFlujoTramite =
                    "CREATE TABLE IF NOT EXISTS flujo_tramite (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "etapa_actual VARCHAR(100) NOT NULL," +
                    "id_usuario_responsable INTEGER," +
                    "fecha_inicio_etapa TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_fin_etapa TIMESTAMP," +
                    "estado_etapa VARCHAR(30) NOT NULL DEFAULT 'EN_PROCESO'," +
                    "CONSTRAINT fk_flujo_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_flujo_responsable FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario)" +
                    ");";

            String createObservacionTramite =
                    "CREATE TABLE IF NOT EXISTS observacion_tramite (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_usuario INTEGER NOT NULL," +
                    "tipo_observacion VARCHAR(50) NOT NULL," +
                    "descripcion TEXT NOT NULL," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'ABIERTA'," +
                    "fecha_observacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_subsanacion TIMESTAMP," +
                    "CONSTRAINT fk_obs_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_obs_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)" +
                    ");";

            String createBitacoraTramite =
                    "CREATE TABLE IF NOT EXISTS bitacora_tramite (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_usuario INTEGER," +
                    "accion VARCHAR(100) NOT NULL," +
                    "detalle TEXT," +
                    "ip_origen VARCHAR(45)," +
                    "fecha_evento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "CONSTRAINT fk_bitacora_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_bitacora_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)" +
                    ");";

            String createFirmaDigital =
                    "CREATE TABLE IF NOT EXISTS firma_digital (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_firmante INTEGER NOT NULL," +
                    "tipo_firma VARCHAR(50) NOT NULL," +
                    "algoritmo VARCHAR(50) NOT NULL DEFAULT 'SHA256withRSA'," +
                    "hash_documento VARCHAR(128) NOT NULL," +
                    "firma_digital_valor TEXT NOT NULL," +
                    "numero_certificado_digital VARCHAR(100) NOT NULL," +
                    "fecha_firma TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "valido BOOLEAN NOT NULL DEFAULT TRUE," +
                    "CONSTRAINT fk_firma_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_firma_firmante FOREIGN KEY (id_firmante) REFERENCES usuario(id_usuario)" +
                    ");";

            String createDespachoLegajo =
                    "CREATE TABLE IF NOT EXISTS despacho_legajo (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_usuario_despachador INTEGER NOT NULL," +
                    "numero_despacho VARCHAR(50) UNIQUE NOT NULL," +
                    "legajo_previsualizado_url TEXT," +
                    "tipo_entrega VARCHAR(50) NOT NULL DEFAULT 'DIGITAL_CORREO'," +
                    "fecha_despacho TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'DESPACHADO'," +
                    "CONSTRAINT fk_despacho_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_despacho_usuario FOREIGN KEY (id_usuario_despachador) REFERENCES usuario(id_usuario)" +
                    ");";

            String createCertificadoOficial =
                    "CREATE TABLE IF NOT EXISTS certificado_oficial (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER NOT NULL," +
                    "id_firma_digital INTEGER NOT NULL," +
                    "codigo_verificacion VARCHAR(64) UNIQUE NOT NULL," +
                    "qr_data TEXT NOT NULL," +
                    "titulo_documento VARCHAR(200) NOT NULL," +
                    "fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_expiracion DATE," +
                    "portal_url VARCHAR(255) NOT NULL," +
                    "estado VARCHAR(30) NOT NULL DEFAULT 'EMITIDO'," +
                    "CONSTRAINT fk_cert_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_cert_firma FOREIGN KEY (id_firma_digital) REFERENCES firma_digital(id)" +
                    ");";

            String createPortalSeguimiento =
                    "CREATE TABLE IF NOT EXISTS portal_seguimiento (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER UNIQUE NOT NULL," +
                    "pin_seguimiento VARCHAR(20) NOT NULL," +
                    "portal_activo BOOLEAN NOT NULL DEFAULT TRUE," +
                    "total_visitas INTEGER DEFAULT 0," +
                    "ultimo_acceso TIMESTAMP," +
                    "CONSTRAINT fk_portal_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE" +
                    ");";

            String createMonitoreoSla =
                    "CREATE TABLE IF NOT EXISTS monitoreo_sla (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_tramite INTEGER UNIQUE NOT NULL," +
                    "horas_sla_limite INTEGER NOT NULL DEFAULT 72," +
                    "fecha_inicio_sla TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "fecha_limite_sla TIMESTAMP NOT NULL," +
                    "horas_transcurridas NUMERIC(6,2) DEFAULT 0," +
                    "estado_sla VARCHAR(30) NOT NULL DEFAULT 'EN_TIEMPO'," +
                    "alertas_emitidas INTEGER DEFAULT 0," +
                    "ultima_alerta TIMESTAMP," +
                    "CONSTRAINT fk_sla_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE" +
                    ");";

            String createAlertaSla =
                    "CREATE TABLE IF NOT EXISTS alerta_sla (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_monitoreo INTEGER NOT NULL," +
                    "id_tramite INTEGER NOT NULL," +
                    "tipo_alerta VARCHAR(50) NOT NULL," +
                    "mensaje TEXT NOT NULL," +
                    "fecha_alerta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "notificado_a VARCHAR(150)," +
                    "CONSTRAINT fk_alerta_monitoreo FOREIGN KEY (id_monitoreo) REFERENCES monitoreo_sla(id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_alerta_tramite FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE" +
                    ");";

            String createReporteEstadistica =
                    "CREATE TABLE IF NOT EXISTS reporte_estadistica (" +
                    "id SERIAL PRIMARY KEY," +
                    "id_usuario INTEGER NOT NULL," +
                    "tipo_reporte VARCHAR(100) NOT NULL," +
                    "parametros TEXT," +
                    "resultado_resumen TEXT," +
                    "fecha_generacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "CONSTRAINT fk_reporte_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)" +
                    ");";

            ejecutarSentencias(stmt,
                    createUsuario,
                    createAlumno,
                    createDocente,
                    createAutoridad,
                    createDelegacionAutoridad,
                    createIndicadorGestion,
                    createTipoTramite,
                    createRequisito,
                    createTramite,
                    createDocumentoExpediente,
                    createGuiaProcesal,
                    createFlujoTramite,
                    createObservacionTramite,
                    createBitacoraTramite,
                    createFirmaDigital,
                    createDespachoLegajo,
                    createCertificadoOficial,
                    createPortalSeguimiento,
                    createMonitoreoSla,
                    createAlertaSla,
                    createReporteEstadistica
            );

            System.out.println("[OK] Todas las tablas de Firma Digital del Vicerrectorado fueron verificadas/creadas correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        } finally {
            cerrarRecursos(stmt, con);
        }
    }

    public static void insertarDatosIniciales() {
        Connection con = null;
        Statement stmt = null;

        try {
            con = Conexion.getInstancia().EstablecerConexion();

            if (con == null) {
                System.err.println("No hay conexion disponible a PostgreSQL. Se omite insercion de datos iniciales.");
                return;
            }

            stmt = con.createStatement();

            ejecutarSentencias(stmt,
                    "INSERT INTO usuario (registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo) " +
                    "SELECT '1001', '1234567', '1234567', 'Reinerio', 'Vargas', 'vicerrector@uagrm.edu.bo', '71000001', 'VICERRECTOR', 'Vicerrectorado', 'Vicerrector General' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE registro = '1001')",

                    "INSERT INTO usuario (registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo) " +
                    "SELECT '1002', '2345678', '2345678', 'Carlos', 'Mendoza', 'encargado.firma@uagrm.edu.bo', '71000002', 'ENCARGADO', 'Despacho Vicerrectorado', 'Encargado de Despacho y Firma' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE registro = '1002')",

                    "INSERT INTO usuario (registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo) " +
                    "SELECT '1045', '3456789', '3456789', 'Alberto', 'Guzman', 'aguzman@uagrm.edu.bo', '71000003', 'DOCENTE', 'Facultad de Ingenieria en Ciencias de la Computacion', 'Docente Titular' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE registro = '1045')",

                    "INSERT INTO usuario (registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo) " +
                    "SELECT '219064748', '4567890', '4567890', 'Juan', 'Perez Rodriguez', 'juan.perez@alu.uagrm.edu.bo', '71000004', 'ALUMNO', 'Ingenieria Informatica', 'Estudiante Regular' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE registro = '219064748')",

                    "INSERT INTO tipo_tramite (codigo, nombre, descripcion, tiempo_sla_horas, costo_bs) " +
                    "SELECT 'DIPLOMA_ACAD', 'Diploma Academico', 'Emision y firma digital de Diploma Academico de Grado', 72, 350.00 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM tipo_tramite WHERE codigo = 'DIPLOMA_ACAD')",

                    "INSERT INTO tipo_tramite (codigo, nombre, descripcion, tiempo_sla_horas, costo_bs) " +
                    "SELECT 'TIT_PROV_NAC', 'Titulo en Provision Nacional', 'Emision y firma vicerrectoral de Titulo en Provision Nacional', 72, 450.00 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM tipo_tramite WHERE codigo = 'TIT_PROV_NAC')",

                    "INSERT INTO tipo_tramite (codigo, nombre, descripcion, tiempo_sla_horas, costo_bs) " +
                    "SELECT 'CERT_NOTAS', 'Certificado de Notas Legalizado', 'Legalizacion con firma digital de historial academico completo', 72, 50.00 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM tipo_tramite WHERE codigo = 'CERT_NOTAS')",

                    "INSERT INTO indicador_gestion (codigo, nombre, descripcion, meta_esperada, valor_actual, unidad_medida, periodo) " +
                    "SELECT 'IND-SLA-72H', 'Tasa Cumplimiento SLA 72 Horas', 'Porcentaje de tramites despachados en 72 horas habiles', 95.00, 92.50, 'PORCENTAJE', 'MENSUAL' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM indicador_gestion WHERE codigo = 'IND-SLA-72H')"
            );

            System.out.println("[OK] Datos iniciales del Vicerrectorado verificados/insertados correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al insertar datos iniciales: " + e.getMessage());
        } finally {
            cerrarRecursos(stmt, con);
        }
    }
}