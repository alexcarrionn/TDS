package cargador;

import java.time.LocalDate;

import controlador.AppChat;
import modelo.ContactoIndividual;

public class CargarAppChat {
	

	public static void main(String[] args) {
		AppChat appChat = AppChat.getUnicaInstancia();
		appChat.crearCuentaUsuario(11, "aa", "/usuarios/fotoJGM.png", "aa", LocalDate.of(1960, 10, 3), "Hola, soy jesus");
		appChat.crearCuentaUsuario(22, "bb", "/usuarios/foto-elena.png", "bb", LocalDate.of(1995, 12, 28), "hola, soy elena");
		appChat.crearCuentaUsuario(33, "cc", "/usuarios/rosalia.jpg", "cc", LocalDate.of(2000, 5, 15), "hola, soy rosalia");
		appChat.crearCuentaUsuario(44, "dd", "/usuarios/foto-diego.png", "dd", LocalDate.of(1970, 5, 11), "hola, soy diego");
		appChat.crearCuentaUsuario(55, "ee", "/usuarios/annetaylor.jpg", "ee", LocalDate.of(1990, 3, 28), "hola, soy anne");

		
		appChat.hacerLogin(11, "aa");
		
		//ContactoIndividual c2 = appChat.agregarContacto("elena", 22);
		//ContactoIndividual c3 = appChat.agregarContacto("rosalia", 33);
/*		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 2, TipoMensaje.ENVIADO);
		
		appChat.enviarMensajeContacto(c3, "Cuando cantas?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c2, "", 6, TipoMensaje.ENVIADO);
		
		appChat.hacerLogin(22, "bb");
		
		//ContactoIndividual c1 =appChat.agregarContacto("jesus", "11");
		ContactoIndividual c1 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorMovil("22").getContactoIndividual("11");
		ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeContacto(c1, "Vienes este finde?", -1, TipoMensaje.ENVIADO);
		appChat.enviarMensajeContacto(c1, "", 3, TipoMensaje.ENVIADO);
	    appChat.enviarMensajeContacto(c4, "Juegas esta semana?", -1, TipoMensaje.ENVIADO);	
	    
	    System.out.println("Fin de la carga de datos");
*/
	}	
}
