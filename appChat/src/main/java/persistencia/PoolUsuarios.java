package persistencia;

import java.util.HashMap;
import java.util.Map;
import modelo.Usuario;

/**
 * Clase singleton que implementa un pool de usuarios.
 * Esta clase se utiliza para evitar problemas de referencias duplicadas al manejar instancias de objetos {@link Usuario}.
 * Funciona como un contenedor único que almacena y guarda los objetos de usuario para que no se creen múltiples instancias del mismo usuario en la memoria.
 * Se asegura de que cada usuario solo tenga una única instancia en el sistema.
 */
public class PoolUsuarios {
    
    /**
     * Instancia única de la clase {@link PoolUsuarios}, implementando el patrón Singleton.
     */
    public static final PoolUsuarios INSTANCE = new PoolUsuarios();
    
    /**
     * Mapa que almacena los usuarios, utilizando su identificador único como clave.
     */
    private Map<Integer, Usuario> pool;

    /**
     * Constructor privado para garantizar que solo haya una instancia de {@link PoolUsuarios}.
     * Inicializa el mapa {@link #pool}.
     */
    private PoolUsuarios() {
        pool = new HashMap<>();
    }

    /**
     * Comprueba si el usuario con el identificador dado ya está almacenado en el pool.
     * 
     * @param id el identificador único del usuario
     * @return {@code true} si el usuario está presente en el pool, {@code false} en caso contrario
     */
    public boolean containsUsuario(int id) {
        return pool.containsKey(id);
    }

    /**
     * Obtiene el usuario almacenado en el pool con el identificador dado.
     * 
     * @param id el identificador único del usuario
     * @return el objeto {@link Usuario} correspondiente al identificador, o {@code null} si no se encuentra
     */
    public Usuario getUsuario(int id) {
        return pool.get(id);
    }

    /**
     * Añade un nuevo usuario al pool.
     * Si el usuario ya existe en el pool, se actualizará con la nueva instancia.
     * 
     * @param id el identificador único del usuario
     * @param usuario el objeto {@link Usuario} a añadir al pool
     */
    public void addUsuario(int id, Usuario usuario) {
        pool.put(id, usuario);
    }
}
