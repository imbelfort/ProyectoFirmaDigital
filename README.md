# SISTEMA DE FIRMA DIGITAL DEL VICERRECTORADO (UAGRM)
## Proyecto Correo Electrónico - Grupo 20 SA | Tecnología Web

Sistema automatizado de gestión y firma digital de trámites académicos y administrativos mediante comandos enviados por correo electrónico (POP3 / SMTP) y base de datos relacional PostgreSQL.

---

### Configuración del Servidor y Base de Datos

- **Servidor de Correo:** `mail.tecnoweb.org.bo`
- **Puerto SMTP:** `25`
- **Puerto POP3:** `110`
- **Usuario Correo:** `grupo20sa`
- **Email:** `grupo20sa@tecnoweb.org.bo`
- **Password:** `grup020grup020*`
- **Motor BD:** PostgreSQL (`jdbc:postgresql://mail.tecnoweb.org.bo/`)
- **Base de Datos:** `db_grupo20sa`
- **Usuario BD:** `grupo20sa`
- **Password BD:** `grup020grup020*`

---

### Política de Credenciales y Autenticación (Estrictamente Numéricos, Sin Letras)
- **Alumnos:** Su identificador es su **Registro Universitario numérico** (ej. `219064748`) y su contraseña inicial es su **C.I.**
- **Docentes:** Su identificador es su **Código de Docente de 4 dígitos** (ej. `1045`) y su contraseña inicial es su **C.I.**
- **Vicerrector y Personal Administrativo / Encargados:** Su identificador es su **Código numérico de 4 dígitos** (ej. `1001`, `1002`) y su contraseña inicial es su **C.I.**
- **Sin letras:** Ningún código o registro contiene letras ni prefijos.

---

### Casos de Uso Implementados

1. **CU1. Gestión de Usuarios**
   - Administración de Vicerrector, Encargado, Docentes y Alumnos.
   - Herencia y roles del personal institucional y estudiantil.
2. **CU2. Gestión de Delegación de Autoridad e Indicadores de Gestión**
   - Registro de delegaciones de firma vicerrectoral por ausencia/viaje.
   - Monitoreo de indicadores de metas, volumen y tiempos.
3. **CU3. Gestión de Trámites, Documentos, Requisitos y Compilación de Expediente**
   - Catálogo de tipos de trámite (Diploma Académico, Título en Provisión Nacional, etc.).
   - Cotejo de requisitos y validación de expedientes.
4. **CU4. Gestión de Enrutamiento de Flujo, Guía Procesal, Observaciones y Bitácora**
   - Trazabilidad por etapas del trámite.
   - Visualización de instrucciones de la guía procesal.
   - Registro de observaciones y bitácora histórica de eventos.
5. **CU5. Gestión de Despacho, Previsualización y Firma Digital Consolidada o Remota**
   - Generación de hashes criptográficos SHA-256.
   - Firma digital local (consolidada) o remota (ADSIB/Token).
   - Generación del legajo despachado.
6. **CU6. Gestión de Certificado Oficial con QR y Seguimiento Estudiantil**
   - Emisión de certificado oficial con código de verificación UUID y código QR.
   - Portal de consulta y seguimiento con PIN para el alumno.
7. **CU7. Gestión de Monitoreo, Tiempos de Atención y Alertas SLA (72 Horas Hábiles)**
   - Control de cumplimiento del estándar de 72 horas hábiles.
   - Notificación y generación de alertas por vencimiento próximo o vencido.
8. **CU8. Reportes y Estadísticas**
   - Informes de volumen de trámites, tiempos de firma, observaciones y tasa de cumplimiento SLA.

---

### Guía de Inicio Rápido
Para interactuar con el sistema por correo electrónico:
1. Envíe un correo a `grupo20sa@tecnoweb.org.bo`
2. En el **Subject** (Asunto), escriba el comando deseado (ej: `help`, `usuario listar`, `tramite listar`, `sla listar`).
3. El sistema procesará la solicitud de forma inmediata y le responderá con el resultado formateado en HTML.
