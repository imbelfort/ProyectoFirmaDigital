package Interfaces;

import Comunication.TokenEvent;

public interface ITokenCasoUsoEventListener {

    // CU1. Gestion de Usuarios
    void usuario(TokenEvent event);

    // CU2. Delegacion de Autoridad e Indicadores
    void delegacion(TokenEvent event);
    void indicador(TokenEvent event);

    // CU3. Tramites, Documentos y Expediente
    void tramite(TokenEvent event);
    void documento(TokenEvent event);
    void tipoTramite(TokenEvent event);

    // CU4. Enrutamiento de Flujo, Guia Procesal, Observaciones y Bitacora
    void flujo(TokenEvent event);
    void guia(TokenEvent event);
    void observacion(TokenEvent event);
    void bitacora(TokenEvent event);

    // CU5. Firma Digital y Despacho
    void firma(TokenEvent event);
    void despacho(TokenEvent event);

    // CU6. Certificado Oficial y Portal Estudiantil
    void certificado(TokenEvent event);
    void portal(TokenEvent event);

    // CU7. Monitoreo SLA y Alertas
    void sla(TokenEvent event);
    void alerta(TokenEvent event);

    // CU8. Reportes y Estadisticas
    void reporte(TokenEvent event);

    // Manejo de Errores y Help
    void error(TokenEvent event);
    void posibleserrores(TokenEvent event);
}