package persistencia;

import java.util.List;
import modelo.Usuario;

/**
 * Interfaz del Adaptador DAO (Data Access Object) para la clase {@link Usuario}.
 * Define las operaciones básicas de persistencia para manejar objetos de tipo Usuario.
 * Permite separar la lógica de acceso a datos del resto de la aplicación mediante el patrón DAO.
 */
public interface IAdaptadorUsuarioDAO {

    /**
     * Registra un nuevo usuario en el sistema de persistencia.
     * 
     * @param cliente el usuario a registrar
     */
    public void registrarUsuario(Usuario cliente);

    /**
     * Modifica los datos de un usuario existente en el sistema de persistencia.
     * 
     * @param cliente el usuario con los datos actualizados
     */
    public void modificarUsuario(Usuario cliente);

    /**
     * Recupera un usuario a partir de su identificador único.
     * 
     * @param codigo el identificador del usuario
     * @return el objeto {@link Usuario} correspondiente al identificador, o {@code null} si no se encuentra
     */
    public Usuario recuperarUsuario(int codigo);

    /**
     * Recupera todos los usuarios registrados en el sistema de persistencia.
     * 
     * @return una lista de objetos {@link Usuario}
     */
    public List<Usuario> recuperarTodosUsuarios();
}
