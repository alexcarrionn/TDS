package modelo;

import java.util.LinkedList;

public class ContactoIndividual extends Contacto{

	private int movil;
	
	//Constructor
	public ContactoIndividual(String nombre, int movil) {
		super(nombre);
		this.movil=movil;
	}
	
	public ContactoIndividual(String nombre, int movil, LinkedList<Mensaje> mensajes) {
		super(nombre,mensajes);
		this.movil=movil;
	}
		
	public int getMovil() {
		return movil;
	}
	
	public String getEstado() {
		Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(movil);
		return usuario.getEstado();	
	}
	
	//Metodos
	
	/*Dado un usuario me devuelve el contacto de este usuario(como lo ve desde su perspectiva)
	 *La lista de mensajes de ese Usuario
	 *el estado
	 *añade a un grupo 
	 *elimina del gupo  */
	
	// HashCode e Equals
		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + movil;
			return result;
		}
		
		

		/**
		 * Dos contactos son iguales si tienen el mismo número de teléfono
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
			ContactoIndividual other = (ContactoIndividual) obj;
			if (movil != other.movil)
				return false;
			return true;
		}

		@Override
		public String getFoto() {
			Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(movil);
			return usuario.getImagen();
		}
}