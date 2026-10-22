**DISEÑO FUNCIONAL Y BASE DE DATOS**  
**Sistema de Gestión de Trámites mediante Comandos por Correo Electrónico**

*Implementación nativa en Java \+ servidor POP3/SMTP \+ PostgreSQL*

# **1\. Descripción general del sistema**

El sistema propuesto permitirá gestionar trámites administrativos y académicos utilizando el correo electrónico como interfaz principal. Los actores no utilizarán una aplicación web para ejecutar las operaciones principales; enviarán correos electrónicos a una cuenta del sistema, y el programa Java se encargará de leerlos mediante POP3, interpretar el comando, validar al usuario, ejecutar la operación sobre PostgreSQL y devolver una respuesta por correo mediante SMTP.

La comunicación puede representarse como: Actor → correo electrónico → servidor POP3 → aplicación Java → PostgreSQL → aplicación Java → servidor SMTP → correo de respuesta al actor.

# **2\. Arquitectura propuesta**

* Servidor de correo: proporciona el buzón del sistema y los servicios POP3 para recibir/leer mensajes y SMTP para enviar respuestas.  
* Aplicación Java nativa: contiene el lector de correos, autenticación/autorización, analizador de comandos, lógica de negocio, generación de documentos y conexión JDBC a PostgreSQL.  
* PostgreSQL: almacena usuarios, roles, trámites, documentos, flujo, observaciones, bitácora, delegaciones, firmas, certificados, alertas y datos para reportes.  
* Almacenamiento de archivos: conserva los documentos adjuntos y los PDF generados; PostgreSQL guarda sus metadatos y ubicación.  
* Generador de QR: genera un código QR asociado a un certificado mediante un código único de verificación.

# **3\. Flujo general de procesamiento de un correo**

1\. Recepción: El actor envía un correo a la cuenta destinada al sistema.

2\. Lectura POP3: La aplicación Java consulta periódicamente el buzón y obtiene los mensajes pendientes.

3\. Identificación: Se obtiene remitente, asunto, cuerpo y archivos adjuntos.

4\. Autenticación: El sistema identifica al usuario mediante su dirección de correo y verifica que esté registrado y activo.

5\. Autorización: Se comprueba que el rol del usuario tenga permiso para ejecutar el comando solicitado.

6\. Interpretación: El asunto se convierte en un comando y el cuerpo se interpreta como parámetros.

7\. Validación: Se revisan datos obligatorios, formatos, relaciones y reglas del trámite.

8\. Transacción: Java ejecuta la operación mediante JDBC en PostgreSQL. Si corresponde, se registran cambios en la bitácora.

9\. Respuesta: El sistema genera un correo indicando éxito, error, estado, observaciones o información solicitada.

# **4\. Convención de comandos por correo**

Se recomienda utilizar asuntos normalizados en mayúsculas. Ejemplo:

Asunto: AGREGAR USUARIO

Cuerpo:  
nombre: Juan Pérez  
correo: juan.perez@universidad.edu.bo  
rol: DOCENTE  
carnet: 12345678

Los comandos pueden ser procesados mediante una clase Java que identifique el asunto y entregue el mensaje a un manejador específico. Los datos del cuerpo se pueden interpretar como pares campo: valor. Esta convención es sencilla de implementar sin framework y facilita las pruebas.

# **5\. CU1. Gestión de Usuarios**

Actores: Vicerrector, Encargado, Docentes y Alumnos.

Objetivo: administrar las cuentas y datos básicos de las personas que participan en el sistema.

Comandos recomendados:

* AGREGAR USUARIO  
* LISTAR USUARIOS  
* CONSULTAR USUARIO  
* EDITAR USUARIO  
* DESACTIVAR USUARIO

No se recomienda eliminar físicamente usuarios porque sus operaciones deben conservarse en la bitácora. En su lugar se utiliza DESACTIVAR USUARIO, conservando el historial.

Ejemplo de AGREGAR USUARIO:

Asunto: AGREGAR USUARIO  
nombre: Juan Pérez  
correo: juan.perez@universidad.edu.bo  
rol: DOCENTE  
carnet: 12345678

