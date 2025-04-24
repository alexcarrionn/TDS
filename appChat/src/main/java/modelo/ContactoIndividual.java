package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa un contacto individual.
 * Esta clase extiende de {@link Contacto} y añade atributos y métodos específicos para
 * manejar un contacto individual, como el número de móvil, el usuario asociado y la gestión
 * de mensajes recibidos.
 */

public class ContactoIndividual extends Contacto {

    // Atributos de ContactoIndividual
    private String movil;
    private Usuario usuario;

    /**
     * Constructor que inicializa un contacto individual con su nombre, número de móvil y usuario asociado.
     * 
     * @param nombre Nombre del contacto individual.
     * @param movil Número de móvil del contacto.
     * @param usuario El usuario asociado a este contacto.
     */
    public ContactoIndividual(String nombre, String movil, Usuario usuario) {
        super(nombre);
        this.movil = movil;
        this.usuario = usuario;
    }

    /**
     * Constructor que inicializa un contacto individual con su nombre, número de móvil, lista de mensajes
     * y usuario asociado.
     * 
     * @param nombre Nombre del contacto individual.
     * @param movil Número de móvil del contacto.
     * @param mensajes Lista de mensajes asociados al contacto.
     * @param usuario El usuario asociado a este contacto.
     */
    public ContactoIndividual(String nombre, String movil, LinkedList<Mensaje> mensajes, Usuario usuario) {
        super(nombre, mensajes);
        this.movil = movil;
        this.usuario = usuario;
    }

    // Getters y setters
    
    /**
     * Obtiene el número de móvil del contacto.
     * 
     * @return El número de móvil del contacto.
     */
    public String getMovil() {
        return movil;
    }
    
    /**
     * Obtiene el estado del usuario asociado al contacto.
     * 
     * @return El estado del usuario.
     */
    public String getEstado() {
        Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(movil);
        return usuario.getEstado();
    }

    /**
     * Obtiene la imagen del usuario asociado al contacto.
     * 
     * @return La imagen del usuario.
     */
    @Override
    public String getFoto() {
        return usuario.getImagen();
    }

    /**
     * Obtiene el usuario asociado al contacto.
     * 
     * @return El usuario asociado.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado al contacto.
     * 
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Métodos

    /**
     * Método para devolver la lista de mensajes recibidos de un usuario específico.
     * 
     * @param usuario El usuario del que se quieren obtener los mensajes.
     * @return Lista con los mensajes recibidos del usuario.
     */
    public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
        ContactoIndividual contacto = getContacto(usuario.orElse(null));
        if (contacto != null) {
            return contacto.getMensajes();
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * Método privado que busca un contacto individual en la lista de contactos del usuario.
     * 
     * @param usuario El usuario que se desea buscar.
     * @return El contacto individual si se encuentra, o {@code null} si no.
     */
    private ContactoIndividual getContacto(Usuario usuario) {
        return usuario.getContactos().stream()
                .filter(contacto -> contacto instanceof ContactoIndividual)
                .map(contacto -> (ContactoIndividual) contacto)
                .filter(contacto -> contacto.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    /**
     * Método que comprueba si el contacto está asociado a un usuario específico.
     * 
     * @param otro El usuario a comprobar.
     * @return {@code true} si el contacto está asociado al usuario, {@code false} si no.
     */
    public boolean isUsuario(Usuario otro) {
        return usuario.equals(otro);
    }

    /**
     * Método que añade una lista de mensajes al contacto.
     * 
     * @param mensajes Lista de mensajes a añadir.
     */
    public void addAllMensajes(List<Mensaje> mensajes) {
        this.getMensajes().addAll(mensajes);
    }

    /**
     * Método que comprueba si el contacto tiene un nombre vacío.
     * 
     * @return {@code true} si el nombre es vacío, {@code false} si no.
     */
    public boolean isContactoInverso() {
        if (getNombre().equals(" ")) return true;
        return false;
    }
}
