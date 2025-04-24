package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import descuentoStrategy.Descuento;

/**
 * Clase que representa a un usuario en el sistema, con atributos como el teléfono,
 * nombre, imagen, contraseña, estado, contactos, y opciones de premium y descuento.
 * Además, permite gestionar los contactos individuales y grupos del usuario, junto
 * con los mensajes recibidos y enviados.
 */

public class Usuario {
    
    /**
     * Precio normal que se le pondrá a un usuario si no es premium
     */
    private static final double PRECIO_PREMIUM = 10.00;

    private String telefono;
    private int id;
    private String nombre;
    private String imagen;
    private String contraseña;
    private Boolean premium;
    private LocalDate fecha;
    private String estado;
    private List<Contacto> contactos;
    private Optional<Descuento> descuento;

    /**
     * Constructor de la clase Usuario
     * 
     * @param telefono   El número de teléfono del usuario
     * @param nombre     El nombre del usuario
     * @param imagen     La imagen del usuario
     * @param contraseña La contraseña del usuario
     * @param fecha      La fecha de registro del usuario
     * @param estado     El estado del usuario (por ejemplo, en línea, ocupado, etc.)
     * @param descuento  Un objeto de descuento asociado al usuario, si aplica
     */
    public Usuario(String telefono, String nombre, String imagen, String contraseña, LocalDate fecha, String estado,
            Descuento descuento) {
        super();
        this.telefono = telefono;
        this.nombre = nombre;
        this.imagen = imagen;
        this.premium = false;
        this.contraseña = contraseña;
        this.fecha = fecha;
        this.estado = estado;
        this.contactos = new ArrayList<Contacto>();
        this.descuento = Optional.ofNullable(descuento);
    }

    // Getters y Setters

    /**
     * Obtiene la contraseña del usuario
     * 
     * @return La contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Obtiene el id del usuario
     * 
     * @return El id del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el id del usuario
     * 
     * @param id El id del usuario
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Verifica si el usuario es premium
     * 
     * @return true si el usuario es premium, false si no lo es
     */
    public Boolean isPremium() {
        return premium;
    }

    /**
     * Establece si el usuario es premium
     * 
     * @param isPremium true si el usuario es premium, false si no lo es
     */
    public void setPremium(Boolean isPremium) {
        this.premium = isPremium;
    }

    /**
     * Obtiene el número de teléfono del usuario
     * 
     * @return El número de teléfono del usuario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene el nombre del usuario
     * 
     * @return El nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la imagen del usuario
     * 
     * @return La imagen del usuario
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del usuario
     * 
     * @param imagen La nueva imagen del usuario
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la fecha de registro del usuario
     * 
     * @return La fecha de registro del usuario
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Obtiene el estado del usuario
     * 
     * @return El estado del usuario (por ejemplo, en línea, ocupado)
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene una lista de los contactos del usuario
     * 
     * @return Una lista con los contactos del usuario
     */
    public List<Contacto> getContactos() {
        return new ArrayList<>(contactos);
    }

    /**
     * Obtiene el precio que se le aplica al usuario, considerando el descuento si
     * aplica
     * 
     * @return El precio del usuario después de aplicar el descuento, si lo tiene
     */
    public double getPrecio() {
        if (descuento.isPresent()) {
            return descuento.get().getDescuento(PRECIO_PREMIUM);
        } else
            return PRECIO_PREMIUM;
    }

    /**
     * Establece un descuento para el usuario
     * 
     * @param d El descuento que se desea aplicar al usuario
     */
    public void setDescuento(Descuento d) {
        this.descuento = Optional.ofNullable(d);
    }

    /**
     * Obtiene el número total de mensajes del usuario
     * 
     * @return El número total de mensajes recibidos por el usuario
     */
    public int getNumMensajes() {
        return contactos.stream().mapToInt(contacto -> contacto.getMensajes().size()).sum();
    }

