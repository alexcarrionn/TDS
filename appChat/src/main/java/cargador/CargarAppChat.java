package cargador;

import java.time.LocalDate;

import controlador.AppChat;
import modelo.ContactoIndividual;
import modelo.RepositorioUsuario;
import modelo.TipoMensaje;


public class CargarAppChat {
	

	public static void main(String[] args) {
		AppChat appChat = AppChat.getUnicaInstancia();
		//TODO CAMBIAR LAS IMAGENES 
		appChat.registrarUsuario("11", "aa", "/usuarios/fotoJGM.jpg", "aa", LocalDate.of(1960, 10, 3), "Hola, soy jesus");
		appChat.registrarUsuario("22", "bb", "/usuarios/foto-elena.jpg", "bb", LocalDate.of(1995, 12, 28), "hola, soy elena");
		appChat.registrarUsuario("33", "cc", "/usuarios/rosalia.jpg", "cc", LocalDate.of(2000, 5, 15), "hola, soy rosalia");
		appChat.registrarUsuario("44", "dd", "/usuarios/foto-diego.jpg", "dd", LocalDate.of(1970, 5, 11), "hola, soy diego");
		appChat.registrarUsuario("55", "ee", "/usuarios/annetaylor.jpg", "ee", LocalDate.of(1990, 3, 28), "hola, soy anne");

		
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
