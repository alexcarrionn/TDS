package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Usuario {
	private static final double PRECIO_PREMIUM = 10.00;
	private int telefono; 
	private int id; 
	private String nombre;
	private String imagen;
	private String contraseña; 
	private Boolean premium;
	private LocalDate fecha; 
	private String estado;
	private List<Contactos> contactos;
	private Optional<Descuento> descuento;
	
	//Constructor
	//eliminar cuando hagamos bien lo de APPCHAT
	public Usuario(int telefono, String nombre, String imagen,String contraseña, String estado) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña;  
		this.estado=estado;
		contactos= new ArrayList<Contactos>();
	}
	
	public Usuario(int telefono, String nombre, String imagen,String contraseña, String estado,Descuento descuentos) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña;  
		this.estado=estado;
		contactos= new ArrayList<Contactos>();
		this.descuento = Optional.ofNullable(descuentos);
	}
	
	public Usuario(int telefono, String nombre, String imagen,String contraseña, LocalDate fecha, String estado,Descuento descuentos) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña; 
		this.fecha=fecha; 
		this.estado=estado;
		contactos= new ArrayList<Contactos>();
		this.descuento = Optional.ofNullable(descuentos);
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

	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
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

	public List<Contactos> getContactos() {
		return new ArrayList<>(contactos);
	}

	public void setContactos(List<Contactos> contactos) {
		this.contactos = contactos;
	}

	public Boolean getPremium() {
		return premium;
	} 
	
	public Optional<Descuento> getDescuento() {
		return descuento;
	}
	
	public double getPrecio() {
		if (descuento.isPresent()) {
			return descuento.get().getDescuento(PRECIO_PREMIUM);
		} else
			return PRECIO_PREMIUM;
	}
	
	//Tenemos que hacer una funcion que nos devuelva el numero de mensajes enviados en cada mes del año, para calcular si se puede o no hacer el desceunto
	
	//Devolver los grupos que tiene el usuario
	
	//Devolver los contactos Ind
	
	public void addContacto(ContactoIndividual contacto) {
		contactos.add(contacto);
	}
	
	public void addGrupo(Grupo grupo) {
		contactos.add(grupo);
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
			final int prime = 31;
			int result = 1;
			result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
			result = prime * result + telefono;
			return result;
		}


		/**
		 * Dos usuarios serán iguales si tienen el mismo nick o número de teléfono
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Usuario other = (Usuario) obj;
			return nombre.equals(other.nombre) || telefono == other.telefono;
		}
	}
