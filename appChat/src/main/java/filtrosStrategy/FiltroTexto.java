package filtrosStrategy;

import java.util.List;
import java.util.stream.Collectors;

import modelo.Mensaje;

/**
 * Filtro de busqueda por Texto, este filtro lo que hace es filtrar los mensajes que contienen el texto especificado en la 
 * Ventana de Grupo, en el caso de que contenga null lo que hace es que no se aplica ningún filtro y los devuelve todos
 */

public class FiltroTexto implements Filtro{
	
	
	private String texto; 
	
	/**
	 * Creamos un filtro basado en el texto especificado
	 * @param texto, si es null no se aplicará ningun filtro y se devolverán todos los mensajes de la lista
	 */
	public FiltroTexto(String texto) {
		this.texto = texto;
	}
	
	/**
	 * Función para filtrar los mensajes según el texto que contengan
	 * 
	 * @param mensajes
	 * @return filtra los mensajes y los devulve segun el texto del mensaje
	 */
	@Override
	public List<Mensaje> filtrarMensaje(List<Mensaje> mensajes) {
		return mensajes.stream()
				//Filtramos los mensajes que sean null, que esten vacios o que tengan el texto especificado
				.filter(m -> texto==null || texto.isEmpty() || m.getTexto().contains(texto))
				.collect(Collectors.toList());
	}

}
