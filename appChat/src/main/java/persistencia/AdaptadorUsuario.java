package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import appChat.Ventanas.VentanaPrincipal;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Descuento;
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
		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getId());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario user) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getId());

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
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;

        // Si la entidad esta registrada no la registra de nuevo
        try {
        	eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
        } catch (NullPointerException e) {}
        if (eUsuario != null) return;

		// registrar contactos del usuario
		registrarSiNoExistenContactosoGrupos(usuario.getContactos());

        // crear entidad Cliente
        eUsuario = new Entidad();
        eUsuario.setNombre("cliente");
        eUsuario.setPropiedades(new ArrayList<Propiedad>(
                Arrays.asList(
                           new Propiedad("telefono", usuario.getTelefono()),
                           new Propiedad("nombre", usuario.getNombre()),
                           new Propiedad("imagen", usuario.getImagen()),
                           new Propiedad("contraseña", usuario.getContraseña()),
                           new Propiedad("fecha", usuario.getFecha() != null ? usuario.getFecha().toString() : "null"),
                           new Propiedad("estado", usuario.getEstado()),
                           new Propiedad("premium", String.valueOf(usuario.isPremium()))
                   )));

        // registrar entidad cliente
        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        // asignar identificador unico
        // Se aprovecha el que genera el servicio de persistencia
        usuario.setId(eUsuario.getId());
		// Guardamos en el pool
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getId(), usuario);
	}

	public Usuario recuperarUsuario(int codigo) {
		// Si la entidad está en el pool, se devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Recuperar entidad del sistema de persistencia
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

		// Recuperar propiedades
		String name = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		LocalDate fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha"));
		String tlf = servPersistencia.recuperarPropiedadEntidad(eUsuario, "numero");
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		Boolean premium = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eUsuario, "isPremium"));
		LocalDate fechaRegistro = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro"));
		String path = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fotoPerfil");

		// Crear el objeto usuario
		String foto;
		if (path.startsWith("..")) {
			foto = VentanaPrincipal.class.getResource(path).toString();
		} else if (path.startsWith("file")) {
			foto = path.replaceFirst("file:", "");
		} else {
			foto = path;
		}
		Usuario usuario = new Usuario(tlf,name,foto,password,fecha,saludo,null);

		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// Recuperar contactos y grupos asociados
		List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos"));
		contactos.forEach(usuario::addContacto);

		List<Grupo> grupos = obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "grupos"));
		grupos.forEach(usuario::addGrupo);

		return usuario;
	}


	// Método auxiliar para obtener contactos a partir de códigos
	private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
	    List<ContactoIndividual> contactos = new ArrayList<>();
	    if (codigos == null || codigos.isEmpty()) {
	        return contactos;
	    }

	    for (String codigo : codigos.split(",")) {
	        contactos.add((ContactoIndividual) recuperarContacto(Integer.parseInt(codigo)));
	    }

	    return contactos;
	}
	
	private List<Grupo> obtenerGruposDesdeCodigos(String codigos) {
	    List<Grupo> grupos = new ArrayList<>();
	    if (codigos == null || codigos.isEmpty()) {
	        return grupos;
	    }

	    for (String codigo : codigos.split(",")) {
	        Grupo grupo = (Grupo) recuperarGrupo(Integer.parseInt(codigo));
	        if (grupo != null) {
	            grupos.add(grupo);
	        }
	    }

	    return grupos;
	}


	// Método para recuperar un contacto (implementación depende de Contacto)
	private Contacto recuperarContacto(int codigo) {
	    // Implementación específica para recuperar Contacto
	    return (Contacto) PoolDAO.getUnicaInstancia().getObjeto(codigo);
	}
	
	private Grupo recuperarGrupo(int codigo) {
	    // Intentar obtener el grupo del pool
	    Grupo grupo = (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
	    if (grupo != null) {
	        return grupo; // Si el grupo ya está en el pool, se devuelve directamente
	    }

	    // Recuperar la entidad desde la persistencia
	    Entidad entidadGrupo = servPersistencia.recuperarEntidad(codigo);
	    if (entidadGrupo == null) {
	        return null; // Si no se encuentra la entidad, devolver null
	    }

	    // Recuperar propiedades del grupo
	    String nombre = servPersistencia.recuperarPropiedadEntidad(entidadGrupo, "nombre");
	    String contactosStr = servPersistencia.recuperarPropiedadEntidad(entidadGrupo, "contactos");

	    // Obtener la lista de contactos del grupo
	    List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(contactosStr);

	    // Crear el grupo con los datos recuperados
	    grupo = new Grupo(nombre, contactos);

	    // Añadir el grupo al pool para futuras recuperaciones
	    PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);

	    return grupo;
	}


	// Método para recuperar un descuento (implementación depende de Descuento)
	private Descuento recuperarDescuento(int codigo) {
	    // Implementación específica para recuperar Descuento
	    return (Descuento) PoolDAO.getUnicaInstancia().getObjeto(codigo);
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
	                     String movil = servPersistencia.recuperarPropiedadEntidad(entidad, "telefono"); 
	                     return new ContactoIndividual(nombre,movil);
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