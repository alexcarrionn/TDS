package modelo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase abstracta que representa un contacto en el sistema.
 * Los contactos pueden ser tanto individuos como grupos, y contienen mensajes
 * asociados a ellos. Proporciona métodos para crear mensajes de texto y emoticonos,
 * además de gestionar los atributos relacionados con el contacto, como su nombre
 * y la lista de mensajes asociados.
 */
public abstract class Contacto {
    
    // Atributos de la clase Contacto
    private int id;
    private String nombre;
    private List<Mensaje> mensajes;

    /**
     * Constructor que inicializa el nombre del contacto y la lista de mensajes como vacía.
     * 
     * @param nombre Nombre del contacto.
     */
    public Contacto(String nombre) {
        this(nombre, new LinkedList<>());
    }

    /**
     * Constructor que inicializa el nombre del contacto y la lista de mensajes.
     * 
     * @param nombre Nombre del contacto.
     * @param mensajes Lista de mensajes asociados al contacto.
     */
    public Contacto(String nombre, List<Mensaje> mensajes) {
        this.nombre = nombre;
        this.mensajes = mensajes;
    }

    // Getters y Setters
    
    /**
     * Obtiene el nombre del contacto.
     * 
     * @return El nombre del contacto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del contacto.
     * 
     * @param nombre El nombre del contacto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de mensajes asociados al contacto.
     * 
     * @return La lista de mensajes del contacto.
     */
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Obtiene el identificador del contacto.
     * 
     * @return El identificador del contacto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del contacto.
     * 
     * @param id El identificador del contacto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método que crea un mensaje de texto para un contacto individual.
     * 
     * @param texto El texto del mensaje.
     * @param tipo El tipo de mensaje.
     * @return El mensaje creado.
     */
    public Mensaje creaMensajeTexto(String texto, TipoMensaje tipo) {
        Mensaje m = new Mensaje(texto, tipo, LocalDateTime.now(), false);
        mensajes.add(m);
        return m;
    }

    /**
     * Método que crea un mensaje de texto para un grupo.
     * 
     * @param texto El texto del mensaje.
     * @param tipo El tipo de mensaje.
     * @return El mensaje creado.
     */
    public Mensaje creaMensajeTextoGrupo(String texto, TipoMensaje tipo) {
        Mensaje m = new Mensaje(texto, tipo, LocalDateTime.now(), true);
        mensajes.add(m);
        return m;
    }

    /**
     * Método que crea un mensaje con un emoticono para un grupo.
     * 
     * @param emoticono El código del emoticono.
     * @param tipo El tipo de mensaje.
     * @return El mensaje creado.
     */
    public Mensaje creaMensajeEmoticonoGrupo(int emoticono, TipoMensaje tipo) {
        Mensaje m = new Mensaje(emoticono, tipo, LocalDateTime.now(), true);
        mensajes.add(m);
        return m;
    }

    /**
     * Método que crea un mensaje con un emoticono para un contacto individual.
     * 
     * @param emoticono El código del emoticono.
     * @param tipo El tipo de mensaje.
     * @return El mensaje creado.
     */
    public Mensaje creaMensajeEmoticono(int emoticono, TipoMensaje tipo) {
        Mensaje m = new Mensaje(emoticono, tipo, LocalDateTime.now(), false);
        mensajes.add(m);
        return m;
    }

    /**
     * Método abstracto que debe ser implementado por las subclases para obtener la imagen
     * asociada al contacto, ya sea de un {@link ContactoIndividual} o un {@link Grupo}.
     * 
     * @return La imagen del contacto.
     */
    public abstract String getFoto();

}
