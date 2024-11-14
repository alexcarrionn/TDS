package persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {
	public TDSFactoriaDAO () {
	}


	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuario.getUnicaInstancia();
	}
	
}
