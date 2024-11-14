package modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private String telefono; 
	private String nombre;
	private String imagen;
	private String contraseña; 
	private Boolean premium;
	private String fecha; 
	private String estado;
	private List<Contactos> contactos; 
	
	public Usuario(String telefono, String nombre, String imagen,String contraseña, String fecha, String estado) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
		this.premium = false; 
		this.contraseña = contraseña; 
		this.fecha=fecha; 
		this.estado=estado;
		contactos= new ArrayList<Contactos>(); 
	}
	
	public String getContraseña() {
		return contraseña;
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
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
	
	
	
}
