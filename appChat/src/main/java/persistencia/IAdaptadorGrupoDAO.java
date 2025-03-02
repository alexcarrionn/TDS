package persistencia;

import modelo.Grupo;

public interface IAdaptadorGrupoDAO {

	public void registrarGrupo(Grupo g);
	
	public Grupo recuperarGrupo(int id);
	
	public void modificarGrupo(Grupo g);
}
