package modelo;

import java.util.LinkedList;
import java.util.List;

public abstract class Contacto {
    // Propiedades
	private int id;
    private String nombre;
    private int telefono;
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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public List<Mensaje> getMensajes() {
        return new LinkedList<>(mensajes);
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    // Métodos de gestión de mensajes
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
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Teléfono: " + telefono;
    }

    // Métodos equals y hashCode para evitar duplicados
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contacto contacto = (Contacto) obj;
        return telefono == contacto.telefono;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(telefono);
    }

	public abstract String getFoto();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
