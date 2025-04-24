package modelo;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase que representa un grupo de contactos. Un grupo puede contener varios 
 * contactos individuales y tiene una foto asociada.
 */
public class Grupo extends Contacto {
    
    // Atributos 
    
    private List<ContactoIndividual> contactos; // Lista de contactos individuales del grupo
    private String foto; // Foto asociada al grupo
    
    // Getters y Setters
    
    /**
     * Obtiene la lista de contactos individuales del grupo.
     * 
     * @return Una copia de la lista de contactos individuales.
     */
    public List<ContactoIndividual> getContactos() {
        return new LinkedList<>(contactos);
    }

    /**
     * Establece la lista de contactos individuales del grupo.
     * 
     * @param contactos Lista de contactos a establecer.
     */
    public void setContactos(List<ContactoIndividual> contactos) {
        this.contactos = contactos;
    }

    // Constructor
    
    /**
     * Constructor para crear un grupo con un nombre, una lista de contactos y una foto.
     * 
     * @param nombre Nombre del grupo.
     * @param contactos Lista de contactos individuales del grupo.
     * @param foto Foto asociada al grupo.
     */
    public Grupo(String nombre, List<ContactoIndividual> contactos, String foto) {
        super(nombre); // Llamada al constructor de la clase base (Contacto)
        this.contactos = contactos != null ? new LinkedList<>(contactos) : new LinkedList<>();
        this.foto = foto;
    }
    
    // Métodos
    
    /**
     * Agrega una lista de contactos individuales al grupo.
     * 
     * @param usuario Lista de usuarios que se desean agregar al grupo.
     */
    public void agregarContactos(List<ContactoIndividual> usuario) {
        for (ContactoIndividual c : usuario) {
            contactos.add(c);
        }
    }

    /**
     * Añade una lista de mensajes al grupo.
     * 
     * @param mensajes Lista de mensajes a añadir.
     */
    public void addAllMensajes(List<Mensaje> mensajes) {
        this.getMensajes().addAll(mensajes);
    }
    
    /**
     * Añade un contacto individual al grupo.
     * 
     * @param contacto El contacto que se desea agregar al grupo.
     */
    public void agregarContacto(ContactoIndividual contacto) {
        contactos.add(contacto);
    }

    /**
     * Obtiene la foto asociada al grupo.
     * 
     * @return Foto del grupo.
     */
    @Override
    public String getFoto() {
        return this.foto;
    }
}
