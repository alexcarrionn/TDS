package modelo;

import java.time.LocalDate;

public class Mensaje {
	
	//propiedades
	private String texto; 
	private Usuario emisor; 
	private Contacto receptor;
	private LocalDate hora;
	private int emoticono; 
	private int id;
	
	//pensar en si en mensaje el receptor es un Contacto o un Usuario y si hora es LocalDate o String
	
	//getters y setters
	public String getTexto() {
		return texto;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public Contacto getReceptor() {
		return receptor;
	}
	
	public LocalDate getHora() {
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

	public Mensaje(String texto, Usuario emisor, Contacto receptor, LocalDate hora) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.hora=hora; 
	}
	
	public Mensaje(int emoticono, Usuario emisor, Contacto receptor, LocalDate hora) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.emoticono=emoticono; 
		this.hora=hora; 
	}
}
