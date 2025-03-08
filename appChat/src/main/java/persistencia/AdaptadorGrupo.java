package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import controlador.AppChat;
import modelo.ContactoIndividual;
import modelo.Grupo;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorGrupo {
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

    private void siNoExistenContactos(List<ContactoIndividual> contactos) {
		contactos.stream()
				 .forEach(AdaptadorContactoIndividual.getUnicaInstancia()::registrarContacto); // Registramos cada contacto
	}

	private String obtenerIdsContactos(List<ContactoIndividual> contactos) {
    	return contactos.stream()
    					.map(contacto -> String.valueOf(contacto.getId()))
    					.collect(Collectors.joining(","));
	}

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
    
    public List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
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

	public void modificarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getId());
		//Eliminamos la propiedad nombre y la agregamos despues con el nuevo valor
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "nombre");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "nombre", grupo.getNombre());
		
		//Eliminamos la propiedad contactos y la agregamos despues con los nuevos valores
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "contactos");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "contactos", String.valueOf(grupo.getContactos()));
		
		//Eliminamos la propiedad mensajes y la agregamos despues con los nuevos valores
        servPersistencia.eliminarPropiedadEntidad(eGrupo, "mensajes"); 
        servPersistencia.anadirPropiedadEntidad(eGrupo, "mensajes", AppChat.getUnicaInstancia().obtenerIdsMensajes(grupo.getMensajes()));
    }
}