Respuesta: USUARIO REGISTRADO. Código: USR-000001. Estado: ACTIVO.

# **6\. CU2. Delegación de Autoridad e Indicadores de Gestión**

Permite que un usuario con autoridad delegue determinadas funciones a otro usuario durante un período definido, y permite consultar indicadores operativos.

Comandos:

* DELEGAR AUTORIDAD  
* LISTAR DELEGACIONES  
* CONSULTAR DELEGACION  
* REVOCAR DELEGACION  
* CONSULTAR INDICADORES

No se requiere EDITAR como operación habitual: una delegación puede revocarse y crearse nuevamente. Esto permite conservar trazabilidad.

Ejemplo:  
Asunto: DELEGAR AUTORIDAD  
delegante: USR-000001  
delegado: USR-000005  
funcion: VALIDAR\_REQUISITOS  
inicio: 2026-10-21  
fin: 2026-11-21

# **7\. CU3. Gestión de Trámites, Documentos, Requisitos y Expediente**

Es el núcleo del sistema. Permite iniciar un trámite, asociar documentos, comprobar requisitos y formar el expediente.

Comandos:

* CREAR TRAMITE  
* LISTAR TRAMITES  
* CONSULTAR TRAMITE  
* SELECCIONAR DOCUMENTOS  
* VALIDAR REQUISITOS  
* COMPILAR EXPEDIENTE

Los documentos se recibirán como archivos adjuntos. El sistema guardará el archivo en el almacenamiento definido y registrará en PostgreSQL su nombre, tipo, tamaño, ubicación, fecha y hash.

Ejemplo:  
Asunto: CREAR TRAMITE  
tipo: TITULACION  
estudiante: USR-000020  
descripcion: Solicitud de título profesional

El sistema genera un código único, por ejemplo TRM-2026-000001.

# **8\. CU4. Enrutar Flujo, Guía Procesal, Observaciones y Bitácora**

El trámite avanza por estados controlados. Un flujo inicial puede ser: RECEPCION → VALIDACION → OBSERVADO/CORRECCION → APROBADO → DESPACHO → FIRMA → CERTIFICADO → FINALIZADO.

Comandos:

* CONSULTAR FLUJO  
* CONSULTAR GUIA  
* ENRUTAR TRAMITE  
* OBSERVAR TRAMITE  
* RESPONDER OBSERVACION  
* CONSULTAR BITACORA

La bitácora registra quién ejecutó cada operación, sobre qué entidad, cuándo se realizó y cuál fue el resultado. No debe permitirse editar o eliminar entradas de bitácora.

# **9\. CU5. Despacho, Legajos y Firma Digital**

El despacho ocurre después de que el expediente cumple los requisitos y es aprobado. El sistema permite previsualizar el legajo, consolidar los documentos en un PDF y solicitar/registrar su firma.

Comandos:

* PREVISUALIZAR LEGAJO  
* CONSOLIDAR LEGAJO  
* FIRMAR LEGAJO  
* CONSULTAR FIRMA  
* DESPACHAR TRAMITE

Para una implementación académica se recomienda separar la lógica de firma de la lógica del correo. Java puede generar el PDF consolidado y calcular su huella criptográfica (hash). La firma digital real debe realizarse mediante un certificado digital y una infraestructura/autoridad certificadora compatible. No debe confundirse una imagen de firma o un texto insertado en PDF con una firma digital criptográfica.

Para una primera versión funcional puede existir un modo de demostración que registre el firmante, fecha y hash del documento, dejando explícito en la documentación que se trata de una simulación y no de una firma digital con validez legal.

# **10\. CU6. Certificado Oficial con QR y Portal de Seguimiento**

Una vez finalizado el trámite, Java puede generar un certificado PDF con un código único. El QR contendrá preferentemente un identificador o una dirección de verificación, no todos los datos del certificado.

Comandos:

