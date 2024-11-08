package modelo;

public class Mensaje {
	private final String texto; 
	private final Usuario emisor; 
	private final Usuario receptor;
	private final String hora;
	private final String emoticono; 
	private final String tipo; 
	
	
	public String getTexto() {
		return texto;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public Usuario getReceptor() {
		return receptor;
	}
	
	public String getHora() {
		return hora;
	}

	public String getEmoticono() {
		return emoticono;
	}

	public String getTipo() {
		return tipo;
	}

	public Mensaje(String texto, Usuario emisor, Usuario receptor, String hora, String emoticono, String tipo) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
		this.emoticono=emoticono; 
		this.hora=hora; 
		this.tipo=tipo; 
	}
	
	
}
