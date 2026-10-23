package proyectomailgrupo20sa;

import Comunication.Home;
import Database.InicializarDB;

public class Principal {

    public static void main(String[] args) {
        System.out.println("=======================================================");
        System.out.println("  SERVIDOR DE CORREO - VICERRECTORADO FIRMA DIGITAL - GRUPO 20 SA");
        System.out.println("=======================================================");
        System.out.println("Inicializando base de datos...");

        try {
            InicializarDB.crearTablasSiNoExisten();
            InicializarDB.insertarDatosIniciales();
            System.out.println("[OK] Base de datos inicializada correctamente");
        } catch (Exception e) {
            System.out.println("[ERROR] Error al inicializar BD: " + e.getMessage());
        }

        System.out.println("Iniciando servicio de monitoreo de correos...");
        Home.iniciar();

        System.out.println("[OK] Servicio de monitoreo ACTIVO");
        System.out.println("[OK] Monitoreando: grupo20sa@tecnoweb.org.bo");
        System.out.println("[OK] Revisando correos cada 5 segundos");
        System.out.println("=======================================================");
        System.out.println("El sistema esta listo para responder correos.");
        System.out.println("Envie un correo con subject 'help' para ver comandos.");
        System.out.println("Presione Ctrl+C para detener el servicio.");
        System.out.println("=======================================================");
    }
}
