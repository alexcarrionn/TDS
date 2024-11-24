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

public class AdaptadorMensaje {
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
                        new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getTelefono())),
                        new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getTelefono())),
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
                prop.setValor(String.valueOf(mensaje.getEmisor().getTelefono()));
            } else if (prop.getNombre().equals("receptor")) {
                prop.setValor(String.valueOf(mensaje.getReceptor().getTelefono()));
            } else if (prop.getNombre().equals("fecha")) {
                prop.setValor(mensaje.getHora().toString());
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }

    public Mensaje recuperarMensaje(int id) {
        Entidad eMensaje = servPersistencia.recuperarEntidad(id);
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        Usuario emisor = PoolUsuarios.INSTANCE.getUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor")));
        Contacto receptor = PoolDAO.getUnicaInstancia().getContacto(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor")));
        LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha"));

        Mensaje mensaje = new Mensaje(texto, emisor, receptor, fecha);
        mensaje.setId(eMensaje.getId());
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