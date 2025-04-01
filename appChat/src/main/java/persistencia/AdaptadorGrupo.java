package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import modelo.Mensaje;
import beans.Propiedad;
import modelo.ContactoIndividual;
import modelo.Grupo;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupo implements IAdaptadorGrupoDAO{
    private static ServicioPersistencia servPersistencia;
    private static AdaptadorGrupo unicaInstancia = null;

    public static AdaptadorGrupo getUnicaInstancia() { // patron singleton
        if (unicaInstancia == null)
            return new  AdaptadorGrupo();
        else
            return unicaInstancia;
    }

    private  AdaptadorGrupo() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    //Funcion para registar un grupo en la bbdd
    public void registrarGrupo(Grupo grupo) {
        // Comprobar si el contacto ya está registrado
        if (servPersistencia.recuperarEntidad(grupo.getId()) != null) {
            return; // Ya existe, no lo registramos de nuevo
        }

        // Crear una nueva entidad para el contacto
        Entidad eGrupo = new Entidad();
        eGrupo.setNombre("Grupo");
        
        //registrar aquellos los cuales sean objetos en este caso contactos
        
        siNoExistenContactos(grupo.getContactos());
        
        // Añadir propiedades
        eGrupo.setPropiedades(new ArrayList<Propiedad>(
            Arrays.asList(
                new Propiedad("nombre", grupo.getNombre()),
                new Propiedad("contactos", obtenerIdsContactos(grupo.getContactos()))
            )
        ));
        // Guardar en la persistencia
        eGrupo = servPersistencia.registrarEntidad(eGrupo);
        // ID unico para ese contacto
        grupo.setId(eGrupo.getId());
        //Guardamos en el pool 
        PoolDAO.getUnicaInstancia().addObjeto(grupo.getId(), grupo); 
    }
    
    //Funcion auxiliar para registar los contactos
    private void siNoExistenContactos(List<ContactoIndividual> contactos) {
		contactos.stream()
				 .forEach(AdaptadorContactoIndividual.getUnicaInstancia()::registrarContacto); // Registramos cada contacto
	}
    //Funcion auxiliar para obtener los ids de los contactos pasados como parametros 
	private String obtenerIdsContactos(List<ContactoIndividual> contactos) {
    	return contactos.stream()
    					.map(contacto -> String.valueOf(contacto.getId()))
    					.collect(Collectors.joining(","));
	}
	
	//Función para poder recuperar un grupo
	public Grupo recuperarGrupo(int id) {
        // Si la entidad está en el pool, la devuelve directamente
        if (PoolDAO.getUnicaInstancia().contiene(id)) {
            return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(id);
        }
        // Sino, la recupera de la base de datos
        // Recuperamos la entidad
        Entidad eGrupo = servPersistencia.recuperarEntidad(id);
        if (eGrupo == null) {
            return null;
        }
        // Recuperar propiedades que no son objetos
        String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombre");
        Grupo grupo = new Grupo(nombre, new LinkedList<>());
        grupo.setId(id);

        // Metemos al grupo en el pool antes de llamar a otros adaptadores
        PoolDAO.getUnicaInstancia().addObjeto(id, grupo);

        // Contactos que el grupo tiene
        String contactosId = servPersistencia.recuperarPropiedadEntidad(eGrupo, "contactos");
        grupo.agregarContactos(obtenerContactosDesdeCodigos(contactosId));

        return grupo;
    }
    
	//Funcion auxiliar para obtener los contactos desde los codigos 
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
    
    
    //Funcion que te permitirá modificar un grupo 
    public void modificarGrupo(Grupo grupo) {
        // Recuperar la entidad asociada al grupo
        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getId());

        // Iterar sobre las propiedades de la entidad y actualizarlas según corresponda
        for (Propiedad prop : eGrupo.getPropiedades()) {
            if (prop.getNombre().equals("nombre")) {
                prop.setValor(grupo.getNombre());
            } else if (prop.getNombre().equals("contactos")) {
                prop.setValor(String.valueOf(grupo.getContactos()));
            } else if (prop.getNombre().equals("mensajes")) {
                prop.setValor(obtenerIdsMensajes(grupo.getMensajes()));
            }
            // Guardar los cambios en la propiedad actualizada
            servPersistencia.modificarPropiedad(prop);
        }
    }
	
    //Funcion auxiliar para obtener los ids de los mensajes recibidos como parametros
	private String obtenerIdsMensajes(List<Mensaje> mensajesRecibidos) {
        return mensajesRecibidos.stream().map(m -> String.valueOf(m.getId())).reduce("", (l, m) -> l + m + " ")
                .trim();
    }
	
}