* GENERAR CERTIFICADO  
* CONSULTAR CERTIFICADO  
* ANULAR CERTIFICADO  
* REENVIAR CERTIFICADO  
* CONSULTAR SEGUIMIENTO

La verificación puede utilizar una pequeña página pública de consulta. Aunque la gestión principal sea por correo, el QR necesita un destino que pueda ser abierto desde un navegador. Por ejemplo, el QR podría apuntar a una ruta de verificación asociada al código CERT-2026-000001. El portal mostraría si el certificado existe, está vigente o fue anulado.

El certificado puede contener: institución, título del certificado, nombre del titular, código, fecha de emisión, información del trámite, datos de verificación y QR.

# **11\. CU7. Monitoreo, tiempos de atención y alertas SLA**

El sistema debe controlar el tiempo de atención de cada trámite. El SLA establecido es de 72 horas hábiles. Para no depender de un comando manual, la aplicación Java debe tener un proceso periódico que revise los trámites pendientes.

Comandos:

* CONSULTAR SLA  
* LISTAR ALERTAS  
* CONSULTAR ALERTA  
* RECALCULAR SLA

No se recomienda ELIMINAR ALERTA. Una alerta debe quedar como historial y puede manejar estados como PENDIENTE, NOTIFICADA, ATENDIDA o VENCIDA.

Para calcular correctamente el SLA deben excluirse sábados, domingos y los feriados definidos por el sistema. Por eso se incluye una tabla de calendario laboral/feriados en la base de datos.

# **12\. CU8. Reportes y Estadísticas**

Permite obtener información agregada para evaluar el funcionamiento del sistema.

Comandos:

* REPORTE TRAMITES  
* REPORTE USUARIOS  
* REPORTE SLA  
* REPORTE TIEMPOS  
* ESTADISTICAS GENERALES  
* REPORTE BITACORA

Los reportes pueden enviarse como texto en el correo o como archivos PDF/CSV adjuntos. Para un proyecto académico, es recomendable empezar con reportes de texto y posteriormente agregar PDF.

# **13\. Diseño físico de la base de datos PostgreSQL**

El siguiente diseño busca mantener normalización, trazabilidad y relaciones claras. Se evita borrar información histórica que sea necesaria para auditoría.

Script SQL base:

\-- CREACIÓN DE BASE DE DATOS  
CREATE DATABASE sistema\_tramites;

\-- Conectarse a sistema\_tramites antes de ejecutar lo siguiente.

CREATE TABLE roles (  
    id\_rol BIGSERIAL PRIMARY KEY,  
    nombre VARCHAR(50) NOT NULL UNIQUE,  
    descripcion VARCHAR(255),  
    activo BOOLEAN NOT NULL DEFAULT TRUE  
);

CREATE TABLE permisos (  
    id\_permiso BIGSERIAL PRIMARY KEY,  
    codigo VARCHAR(80) NOT NULL UNIQUE,  
    nombre VARCHAR(100) NOT NULL,  
    descripcion VARCHAR(255)  
);

CREATE TABLE rol\_permiso (  
    id\_rol BIGINT NOT NULL REFERENCES roles(id\_rol),  
    id\_permiso BIGINT NOT NULL REFERENCES permisos(id\_permiso),  
    PRIMARY KEY (id\_rol, id\_permiso)  
);

CREATE TABLE usuarios (  
    id\_usuario BIGSERIAL PRIMARY KEY,  
    codigo\_usuario VARCHAR(30) NOT NULL UNIQUE,  
    nombre\_completo VARCHAR(150) NOT NULL,  
    correo VARCHAR(254) NOT NULL UNIQUE,  
    carnet VARCHAR(30) UNIQUE,  
    id\_rol BIGINT NOT NULL REFERENCES roles(id\_rol),  
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'  
        CHECK (estado IN ('ACTIVO','INACTIVO','BLOQUEADO')),  
    fecha\_registro TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_actualizacion TIMESTAMP  
);

