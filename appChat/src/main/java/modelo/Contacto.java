package modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class Contacto {
    
	//Atributos de la clase Contacto 
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
        return mensajes;
    }
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
/*
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes.addAll(mensajes);
    }

    // Métodos de gestión de mensajes, en este se añade un mensaje pasado como parametro
    public void addMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    public void removeMensaje(Mensaje mensaje) {
        this.mensajes.remove(mensaje);
    }*/
    
	/**
	 * Método que sirve para crear un mensaje de texto
	 * @param texto
	 * @param tipo
	 * @return
	 */
    public Mensaje creaMensajeTexto(String texto, TipoMensaje tipo) {
    	Mensaje m= new Mensaje(texto,tipo,LocalDateTime.now());
    	mensajes.add(m);
    	return m;
    }
    
    /**
     * Método que sirve para crear un mensaje con un emoji
     * @param emoticono
     * @param tipo
     * @return
     */
    
    public Mensaje creaMensajeEmoticono(int emoticono, TipoMensaje tipo) {
    	Mensaje m= new Mensaje(emoticono,tipo,LocalDateTime.now());
    	mensajes.add(m);
    	return m;
    }
    
    /**
     * Metodo que se ha definido en ContactoIndividual y en Grupo
     * @return la imagen del ContactoIndividual o del grupo
     */
	public abstract String getFoto();


    
    /*
    public List<Mensaje> buscarMensajes(String texto) {
        List<Mensaje> resultados = new LinkedList<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje.getTexto().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(mensaje);
            }
        }
        return resultados;
    }*/

    /**
     *  Método para mostrar Contacto
     */
	@Override
	public String toString() {
		return "Contacto [id=" + id + ", nombre=" + nombre + ", mensajes=" + mensajes + "]";
	}


	
}
