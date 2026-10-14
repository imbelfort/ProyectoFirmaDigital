-- ============================================================
-- BASE DE DATOS: db_grupo20sa
-- SISTEMA DE FIRMA DIGITAL DEL VICERRECTORADO
-- UNIVERSIDAD AUTONOMA GABRIEL RENE MORENO (UAGRM)
-- GRUPO 20 SA - TECNOLOGIA WEB
-- ============================================================
-- REGLA DE CREDENCIALES Y CODIGOS (ESTRICTAMENTE NUMERICOS, SIN LETRAS):
-- * Alumnos: Registro Universitario numérico (ej. 219064748). Su C.I. es su contraseña.
-- * Docentes: Código de Docente numérico de 4 dígitos (ej. 1045). Su C.I. es su contraseña.
-- * Vicerrector / Encargado / Personal Administrativo: Código numérico de 4 dígitos (ej. 1001, 1002). Su C.I. es su contraseña.
-- ============================================================

-- ============================================================
-- ELIMINACION DE TABLAS (ORDEN INVERSO DE DEPENDENCIAS)
-- ============================================================
DROP TABLE IF EXISTS reporte_estadistica CASCADE;
DROP TABLE IF EXISTS alerta_sla CASCADE;
DROP TABLE IF EXISTS monitoreo_sla CASCADE;
DROP TABLE IF EXISTS portal_seguimiento CASCADE;
DROP TABLE IF EXISTS certificado_oficial CASCADE;
DROP TABLE IF EXISTS despacho_legajo CASCADE;
DROP TABLE IF EXISTS firma_digital CASCADE;
DROP TABLE IF EXISTS bitacora_tramite CASCADE;
DROP TABLE IF EXISTS observacion_tramite CASCADE;
DROP TABLE IF EXISTS flujo_tramite CASCADE;
DROP TABLE IF EXISTS guia_procesal CASCADE;
DROP TABLE IF EXISTS documento_expediente CASCADE;
DROP TABLE IF EXISTS requisito CASCADE;
DROP TABLE IF EXISTS tramite CASCADE;
DROP TABLE IF EXISTS tipo_tramite CASCADE;
DROP TABLE IF EXISTS indicador_gestion CASCADE;
DROP TABLE IF EXISTS delegacion_autoridad CASCADE;
DROP TABLE IF EXISTS autoridad CASCADE;
DROP TABLE IF EXISTS docente CASCADE;
DROP TABLE IF EXISTS alumno CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- ============================================================
-- CU1. GESTION DE USUARIOS (Vicerrector, Encargado, Docentes, Alumnos)
-- ============================================================

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    registro VARCHAR(50) UNIQUE NOT NULL,      -- Registro universitario o código de funcionario
    ci VARCHAR(30) UNIQUE NOT NULL,            -- Cédula de Identidad (actúa por defecto como contraseña)
    password VARCHAR(255) NOT NULL,            -- Contraseña (por defecto idéntica al CI)
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(30),
    rol VARCHAR(50) NOT NULL,                  -- 'VICERRECTOR', 'ENCARGADO', 'DOCENTE', 'ALUMNO', 'ADMINISTRATIVO'
    unidad_academica VARCHAR(150),             -- Facultad / Carrera / Dirección
    cargo VARCHAR(100),                        -- Cargo institucional
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('VICERRECTOR', 'ENCARGADO', 'DOCENTE', 'ALUMNO', 'ADMINISTRATIVO')),
    CONSTRAINT chk_usuario_estado
        CHECK (estado IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO'))
);

