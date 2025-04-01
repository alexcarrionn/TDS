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

public class AdaptadorContactoIndividual implements IAdaptadorContactoIndividualDAO{
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorContactoIndividual unicaInstancia = null;

    public static AdaptadorContactoIndividual getUnicaInstancia() { // patron singleton
        if (unicaInstancia == null)
            return new AdaptadorContactoIndividual();
        else
            return unicaInstancia;
    }

    private AdaptadorContactoIndividual() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public void registrarContacto(ContactoIndividual c) {
        // Comprobar si el contacto ya está registrado
        if (servPersistencia.recuperarEntidad(c.getId()) != null) {
            return; // Ya existe, no lo registramos de nuevo
        }

        // Crear una nueva entidad para el contacto
        Entidad eContacto = new Entidad();
        eContacto.setNombre("Contacto");
        
        //registrar aquellos los cuales sean objetos en este caso Usuario y Mensajes
        siNoExisteUsuario(c.getUsuario()); 
        siNoExisteMensajes(c.getMensajes());
        
        // Añadir propiedades
        eContacto.setPropiedades(new ArrayList<Propiedad>(
            Arrays.asList(
                new Propiedad("nombre", c.getNombre()),
                new Propiedad("movil", c.getMovil()),
                new Propiedad("mensajes", obtenerIdsMensajes(c.getMensajes())),
                new Propiedad("usuario", String.valueOf(c.getUsuario().getId()))
            )
        ));
        // Guardar en la persistencia
        eContacto = servPersistencia.registrarEntidad(eContacto);
        // ID unico para ese contacto
        c.setId(eContacto.getId());
        //Guardamos en el pool 
        PoolDAO.getUnicaInstancia().addObjeto(c.getId(), c); 
    }

    //Funcion auxiliar para registrar los mensajes 
    private void siNoExisteMensajes(List<Mensaje> mensajes) {
        mensajes.stream()
                .forEach(AdaptadorMensaje.getUnicaInstancia()::registrarMensaje); // Registramos cada mensaje
    }
    
    //Funcion auxiliar para registar el usuario
    private void siNoExisteUsuario(Usuario usuario) {
        AdaptadorUsuario.getUnicaInstancia().registrarUsuario(usuario);        
    }
    
    //Funcion para recuperar un contactoIndividual de la BBDDs
    public ContactoIndividual recuperarContacto(int id) {
    	if (PoolDAO.getUnicaInstancia().contiene(id))
            return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(id);
    	
    	Entidad entidadContacto = servPersistencia.recuperarEntidad(id); 
    	
    	if(entidadContacto==null)return null;
    	
    	String nombre = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "nombre");
        String movil = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "movil");
        String usuarioId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "usuario");
        
        
        ContactoIndividual contacto = new ContactoIndividual(nombre, movil,new LinkedList<Mensaje>(),null);
        contacto.setId(id);
        
        //IMPORTANTE: Llamar primero al DAO antes de llamar a otros adaptadores
        PoolDAO.getUnicaInstancia().addObjeto(id, contacto);
        
        //Recuperamos al usuario y se lo añadimos
        Usuario usuario = obtenerUsuarioDesdeCodigo(usuarioId);
        contacto.setUsuario(usuario);
        //recuperamos los mensajes y se lo añadimos
        String mensajesId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "mensajes"); 
        contacto.addAllMensajes(obtenerMensajesDesdeCodigos(mensajesId)); 
        
        
        return contacto; 
    }
	
    //Funcion auxiliar par obtener al usuario desde el codigo 
    private Usuario obtenerUsuarioDesdeCodigo(String usuarioId) {
    	return AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(usuarioId));
    }
    //funcion auxiliar para obtener los mensajes desde los codigos 
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
    
    
    //Funcion para modificar un contacto
    public void modificarContacto(ContactoIndividual contacto) {
        // Recuperar la entidad asociada al contacto
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getId());

        // Iterar sobre las propiedades de la entidad y actualizarlas según corresponda
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
            // Guardar los cambios en la propiedad actualizada
            servPersistencia.modificarPropiedad(p);
        }
    }

    //Funcion auxiliar para obtener los ids de los mensajes pasados como parametros
    private String obtenerIdsMensajes(List<Mensaje> mensajesRecibidos) {
        return mensajesRecibidos.stream().map(m -> String.valueOf(m.getId())).reduce("", (l, m) -> l + m + " ")
                .trim();
    }


    
}