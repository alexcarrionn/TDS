package modelo;
public class ContactoIndividual extends Contacto{

	private int movil;
	private Usuario usuario;
	
	//Constructor
	public ContactoIndividual(String nombre, int movil) {
		super(nombre, movil);
	}
		
	public int getMovil() {
		return movil;
	}
	
	public Usuario getUsuario() {
		return usuario;
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

		@Override
		public String getFoto() {
			return usuario.getImagen();
		}
}