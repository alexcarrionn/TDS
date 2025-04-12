package modelo;

import java.util.LinkedList;
import java.util.List;

public class Grupo extends Contacto{
	//Atributos 
	
	private List<ContactoIndividual> contactos;
	private String foto; 
	
	//Getters and setters
	public List<ContactoIndividual> getContactos() {
		return new LinkedList<>(contactos);
	}

	public void setContactos(List<ContactoIndividual> contactos) {
		this.contactos = contactos;
	}

	//Constructor
	public Grupo(String nombre, List<ContactoIndividual> contactos, String foto) {
		super(nombre);
		this.contactos = contactos != null ? new LinkedList<>(contactos) : new LinkedList<>(); 
		this.foto = foto;

	}
	
	/**
	 * Para agregar una lista de individuos al grupo
	 * @param usuaio lista de usuarios para agregar al grupo
	 */
	
	public void agregarContactos(List<ContactoIndividual> usuario) {
		for(ContactoIndividual c : usuario) {
			contactos.add(c); 
		}
	}
	
	
	//Para borrar a un integrante del grupo 
	
	public void borraContacto(ContactoIndividual usuario) {
		contactos.remove(usuario);
	}
	
	/**
	 * Método añade una lista de mensajes
	 * @param mensajes
	 */
	public void addAllMensajes(List<Mensaje> mensajes) {
		this.getMensajes().addAll(mensajes);
	}
	
	/**
	 * Método que te permitirá añadir a un contactoIndividual a un grupo específico
	 * @param contacto contacto que se desea añadir al grupo
	 */
	public void agregarContacto(ContactoIndividual contacto) {
		contactos.add(contacto);
	}

	
	// HashCode e Equals
		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getNombre() == null) ? 0 : getNombre().hashCode());
			return result;
		}

		/**
		 * Dos grupos son iguales si tienen el mismo nombre.
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Grupo other = (Grupo) obj;
			if (getNombre() == null) {
				if (other.getNombre() != null)
					return false;
			} else if (!getNombre().equals(other.getNombre()))
				return false;
			return true;
		}

		@Override
		public String getFoto() {
			return this.foto;
		}


}
