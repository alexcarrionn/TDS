package persistencia;

import java.util.List;

import modelo.Mensaje;

public interface IAdaptadorMensajeDAO {
	public void registrarMensaje(Mensaje mensaje);
	public Mensaje recuperarMensaje(int codigo);
	public List<Mensaje> recuperarTodosMensajes();
}
