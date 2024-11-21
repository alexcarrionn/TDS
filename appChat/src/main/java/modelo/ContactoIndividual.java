package modelo;
public class ContactoIndividual extends Contacto{

	private Usuario usuario;
	private int movil;
	
	//Constructor
	public ContactoIndividual(String nombre, Usuario usuario, int movil) {
		super(nombre);
		// TODO Auto-generated constructor stub
		usuario=this.usuario;
		this.movil=movil;
	}
	
	
	//Getters y setters
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

	public int getMovil() {
		return movil;
	}
	
	public void setMovil(int movil) {
		this.movil = movil;
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
}