package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import descuentoStrategy.Descuento;

public class Usuario {
	//Atributos 
	/**
	 * Precio normal que se le pondra a un usuario si no es premium
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
	
	//Constructor
	public Usuario(String telefono, String nombre, String imagen,String contraseña, String estado,Descuento descuentos) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña;
		this.fecha=LocalDate.now();
		this.estado=estado;
		this.contactos= new ArrayList<Contacto>();
		this.descuento = Optional.ofNullable(descuentos);
	}
	
	public Usuario(String telefono, String nombre, String imagen,String contraseña, LocalDate fecha, String estado,Descuento descuento) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña; 
		this.fecha=fecha; 
		this.estado=estado;
		this.contactos= new ArrayList<Contacto>();
		this.descuento = Optional.ofNullable(descuento);
	}

	public Usuario(String nombre2, String movil, String contrasena, LocalDate parse, String imagen2, String saludo) {
		nombre=nombre2; 
		telefono=movil; 
		contraseña=contrasena;
		fecha=parse; 
		imagen=imagen2; 
		estado=saludo;
		premium=false;
		descuento = Optional.empty();
	}

	//Getters y Setters
	public String getContraseña() {
		return contraseña;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public Boolean isPremium() {
		return premium;
	}

	public void setPremium(Boolean isPremium) {
		this.premium = isPremium;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Contacto> getContactos() {
		return new ArrayList<>(contactos);
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	/*
	public Optional<Descuento> getDescuento() {
		return descuento;
	}*/
	
	public double getPrecio() {
		if (descuento.isPresent()){
			return descuento.get().getDescuento(PRECIO_PREMIUM);
		} else
			return PRECIO_PREMIUM;
	}
	
	public void setDescuento(Descuento d) {
		this.descuento = Optional.ofNullable(d);
	}

	
	public int getNumMensajes() {
		return contactos.stream()
		.mapToInt(contacto-> contacto.getMensajes().size())
		.sum();
		
	}
	
	public List<Mensaje> getMensajes(Contacto contacto) {
		return contacto.getMensajes();
	}

	
	//Metodos 
	
	/**
	 * Método que devuelve los grupos que tiene el usuario
	 * @return lista de grupos del usuario
	 */
    
    public List<Contacto> obtenerGrupos() {
        List<Contacto> grupos = new ArrayList<>();
        for (Contacto contacto : contactos) {
            if (contacto instanceof Grupo) {
                grupos.add((Grupo) contacto);  // Añadir los grupos a la lista
            }
        }
        return grupos;
    }
	
	/**
	 * Método para añadir un Contacto individual a los contactos del usuario
	 * @param contacto que se quiere añadir
	 */
	
	public void addContacto(ContactoIndividual contacto) {
		contactos.add(contacto);
	}
	
	/*
	public void removeContacto(Contacto contacto) {
		contactos.remove(contacto);
	}*/
	
	/**
	 * Método que sirve para añadir un Grupo a la lista de contactos del usuario
	 * @param grupo
	 */
	public void addGrupo(Grupo grupo) {
		contactos.add(grupo);
	}
	
	/*
	public void addAllContactos(List<Contacto> Contactos) {
		//aqui tenemos que hacer un for each para añadir todos los contactos
		for (Contacto c: Contactos) {
			contactos.add(c);
		}
	}*/
	
	/*
	//Función que sirve para poder contar todos los mensajes que tiene el usuario
	public int contarMensajesTotales() {
	    return contactos.stream() // Convertimos la lista de contactos en un Stream
	            .mapToInt(contacto -> contacto.getMensajes().size()) // Obtenemos el número de mensajes por contacto
	            .sum(); // Sumamos todos los tamaños de las listas de mensajes
	}
	*/
	

	/**
	 * Método que sirve para saber si existe algun contacto, en la lista del usuario, con el nombre pasado como parametro
	 * @param nombre
	 * @return true si existe, false en caso contrario
	 */
	public boolean contieneContacto(String nombre) {
		return contactos.stream()
						.filter(c-> c instanceof ContactoIndividual)
						.anyMatch(c->c.getNombre().equals(nombre));
	}
	
	/**
	 *  Método que sirve para saber si existe algun grupo, en la lista del usuario, con el nombre pasado como parametro
	 * @param nombre
	 * @return
	 */
	public boolean contieneGrupo(String nombre) {
		return contactos.stream()
						.filter(c-> c instanceof Grupo)
						.anyMatch(c->c.getNombre().equals(nombre));
	}
	
	/**
	 * Método para poder crear un contactoIndividual
	 * @param nombre2
	 * @param telefono2
	 * @param usuario
	 * @return contacto nuevo
	 */
	public ContactoIndividual crearContactoIndividual(String nombre2, String telefono2, Usuario usuario) {
		return new ContactoIndividual(nombre2, telefono2, usuario);
	}
	
	/**
	 * Método que sirve para poder crear un Grupo
	 * @param groupName
	 * @param contactos2
	 * @param foto
	 * @return grupo nuevo
	 */
	public Grupo crearGrupo(String groupName, List<ContactoIndividual> contactos2, String foto) {
		return new Grupo(groupName, contactos2, foto);
	}
	
	/**
	 * Método que te devuelve un ContactoIndividual a traves del telefono
	 * @param telefono
	 * @return
	 */
	public ContactoIndividual getContactoIndividual(String telefono) {
	    return contactos.stream()
	                    .filter(c -> c instanceof ContactoIndividual)
	                    .map(c -> (ContactoIndividual) c)
	                    .filter(c -> c.getMovil().equals(telefono))
	                    .findFirst()
	                    .orElse(null);
	}
	
	/**
	 * Método que devuelve todos los grupos del usuario logueado
	 * @return Lista de los grupos del usuario
	 * 
	 */
	public List<Grupo> recuperarTodosGrupos(){
		return this.contactos.stream()
				.filter(c -> c instanceof Grupo)
				.map(c -> (Grupo) c)
				.collect(Collectors.toList());
	}
	
	/**
	 * Método para reuperar todos los usuarios individuales del usuario
	 * @return Lista de Contactos Individuales
	 */
	public List<ContactoIndividual> recuperarTodosLosContactos(){
		return contactos.stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual)c)
				.collect(Collectors.toList());
	}
	
	//To string y el hashCode y el equals
		@Override
		public String toString() {
			return "Usuario [telefono=" + telefono + ", nombre=" + nombre + ", imagen=" + imagen + ", contraseña="
					+ contraseña + ", premium=" + premium + ", fecha=" + fecha + ", estado=" + estado + "]";
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		

		@Override
		public int hashCode() {
			return Objects.hash(contactos, contraseña, descuento, estado, fecha, id, imagen, nombre, premium, telefono);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Usuario other = (Usuario) obj;
			return Objects.equals(contactos, other.contactos) && Objects.equals(contraseña, other.contraseña)
					&& Objects.equals(descuento, other.descuento) && Objects.equals(estado, other.estado)
					&& Objects.equals(fecha, other.fecha) && id == other.id && Objects.equals(imagen, other.imagen)
					&& Objects.equals(nombre, other.nombre) && Objects.equals(premium, other.premium)
					&& Objects.equals(telefono, other.telefono);
		}

	}
