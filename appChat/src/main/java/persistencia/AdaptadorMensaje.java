package persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.Mensaje;
import modelo.Usuario;

public class AdaptadorMensaje implements IAdaptadorMensajeDAO{
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorMensaje unicaInstancia = null;

    public static AdaptadorMensaje getUnicaInstancia() {
        if (unicaInstancia == null)
            return new AdaptadorMensaje();
        else
            return unicaInstancia;
    }

    private AdaptadorMensaje() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

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
                        new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getId())),
                        new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getId())),
                        new Propiedad("fecha", mensaje.getHora().toString())
                )));
        // registrar entidad mensaje
        eMensaje = servPersistencia.registrarEntidad(eMensaje);
        // asignar identificador unico
        mensaje.setId(eMensaje.getId());
        // Guardamos en el pool
        PoolDAO.getUnicaInstancia().addObjeto(mensaje.getId(), mensaje);
    }

    public void borrarMensaje(Mensaje mensaje) {
        Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getId());
        servPersistencia.borrarEntidad(eMensaje);
    }

    public void modificarMensaje(Mensaje mensaje) {
        Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getId());

        for (Propiedad prop : eMensaje.getPropiedades()) {
            if (prop.getNombre().equals("texto")) {
                prop.setValor(mensaje.getTexto());
            } else if (prop.getNombre().equals("emisor")) {
                prop.setValor(String.valueOf(mensaje.getEmisor().getId()));
            } else if (prop.getNombre().equals("receptor")) {
                prop.setValor(String.valueOf(mensaje.getReceptor().getId()));
            } else if (prop.getNombre().equals("fecha")) {
                prop.setValor(mensaje.getHora().toString());
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }

    /*public Mensaje recuperarMensaje(int id) {
        Entidad eMensaje = servPersistencia.recuperarEntidad(id);
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        Usuario emisor = null;
        Contacto receptor = null;
        LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha"));

        Mensaje mensaje = new Mensaje(texto, emisor, receptor, fecha);
        mensaje.setId(eMensaje.getId());
        
        PoolDAO.getUnicaInstancia().addObjeto(id, mensaje);
        
        AdaptadorUsuario adaptadorU = AdaptadorUsuario.getUnicaInstancia();
		int codigoUsuario = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"));
		emisor = adaptadorU.recuperarUsuario(codigoUsuario);
		mensaje.setEmisor(emisor);
        
		AdaptadorContactoIndividual adaptadorC = AdaptadorContactoIndividual.getUnicaInstancia();
		int codigoContacto = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
		receptor = adaptadorC.recuperarContacto(codigoContacto);
		
        return mensaje;
    }
	*/
    
    public Mensaje recuperarMensaje(int id) {
    	 // Si la entidad está en el pool, la devuelve directamente
	    if (PoolDAO.getUnicaInstancia().contiene(id))
	        return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(id);

	    // Recuperamos la entidad 
	    Entidad eMensaje = servPersistencia.recuperarEntidad(id);
	    
	    //Recuperamos las propiedades que no son objeto
	    String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
	    String emisorId = servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor");
	    Usuario emisor = AdaptadorUsuario.getUnicaInstancia().recuperarUsuario(Integer.valueOf(emisorId));
	    String receptorId = servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor");
	    Contacto receptor = AdaptadorContactoIndividual.getUnicaInstancia().recuperarContacto(Integer.valueOf(receptorId)); 
	    LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha"));; 
	    
	    // Crear el objeto Usuario
	    Mensaje mensaje = new Mensaje(texto, emisor, receptor, fecha);
        mensaje.setId(eMensaje.getId());

	    // IMPORTANTE: Añadir el usuario al pool antes de llamar a otros adaptadores
        PoolDAO.getUnicaInstancia().addObjeto(id, mensaje);
	  
	    return mensaje;
    }
    
    public List<Mensaje> recuperarTodosMensajes() {
        List<Mensaje> mensajes = new ArrayList<>();
        List<Entidad> eMensajes = servPersistencia.recuperarEntidades("Mensaje");
        for (Entidad eMensaje : eMensajes) {
            mensajes.add(recuperarMensaje(eMensaje.getId()));
        }
        return mensajes;
    }
}