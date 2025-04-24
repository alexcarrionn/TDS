package filtrosStrategy;
import java.util.List;
import modelo.Mensaje;


/*
 * Esta función la vamos a utilizar con el patrón de diseño Strategy, para poder hacer que cada filtro sea una 
 * estrategia que defina un comportamiento específico de filtrado de mensajes. Esta clase va a especificar el comportamiento 
 * que es común para todos los filtros, ya sea el nombre, numero de telefono...
 * 
 * Estas clases deberán de implementar la lógica específica de filtrado de los mensajes según el critério deseado
 * 
 */

public interface Filtro {
	/**
	 * Filtra la lista de mensajes según se desee
	 * 
	 * @param mensajes mensaje a filtrar 
	 * @return Retornará una lista de los mensajes del usuario que cumplan con el filtro deseado
	 */
	List<Mensaje>filtrarMensaje(List<Mensaje> mensajes);
}
