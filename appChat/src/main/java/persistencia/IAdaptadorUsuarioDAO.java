package persistencia;

import java.util.List;
import modelo.Usuario;

public interface IAdaptadorUsuarioDAO {

	public void registrarCliente(Usuario cliente);
	public void borrarCliente(Usuario cliente);
	public void modificarCliente(Usuario cliente);
	public Usuario recuperarCliente(int codigo);
	public List<Usuario> recuperarTodosClientes();
}
