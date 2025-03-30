package modelo;

import java.time.LocalDateTime;

public class Mensaje implements Comparable<Mensaje>{
	
	//CAMBIAR EMISOR Y RECEPTOR A TIPO 
	
	//propiedades
	private String texto; 
	private TipoMensaje tipo;
	private LocalDateTime hora;
	private int emoticono; 
	private int id;
	private boolean grupo=false;
	
	//pensar en si en mensaje el receptor es un Contacto o un Usuario y si hora es LocalDate o String
	
	public Mensaje(String texto, TipoMensaje tipo, LocalDateTime hora) {
		this.texto = texto;
		this.tipo=tipo;
		this.hora=hora; 
	}
	
	public Mensaje(int emoticono, TipoMensaje tipo, LocalDateTime hora) {
		this.tipo=tipo;
		this.emoticono=emoticono; 
		this.hora=hora; 
	}
	
	
	//getters y setters
	public String getTexto() {
		return texto;
	}
	
	public TipoMensaje getTipo() {
		return tipo;
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
	
	public void setTipo(TipoMensaje tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Mensaje [texto=" + texto + ", TipoMensaje=" + tipo + ", hora=" + hora
				+ ", emoticono=" + emoticono + ", id=" + id + ", grupo=" + grupo + "]";
	}

	@Override
	public int compareTo(Mensaje o) {
		return hora.compareTo(o.hora);
	}
}
