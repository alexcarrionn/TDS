package modelo;

import java.util.LinkedList;
import java.util.List;

public abstract class Contacto {
    // Propiedades
	private int id;
    private String nombre;
    private List<Mensaje> mensajes;

    // Constructor
    public Contacto(String nombre) {
    	this(nombre, new LinkedList<>());
    }
    
    public Contacto(String nombre, List<Mensaje> mensajes) {
    	this.nombre = nombre;
    	this.mensajes = mensajes;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Mensaje> getMensajes() {
        return new LinkedList<>(mensajes);
    }
    //Metodo de gestion de mensajes, en este metodo se añaden todos los mensajes pasados como parametro
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes.addAll(mensajes);
    }

    // Métodos de gestión de mensajes, en este se añade un mensaje pasado como parametro
    public void addMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public void removeMensaje(Mensaje mensaje) {
        this.mensajes.remove(mensaje);
    }

    public List<Mensaje> buscarMensajes(String texto) {
        List<Mensaje> resultados = new LinkedList<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje.getTexto().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(mensaje);
            }
        }
        return resultados;
    }

    // Mostrar Contacto
   

	public abstract String getFoto();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