CREATE TABLE delegaciones (  
    id\_delegacion BIGSERIAL PRIMARY KEY,  
    id\_delegante BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    id\_delegado BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    id\_permiso BIGINT NOT NULL REFERENCES permisos(id\_permiso),  
    fecha\_inicio TIMESTAMP NOT NULL,  
    fecha\_fin TIMESTAMP NOT NULL,  
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'  
        CHECK (estado IN ('ACTIVA','REVOCADA','VENCIDA')),  
    motivo VARCHAR(500),  
    fecha\_creacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    CHECK (fecha\_fin \> fecha\_inicio),  
    CHECK (id\_delegante \<\> id\_delegado)  
);

CREATE TABLE tipos\_tramite (  
    id\_tipo\_tramite BIGSERIAL PRIMARY KEY,  
    codigo VARCHAR(40) NOT NULL UNIQUE,  
    nombre VARCHAR(100) NOT NULL UNIQUE,  
    descripcion VARCHAR(255),  
    activo BOOLEAN NOT NULL DEFAULT TRUE  
);

CREATE TABLE estados\_tramite (  
    id\_estado BIGSERIAL PRIMARY KEY,  
    codigo VARCHAR(40) NOT NULL UNIQUE,  
    nombre VARCHAR(80) NOT NULL UNIQUE,  
    orden SMALLINT NOT NULL UNIQUE,  
    estado\_final BOOLEAN NOT NULL DEFAULT FALSE  
);

CREATE TABLE tramites (  
    id\_tramite BIGSERIAL PRIMARY KEY,  
    codigo\_tramite VARCHAR(40) NOT NULL UNIQUE,  
    id\_tipo\_tramite BIGINT NOT NULL REFERENCES tipos\_tramite(id\_tipo\_tramite),  
    id\_solicitante BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    id\_responsable BIGINT REFERENCES usuarios(id\_usuario),  
    id\_estado BIGINT NOT NULL REFERENCES estados\_tramite(id\_estado),  
    descripcion TEXT,  
    fecha\_recepcion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_limite\_sla TIMESTAMP,  
    fecha\_finalizacion TIMESTAMP,  
    prioridad VARCHAR(20) NOT NULL DEFAULT 'NORMAL'  
        CHECK (prioridad IN ('BAJA','NORMAL','ALTA','URGENTE')),  
    creado\_en TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    actualizado\_en TIMESTAMP  
);

CREATE TABLE requisitos (  
    id\_requisito BIGSERIAL PRIMARY KEY,  
    id\_tipo\_tramite BIGINT NOT NULL REFERENCES tipos\_tramite(id\_tipo\_tramite),  
    codigo VARCHAR(50) NOT NULL,  
    nombre VARCHAR(150) NOT NULL,  
    descripcion VARCHAR(500),  
    obligatorio BOOLEAN NOT NULL DEFAULT TRUE,  
    activo BOOLEAN NOT NULL DEFAULT TRUE,  
    UNIQUE (id\_tipo\_tramite, codigo)  
);

CREATE TABLE documentos\_tramite (  
    id\_documento BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    id\_requisito BIGINT REFERENCES requisitos(id\_requisito),  
    tipo\_documento VARCHAR(100) NOT NULL,  
    nombre\_archivo VARCHAR(255) NOT NULL,  
    ruta\_archivo VARCHAR(500) NOT NULL,  
    extension VARCHAR(20),  
    tamano\_bytes BIGINT,  
    hash\_sha256 VARCHAR(64),  
    estado VARCHAR(20) NOT NULL DEFAULT 'RECIBIDO'  
        CHECK (estado IN ('RECIBIDO','VALIDADO','OBSERVADO','RECHAZADO')),  
    fecha\_recepcion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_validacion TIMESTAMP  
);

CREATE TABLE validaciones\_requisito (  
    id\_validacion BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    id\_requisito BIGINT NOT NULL REFERENCES requisitos(id\_requisito),  
    id\_documento BIGINT REFERENCES documentos\_tramite(id\_documento),  
    id\_validador BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    estado VARCHAR(20) NOT NULL  
        CHECK (estado IN ('PENDIENTE','CUMPLE','NO\_CUMPLE')),  
    observacion VARCHAR(1000),  
    fecha\_validacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    UNIQUE (id\_tramite, id\_requisito)  
);

