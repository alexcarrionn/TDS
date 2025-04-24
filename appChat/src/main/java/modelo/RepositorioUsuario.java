package modelo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorUsuarioDAO;


/* El catálogo mantiene los objetos en memoria, en una tabla hash
 * para mejorar el rendimiento. Esto no se podría hacer en una base de
 * datos con un número grande de objetos. En ese caso se consultaria
 * directamente la base de datos
 */

public class RepositorioUsuario {
	//Atributos
	
	private Map<String,Usuario> usuarios; 
	private static RepositorioUsuario unicaInstancia = new RepositorioUsuario();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	 /**
     * Constructor privado para inicializar el repositorio de usuarios.
     * Configura el DAO y carga el catálogo de usuarios desde la base de datos.
     */
	
	private RepositorioUsuario() {
		try {
  			dao = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
  			adaptadorUsuario = dao.getUsuarioDAO();
  			usuarios = new HashMap<String,Usuario>();
  			this.cargarCatalogo();
  		} catch (DAOException eDAO) {
  			eDAO.printStackTrace();
  		}
	}
	
	/**
     * Obtiene la instancia única del repositorio de usuarios (Singleton).
     * 
     * @return La instancia única del repositorio de usuarios.
     */
	public static RepositorioUsuario getUnicaInstancia(){
		return unicaInstancia;
	}

	
	/**
	 * Método que devuelve el usuario del telefono que se ha pasado como parametro
	 * @param telefono telefono del ususario que se quiere obtener
	 * @return 
	 */
	public Usuario getUsuario(String telefono) {
		return usuarios.get(telefono);
	}
	
	/**
	 * Método para añadir un usuario a la lista de usuarios del repositorio
	 * @param user
	 */
	public void addUsuario(Usuario user) {
	    usuarios.put(user.getTelefono(), user);
	    adaptadorUsuario.registrarUsuario(user); // Persistir en la base de datos
	}

	
	/**
	 * Recupera todos los usuarios para trabajar con ellos en memoria
	 * @throws DAOException
	 */
	private void cargarCatalogo() throws DAOException {
		 List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		 for (Usuario user: usuariosBD) 
			     usuarios.put(user.getTelefono(),user);
	}
	
	/**
	 * Método que comprueba si el Usuario esta contenido en el repositorio
	 * @param usuario usuario que se quiere comprobar
	 * @return true si esta contenido, false en caso contrario
	 */
	public boolean contains(Usuario usuario) {
		return usuarios.containsValue(usuario);
	}
	
	/**
	 * Metodo que busca un Usuario por el telefono
	 * @param telefono
	 * @return
	 */
	public Optional<Usuario> obtenerUsuarioPorTelefono(String telefono) {
		return usuarios.values().stream()
			    .filter(u -> u.getTelefono().equals(telefono))
			    .findAny();
	}
}
