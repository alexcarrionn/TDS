package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Usuario;


//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public static AdaptadorUsuario getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuario();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un cliente se le asigna un identificador unico */
	public void registrarUsuario(Usuario user) {
		Entidad eUsuario = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());
		} catch (NullPointerException e) {}
		if (eUsuario != null) return;

		// registrar primero los atributos que son objetos
		// registrar contactos del usuario
		registrarSiNoExistenContactosoGrupos(user.getContactos());


		// crear entidad usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				 Arrays.asList(
		                    new Propiedad("telefono", String.valueOf(user.getTelefono())),
		                    new Propiedad("nombre", user.getNombre()),
		                    new Propiedad("imagen", user.getImagen()),
		                    new Propiedad("contraseña", user.getContraseña()),
		                    new Propiedad("fecha", user.getFecha().toString()),
		                    new Propiedad("estado", user.getEstado()),
		                    new Propiedad("premium", String.valueOf(user.isPremium()))
		            )));
		// registrar entidad usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		user.setTelefono(eUsuario.getId());
		// Guardamos en el pool
		PoolDAO.getUnicaInstancia().addObjeto(user.getTelefono(), user);
	}

	private void registrarSiNoExistenContactosoGrupos(List<Contacto> contactos) {
		AdaptadorContactoIndividual adaptadorContactos = AdaptadorContactoIndividual.getUnicaInstancia();
		AdaptadorGrupo adaptadorGrupos = AdaptadorGrupo.getUnicaInstancia();
		contactos.stream().forEach(c -> {
			if (c instanceof ContactoIndividual) {
				adaptadorContactos.registrarContacto((ContactoIndividual) c);
			} else {
				adaptadorGrupos.registrarGrupo((Grupo) c);
			}
		});
		
	}

	public void borrarUsuario(Usuario user) {
		// No se comprueban restricciones de integridad con Contacto
		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario user) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("telefono")) {
				prop.setValor(String.valueOf(user.getTelefono()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(String.valueOf(user.getNombre()));
			} else if (prop.getNombre().equals("imagen")) {
				prop.setValor(String.valueOf(user.getImagen()));
			}else if (prop.getNombre().equals("contraseña")){
				prop.setValor(user.getContraseña());
			}else if(prop.getNombre().equals("estado")){
				prop.setValor(user.getEstado());
			}else if(prop.getNombre().equals("premium")) {
				prop.setValor(String.valueOf(user.isPremium())); 
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}
	@Override
	public Usuario recuperarUsuario(int id) {
		 if (PoolUsuarios.INSTANCE.containsUsuario(id)) {
		return PoolUsuarios.INSTANCE.getUsuario(id);
		 }
		 Entidad entidadUsuario = servPersistencia.recuperarEntidad(id);
		 if (entidadUsuario == null) return null;
		 String nombre = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "nombre");
		 int movil = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "movil"));
		 String contrasena = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "contrasena");
		 String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(entidadUsuario,
		"fechaNacimiento");
		 String imagen = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "imagen");
		 String saludo = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "saludo");
		 boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(entidadUsuario,
		"isPremium"));
		 Usuario usuario = new Usuario(nombre, movil, contrasena,LocalDate.parse(fechaNacimientoStr), imagen, saludo);
		 usuario.setId(id);
		 usuario.setPremium(premium);
		 PoolUsuarios.INSTANCE.addUsuario(usuario.getId(), usuario);
		 usuario.addAllContactos(obtenerContactos(
		servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "contactos")));
		 return usuario;
		}
	
	private List<Contacto> obtenerContactos(String contactos) {
		//primero pasamos a stream el String de contactos contactos
	    return Arrays.stream(contactos.split(" "))
	    			  //Convertimos primero cada cadena a entero
	                 .map(Integer::parseInt)
	                 //Una vez que hacemos eso utilizamos map para convertirlos en la entidad correspondiente a traves de la persistencia
	                 .map(servPersistencia::recuperarEntidad)
	                 //Filtramos todos aquellos los cuales no sean null
	                 .filter(Objects::nonNull)
	                 //Utilizamos un map para recoger aquellos COntactos con nombre y telefono que encontramos 
	                 .map(entidad -> {
	                     String nombre = servPersistencia.recuperarPropiedadEntidad(entidad, "nombre");
	                     int movil = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(entidad, "telefono")); 
	                     return new Contacto(nombre,movil);
	                 })
	                 //con el collect lo que hacemos es enviarlos a una lista
	                 .collect(Collectors.toList());
	}
	public List<Usuario> recuperarTodosUsuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}
}