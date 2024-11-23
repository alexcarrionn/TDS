package persistencia;

/*Esta clase la vamos a utilizar para evitar problemas de referencias duplicadas al manejar instancias de Usuario. Funciona como un Singleton que almacena
y guarda los objetos de Usuario para que no se creen varias instancias del mismo usuario en la memoria */
import java.util.HashMap;
import java.util.Map;

import modelo.Usuario;

	public class PoolUsuarios {
	    public static final PoolUsuarios INSTANCE = new PoolUsuarios();
	    private Map<Integer, Usuario> pool;

	    private PoolUsuarios() {
	        pool = new HashMap<>();
	    }

	    public boolean containsUsuario(int id) {
	        return pool.containsKey(id);
	    }

	    public Usuario getUsuario(int id) {
	        return pool.get(id);
	    }

	    public void addUsuario(int id, Usuario usuario) {
	        pool.put(id, usuario);
	    }
	}
