package modelo;

import java.util.ArrayList;
import java.util.List;

public class Grupo extends Contacto{

	private List<ContactoIndividual> contactos;
	
	public List<ContactoIndividual> getContactos() {
		return new ArrayList<>(contactos);
	}

	public void setContactos(List<ContactoIndividual> contactos) {
		this.contactos = contactos;
	}

	//Constructor
	public Grupo(String nombre, List<ContactoIndividual> contactos) {
		super(nombre);
		contactos=new ArrayList<>(); 

	}
	
	//Para agregar una lista de individuos al grupo
	
	public void agregarContactos(List<ContactoIndividual> usuario) {
		for(ContactoIndividual c : usuario) {
			contactos.add(c); 
		}
	}
	
	//Para agregar un individuo al grupo 
	public void agregarIndividuo(ContactoIndividual c) {
		contactos.add(c);
	}
	//Para borrar a un integrante del grupo 
	
	public void borraContacto(ContactoIndividual usuario) {
		contactos.remove(usuario);
	}
	
	/*A lo mejor tenemos que hacer funciones para conseguir los mensajes 
	como conseguir aquellos que tu envias o para borrar los mensajes
	Devuelve los mensajes que han enviado el resto de usuarios por el grupo. El
	valor del parametro pasado como parámetro no importa
	*/
	
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
			// TODO 
			return null;
		}
}