CREATE TABLE alumno (
    id_alumno INTEGER PRIMARY KEY,
    carrera VARCHAR(120) NOT NULL,
    facultad VARCHAR(120) NOT NULL,
    semestre INTEGER DEFAULT 1,
    estado_academico VARCHAR(50) DEFAULT 'REGULAR',
    CONSTRAINT fk_alumno_usuario
        FOREIGN KEY (id_alumno) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE docente (
    id_docente INTEGER PRIMARY KEY,
    facultad VARCHAR(120) NOT NULL,
    departamento VARCHAR(120),
    categoria_docente VARCHAR(50) DEFAULT 'TIEMPO_COMPLETO', -- 'TITULAR', 'ADJUNTO', 'TIEMPO_COMPLETO'
    CONSTRAINT fk_docente_usuario
        FOREIGN KEY (id_docente) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE autoridad (
    id_autoridad INTEGER PRIMARY KEY,
    tipo_autoridad VARCHAR(50) NOT NULL,       -- 'VICERRECTOR', 'ENCARGADO_DESPACHO'
    resolucion_nombramiento VARCHAR(100),
    fecha_posesion DATE,
    CONSTRAINT fk_autoridad_usuario
        FOREIGN KEY (id_autoridad) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- ============================================================
-- CU2. GESTION DE DELEGACION DE AUTORIDAD E INDICADORES DE GESTION
-- ============================================================

CREATE TABLE delegacion_autoridad (
    id SERIAL PRIMARY KEY,
    id_autoridad_titular INTEGER NOT NULL,     -- Vicerrector Titular que delega
    id_autoridad_delegada INTEGER NOT NULL,    -- Encargado o autoridad que asume
    resolucion VARCHAR(100) NOT NULL,          -- Nro. de Resolución Vicerrectoral
    motivo TEXT NOT NULL,                      -- Motivo de la delegación (ej: Viaje oficial, licencia)
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    alcance_firma VARCHAR(100) DEFAULT 'TOTAL',-- 'TOTAL', 'SOLO_TITULOS', 'CERTIFICACIONES'
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_delegacion_titular
        FOREIGN KEY (id_autoridad_titular) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_delegacion_delegado
        FOREIGN KEY (id_autoridad_delegada) REFERENCES usuario(id_usuario),
    CONSTRAINT chk_delegacion_estado
        CHECK (estado IN ('ACTIVA', 'FINALIZADA', 'REVOCADA'))
);

CREATE TABLE indicador_gestion (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    meta_esperada NUMERIC(10,2) NOT NULL,      -- Ej: 72.00 horas, 95.00%
    valor_actual NUMERIC(10,2) DEFAULT 0,
    unidad_medida VARCHAR(30) NOT NULL,        -- 'HORAS', 'PORCENTAJE', 'CANTIDAD'
    periodo VARCHAR(50) DEFAULT 'MENSUAL',     -- 'DIARIO', 'SEMANAL', 'MENSUAL', 'ANUAL'
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- CU3. GESTION DE TRAMITES, SELECCIONAR DOCUMENTOS, VALIDAR REQUISITOS Y COMPILAR EXPEDIENTE
-- ============================================================

CREATE TABLE tipo_tramite (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,        -- 'DIPLOMA_ACAD', 'TIT_PROV_NAC', 'CERT_NOTAS', 'LEGALIZACION'
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tiempo_sla_horas INTEGER NOT NULL DEFAULT 72, -- Tiempo estándar SLA: 72 horas hábiles
    costo_bs NUMERIC(10,2) DEFAULT 0.00,
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO'
);

CREATE TABLE requisito (
    id SERIAL PRIMARY KEY,
    id_tipo_tramite INTEGER NOT NULL,
    nombre VARCHAR(150) NOT NULL,              -- Ej: 'Fotocopia Legalizada CI', 'Certificado de Notas Original'
    descripcion TEXT,
    es_obligatorio BOOLEAN NOT NULL DEFAULT TRUE,
    formato_permitido VARCHAR(50) DEFAULT 'PDF',
    CONSTRAINT fk_requisito_tipo_tramite
        FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id) ON DELETE CASCADE
);

CREATE TABLE tramite (
    id SERIAL PRIMARY KEY,
    codigo_tramite VARCHAR(50) UNIQUE NOT NULL, -- Ej: 'TR-2026-0001'
    id_usuario_solicitante INTEGER NOT NULL,    -- Alumno o Docente que solicita
    id_tipo_tramite INTEGER NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'INICIADO',
    -- Estados: INICIADO, REQUISITOS_VALIDADOS, EXPEDIENTE_COMPILADO, EN_FIRMA, FIRMADO, DESPACHADO, OBSERVADO, RECHAZADO
    prioridad VARCHAR(30) NOT NULL DEFAULT 'NORMAL', -- 'NORMAL', 'URGENTE'
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_finalizacion TIMESTAMP,

    CONSTRAINT fk_tramite_solicitante
        FOREIGN KEY (id_usuario_solicitante) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_tramite_tipo
        FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id)
);

CREATE TABLE documento_expediente (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_requisito INTEGER,
    nombre_documento VARCHAR(200) NOT NULL,
    tipo_documento VARCHAR(50) DEFAULT 'PDF',
    archivo_url TEXT,
    hash_documento VARCHAR(128),               -- Hash SHA-256 del documento para integridad
    estado_validacion VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE', -- 'PENDIENTE', 'APROBADO', 'OBSERVADO', 'RECHAZADO'
    observacion_validacion TEXT,
    fecha_validacion TIMESTAMP,

    CONSTRAINT fk_doc_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_doc_requisito
        FOREIGN KEY (id_requisito) REFERENCES requisito(id)
);

-- ============================================================
-- CU4. GESTION DE ENROSTRAR FLUJO, VISUALIZAR GUIA PROCESAL, GESTIONAR OBSERVACIONES Y BITACORA
-- ============================================================

CREATE TABLE guia_procesal (
    id SERIAL PRIMARY KEY,
    id_tipo_tramite INTEGER NOT NULL,
    paso_numero INTEGER NOT NULL,
    nombre_etapa VARCHAR(150) NOT NULL,        -- 'Recepción', 'Revisión y Cotejo', 'Compilación', 'Firma Vicerrectoral', 'Despacho'
    descripcion_instrucciones TEXT NOT NULL,
    rol_responsable VARCHAR(50) NOT NULL,      -- 'ENCARGADO', 'VICERRECTOR'
    tiempo_estimado_horas INTEGER DEFAULT 24,

    CONSTRAINT fk_guia_tipo_tramite
        FOREIGN KEY (id_tipo_tramite) REFERENCES tipo_tramite(id) ON DELETE CASCADE
);

CREATE TABLE flujo_tramite (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    etapa_actual VARCHAR(100) NOT NULL,
    id_usuario_responsable INTEGER,
    fecha_inicio_etapa TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_fin_etapa TIMESTAMP,
    estado_etapa VARCHAR(30) NOT NULL DEFAULT 'EN_PROCESO', -- 'EN_PROCESO', 'COMPLETADO', 'OBSERVADO'

    CONSTRAINT fk_flujo_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_flujo_responsable
        FOREIGN KEY (id_usuario_responsable) REFERENCES usuario(id_usuario)
);

CREATE TABLE observacion_tramite (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,               -- Encargado o Vicerrector que emite la observación
    tipo_observacion VARCHAR(50) NOT NULL,     -- 'FALTA_REQUISITO', 'DOCUMENTO_ILEGIBLE', 'ERROR_DATOS', 'OTRO'
    descripcion TEXT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ABIERTA', -- 'ABIERTA', 'SUBSANADA', 'CERRADA'
    fecha_observacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_subsanacion TIMESTAMP,

    CONSTRAINT fk_obs_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_obs_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE bitacora_tramite (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_usuario INTEGER,
    accion VARCHAR(100) NOT NULL,              -- 'CREACION', 'VALIDO_REQUISITO', 'OBSERVO', 'AVANZO_FLUJO', 'FIRMO_DIGITALMENTE', 'DESPACHO'
    detalle TEXT,
    ip_origen VARCHAR(45),
    fecha_evento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bitacora_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_bitacora_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================================
-- CU5. GESTION DE DESPACHAR, PREVISUALIZAR LEGAJOS Y EJECUTAR FIRMA DIGITAL CONSOLIDADA O REMOTA
-- ============================================================

CREATE TABLE firma_digital (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_firmante INTEGER NOT NULL,              -- Vicerrector o Delegado
    tipo_firma VARCHAR(50) NOT NULL,           -- 'CONSOLIDADA' o 'REMOTA'
    algoritmo VARCHAR(50) NOT NULL DEFAULT 'SHA256withRSA',
    hash_documento VARCHAR(128) NOT NULL,      -- Hash criptográfico del expediente compilado
    firma_digital_valor TEXT NOT NULL,         -- Firma electrónica generada (Base64)
    numero_certificado_digital VARCHAR(100) NOT NULL, -- Nro. de Certificado digital ADSIB/UAGRM
    fecha_firma TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valido BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_firma_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_firma_firmante
        FOREIGN KEY (id_firmante) REFERENCES usuario(id_usuario),
    CONSTRAINT chk_tipo_firma
        CHECK (tipo_firma IN ('CONSOLIDADA', 'REMOTA'))
);

CREATE TABLE despacho_legajo (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_usuario_despachador INTEGER NOT NULL,
    numero_despacho VARCHAR(50) UNIQUE NOT NULL,
    legajo_previsualizado_url TEXT,
    tipo_entrega VARCHAR(50) NOT NULL DEFAULT 'DIGITAL_CORREO', -- 'DIGITAL_CORREO', 'PORTAL_DESCARGA', 'VENTANILLA'
    fecha_despacho TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'DESPACHADO',

    CONSTRAINT fk_despacho_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_despacho_usuario
        FOREIGN KEY (id_usuario_despachador) REFERENCES usuario(id_usuario)
);

-- ============================================================
-- CU6. GESTION DE CERTIFICADO OFICIAL CON QR Y HABILITAR PORTAL DE SEGUIMIENTO ESTUDIANTIL
-- ============================================================

CREATE TABLE certificado_oficial (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL,
    id_firma_digital INTEGER NOT NULL,
    codigo_verificacion VARCHAR(64) UNIQUE NOT NULL, -- Código UUID único de validación
    qr_data TEXT NOT NULL,                           -- Contenido o URL codificada en el QR
    titulo_documento VARCHAR(200) NOT NULL,          -- Ej: 'DIPLOMA ACADEMICO DE LICENCIATURA EN ING. INFORMATICA'
    fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion DATE,
    portal_url VARCHAR(255) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'EMITIDO',   -- 'EMITIDO', 'REVOCADO'

    CONSTRAINT fk_cert_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT fk_cert_firma
        FOREIGN KEY (id_firma_digital) REFERENCES firma_digital(id)
);

CREATE TABLE portal_seguimiento (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER UNIQUE NOT NULL,
    pin_seguimiento VARCHAR(20) NOT NULL,            -- PIN secreto de consulta para el alumno
    portal_activo BOOLEAN NOT NULL DEFAULT TRUE,
    total_visitas INTEGER DEFAULT 0,
    ultimo_acceso TIMESTAMP,

    CONSTRAINT fk_portal_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE
);

-- ============================================================
-- CU7. GESTION DE MONITOREO Y TIEMPOS DE ATENCION Y ALERTAS SLA (72 HORAS HABILES)
-- ============================================================

CREATE TABLE monitoreo_sla (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER UNIQUE NOT NULL,
    horas_sla_limite INTEGER NOT NULL DEFAULT 72,     -- Límite estricto: 72 horas hábiles
    fecha_inicio_sla TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_limite_sla TIMESTAMP NOT NULL,             -- Calculada: inicio + 72 horas hábiles
    horas_transcurridas NUMERIC(6,2) DEFAULT 0,
    estado_sla VARCHAR(30) NOT NULL DEFAULT 'EN_TIEMPO',
    -- Estados: 'EN_TIEMPO', 'POR_VENCER', 'VENCIDO', 'CUMPLIDO_EN_TIEMPO', 'CUMPLIDO_FUERA_DE_TIEMPO'
    alertas_emitidas INTEGER DEFAULT 0,
    ultima_alerta TIMESTAMP,

    CONSTRAINT fk_sla_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE,
    CONSTRAINT chk_estado_sla
        CHECK (estado_sla IN ('EN_TIEMPO', 'POR_VENCER', 'VENCIDO', 'CUMPLIDO_EN_TIEMPO', 'CUMPLIDO_FUERA_DE_TIEMPO'))
);

CREATE TABLE alerta_sla (
    id SERIAL PRIMARY KEY,
    id_monitoreo INTEGER NOT NULL,
    id_tramite INTEGER NOT NULL,
    tipo_alerta VARCHAR(50) NOT NULL,                -- 'ADVERTENCIA_24H', 'ADVERTENCIA_12H', 'VENCIMIENTO_SLA'
    mensaje TEXT NOT NULL,
    fecha_alerta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notificado_a VARCHAR(150),                       -- Correo del responsable o Vicerrector notificado

    CONSTRAINT fk_alerta_monitoreo
        FOREIGN KEY (id_monitoreo) REFERENCES monitoreo_sla(id) ON DELETE CASCADE,
    CONSTRAINT fk_alerta_tramite
        FOREIGN KEY (id_tramite) REFERENCES tramite(id) ON DELETE CASCADE
);

-- ============================================================
-- CU8. REPORTES Y ESTADISTICAS
-- ============================================================

CREATE TABLE reporte_estadistica (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,                     -- Usuario que generó el reporte
    tipo_reporte VARCHAR(100) NOT NULL,
    -- 'TRAMITES_POR_ESTADO', 'CUMPLIMIENTO_SLA', 'FIRMAS_POR_VICERRECTOR', 'TRAMITES_POR_CARRERA', 'OBSERVACIONES_FRECUENTES'
    parametros TEXT,
    resultado_resumen TEXT,
    fecha_generacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reporte_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================================
-- DATOS SEMILLA / INICIALES
-- ============================================================

-- 1. Usuarios Iniciales (Contraseña = C.I.)
-- Formato numérico estricto: Alumnos 9 dígitos (ej. 219064748), Docentes 4 dígitos (ej. 1045), Autoridades 4 dígitos (ej. 1001)
INSERT INTO usuario (registro, ci, password, nombre, apellidos, email, telefono, rol, unidad_academica, cargo) VALUES
('1001', '1234567', '1234567', 'Reinerio', 'Vargas', 'vicerrector@uagrm.edu.bo', '71000001', 'VICERRECTOR', 'Vicerrectorado', 'Vicerrector General'),
('1002', '2345678', '2345678', 'Carlos', 'Mendoza', 'encargado.firma@uagrm.edu.bo', '71000002', 'ENCARGADO', 'Despacho Vicerrectorado', 'Encargado de Despacho y Firma'),
('1045', '3456789', '3456789', 'Alberto', 'Guzman', 'aguzman@uagrm.edu.bo', '71000003', 'DOCENTE', 'Facultad de Ingenieria en Ciencias de la Computacion', 'Docente Titular'),
('219064748', '4567890', '4567890', 'Juan', 'Perez Rodriguez', 'juan.perez@alu.uagrm.edu.bo', '71000004', 'ALUMNO', 'Ingenieria Informatica', 'Estudiante Regular'),
('220015482', '5678901', '5678901', 'Maria', 'Lopez Flores', 'maria.lopez@alu.uagrm.edu.bo', '71000005', 'ALUMNO', 'Ingenieria en Sistemas', 'Estudiante Regular');

INSERT INTO autoridad (id_autoridad, tipo_autoridad, resolucion_nombramiento, fecha_posesion) VALUES
(1, 'VICERRECTOR', 'RES-VR-2022-001', '2022-01-10'),
(2, 'ENCARGADO_DESPACHO', 'RES-VR-2024-045', '2024-02-01');

INSERT INTO docente (id_docente, facultad, departamento, categoria_docente) VALUES
(3, 'Facultad de Ingenieria en Ciencias de la Computacion', 'Computacion e Informatica', 'TITULAR');

INSERT INTO alumno (id_alumno, carrera, facultad, semestre, estado_academico) VALUES
(4, 'Ingenieria Informatica', 'Facultad de Ingenieria en Ciencias de la Computacion', 10, 'EGRESADO'),
(5, 'Ingenieria en Sistemas', 'Facultad de Ingenieria en Ciencias de la Computacion', 9, 'REGULAR');

-- 2. Delegación de Autoridad Inicial
INSERT INTO delegacion_autoridad (id_autoridad_titular, id_autoridad_delegada, resolucion, motivo, fecha_inicio, fecha_fin, alcance_firma, estado) VALUES
(1, 2, 'RES-VR-DEL-01/2026', 'Comision oficial en reunion de Vicerrectores CEUB Sucre', CURRENT_DATE, CURRENT_DATE + INTERVAL '10 days', 'TOTAL', 'ACTIVA');

-- 3. Indicadores de Gestión Iniciales
INSERT INTO indicador_gestion (codigo, nombre, descripcion, meta_esperada, valor_actual, unidad_medida, periodo) VALUES
('IND-SLA-72H', 'Tasa Cumplimiento SLA 72 Horas', 'Porcentaje de tramites despachados en 72 horas habiles', 95.00, 92.50, 'PORCENTAJE', 'MENSUAL'),
('IND-TIEMPO-FIRMA', 'Tiempo Promedio de Firma Digital', 'Horas promedio desde compilacion hasta firma vicerrectoral', 24.00, 18.50, 'HORAS', 'MENSUAL'),
('IND-TRAMITES-MES', 'Volumen Mensual de Tramites', 'Cantidad total de tramites atendidos', 500.00, 340.00, 'CANTIDAD', 'MENSUAL');

-- 4. Tipos de Trámites Oficiales
INSERT INTO tipo_tramite (codigo, nombre, descripcion, tiempo_sla_horas, costo_bs) VALUES
('DIPLOMA_ACAD', 'Diploma Academico', 'Emision y firma digital de Diploma Academico de Grado', 72, 350.00),
('TIT_PROV_NAC', 'Titulo en Provision Nacional', 'Emision y firma vicerrectoral de Titulo en Provision Nacional', 72, 450.00),
('CERT_NOTAS', 'Certificado de Notas Legalizado', 'Legalizacion con firma digital de historial academico completo', 72, 50.00),
('CONVALIDACION', 'Convalidacion de Materias', 'Dictamen y resolucion de convalidacion academica', 72, 120.00);

-- 5. Requisitos por Trámite
INSERT INTO requisito (id_tipo_tramite, nombre, descripcion, es_obligatorio, formato_permitido) VALUES
(1, 'Fotocopia Legalizada CI', 'Cedula de Identidad anverso y reverso legible', TRUE, 'PDF'),
(1, 'Certificado de Calificaciones Completo', 'Historial oficial emitido por DPA', TRUE, 'PDF'),
(1, 'Acta de Defensa de Grado', 'Acta formal firmada por el tribunal calificador', TRUE, 'PDF'),
(2, 'Fotocopia de Diploma Academico', 'Diploma academico emitido previamente', TRUE, 'PDF'),
(2, 'Certificado de Nacimiento Computarizado', 'Emitido por SERECI no mayor a 1 año', TRUE, 'PDF'),
(3, 'Formulario de Solicitud de Notas', 'Formulario oficial de DPA firmado', TRUE, 'PDF');

-- 6. Guías Procesales
INSERT INTO guia_procesal (id_tipo_tramite, paso_numero, nombre_etapa, descripcion_instrucciones, rol_responsable, tiempo_estimado_horas) VALUES
(1, 1, 'Recepcion y Cotejo', 'Verificar registro del alumno, CI y requisitos obligatorios', 'ENCARGADO', 12),
(1, 2, 'Compilacion de Expediente', 'Compilar legajo unico digital y generar hash SHA-256', 'ENCARGADO', 12),
(1, 3, 'Firma Vicerrectoral', 'Ejecutar firma digital consolidada o remota con token', 'VICERRECTOR', 24),
(1, 4, 'Despacho y Notificacion', 'Generar Certificado QR, habilitar portal de seguimiento y notificar', 'ENCARGADO', 24);

-- 7. Trámite de Ejemplo
INSERT INTO tramite (codigo_tramite, id_usuario_solicitante, id_tipo_tramite, asunto, estado, prioridad) VALUES
('TR-2026-0001', 4, 1, 'Solicitud Diploma Academico de Licenciatura en Informatica', 'EN_FIRMA', 'NORMAL');

-- 8. Documentos del Trámite
INSERT INTO documento_expediente (id_tramite, id_requisito, nombre_documento, tipo_documento, archivo_url, hash_documento, estado_validacion, fecha_validacion) VALUES
(1, 1, 'CI_JuanPerez.pdf', 'PDF', 'http://archivos.uagrm.edu.bo/exp/ci_4567890.pdf', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'APROBADO', CURRENT_TIMESTAMP),
(1, 2, 'Historial_JuanPerez.pdf', 'PDF', 'http://archivos.uagrm.edu.bo/exp/historial_4567890.pdf', 'ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad', 'APROBADO', CURRENT_TIMESTAMP);

-- 9. Flujo y Bitácora
INSERT INTO flujo_tramite (id_tramite, etapa_actual, id_usuario_responsable, estado_etapa) VALUES
(1, 'Firma Vicerrectoral', 1, 'EN_PROCESO');

INSERT INTO bitacora_tramite (id_tramite, id_usuario, accion, detalle) VALUES
(1, 4, 'INICIO_TRAMITE', 'El estudiante inicio la solicitud mediante correo'),
(1, 2, 'VALIDACION_DOCUMENTOS', 'Encargado aprobo los requisitos presentados'),
(1, 2, 'COMPILACION_EXPEDIENTE', 'Expediente compilado listo para firma');

-- 10. Monitoreo SLA (72 horas)
INSERT INTO monitoreo_sla (id_tramite, horas_sla_limite, fecha_limite_sla, horas_transcurridas, estado_sla) VALUES
(1, 72, CURRENT_TIMESTAMP + INTERVAL '72 hours', 14.50, 'EN_TIEMPO');

-- 11. Portal de Seguimiento
INSERT INTO portal_seguimiento (id_tramite, pin_seguimiento, portal_activo, total_visitas) VALUES
(1, 'PIN-9482', TRUE, 1);
