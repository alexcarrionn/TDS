package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import controlador.AppChat;
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

    
    private void siNoExisteMensajes(List<Mensaje> mensajes) {
        mensajes.stream()
                .forEach(AdaptadorMensaje.getUnicaInstancia()::registrarMensaje); // Registramos cada mensaje
    }

    private void siNoExisteUsuario(Usuario usuario) {
        AdaptadorUsuario.getUnicaInstancia().registrarUsuario(usuario);        
    }
    
    public ContactoIndividual recuperarContacto(int id) {
    	if (PoolDAO.getUnicaInstancia().contiene(id))
            return (ContactoIndividual) PoolDAO.getUnicaInstancia().getObjeto(id);
    	
    	Entidad entidadContacto = servPersistencia.recuperarEntidad(id); 
    	
    	if(entidadContacto==null)return null;
    	
    	String nombre = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "nombre");
        String movil = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "movil");
        String usuarioId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "usuario");
        Usuario usuario = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(usuarioId));
        
        ContactoIndividual contacto = new ContactoIndividual(nombre, movil,new LinkedList<Mensaje>(),usuario);
        contacto.setId(id);
        
        String mensajesId = servPersistencia.recuperarPropiedadEntidad(entidadContacto, "mensajes"); 
        contacto.addAllMensajes(obtenerMensajesDesdeCodigos(mensajesId)); 
        
        PoolDAO.getUnicaInstancia().addObjeto(id, contacto);
        return contacto; 
    }
	
   /* private Usuario obtenerUsuarioDesdeCodigo(String codigo) {
        return AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(codigo));
    }*/

	/*public void modificarContacto(ContactoIndividual contacto) {
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getId());
        //Cambiamos cada una de las propiedades del contacto 
        servPersistencia.eliminarPropiedadEntidad(eContacto, "nombre"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "nombre", contacto.getNombre()); 
        
        servPersistencia.eliminarPropiedadEntidad(eContacto, "movil"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "movil", contacto.getMovil()); 
        
        servPersistencia.eliminarPropiedadEntidad(eContacto, "mensajes"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "mensajes", AppChat.getUnicaInstancia().obtenerIdsMensajes(contacto.getMensajes()));
        
        servPersistencia.eliminarPropiedadEntidad(eContacto, "usuario"); 
        servPersistencia.anadirPropiedadEntidad(eContacto, "usuario", String.valueOf(contacto.getUsuario().getId()));
    */
    
    public void modificarContacto(ContactoIndividual contacto) {
        // Recuperar la entidad asociada al contacto
        Entidad eContacto = servPersistencia.recuperarEntidad(contacto.getId());

        // Iterar sobre las propiedades de la entidad y actualizarlas según corresponda
        for (Propiedad p : eContacto.getPropiedades()) {
            switch (p.getNombre()) {
                case "nombre":
                    p.setValor(contacto.getNombre());
                    break;
                case "movil":
                    p.setValor(contacto.getMovil());
                    break;
                case "mensajes":
                    p.setValor(obtenerIdsMensajes(contacto.getMensajes()));
                    break;
                case "usuario":
                    p.setValor(String.valueOf(contacto.getUsuario().getId()));
                    break;
            }
            // Guardar los cambios en la propiedad actualizada
            servPersistencia.modificarPropiedad(p);
        }
    }
    
    private String obtenerIdsMensajes(List<Mensaje> mensajesRecibidos) {
        return mensajesRecibidos.stream().map(m -> String.valueOf(m.getId())).reduce("", (l, m) -> l + m + " ")
                .trim();
    }

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
    
}