CREATE TABLE observaciones (  
    id\_observacion BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    id\_usuario BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    observacion TEXT NOT NULL,  
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'  
        CHECK (estado IN ('PENDIENTE','RESPONDIDA','RESUELTA')),  
    fecha\_creacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_respuesta TIMESTAMP  
);

CREATE TABLE bitacora (  
    id\_bitacora BIGSERIAL PRIMARY KEY,  
    id\_usuario BIGINT REFERENCES usuarios(id\_usuario),  
    id\_tramite BIGINT REFERENCES tramites(id\_tramite),  
    accion VARCHAR(100) NOT NULL,  
    entidad VARCHAR(80),  
    id\_entidad BIGINT,  
    detalle TEXT,  
    resultado VARCHAR(20) NOT NULL DEFAULT 'EXITO'  
        CHECK (resultado IN ('EXITO','ERROR','RECHAZADO')),  
    fecha\_hora TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP  
);

CREATE TABLE historial\_estados (  
    id\_historial BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    estado\_anterior BIGINT REFERENCES estados\_tramite(id\_estado),  
    estado\_nuevo BIGINT NOT NULL REFERENCES estados\_tramite(id\_estado),  
    id\_usuario BIGINT REFERENCES usuarios(id\_usuario),  
    comentario TEXT,  
    fecha\_hora TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP  
);

CREATE TABLE guia\_procesal (  
    id\_guia BIGSERIAL PRIMARY KEY,  
    id\_tipo\_tramite BIGINT NOT NULL REFERENCES tipos\_tramite(id\_tipo\_tramite),  
    id\_estado BIGINT NOT NULL REFERENCES estados\_tramite(id\_estado),  
    instrucciones TEXT NOT NULL,  
    responsable\_rol BIGINT REFERENCES roles(id\_rol),  
    orden SMALLINT NOT NULL,  
    activo BOOLEAN NOT NULL DEFAULT TRUE,  
    UNIQUE (id\_tipo\_tramite, id\_estado, orden)  
);

CREATE TABLE documentos\_legajo (  
    id\_legajo\_documento BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    id\_documento BIGINT NOT NULL REFERENCES documentos\_tramite(id\_documento),  
    orden SMALLINT NOT NULL,  
    incluido BOOLEAN NOT NULL DEFAULT TRUE,  
    UNIQUE (id\_tramite, id\_documento)  
);

CREATE TABLE legajos (  
    id\_legajo BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL UNIQUE REFERENCES tramites(id\_tramite),  
    nombre\_archivo VARCHAR(255),  
    ruta\_archivo VARCHAR(500),  
    hash\_sha256 VARCHAR(64),  
    estado VARCHAR(20) NOT NULL DEFAULT 'GENERADO'  
        CHECK (estado IN ('GENERADO','FIRMADO','DESPACHADO')),  
    fecha\_generacion TIMESTAMP,  
    fecha\_firma TIMESTAMP,  
    fecha\_despacho TIMESTAMP  
);

CREATE TABLE firmas (  
    id\_firma BIGSERIAL PRIMARY KEY,  
    id\_legajo BIGINT NOT NULL REFERENCES legajos(id\_legajo),  
    id\_firmante BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    tipo\_firma VARCHAR(30) NOT NULL  
        CHECK (tipo\_firma IN ('REAL','SIMULADA','REMOTA')),  
    certificado\_digital VARCHAR(255),  
    algoritmo VARCHAR(50),  
    hash\_documento VARCHAR(128),  
    referencia\_firma VARCHAR(255),  
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'  
        CHECK (estado IN ('PENDIENTE','FIRMADO','RECHAZADO')),  
    fecha\_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_firma TIMESTAMP  
);

CREATE TABLE despachos (  
    id\_despacho BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL UNIQUE REFERENCES tramites(id\_tramite),  
    id\_usuario BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    medio VARCHAR(30) NOT NULL DEFAULT 'CORREO',  
    destinatario VARCHAR(254) NOT NULL,  
    asunto VARCHAR(255),  
    fecha\_despacho TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    estado VARCHAR(20) NOT NULL DEFAULT 'ENVIADO'  
        CHECK (estado IN ('PENDIENTE','ENVIADO','ERROR'))  
);

