package modelo;

public class Mensaje {
	private final String texto; 
	private final Usuario emisor; 
	private final Usuario receptor;
	
	public String getTexto() {
		return texto;
	}
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public Usuario getReceptor() {
		return receptor;
	}

	public Mensaje(String texto, Usuario emisor, Usuario receptor) {
		super();
		this.texto = texto;
		this.emisor = emisor;
		this.receptor = receptor;
	}
	
	
}
