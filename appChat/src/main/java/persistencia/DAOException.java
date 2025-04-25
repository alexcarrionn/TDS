package persistencia;


/**
 * Excepción personalizada para manejar errores en la capa de persistencia (DAO).
 * Esta clase extiende la clase {@link Exception} y proporciona una forma de
 * lanzar y manejar excepciones relacionadas con la persistencia de datos en la aplicación.
 */

@SuppressWarnings("serial")
public class DAOException extends Exception {
	
	/**
     * Constructor de la excepción DAOException.
     * 
     * @param mensaje El mensaje que describe el error o la causa de la excepción.
     */
	public DAOException(String mensaje) {
		super(mensaje);
	}
}
