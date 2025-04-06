package filtrosStrategy;

import java.util.List;
import java.util.stream.Collectors;

import modelo.Mensaje;
import modelo.TipoMensaje;

/**
 * Este filtro lo que hace es escoger aquellos mensajes que cumplan con el tipo de mensaje que se especifica 
  */

public class FiltroTipoMensaje implements Filtro{

	private TipoMensaje tipo; 
	
	/**
	 * Creamos un filtro basado en el tipo del mensaje ya sea enviado o recibido
	 * @param tipo
	 */
	public FiltroTipoMensaje(TipoMensaje tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Filtro que va a coger solo aquellos mensajes que sean del tipo especificado
	 * @param lista de mensajes a los que se le aplicará el filtro
	 */
	@Override
	public List<Mensaje> filtrarMensaje(List<Mensaje> mensajes) {
		return mensajes.stream()
				.filter(m -> m.getTipo()== tipo)
				.collect(Collectors.toList());
	}

}
