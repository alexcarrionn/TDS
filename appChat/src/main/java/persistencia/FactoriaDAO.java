package persistencia;

/**
 * Define una factoria abstracta que devuelve todos los DAO de la aplicacion
 */

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia;

	public static final String DAO_TDS = "persistencia.TDSFactoriaDAO";

	/**
	 * Crea un tipo de factoria DAO. Solo existe el tipo TDSFactoriaDAO
	 * @param tipo tipo
	 * @throws DAOException 
	 */
	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null)
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		return unicaInstancia;
	}

	/**
	 * Devuelve una única Instancia de Factoria Dao
	 * 
	 * @return FactoriaDAO
	 * @throws DAOException 
	 */
	public static FactoriaDAO getInstancia() throws DAOException {
		if (unicaInstancia == null)
			return getInstancia(FactoriaDAO.DAO_TDS);
		else
			return unicaInstancia;
	}

	/**
	 * Construsctor
	 */
	protected FactoriaDAO() {
	};

	/*
	 * Metodos factoria que devuelven adaptadores que implementen estos interfaces
	 */
	

	/**
	 * Devuelve el adaptador DAO para la entidad Usuario. Este adaptador
	 * gestiona las operaciones de persistencia relacionadas con los objetos
	 * Usuario.
	 * 
	 * @return una instancia de IAdaptadorUsuarioDAO
	 */
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();

	/**
	 * Devuelve el adaptador DAO para la entidad  Mensaje. Este adaptador
	 * gestiona las operaciones de persistencia relacionadas con los objetos
	 * Mensaje.
	 * 
	 * @return una instancia de  IAdaptadorMensajeDAO
	 */
	public abstract IAdaptadorMensajeDAO getMensajeDAO();

	/**
	 * Devuelve el adaptador DAO para la entidad ContactoIndividual. Este
	 * adaptador gestiona las operaciones de persistencia relacionadas con los
	 * objetos ContactoIndividual.
	 * 
	 * @return una instancia de {@link IAdaptadorContactoIndividualDAO}
	 */
	public abstract IAdaptadorContactoIndividualDAO getContactoIndividualDAO();

	/**
	 * Devuelve el adaptador DAO para la entidad Grupo. Este adaptador
	 * gestiona las operaciones de persistencia relacionadas con los objetos
	 * grupo.
	 * 
	 * @return una instancia de {@link IAdaptadorGrupoDAO}
	 */
	public abstract IAdaptadorGrupoDAO getGrupoDAO();

}
