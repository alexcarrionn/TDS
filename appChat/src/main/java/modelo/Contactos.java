package modelo;

import java.util.ArrayList;
import java.util.List;

public class Contactos {
	
	private String nombre; 
	private List<Mensaje> mensajes;
	
	public Contactos(String nombre) {
		this.nombre = nombre;
		mensajes = new ArrayList<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	} 
	
	
	
}
