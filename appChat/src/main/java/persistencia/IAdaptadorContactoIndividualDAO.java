package persistencia;

import modelo.ContactoIndividual;

/**
 * Interfaz del Adaptador DAO (Data Access Object) para la clase {@link ContactoIndividual}.
 * Define las operaciones básicas de persistencia que deben implementarse para manejar objetos de tipo ContactoIndividual.
 * Esta interfaz forma parte del patrón DAO y permite separar la lógica de acceso a datos del resto de la aplicación.
 */
public interface IAdaptadorContactoIndividualDAO {

    /**
     * Registra un nuevo contacto individual en el sistema de persistencia.
     * 
     * @param c el contacto individual a registrar
     */
    public void registrarContacto(ContactoIndividual c);

    /**
     * Recupera un contacto individual a partir de su identificador único.
     * 
     * @param id el identificador del contacto individual
     * @return el objeto {@link ContactoIndividual} correspondiente al identificador,
     *         o {@code null} si no se encuentra
     */
    public ContactoIndividual recuperarContacto(int id);

    /**
     * Modifica los datos de un contacto individual ya existente en el sistema de persistencia.
     * 
     * @param c el contacto individual con los datos actualizados
     */
    public void modificarContacto(ContactoIndividual c);
}
