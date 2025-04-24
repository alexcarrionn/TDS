package cargador;

import java.time.LocalDate;

import controlador.AppChat;
import modelo.ContactoIndividual;
import modelo.RepositorioUsuario;
import modelo.TipoMensaje;

/**
 * Clase que te permite cargar la Base de datos solo Una vez
 */

public class CargarAppChat {
	
	/**
	 * Método principal que carga la base de datos
	 * @param args
	 */
	public static void main(String[] args) {
		AppChat appChat = AppChat.getUnicaInstancia();
		appChat.registrarUsuario("11", "aa", "https://robohash.org/11.png", "aa", LocalDate.of(1960, 10, 3), "Hola, soy jesus");
		appChat.registrarUsuario("22", "bb", "https://robohash.org/22.png", "bb", LocalDate.of(1995, 12, 28), "hola, soy elena");
		appChat.registrarUsuario("33", "cc", "https://robohash.org/33.png", "cc", LocalDate.of(2000, 5, 15), "hola, soy rosalia");
		appChat.registrarUsuario("44", "dd", "https://robohash.org/44.png", "dd", LocalDate.of(1970, 5, 11), "hola, soy diego");
		appChat.registrarUsuario("55", "ee", "https://robohash.org/55.png", "ee", LocalDate.of(1990, 3, 28), "hola, soy anne");

		
		appChat.hacerLogin("11", "aa");
		
		ContactoIndividual c2 = appChat.agregarContacto("elena", "22");
		ContactoIndividual c3 = appChat.agregarContacto("rosalia", "33");
		
		appChat.enviarMensajeTextoContacto(c2, "Hola, ¿cómo estás?", TipoMensaje.ENVIADO);
		appChat.enviarMensajeEmoticonoContacto(c2, 4, TipoMensaje.ENVIADO);
		
		appChat.enviarMensajeTextoContacto(c3, "Cuando cantas?", TipoMensaje.ENVIADO);
		appChat.enviarMensajeEmoticonoContacto(c3, 6, TipoMensaje.ENVIADO);
		
		appChat.hacerLogin("22", "bb");
		
		ContactoIndividual c1 = RepositorioUsuario.getUnicaInstancia().getUsuario("22").getContactoIndividual("11");
		ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeTextoContacto(c1, "Vienes este finde?", TipoMensaje.ENVIADO);
		appChat.enviarMensajeEmoticonoContacto(c1, 3, TipoMensaje.ENVIADO);
	    appChat.enviarMensajeTextoContacto(c4, "Juegas esta semana?", TipoMensaje.ENVIADO);	
	    appChat.enviarMensajeEmoticonoContacto(c5, 0, TipoMensaje.ENVIADO);
	    
	    System.out.println("Fin de la carga de datos");
	}	
}
