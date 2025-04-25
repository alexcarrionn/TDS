package persistencia;

import modelo.Grupo;

/**
 * Interfaz del Adaptador DAO (Data Access Object) para la clase {@link Grupo}.
 * Define las operaciones básicas de persistencia que deben implementarse para manejar objetos de tipo Grupo.
 * Esta interfaz forma parte del patrón DAO y permite desacoplar la lógica de acceso a datos del resto del sistema.
 */
public interface IAdaptadorGrupoDAO {

    /**
     * Registra un nuevo grupo en el sistema de persistencia.
     * 
     * @param g el grupo a registrar
     */
    public void registrarGrupo(Grupo g);

    /**
     * Recupera un grupo a partir de su identificador único.
     * 
     * @param id el identificador del grupo
     * @return el objeto {@link Grupo} correspondiente al identificador,
     *         o {@code null} si no se encuentra
     */
    public Grupo recuperarGrupo(int id);

    /**
     * Modifica los datos de un grupo ya existente en el sistema de persistencia.
     * 
     * @param g el grupo con los datos actualizados
     */
    public void modificarGrupo(Grupo g);
}
