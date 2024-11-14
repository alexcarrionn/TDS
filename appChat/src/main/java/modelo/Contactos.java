package modelo;

import java.util.ArrayList;
import java.util.List;

public class Contactos {
	//Propiedades
	private String nombre; 
	private List<Mensaje> mensajes;
	
	//Constructor
	public Contactos(String nombre) {
		this.nombre = nombre;
		mensajes = new ArrayList<>();
	}
	
	//Getters y setters
	//devuelve el nombre del contacto
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	//Devuelve la lista de los mensajes enviados
	public List<Mensaje> getMensajes() {
		return new ArrayList<>(mensajes);
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	} 
	
	
	
}
