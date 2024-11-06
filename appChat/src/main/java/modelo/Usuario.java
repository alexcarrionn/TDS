package modelo;

public class Usuario {
	private String telefono; 
	private String nombre;
	private String imagen;
	
	public Usuario(String telefono, String nombre, String imagen) {
		super();
		this.telefono = telefono;
		this.nombre = nombre;
		this.imagen = imagen;
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
}
