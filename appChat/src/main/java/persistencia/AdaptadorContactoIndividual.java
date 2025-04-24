package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import modelo.ContactoIndividual;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Adaptador de la entidad ContactoIndividual para interactuar con la base de datos.
 * Implementa la interfaz IAdaptadorContactoIndividualDAO y se encarga de registrar,
 * modificar y recuperar objetos ContactoIndividual de la base de datos.
 */
public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO {

    // Atributos
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorContactoIndividual unicaInstancia = null;

    // Inicializador
    /**
     * Método que devuelve la instancia única del adaptador (patrón Singleton).
     * 
     * @return Instancia única de AdaptadorContactoIndividual.
     */
    public static AdaptadorContactoIndividual getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorContactoIndividual();
        else
            return unicaInstancia;
    }

    /**
     * Constructor privado para inicializar el adaptador de contacto individual.
     */
    private AdaptadorContactoIndividual() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Registra un contacto individual en la base de datos.
     * 
     * @param c ContactoIndividual a registrar.
     */
    public void registrarContacto(ContactoIndividual c) {
        // Comprobar si el contacto ya está registrado
        if (servPersistencia.recuperarEntidad(c.getId()) != null) {
            return; // Ya existe, no lo registramos de nuevo
        }

        // Crear una nueva entidad para el contacto
        Entidad eContacto = new Entidad();
        eContacto.setNombre("Contacto");

        // Registrar objetos asociados como Usuario y Mensajes
        siNoExisteUsuario(c.getUsuario()); 
        siNoExisteMensajes(c.getMensajes());

        // Añadir propiedades del contacto
        eContacto.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
                new Propiedad("nombre", c.getNombre()),
                new Propiedad("movil", c.getMovil()),
                new Propiedad("mensajes", obtenerIdsMensajes(c.getMensajes())),
                new Propiedad("usuario", String.valueOf(c.getUsuario().getId()))
        )));
        // Guardar en la persistencia
        eContacto = servPersistencia.registrarEntidad(eContacto);
        // Asignar el ID único al contacto
        c.setId(eContacto.getId());
        // Guardar en el pool de objetos
        PoolDAO.getUnicaInstancia().addObjeto(c.getId(), c);
    }

    // Función auxiliar para registrar los mensajes
    private void siNoExisteMensajes(List<Mensaje> mensajes) {
        mensajes.forEach(AdaptadorMensaje.getUnicaInstancia()::registrarMensaje); // Registrar cada mensaje
    }

    // Función auxiliar para registrar el usuario
    private void siNoExisteUsuario(Usuario usuario) {
        AdaptadorUsuario.getUnicaInstancia().registrarUsuario(usuario);
    }

    /**
     * Recupera un ContactoIndividual de la base de datos utilizando su ID.
     * 
     * @param id Identificador único del ContactoIndividual.
     * @return El ContactoIndividual recuperado de la base de datos.
     */
    public ContactoIndividual recuperarContacto(int id) {
        if (PoolDAO.getUnicaInstancia().contiene(id))
            return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(id);

        Entidad entidadContacto = servPersistencia.recuperarEntidad(id);

        if (entidadContacto == null) return null;

        // Obtener las propiedades del contacto
        String nombre = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "nombre");
        String movil = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "movil");
        String usuarioId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "usuario");

        // Crear el objeto ContactoIndividual
        ContactoIndividual contacto = new ContactoIndividual(nombre, movil, new LinkedList<Mensaje>(), null);
        contacto.setId(id);

        // Añadir al pool de objetos
        PoolDAO.getUnicaInstancia().addObjeto(id, contacto);

        // Recuperar el usuario y asignarlo al contacto
        Usuario usuario = obtenerUsuarioDesdeCodigo(usuarioId);
        contacto.setUsuario(usuario);

        // Recuperar los mensajes y asignarlos al contacto
        String mensajesId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "mensajes");
        contacto.addAllMensajes(obtenerMensajesDesdeCodigos(mensajesId));

        return contacto;
    }

    // Función auxiliar para obtener el usuario desde el código
    private Usuario obtenerUsuarioDesdeCodigo(String usuarioId) {
        return AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(usuarioId));
    }

    // Función auxiliar para obtener los mensajes desde los códigos
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
     * Modifica los datos de un contacto en la base de datos.
     * 
     * @param contacto ContactoIndividual con los datos actualizados.
     */
    public void modificarContacto(ContactoIndividual contacto) {
        // Recuperar la entidad asociada al contacto
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getId());

        // Iterar sobre las propiedades y actualizarlas
        for (Propiedad p : eContacto.getPropiedades()) {
            if (p.getNombre().equals("nombre")) {
                p.setValor(contacto.getNombre());
            } else if (p.getNombre().equals("movil")) {
                p.setValor(contacto.getMovil());
            } else if (p.getNombre().equals("mensajes")) {
                p.setValor(obtenerIdsMensajes(contacto.getMensajes()));
            } else if (p.getNombre().equals("usuario")) {
                p.setValor(String.valueOf(contacto.getUsuario().getId()));
            }
            // Guardar los cambios en la propiedad
            servPersistencia.modificarPropiedad(p);
        }
    }

    // Función auxiliar para obtener los IDs de los mensajes
    private String obtenerIdsMensajes(List<Mensaje> mensajesRecibidos) {
        return mensajesRecibidos.stream()
                .map(m -> String.valueOf(m.getId()))
                .reduce("", (l, m) -> l + m + " ")
                .trim();
    }
}
