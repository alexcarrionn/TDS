package lanzador;

import java.io.IOException;
import java.time.LocalDate;

import appChat.Ventanas.VentanaLogin;
import cargador.CargarAppChat;
import controlador.AppChat;

import java.io.File;


 public class Lanzador {
 
  @SuppressWarnings("static-access")
	public static void main(String[] args) {
	  /*ProcessBuilder terminalPersistencia = new ProcessBuilder("cmd.exe", "/k", 
    		    "java -jar ServidorPersistenciaH2.jar");
    	
    		//IMPORTANTE CADA VEZ QUE SE QUIERA EJECUTAR EN UN EQUIPO DIFERENTE, CAMBIAR LA RUTA DEL ARCHIVO
    		terminalPersistencia.directory(new File("C:\\Users\\juanp\\Downloads\\ServidorPersistenciaH2\\ServidorPersistenciaH2"));
        try {
            terminalPersistencia.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        //CargarAppChat.main(null);
        AppChat appChat = AppChat.getUnicaInstancia();
		//TODO CAMBIAR LAS IMAGENES 
		appChat.registrarUsuario("1", "a", "", "a", LocalDate.of(1960, 10, 3), "Hola, soy jesus");
		appChat.registrarUsuario("2", "a", "", "a", LocalDate.of(1995, 12, 28), "hola, soy elena");
        
        VentanaLogin login = new VentanaLogin(); 
        login.main(null);  
        
    }
}
