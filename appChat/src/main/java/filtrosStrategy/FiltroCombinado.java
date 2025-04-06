package filtrosStrategy;

import java.util.ArrayList;
import java.util.List;

import modelo.Mensaje;

/**
 * Esta clase lo que te va a permitir es aplicar diferentes filtros a la misma vez, es decir, aplicar multiples filtros
 * de forma secuencial. Cada filtro se aplica sobre el resultado del filtro anterior, asegurando así que los mensajes devueltos
 * cumplan con todos los citerios establecidos
 */

public class FiltroCombinado implements Filtro{
	
	//Tenemos la lista con los filtros que se van a usar 
	private List<Filtro> filtros; 
	
	
	/**
	 * Creamos el filtro con los filtros a utilizar 
	 */
	public FiltroCombinado() {
		this.filtros = new ArrayList<>(); 
	}
	
	/**
	 * Funcion Get de los filtros que hay actualmente en el filtro combiando
	 * @return Copia de los filtros
	 */
	public List<Filtro> getFiltros() {
		return new ArrayList<>(filtros);
	}
	
	/**
	 * Creamos la función para añadir un nuevo filtro
	 */
	
	public void anadirFiltro(Filtro filtro) {
		if (filtro != null) {
			filtros.add(filtro); 
		}
	}
	
	/**
	 * Funcion para eliminar un filtro de la lista de los filtros
	 */
	public boolean eliminarFiltro(Filtro filtro) {
		return filtros.remove(filtro);
	}
	
	/**
	 * Filtra la lista de mensajes pasada como parametro, aplicando todos los filtros combinados
	 * 
	 * Cada uno de los filtros de la lista se aplicará de forma secuencial sobre el resultado de los anteriores filtros
	 * 
	 * @param mensajes Lista de mensajes a filtrar, No debe de ser null
	 * @return Retorna la lista de los mensajes con los filtros correspondientes
	 */
	@Override
	public List<Mensaje> filtrarMensaje(List<Mensaje> mensajes) {
		List<Mensaje> mensajesFiltrados = new ArrayList<>(mensajes); 
		for (Filtro f : filtros)
			f.filtrarMensaje(mensajesFiltrados);
		return mensajesFiltrados;
	}

}