    /**
     * Obtiene la lista de mensajes de un contacto en particular
     * 
     * @param contacto El contacto del que se desean obtener los mensajes
     * @return Una lista con los mensajes del contacto
     */
    public List<Mensaje> getMensajes(Contacto contacto) {
        return contacto.getMensajes();
    }

    // Métodos para manejar contactos y grupos

    /**
     * Obtiene la lista de grupos a los que pertenece el usuario
     * 
     * @return Una lista con los grupos del usuario
     */
    public List<Contacto> obtenerGrupos() {
        List<Contacto> grupos = new ArrayList<>();
        for (Contacto contacto : contactos) {
            if (contacto instanceof Grupo) {
                grupos.add((Grupo) contacto); // Añadir los grupos a la lista
            }
        }
        return grupos;
    }

    /**
     * Añade un contacto individual a la lista de contactos del usuario
     * 
     * @param contacto El contacto individual que se desea añadir
     */
    public void addContacto(ContactoIndividual contacto) {
        contactos.add(contacto);
    }

    /**
     * Añade un grupo a la lista de contactos del usuario
     * 
     * @param grupo El grupo que se desea añadir
     */
    public void addGrupo(Grupo grupo) {
        contactos.add(grupo);
    }

    /**
     * Verifica si existe un contacto con un nombre específico en la lista de
     * contactos del usuario
     * 
     * @param nombre El nombre del contacto a buscar
     * @return true si existe el contacto, false si no existe
     */
    public boolean contieneContacto(String nombre) {
        return contactos.stream().filter(c -> c instanceof ContactoIndividual)
                .anyMatch(c -> c.getNombre().equals(nombre));
    }

    /**
     * Verifica si existe un grupo con un nombre específico en la lista de contactos
     * del usuario
     * 
     * @param nombre El nombre del grupo a buscar
     * @return true si existe el grupo, false si no existe
     */
    public boolean contieneGrupo(String nombre) {
        return contactos.stream().filter(c -> c instanceof Grupo).anyMatch(c -> c.getNombre().equals(nombre));
    }

    /**
     * Crea un nuevo contacto individual para el usuario
     * 
     * @param nombre2   El nombre del nuevo contacto
     * @param telefono2 El teléfono del nuevo contacto
     * @param usuario   El usuario al que pertenece este contacto
     * @return Un nuevo objeto ContactoIndividual
     */
    public ContactoIndividual crearContactoIndividual(String nombre2, String telefono2, Usuario usuario) {
        return new ContactoIndividual(nombre2, telefono2, usuario);
    }

    /**
     * Crea un nuevo grupo para el usuario
     * 
     * @param groupName  El nombre del nuevo grupo
     * @param contactos2 La lista de contactos que serán parte del grupo
     * @param foto       La foto del grupo
     * @return Un nuevo objeto Grupo
     */
    public Grupo crearGrupo(String groupName, List<ContactoIndividual> contactos2, String foto) {
        return new Grupo(groupName, contactos2, foto);
    }

    /**
     * Obtiene un contacto individual a partir de su número de teléfono
     * 
     * @param telefono El número de teléfono del contacto
     * @return El contacto individual correspondiente, o null si no se encuentra
     */
    public ContactoIndividual getContactoIndividual(String telefono) {
        return contactos.stream().filter(c -> c instanceof ContactoIndividual).map(c -> (ContactoIndividual) c)
                .filter(c -> c.getMovil().equals(telefono)).findFirst().orElse(null);
    }

    /**
     * Recupera todos los grupos del usuario
     * 
     * @return Una lista con todos los grupos del usuario
     */
    public List<Grupo> recuperarTodosGrupos() {
        return this.contactos.stream().filter(c -> c instanceof Grupo).map(c -> (Grupo) c).collect(Collectors.toList());
    }

    /**
     * Recupera todos los contactos individuales del usuario
     * 
     * @return Una lista con todos los contactos individuales del usuario
     */
    public List<ContactoIndividual> recuperarTodosLosContactos() {
        return contactos.stream().filter(c -> c instanceof ContactoIndividual).map(c -> (ContactoIndividual) c)
                .collect(Collectors.toList());
    }

}
