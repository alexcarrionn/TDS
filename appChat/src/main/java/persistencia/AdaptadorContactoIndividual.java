package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (servPersistencia.recuperarEntidades(c.getMovil()) != null) {
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

    
    private void siNoExisteMensajes(List<Mensaje> mensajes) {
        mensajes.stream()
                .forEach(AdaptadorMensaje.getUnicaInstancia()::registrarMensaje); // Registramos cada mensaje
    }

    private void siNoExisteUsuario(Usuario usuario) {
        AdaptadorUsuario.getUnicaInstancia().registrarUsuario(usuario);        
    }


	public ContactoIndividual recuperarContacto(int id) {
        // Recuperar la entidad desde la persistencia
        Entidad eContacto = servPersistencia.recuperarEntidad(id);
        if (eContacto == null) return null;

        // Obtener las propiedades
        String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, "nombre");
        String movil = servPersistencia.recuperarPropiedadEntidad(eContacto, "movil");
        
        // Crear el objeto ContactoIndividual
        ContactoIndividual contacto = new ContactoIndividual(nombre, movil);
        contacto.setId(id); // Asignar ID recuperado
        
        //recuperamos todos los mensajes del usuario 
        List<Mensaje> mensajesUsuario=obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eContacto, "mensajes")); 
        contacto.setMensajes(mensajesUsuario);
        
        //Ahora lo que haremos sera obtener el Usuario del contacto
        //primero llamamos al adaptadorUsuario para recuperara a aquel usuario cuyo codigo sea el que tenemos
        AdaptadorUsuario adap = AdaptadorUsuario.getUnicaInstancia(); 
    	Usuario u = adap.recuperarUsuario(Integer.valueOf(id)); 
    	//cambiamos
        contacto.setUsuario(u); 
        //devolvemos el contacto individual
        return contacto;
    }

    public void modificarContacto(ContactoIndividual contacto) {
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getId());
        //Cambiamos cada una de las propiedades del contacto 
        servPersistencia.eliminarPropiedadEntidad(eContacto, "nombre"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "nombre", contacto.getNombre()); 
        servPersistencia.eliminarPropiedadEntidad(eContacto, "movil"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "movil", contacto.getMovil()); 
        servPersistencia.eliminarPropiedadEntidad(eContacto, "mensajes"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "mensajes", obtenerIdsMensajes(contacto.getMensajes()));
        servPersistencia.eliminarPropiedadEntidad(eContacto, "usuario"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "usuario", String.valueOf(contacto.getUsuario().getId()));
    }
    
    //Para recuperar todos los Contactos Individuales lo que hacemos es 
    public List<ContactoIndividual> recuperarTodosContactos(){
    	//TODO crear funcion para conseguir todos los Contactos Individuales
    	return null;
    }
    
    
    public static String obtenerIdsMensajes(List<Mensaje> mensajes) {
        // Usando Java Streams (método moderno)
        return mensajes.stream()
                      .map(mensaje -> String.valueOf(mensaje.getId()))
                      .collect(Collectors.joining(","));}
    
    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        AdaptadorMensaje adaptadorMensajes = AdaptadorMensaje.getUnicaInstancia();
        
        return Arrays.stream(codigos.split(" "))
                     .map(code -> {
                         try {
                             return adaptadorMensajes.recuperarMensaje(Integer.valueOf(code));
                         } catch (NumberFormatException e) {
                             System.err.println("Código de mensaje inválido: " + code);
                             return null;
                         }
                     })
                     .filter(mensaje -> mensaje != null)
                     .collect(Collectors.toList());
    }
    
   
}