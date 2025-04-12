package lanzador;

import java.io.IOException;
import appChat.Ventanas.VentanaLogin;
import cargador.CargarAppChat;

import java.io.File;


 public class Lanzador {
 
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
    	ProcessBuilder terminalPersistencia = new ProcessBuilder("cmd.exe", "/k", 
    		    "java -jar ServidorPersistenciaH2.jar");
    	
    		//IMPORTANTE CADA VEZ QUE SE QUIERA EJECUTAR EN UN EQUIPO DIFERENTE, CAMBIAR LA RUTA DEL ARCHIVO
    		terminalPersistencia.directory(new File("C:\\Users\\acarr\\Downloads\\ServidorPersistenciaH2"));
        try {
            terminalPersistencia.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        CargarAppChat.main(null);
        
        VentanaLogin login = new VentanaLogin(); 
        login.main(null);  
        
    }
}
