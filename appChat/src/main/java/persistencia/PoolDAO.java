package persistencia;

import java.util.Hashtable;

/**
 * Clase singleton que implementa un pool de objetos para los adaptadores que lo necesiten.
 * Esta clase proporciona una manera eficiente de manejar instancias únicas de adaptadores, almacenándolos en un contenedor común.
 * Se asegura de que cada adaptador se cree solo una vez y se reutilice en todo el sistema.
 * 
 * Utiliza el patrón Singleton para garantizar que solo haya una instancia de {@link PoolDAO}.
 */
public class PoolDAO {
    
    /**
     * Instancia única de la clase {@link PoolDAO}, implementando el patrón Singleton.
     */
    private static PoolDAO unicaInstancia;

    /**
     * Mapa que almacena los objetos de tipo {@link Object}, utilizando su identificador único como clave.
     * Se utiliza para gestionar los adaptadores de forma eficiente y evitar la creación redundante de instancias.
     */
    private Hashtable<Integer, Object> pool;

    /**
     * Constructor privado para garantizar que solo haya una instancia de {@link PoolDAO}.
     * Inicializa el contenedor {@link #pool}.
     */
    private PoolDAO() {
        pool = new Hashtable<Integer, Object>();
    }

    /**
     * Devuelve la instancia única del {@link PoolDAO}.
     * Si no existe, crea una nueva instancia.
     * 
     * @return la instancia única de {@link PoolDAO}
     */
    public static PoolDAO getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new PoolDAO();
        }
        return unicaInstancia;
    }

    /**
     * Recupera un objeto almacenado en el pool utilizando su identificador único.
     * 
     * @param id el identificador único del objeto
     * @return el objeto correspondiente al identificador, o {@code null} si no se encuentra
     */
    public Object getObjeto(int id) {
        return pool.get(id);
    }

    /**
     * Añade un nuevo objeto al pool o actualiza el objeto existente con el mismo identificador.
     * 
     * @param id el identificador único del objeto
     * @param objeto el objeto a añadir o actualizar en el pool
     */
    public void addObjeto(int id, Object objeto) {
        pool.put(id, objeto);
    }

    /**
     * Comprueba si el pool contiene un objeto con el identificador dado.
     * 
     * @param id el identificador único del objeto
     * @return {@code true} si el objeto está presente en el pool, {@code false} en caso contrario
     */
    public boolean contiene(int id) {
        return pool.containsKey(id);
    }
}
