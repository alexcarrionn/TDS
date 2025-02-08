package persistencia;

import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import modelo.ContactoIndividual;
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
        eContacto.setNombre("ContactoIndividual");

        // Añadir propiedades
        eContacto.setPropiedades(new ArrayList<Propiedad>(
            Arrays.asList(
                new Propiedad("nombre", c.getNombre()),
                new Propiedad("movil", c.getMovil()),
                new Propiedad("usuario", String.valueOf(c.getUsuario().getId()))
            )
        ));

        // Guardar en la persistencia
        eContacto = servPersistencia.registrarEntidad(eContacto);

        // Asignar ID al contacto
        c.setId(eContacto.getId());
    }

    
    public ContactoIndividual recuperarContacto(int tel) {
        return null;
    }
    public void modificarContacto(ContactoIndividual contacto) {
    	
    }
}