CREATE TABLE certificados (  
    id\_certificado BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL UNIQUE REFERENCES tramites(id\_tramite),  
    codigo\_certificado VARCHAR(50) NOT NULL UNIQUE,  
    titular BIGINT NOT NULL REFERENCES usuarios(id\_usuario),  
    ruta\_pdf VARCHAR(500) NOT NULL,  
    hash\_sha256 VARCHAR(64),  
    datos\_qr TEXT NOT NULL,  
    estado VARCHAR(20) NOT NULL DEFAULT 'VIGENTE'  
        CHECK (estado IN ('VIGENTE','ANULADO')),  
    fecha\_emision TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_anulacion TIMESTAMP  
);

CREATE TABLE calendario\_laboral (  
    fecha DATE PRIMARY KEY,  
    es\_habil BOOLEAN NOT NULL DEFAULT TRUE,  
    descripcion VARCHAR(255)  
);

CREATE TABLE sla\_tramites (  
    id\_sla BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL UNIQUE REFERENCES tramites(id\_tramite),  
    horas\_objetivo NUMERIC(8,2) NOT NULL DEFAULT 72,  
    horas\_consumidas NUMERIC(8,2) NOT NULL DEFAULT 0,  
    porcentaje\_consumido NUMERIC(6,2) NOT NULL DEFAULT 0,  
    estado VARCHAR(20) NOT NULL DEFAULT 'EN\_TIEMPO'  
        CHECK (estado IN ('EN\_TIEMPO','PROXIMO\_VENCER','VENCIDO','DETENIDO')),  
    ultima\_revision TIMESTAMP  
);

CREATE TABLE alertas\_sla (  
    id\_alerta BIGSERIAL PRIMARY KEY,  
    id\_tramite BIGINT NOT NULL REFERENCES tramites(id\_tramite),  
    tipo VARCHAR(30) NOT NULL  
        CHECK (tipo IN ('AVISO','VENCIMIENTO')),  
    destinatario BIGINT REFERENCES usuarios(id\_usuario),  
    mensaje TEXT NOT NULL,  
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'  
        CHECK (estado IN ('PENDIENTE','NOTIFICADA','ATENDIDA')),  
    fecha\_generacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,  
    fecha\_notificacion TIMESTAMP  
);

CREATE TABLE correos\_procesados (  
    id\_correo BIGSERIAL PRIMARY KEY,  
    identificador VARCHAR(255) UNIQUE,  
    remitente VARCHAR(254) NOT NULL,  
    asunto VARCHAR(255),  
    fecha\_recepcion TIMESTAMP,  
    comando VARCHAR(100),  
    estado VARCHAR(20) NOT NULL DEFAULT 'RECIBIDO'  
        CHECK (estado IN ('RECIBIDO','PROCESADO','ERROR','RECHAZADO')),  
    mensaje\_error TEXT,  
    fecha\_procesamiento TIMESTAMP  
);

\-- Índices principales  
CREATE INDEX idx\_usuarios\_rol ON usuarios(id\_rol);  
CREATE INDEX idx\_usuarios\_estado ON usuarios(estado);  
CREATE INDEX idx\_tramites\_estado ON tramites(id\_estado);  
CREATE INDEX idx\_tramites\_solicitante ON tramites(id\_solicitante);  
CREATE INDEX idx\_tramites\_responsable ON tramites(id\_responsable);  
CREATE INDEX idx\_tramites\_fecha\_recepcion ON tramites(fecha\_recepcion);  
CREATE INDEX idx\_documentos\_tramite ON documentos\_tramite(id\_tramite);  
CREATE INDEX idx\_bitacora\_tramite ON bitacora(id\_tramite);  
CREATE INDEX idx\_bitacora\_fecha ON bitacora(fecha\_hora);  
CREATE INDEX idx\_alertas\_estado ON alertas\_sla(estado);  
CREATE INDEX idx\_correo\_estado ON correos\_procesados(estado);

