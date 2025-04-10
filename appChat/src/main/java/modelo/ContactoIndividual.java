package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ContactoIndividual extends Contacto{

	private String movil;
	private Usuario usuario;
	
	//Constructor
	public ContactoIndividual(String nombre, String movil, Usuario usuario) {
		super(nombre);
		this.movil=movil;
		this.usuario=usuario;
	}
	
	public ContactoIndividual(String nombre, String movil, LinkedList<Mensaje> mensajes, Usuario usuario) {
		super(nombre,mensajes);
		this.movil=movil;
		this.usuario=usuario;
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
	
	@Override
	public String getFoto() {
		return usuario.getImagen();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	//Metodos
		
		//Funcion para buscar un tipo de contactoindividual de la lista de contactos del usuario actual y lo devuelve si lo encuentra
		private ContactoIndividual getContacto(Usuario usuario) {
			return usuario.getContactos().stream()
										 .filter(contacto -> contacto instanceof ContactoIndividual)
										 .map(contacto -> (ContactoIndividual) contacto)
										 .filter(contacto -> contacto.getUsuario().equals(usuario))
										 .findFirst()
										 .orElse(null);
		}
		
		
		//Funcion para devolver la lista de mensajes recibidos del contacto
		public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
			ContactoIndividual contacto = getContacto(usuario.orElse(null));
			if(contacto != null) {
				return contacto.getMensajes();
			} else {
				return new LinkedList<>();
			}
		}	

		//Comprueba si el contacto esta asociado a un usuario en concreto
		public boolean isUsuario(Usuario otro) {
			return usuario.equals(otro);
		}
		
		public void addAllMensajes(List<Mensaje> mensajes) {
			this.getMensajes().addAll(mensajes);
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


		@Override
		public String toString() {
			return getNombre() + " - " + getMovil();
		}

}