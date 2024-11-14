package modelo;

import java.util.ArrayList;
import java.util.List;

public class Grupo extends Contactos{

	private List<ContactoIndividual> contactos; 
	
	public List<ContactoIndividual> getContactos() {
		return new ArrayList<>(contactos);
	}

	public void setContactos(List<ContactoIndividual> contactos) {
		this.contactos = contactos;
	}
	
	public Grupo(String nombre) {
		super(nombre);
		contactos=new ArrayList<>(); 
	}
	
	public void agregarContacto(List<ContactoIndividual> usuario) {
		for(ContactoIndividual c : usuario) {
			contactos.add(c); 
		}
	}
	
	public void borraContacto(ContactoIndividual usuario) {
		contactos.remove(usuario);
	}

}
