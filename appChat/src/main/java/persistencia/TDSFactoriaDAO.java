package persistencia;

/**
 * Implementación concreta de la factoría de DAOs para el sistema de persistencia TDS.
 * Esta clase proporciona instancias únicas de los adaptadores DAO utilizados para acceder a los datos
 * de las entidades del sistema (Usuario, Mensaje, ContactoIndividual y Grupo).
 * 
 * Utiliza el patrón Singleton en cada adaptador para garantizar una única instancia por clase DAO.
 */
public class TDSFactoriaDAO extends FactoriaDAO {

    /**
     * Constructor por defecto de la factoría TDS.
     */
    public TDSFactoriaDAO() {
    }

    /**
     * Devuelve la instancia única del adaptador DAO para la entidad Usuario.
     * 
     * @return instancia de {@link IAdaptadorUsuarioDAO}
     */
    @Override
    public IAdaptadorUsuarioDAO getUsuarioDAO() {
        return AdaptadorUsuario.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO para la entidad Mensaje.
     * 
     * @return instancia de {@link IAdaptadorMensajeDAO}
     */
    @Override
    public IAdaptadorMensajeDAO getMensajeDAO() {
        return AdaptadorMensaje.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO para la entidad ContactoIndividual.
     * 
     * @return instancia de {@link IAdaptadorContactoIndividualDAO}
     */
    @Override
    public IAdaptadorContactoIndividualDAO getContactoIndividualDAO() {
        return AdaptadorContactoIndividual.getUnicaInstancia();
    }

    /**
     * Devuelve la instancia única del adaptador DAO para la entidad Grupo.
     * 
     * @return instancia de {@link IAdaptadorGrupoDAO}
     */
    @Override
    public IAdaptadorGrupoDAO getGrupoDAO() {
        return AdaptadorGrupo.getUnicaInstancia();
    }
}
