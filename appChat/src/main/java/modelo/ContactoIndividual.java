package modelo;

import java.util.Collections;
import java.util.List;

public class ContactoIndividual extends Contactos{

	private Usuario usuario;
	
	public ContactoIndividual(String nombre, Usuario usuario) {
		super(nombre);
		// TODO Auto-generated constructor stub
		usuario=this.usuario;
	}
	
	public List<Usuario> getContactoIndividual(){
		return Collections.singletonList(usuario); 
	}
}