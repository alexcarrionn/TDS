package filtrosStrategy;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import modelo.Mensaje;

/**
 * Filtro que te va a entregar los mensajes que se hayan enviado entre dos usuarios
 * y que sean desde un tiempo "desde" y otro "hasta", usando solo la fecha (sin hora).
 */
public class FiltroFechaHora implements Filtro {

    private LocalDate desde;
    private LocalDate hasta;

    /**
     * Constructor del Filtro pasándole las dos fechas (sin hora)
     * @param desde Fecha desde la cual se empieza a buscar
     * @param hasta Fecha hasta la cual se busca
     */
    public FiltroFechaHora(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    /**
     * Función que ejecutará la lógica de filtrar los mensajes por Fecha
     * @param mensajes Lista de mensajes que se le pasará para que los filtre
     * @return Retorna la lista de los mensajes que se encuentran entre las dos fechas correspondientes
     */
    @Override
    public List<Mensaje> filtrarMensaje(List<Mensaje> mensajes) {
        return mensajes.stream()
                .filter(m -> (m.getHora().toLocalDate().isEqual(desde) || 
                             m.getHora().toLocalDate().isAfter(desde)) && 
                             (m.getHora().toLocalDate().isEqual(hasta) || 
                             m.getHora().toLocalDate().isBefore(hasta)))
                .collect(Collectors.toList());
    }

}
