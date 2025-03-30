package persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import modelo.Mensaje;
import modelo.TipoMensaje;

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
                        new Propiedad("tipo", String.valueOf(mensaje.getTipo())),
                        new Propiedad("fecha", mensaje.getHora().toString()),
                        new Propiedad("grupo", String.valueOf(mensaje.isGrupo()))
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
            } else if (prop.getNombre().equals("tipo")) {
                prop.setValor(String.valueOf(mensaje.getTipo()));
            } else if (prop.getNombre().equals("fecha")) {
                prop.setValor(mensaje.getHora().toString());
            } else if (prop.getNombre().equals("grupo")) {
            	prop.setValor(String.valueOf(mensaje.isGrupo()));
            }
            servPersistencia.modificarPropiedad(prop);
        }
    }
    
    public Mensaje recuperarMensaje(int codigo) {
        // Si el mensaje está en el pool, se devuelve directamente
        if (PoolDAO.getUnicaInstancia().contiene(codigo))
            return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);

        Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);

        // Recuperar propiedades
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, "texto");
        LocalDateTime fecha = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, "fecha"));
        String tipo = servPersistencia.recuperarPropiedadEntidad(eMensaje, "tipo");

        // Crear el mensaje sin emisor ni receptor al principio
        Mensaje mensaje = new Mensaje(texto, TipoMensaje.valueOf(tipo.toUpperCase()), fecha);
        mensaje.setId(codigo);

        // Añadir al pool antes de recuperar más propiedades
        PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);

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