package controlador;

import java.util.ArrayList;
import java.util.List;

import modelo.Mensaje;
import modelo.Usuario;


public class AppChat {
	private Usuario usuario_logueado; 
	
	
	public Usuario getUsuario_logueado() {
		return usuario_logueado;
	}
	
	public static boolean hacerLogin(String tel, String con) {
		return true;
	}

	public static List<Mensaje> ObtenerMensajesReMensaje() {
		Usuario Javi = new Usuario("98523", "Javi", ""); 
		Usuario Ana = new Usuario("98523", "Ana", ""); 
		
		
		ArrayList<Mensaje> resultado = new ArrayList<Mensaje>(); 
		resultado.add(new Mensaje("Hola Javier", Ana, Javi));
		resultado.add(new Mensaje("Hola Ana", Javi, Ana));

		return resultado;
	}
	
}
