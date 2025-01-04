package modelo;

//Cambiar cliente por Usuario
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorUsuarioDAO;


/* El catálogo mantiene los objetos en memoria, en una tabla hash
 * para mejorar el rendimiento. Esto no se podría hacer en una base de
 * datos con un número grande de objetos. En ese caso se consultaria
 * directamente la base de datos
 */
public class RepositorioUsuario {
	private Map<String,Usuario> usuarios; 
	private static RepositorioUsuario unicaInstancia = new RepositorioUsuario();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
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
	
	public static RepositorioUsuario getUnicaInstancia(){
		return unicaInstancia;
	}
	
	/*devuelve todos los Usuarios*/
	public List<Usuario> getUsuarios(){
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario c:usuarios.values()) 
			lista.add(c);
		return lista;
	}
	
	public Usuario getUsuario(String numero) {
		return usuarios.get(numero);
	}
	
	public void addUsuario(Usuario user) {
		usuarios.put(user.getTelefono(),user);
	}
	public void removeUsuario (Usuario user) {
		usuarios.remove(user.getTelefono());
	}
	
	//Recupera todos los usuarios para trabajar con ellos en memoria
	private void cargarCatalogo() throws DAOException {
		 List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		 for (Usuario user: usuariosBD) 
			     usuarios.put(user.getTelefono(),user);
	}
	
	public boolean contains(Usuario usuario) {
		return usuarios.containsValue(usuario);
	}

	
}
