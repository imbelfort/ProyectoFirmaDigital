# GUIA DE COMANDOS POR CORREO - VICERRECTORADO FIRMA DIGITAL
### SERVIDOR: `grupo20sa@tecnoweb.org.bo` | GRUPO 20 SA

> **REGLA DE ACCESO Y CREDENCIALES (SOLO NUMEROS, SIN LETRAS):**
> - **Alumnos:** Su login es su **Registro Universitario numérico** (ej. `219064748`, `218055412`) y su contraseña inicial es su **C.I.**
> - **Docentes:** Su login es su **Código de Docente numérico de 4 dígitos** (ej. `1045`, `2034`) y su contraseña inicial es su **C.I.**
> - **Vicerrector y Personal Administrativo/Encargado:** Su login es su **Código numérico de 4 dígitos** (ej. `1001`, `1002`) y su contraseña inicial es su **C.I.**
> - **No se utilizan letras ni prefijos** en los registros ni en los códigos.

---

## CU1. Gestion de Usuarios (Vicerrector, Encargado, Docentes, Alumnos)
- **usuario help**
- **usuario listar**
- **usuario agregar** `[nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo]`
  - *Ejemplo Alumno:* `usuario agregar [Juan;Perez;4567890;219064748;juan@alu.uagrm.edu.bo;ALUMNO;71000004;FICCT;Ingenieria Informatica]`
    *(Nota: Los alumnos **no tienen cargo**, se registra directamente su Facultad y la Carrera que estudian).*
  - *Ejemplo Docente:* `usuario agregar [Alberto;Guzman;3456789;1045;aguzman@uagrm.edu.bo;DOCENTE;71000003;FICCT;Docente Titular]`
  - *Ejemplo Autoridad:* `usuario agregar [Carlos;Mendoza;2345678;1002;cmendoza@uagrm.edu.bo;ENCARGADO;71000002;Despacho Vicerrectorado;Encargado de Firma]`
  - *Nota de Acceso:* La contraseña se genera automáticamente igual al CI. El registro o código es puramente numérico (sin letras).
  - *Roles permitidos:* `VICERRECTOR`, `ENCARGADO`, `DOCENTE`, `ALUMNO`, `ADMINISTRATIVO`.
- **usuario modificar** `[id_usuario;nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo;estado]`
- **usuario eliminar** `[id_usuario]`

---

## CU2. Gestion de Delegacion de Autoridad e Indicadores de Gestion
### Delegación de Autoridad
- **delegacion help**
- **delegacion listar**
- **delegacion agregar** `[id_autoridad_titular;id_autoridad_delegada;resolucion;motivo;fecha_inicio;fecha_fin;alcance_firma]`
  - *Alcance:* `TOTAL`, `SOLO_TITULOS`, `CERTIFICACIONES`.
- **delegacion modificar** `[id;resolucion;motivo;fecha_inicio;fecha_fin;alcance_firma;estado]`
- **delegacion eliminar** `[id]`

### Indicadores de Gestión
- **indicador help**
- **indicador listar**
- **indicador agregar** `[codigo;nombre;descripcion;meta_esperada;unidad_medida;periodo]`
- **indicador modificar** `[id;meta_esperada;valor_actual;estado]`

---

## CU3. Gestion de Tramites, Seleccionar Documentos, Validar Requisitos y Compilar Expediente
### Tipos de Trámite
- **tipo_tramite help**
- **tipo_tramite listar**
- **tipo_tramite agregar** `[codigo;nombre;descripcion;tiempo_sla_horas;costo_bs]`

### Trámites
- **tramite help**
- **tramite listar**
- **tramite agregar** `[codigo_tramite;id_usuario_solicitante;id_tipo_tramite;asunto;prioridad]`
- **tramite modificar** `[id_tramite;asunto;prioridad;estado]`
- **tramite eliminar** `[id_tramite]`

### Requisitos y Documentos del Expediente
- **documento help**
- **documento listar**
- **documento agregar** `[id_tramite;id_requisito;nombre_documento;tipo_documento;archivo_url]`
- **documento validar** `[id_documento;estado_validacion;observacion_validacion]`
  - *Estados de validación:* `APROBADO`, `OBSERVADO`, `RECHAZADO`.

---

## CU4. Gestion de Enrostrar Flujo, Visualizar Guia Procesal, Gestionar Observaciones y Bitacora
### Guía Procesal
- **guia help**
- **guia listar**
- **guia mostrar** `[id_tipo_tramite]`

### Flujo del Trámite
- **flujo help**
- **flujo listar**
- **flujo avanzar** `[id_tramite;etapa_actual;id_usuario_responsable]`

### Observaciones
- **observacion help**
- **observacion listar**
- **observacion agregar** `[id_tramite;id_usuario;tipo_observacion;descripcion]`
- **observacion subsanar** `[id_observacion;descripcion_subsanacion]`

### Bitácora de Auditoría
- **bitacora help**
- **bitacora listar**
- **bitacora consultar** `[id_tramite]`

---

## CU5. Gestion de Despachar, Previsualizar Legajos y Ejecutar Firma Digital Consolidada o Remota
### Firma Digital
- **firma help**
- **firma listar**
- **firma consolidada** `[id_tramite;id_firmante;numero_certificado_digital]`
  - Genera firma digital local/consolidada mediante token criptográfico y hash SHA-256.
- **firma remota** `[id_tramite;id_firmante;numero_certificado_digital]`
  - Ejecuta firma digital remota vicerrectoral mediante servicio seguro ADSIB.

### Despacho de Legajo
- **despacho help**
- **despacho listar**
- **despacho agregar** `[id_tramite;id_usuario_despachador;numero_despacho;tipo_entrega]`
  - *Tipos de entrega:* `DIGITAL_CORREO`, `PORTAL_DESCARGA`, `VENTANILLA`.

---

## CU6. Gestion de Certificado Oficial con QR y Habilitar Portal de Seguimiento Estudiantil
### Certificado Oficial
- **certificado help**
- **certificado listar**
- **certificado generar** `[id_tramite;id_firma_digital;titulo_documento]`
  - Genera código UUID de verificación y enlace oficial con QR.
- **certificado consultar** `[codigo_verificacion]`

### Portal de Seguimiento Estudiantil
- **portal help**
- **portal consultar** `[pin_seguimiento]`
  - Permite al estudiante consultar en línea el estado de su trámite.

---

## CU7. Gestion de Monitoreo y Tiempos de Atencion y Alertas SLA (72 Horas Habiles)
### Monitoreo SLA
- **sla help**
- **sla listar**
- **sla verificar** `[id_tramite]`
  - Calcula las horas hábiles transcurridas contra el límite de 72 horas y emite alerta si está vencido o por vencer.

### Alertas SLA
- **alerta help**
- **alerta listar**
- **alerta agregar** `[id_tramite;tipo_alerta;mensaje;notificado_a]`

---

## CU8. Reportes y Estadisticas
- **reporte help**
- **reporte tramites**
- **reporte sla**
- **reporte firmas**
- **reporte observaciones**
