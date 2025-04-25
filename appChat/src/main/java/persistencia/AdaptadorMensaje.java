package persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import modelo.Mensaje;
import modelo.TipoMensaje;

/**
 * Clase encargada de interactuar con la base de datos para realizar operaciones
 * de persistencia relacionadas con los objetos de tipo {@link Mensaje}.
 * Utiliza el patrón de diseño Singleton para asegurar que haya una única
 * instancia de la clase.
 */
public class AdaptadorMensaje implements IAdaptadorMensajeDAO{
	//Atributos
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorMensaje unicaInstancia = null;
    
    /**
     * Obtiene la única instancia del adaptador, asegurando el patrón Singleton.
     * 
     * @return La instancia única del adaptador de mensajes.
     */
    
    public static AdaptadorMensaje getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorMensaje();
        else
            return unicaInstancia;
    }

    
    /**
     * Constructor privado para inicializar el servicio de persistencia.
     */
    
    private AdaptadorMensaje() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }
    
    //Métodos 
    
    /**
     * Método que sirve para registar un mensaje en la base de datos
     * @param mensaje mensaje que se quiere registar en la base de datos
     */
    public void registrarMensaje(Mensaje mensaje) {
        Entidad eMensaje = null;

        // Si la entidad esta registrada no la registra de nuevo
        try {
            eMensaje = servPersistencia.recuperarEntidad(mensaje.getId());
        } catch (NullPointerException e) {}
        if (eMensaje != null) return;

        // crear entidad mensaje
        eMensaje = new Entidad();
        eMensaje.setNombre("Mensaje");
        eMensaje.setPropiedades(new ArrayList<Propiedad>(
                Arrays.asList(
                        new Propiedad("texto", mensaje.getTexto()),
                        new Propiedad("emoji", String.valueOf(mensaje.getEmoticono())),
                        new Propiedad("tipo", String.valueOf(mensaje.getTipo())),
                        new Propiedad("fecha", mensaje.getHora().toString()),
                        new Propiedad("grupo", String.valueOf(mensaje.isGrupo()))
                )));
        // registrar entidad mensaje
        eMensaje = servPersistencia.registrarEntidad(eMensaje);
        // asignar identificador unico
        mensaje.setId(eMensaje.getId());
        // Guardamos en el pool
        PoolDAO.getUnicaInstancia().addObjeto(mensaje.getId(), mensaje);
    }
    
    /**
     * Método para recuperar los mensajes a traves del identificador
     * @param codigo identificador del mensaje que se quiere recuperar
     * @return mensaje con el identificador correspondiente
     */
    public Mensaje recuperarMensaje(int codigo) {
        // Si el mensaje está en el pool, se devuelve directamente
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);

        // Recuperar propiedades
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        int emoji = Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emoji"));
        LocalDateTime fecha = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha"));
        String tipo = servPersistencia.recuperarPropiedadEntidad(eMensaje, "tipo");
        
        Mensaje mensaje;

        // Crear el mensaje sin emisor ni receptor al principio
        if(texto==null) {
             mensaje = new Mensaje(emoji, TipoMensaje.valueOf(tipo.toUpperCase()), fecha, false);	
        }
        else {
        	 mensaje = new Mensaje(texto, TipoMensaje.valueOf(tipo.toUpperCase()), fecha, false);
        }

        mensaje.setId(codigo);

        // Añadir al pool antes de recuperar más propiedades
        PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

        return mensaje;
    }
}