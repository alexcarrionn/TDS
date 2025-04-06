package filtrosStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import modelo.Mensaje;

/**
 * Filtro que te va a entregar los mensajes que se hayan enviado entre dos usuarios y que sean desde un tiempo 
 * especificado "desde" y otro "hasta"
 */

public class FiltroFechaHora implements Filtro{
	
	private LocalDateTime desde; 
	private LocalDateTime hasta;
	
	/**
	 * Constructor del Filtro pasandole los dos tiempos específicos
	 * @param desde Tiempo desde el que se empieza a buscar 
	 * @param hasta Tiempo hasta el que se busca
	 */
	public FiltroFechaHora(LocalDateTime desde, LocalDateTime hasta) {
		this.desde = desde; 
		this.hasta = hasta; 
	}
	
	/**
	 * Función que ejecutará la lógica de filtrar los mensajes por Fecha y Hora 
	 * @param mensajes Lista de mensajes que se le pasará para que los filtre
	 * @return Retorna la lista de los mensajes que se encuentran entre las dos horas correspondientes
	 */
	
	@Override
	public List<Mensaje> filtrarMensaje(List<Mensaje> mensajes) {
		return mensajes.stream()
				.filter(m -> m.getHora().isAfter(desde) && m.getHora().isBefore(hasta))
				.collect(Collectors.toList());
	}

}
