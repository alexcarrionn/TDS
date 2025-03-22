package modelo;

import java.time.LocalDateTime;

public class Mensaje {
	
	//propiedades
	private String texto; 
	private Usuario emisor; 
	private Contacto receptor;
	private LocalDateTime hora;
	private int emoticono; 
	private int id;
	private boolean grupo=false;
	
	//pensar en si en mensaje el receptor es un Contacto o un Usuario y si hora es LocalDate o String
	
	public Mensaje(String texto, Usuario emisor, Contacto receptor, LocalDateTime hora) {
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.hora=hora; 
	}
	
	public Mensaje(int emoticono, Usuario emisor, Contacto receptor, LocalDateTime hora) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.emoticono=emoticono; 
		this.hora=hora; 
	}
	
	
	//getters y setters
	public String getTexto() {
		return texto;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public Contacto getReceptor() {
		return receptor;
	}
	
	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}
	
	public LocalDateTime getHora() {
		return hora;
	}

	public int getEmoticono() {
		return emoticono;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isGrupo() {
		return grupo;
	}

	public void setGrupo(boolean grupo) {
		this.grupo = grupo;
	}
}
