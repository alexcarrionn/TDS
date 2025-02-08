package modelo;

import java.util.LinkedList;
import java.util.Objects;

public class ContactoIndividual extends Contacto{

	private String movil;
	private Usuario usuario;
	
	//Constructor
	public ContactoIndividual(String nombre, String movil) {
		super(nombre);
		this.movil=movil;

	}
	
	public ContactoIndividual(String nombre, String movil, LinkedList<Mensaje> mensajes) {
		super(nombre,mensajes);
		this.movil=movil;

	}
		
	public String getMovil() {
		return movil;
	}
	
	public void setMovil(String movil) {
		this.movil=movil;
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
	


		@Override
		public String getFoto() {
			Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(movil);
			return usuario.getImagen();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(movil);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ContactoIndividual other = (ContactoIndividual) obj;
			return Objects.equals(movil, other.movil);
		}

		public Usuario getUsuario() {
			return usuario;
		}

}