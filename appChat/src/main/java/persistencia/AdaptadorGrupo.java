package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import modelo.Mensaje;
import modelo.ContactoIndividual;
import modelo.Grupo;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase encargada de interactuar con la base de datos para realizar operaciones
 * de persistencia relacionadas con los objetos de tipo {@link Grupo}.
 * Utiliza el patrón de diseño Singleton para asegurar que haya una única
 * instancia de la clase.
 */
public class AdaptadorGrupo implements IAdaptadorGrupoDAO {

    // Atributos
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorGrupo unicaInstancia = null;

    /**
     * Obtiene la única instancia del adaptador, asegurando el patrón Singleton.
     * 
     * @return La instancia única del adaptador de grupos.
     */
    public static AdaptadorGrupo getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorGrupo();
        else
            return unicaInstancia;
    }

    /**
     * Constructor privado para inicializar el servicio de persistencia.
     */
    private AdaptadorGrupo() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Método para registrar un grupo en la base de datos. Si el grupo ya está
     * registrado, no realiza ninguna acción.
     * 
     * @param grupo El grupo que se quiere registrar.
     */
    public void registrarGrupo(Grupo grupo) {
        // Comprobar si el grupo ya está registrado
        if (servPersistencia.recuperarEntidad(grupo.getId()) != null) {
            return; // Ya existe, no lo registramos de nuevo
        }

        // Crear una nueva entidad para el grupo
        Entidad eGrupo = new Entidad();
        eGrupo.setNombre("Grupo");

        // Registrar los contactos si no están registrados
        siNoExistenContactos(grupo.getContactos());

        // Añadir propiedades
        eGrupo.setPropiedades(new ArrayList<Propiedad>(
            Arrays.asList(
                new Propiedad("nombre", grupo.getNombre()),
                new Propiedad("contactos", obtenerIdsContactos(grupo.getContactos())),
                new Propiedad("Imagen", grupo.getFoto()),
                new Propiedad("mensajes", obtenerIdsMensajes(grupo.getMensajes()))
            )
        ));
        
        // Guardar en la persistencia
        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        
        // Asignar ID único al grupo
        grupo.setId(eGrupo.getId());
        
        // Guardar en el pool
        PoolDAO.getUnicaInstancia().addObjeto(grupo.getId(), grupo);
    }

    /**
     * Método auxiliar para registrar los contactos que pertenecen al grupo.
     * 
     * @param contactos Lista de contactos que se desean registrar.
     */
    private void siNoExistenContactos(List<ContactoIndividual> contactos) {
        contactos.stream()
                 .forEach(AdaptadorContactoIndividual.getUnicaInstancia()::registrarContacto); // Registramos cada contacto
    }

    /**
     * Método auxiliar para obtener los identificadores de los contactos del grupo
     * como una cadena de texto.
     * 
     * @param contactos Lista de contactos del grupo.
     * @return Cadena de texto con los identificadores de los contactos.
     */
    private String obtenerIdsContactos(List<ContactoIndividual> contactos) {
        return contactos.stream()
                        .map(contacto -> String.valueOf(contacto.getId()))
                        .collect(Collectors.joining(","));
    }

    /**
     * Método para recuperar un grupo de la base de datos utilizando su identificador.
     * Si el grupo ya está en el pool, se devuelve directamente.
     * 
     * @param id El identificador del grupo que se desea recuperar.
     * @return El grupo correspondiente al identificador dado.
     */
    public Grupo recuperarGrupo(int id) {
        // Si la entidad está en el pool, la devuelve directamente
        if (PoolDAO.getUnicaInstancia().contiene(id)) {
            return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(id);
        }
        
        // Recuperar la entidad del grupo de la base de datos
        Entidad eGrupo = servPersistencia.recuperarEntidad(id);
        if (eGrupo == null) {
            return null;
        }

        // Recuperar propiedades que no son objetos
        String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombre");
        String rutaImagen = servPersistencia.recuperarPropiedadEntidad(eGrupo, "Imagen");
        Grupo grupo = new Grupo(nombre, new LinkedList<>(), rutaImagen);
        grupo.setId(id);

        // Añadir el grupo al pool antes de recuperar sus contactos y mensajes
        PoolDAO.getUnicaInstancia().addObjeto(id, grupo);

        // Recuperar los contactos del grupo
        String contactosId = servPersistencia.recuperarPropiedadEntidad(eGrupo, "contactos");
        grupo.agregarContactos(obtenerContactosDesdeCodigos(contactosId));

        // Recuperar los mensajes del grupo
        String mensajesId = servPersistencia.recuperarPropiedadEntidad(eGrupo, "mensajes");
        grupo.addAllMensajes(obtenerMensajesDesdeCodigos(mensajesId));

        return grupo;
    }

    /**
     * Método auxiliar para obtener los mensajes desde sus códigos.
     * 
     * @param codigos Cadena con los identificadores de los mensajes.
     * @return Lista de mensajes correspondientes a los identificadores dados.
     */
    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> mensajes = new LinkedList<>();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorMensaje adaptadorMensaje = AdaptadorMensaje.getUnicaInstancia();
        while (strTok.hasMoreTokens()) {
            String code = (String) strTok.nextElement();
            mensajes.add(adaptadorMensaje.recuperarMensaje(Integer.valueOf(code)));
        }
        return mensajes;
    }

    /**
     * Método auxiliar para obtener los contactos desde sus códigos.
     * 
     * @param codigos Cadena con los identificadores de los contactos.
     * @return Lista de contactos correspondientes a los identificadores dados.
     */
    private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
        AdaptadorContactoIndividual adaptadorContactos = AdaptadorContactoIndividual.getUnicaInstancia();

        return Arrays.stream(codigos.split(","))
                     .map(code -> {
                         try {
                             return adaptadorContactos.recuperarContacto(Integer.valueOf(code));
                         } catch (NumberFormatException e) {
                             return null;
                         }
                     })
                     .filter(contacto -> contacto != null)
                     .collect(Collectors.toList());
    }

    /**
     * Método para modificar los datos de un grupo en la base de datos.
     * 
     * @param grupo El grupo con los nuevos datos.
     */
    public void modificarGrupo(Grupo grupo) {
        // Recuperar la entidad asociada al grupo
        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getId());

        // Iterar sobre las propiedades de la entidad y actualizarlas
        for (Propiedad prop : eGrupo.getPropiedades()) {
            if (prop.getNombre().equals("nombre")) {
                prop.setValor(grupo.getNombre());
            } else if (prop.getNombre().equals("contactos")) {
                prop.setValor(String.valueOf(grupo.getContactos()));
            } else if (prop.getNombre().equals("mensajes")) {
                prop.setValor(obtenerIdsMensajes(grupo.getMensajes()));
            } else if (prop.getNombre().equals("Imagen")) {
                prop.setValor(grupo.getFoto());
            }
            // Guardar los cambios en la propiedad actualizada
            servPersistencia.modificarPropiedad(prop);
        }
    }

    /**
     * Método auxiliar para obtener los identificadores de los mensajes recibidos
     * como parámetros.
     * 
     * @param mensajesRecibidos Lista de mensajes del grupo.
     * @return Cadena de texto con los identificadores de los mensajes.
     */
    private String obtenerIdsMensajes(List<Mensaje> mensajesRecibidos) {
        return mensajesRecibidos.stream()
                                .map(m -> String.valueOf(m.getId()))
                                .reduce("", (l, m) -> l + m + " ")
                                .trim();
    }
}