# **14\. Relación de las tablas con los casos de uso**

| Caso de uso | Tablas principales | Operaciones |
| :---- | :---- | :---- |
| CU1 Usuarios | usuarios, roles, permisos, rol\_permiso | Agregar, listar, consultar, editar, desactivar |
| CU2 Autoridad/Indicadores | delegaciones, permisos, usuarios, tramites | Delegar, listar, consultar, revocar, consultar indicadores |
| CU3 Trámites | tramites, tipos\_tramite, requisitos, documentos\_tramite, validaciones\_requisito | Crear, listar, consultar, seleccionar, validar, compilar |
| CU4 Flujo/Bitácora | estados\_tramite, historial\_estados, observaciones, guia\_procesal, bitacora | Enrutar, consultar, observar, responder, consultar bitácora |
| CU5 Despacho/Firma | legajos, documentos\_legajo, firmas, despachos | Previsualizar, consolidar, firmar, consultar, despachar |
| CU6 Certificado/QR | certificados, usuarios, tramites | Generar, consultar, anular, reenviar, verificar |
| CU7 SLA | calendario\_laboral, sla\_tramites, alertas\_sla, tramites | Consultar, listar alertas, recalcular, notificar |
| CU8 Reportes | Todas las tablas operativas | Generar reportes y estadísticas |

# **15\. Ejemplo completo de operación**

Ejemplo: un alumno envía CREAR TRAMITE. Java recibe el mensaje mediante POP3, busca el correo del remitente en usuarios, comprueba que esté ACTIVO, crea el registro en tramites, calcula la fecha límite del SLA, crea el registro correspondiente en sla\_tramites, registra la acción en bitacora y envía un correo de respuesta con el código del trámite.

Posteriormente, el alumno puede enviar SELECCIONAR DOCUMENTOS con archivos adjuntos. El sistema registra cada archivo. Un encargado puede enviar VALIDAR REQUISITOS. Si falta un documento, se crea una observación y se devuelve un correo al alumno. Si todo cumple, el trámite continúa al siguiente estado.

# **16\. Recomendación de implementación en Java nativo**

Como el requisito es Java sin frameworks, la aplicación puede organizarse en paquetes simples:

* correo: conexión POP3, lectura de mensajes, extracción de adjuntos y envío SMTP.  
* comandos: representación y análisis de comandos.  
* usuarios: operaciones CRUD y autorización.  
* tramites: creación, consulta y gestión del expediente.  
* workflow: estados, transiciones, guía y observaciones.  
* documentos: almacenamiento, metadatos y hashes.  
* firma: consolidación y control del proceso de firma.  
* certificados: generación de certificados y códigos QR.  
* sla: cálculo de horas hábiles y alertas.  
* reportes: consultas y generación de resultados.  
* persistencia: conexión JDBC, DAO y transacciones.  
* modelo: clases Java que representan las entidades.

La capa de persistencia debe utilizar JDBC para PostgreSQL. Para mantener el proyecto ordenado, es conveniente separar DAO (acceso a datos), servicios (reglas de negocio) y modelos (datos), aunque todo sea Java nativo.

# **17\. Conclusión**

Los ocho casos de uso pueden implementarse de forma coherente mediante comandos enviados por correo electrónico. El correo funciona como interfaz de entrada y salida; Java actúa como motor de procesamiento; PostgreSQL conserva la información y trazabilidad; los documentos adjuntos permiten construir expedientes; el SLA controla los tiempos; la generación de PDF y QR permite emitir certificados; y el módulo de firma puede conectarse posteriormente con una infraestructura real de firma digital.

Para el desarrollo incremental se recomienda implementar primero el ciclo completo POP3 → parser → autorización → JDBC → respuesta SMTP con CU1. Después se incorpora CU3 y CU4, que forman el núcleo del flujo de trámites, y finalmente se agregan SLA, firma, certificados, QR y reportes.