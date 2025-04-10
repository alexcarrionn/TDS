package lanzador;

import java.io.IOException;
import java.time.LocalDate;

import appChat.Ventanas.VentanaLogin;
import controlador.AppChat;
import java.io.File;


 public class Lanzador {
 
    public static void main(String[] args) {
    	/*ProcessBuilder terminalPersistencia = new ProcessBuilder("cmd.exe", "/k", 
    		    "java -jar ServidorPersistenciaH2.jar");
    		terminalPersistencia.directory(new File("C:\\Users\\Juanpe\\Downloads\\ServidorPersistenciaH2"));
        try {
            terminalPersistencia.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        AppChat appChat=AppChat.getUnicaInstancia();
		appChat.registrarUsuario("1","a","","a",LocalDate.now(),"a");
		appChat.registrarUsuario("2","a","","a",LocalDate.now(),"a");

        VentanaLogin login = new VentanaLogin(); 
        login.main(null);  
        
    }
}
