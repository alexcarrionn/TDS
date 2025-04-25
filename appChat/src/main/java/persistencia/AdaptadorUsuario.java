package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.ContactoIndividual;

import modelo.Grupo;
import modelo.Usuario;

/**
 * Adaptador para gestionar la persistencia de los usuarios en la base de datos.
 * Implementa la interfaz {@link IAdaptadorUsuarioDAO} y utiliza el patrón
 * Singleton para asegurar que solo exista una instancia de la clase.
 * 
 * Proporciona métodos para registrar, modificar, recuperar y listar usuarios,
 * así como para gestionar los contactos y grupos asociados a cada usuario.
 */

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	/**
	 * Obtiene la instancia única de {@link AdaptadorUsuario} utilizando el patrón
	 * Singleton.
	 * 
	 * @return La única instancia de {@link AdaptadorUsuario}.
	 */

	public static AdaptadorUsuario getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuario();
		else
			return unicaInstancia;
	}

	/**
	 * Constructor privado que inicializa el servicio de persistencia.
	 */
	private AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/**
	 * Método que sirve para modificar un usuario de la base de datos
	 * 
	 * @param user usuario que se quiere modificar
	 */
	public void modificarUsuario(Usuario user) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getId());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals("telefono")) {
				prop.setValor(String.valueOf(user.getTelefono()));
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(String.valueOf(user.getNombre()));
			} else if (prop.getNombre().equals("imagen")) {
				prop.setValor(String.valueOf(user.getImagen()));
			} else if (prop.getNombre().equals("contraseña")) {
				prop.setValor(user.getContraseña());
			} else if (prop.getNombre().equals("estado")) {
				prop.setValor(user.getEstado());
			} else if (prop.getNombre().equals("premium")) {
				prop.setValor(String.valueOf(user.isPremium()));
			} else if (prop.getNombre().equals("contactos")) {
				prop.setValor(obtenerCodigosContactos(user.getContactos()));
			} else if (prop.getNombre().equals("grupos")) {
				prop.setValor(obtenerCodigosGrupos(user.getContactos()));
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}

	/**
	 * Método para poder registar un usuario en la base de datos
	 * 
	 * @param usuario usuario que queremos registar
	 */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;

		// Si la entidad ya está registrada, no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
		} catch (NullPointerException e) {
		}
		if (eUsuario != null)
			return;

		// Si la imagen del usuario es null o vacía, asignamos una imagen predeterminada
		if (usuario.getImagen() == null || usuario.getImagen().isEmpty()) {
			usuario.setImagen("https://robohash.org/" + usuario.getNombre() + usuario.getTelefono()); // Aquí le
																										// asignamos
																										// el nombre de
																										// la imagen
																										// predeterminada
		}

		// Registrar los contactos y grupos si no existen
		siNoExisteContacto(usuario);

		// Crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("telefono", usuario.getTelefono()),
				new Propiedad("nombre", usuario.getNombre()), new Propiedad("contraseña", usuario.getContraseña()),
				new Propiedad("estado", usuario.getEstado()),
				new Propiedad("premium", String.valueOf(usuario.isPremium())),
				new Propiedad("fecha", usuario.getFecha().toString()), new Propiedad("imagen", usuario.getImagen()), // Usar
																														// la
																														// imagen
																														// que
																														// puede
																														// ser
																														// predeterminada
				new Propiedad("contactos", obtenerCodigosContactos(usuario.getContactos())),
				new Propiedad("grupos", obtenerCodigosGrupos(usuario.obtenerGrupos())))));

		// Registrar entidad Usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// Asignar identificador único al usuario
		usuario.setId(eUsuario.getId());
	}

	/**
	 * Método privado que se asegura de registrar los contactos y grupos de un
	 * usuario si estos no están registrados previamente.
	 * 
	 * @param usuario El usuario cuyo contacto se desea registrar.
	 */
	private void siNoExisteContacto(Usuario usuario) {
		AdaptadorContactoIndividual adaptadorContacto = AdaptadorContactoIndividual.getUnicaInstancia();
		for (Contacto contacto : usuario.getContactos()) {
			if (contacto instanceof ContactoIndividual) {
				adaptadorContacto.registrarContacto((ContactoIndividual) contacto);
			} else if (contacto instanceof Grupo) {
				AdaptadorGrupo.getUnicaInstancia().registrarGrupo((Grupo) contacto);
			}
		}
	}

	/**
	 * Obtiene los códigos de los contactos de un usuario en forma de una cadena de
	 * texto.
	 * 
	 * @param contactos La lista de contactos del usuario.
	 * @return Una cadena de texto con los códigos de los contactos.
	 */
	private String obtenerCodigosContactos(List<Contacto> contactos) {
		return contactos.stream().filter(contacto -> contacto instanceof ContactoIndividual)
				.map(contacto -> String.valueOf(contacto.getId())) // Usamos el teléfono como identificador
				.collect(Collectors.joining(","));
	}

	/**
	 * Obtiene los códigos de los grupos de un usuario en forma de una cadena de
	 * texto.
	 * 
	 * @param grupos La lista de grupos del usuario.
	 * @return Una cadena de texto con los códigos de los grupos.
	 */
	private String obtenerCodigosGrupos(List<Contacto> grupos) {
		return grupos.stream().filter(grupo -> grupo instanceof Grupo) // Para cada grupo, obtenemos sus contactos
				.map(contacto -> String.valueOf(contacto.getId())) // Usamos el teléfono como identificador
				.collect(Collectors.joining(",")); // Unimos los códigos de los contactos en una cadena separada por
													// comas
	}

	/**
	 * Método que sirve para recuperar un usuario de la base de datos a partir de un
	 * identificador
	 * 
	 * @param codigo identificador del usuario que se quiere recuperar
	 * @return Usuario con el identificador especificado
	 */
	public Usuario recuperarUsuario(int codigo) {

		// Si la entidad está en el pool, la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Si no, la recupera de la base de datos
		Entidad eUsuario;
		String nombre;
		String telefono;
		String imagen;
		String contraseña;
		String estado;
		LocalDate fecha;
		Boolean premium;

		// Recuperar entidad
		eUsuario = servPersistencia.recuperarEntidad(codigo);

		// Recuperar propiedades que no son objetos
		nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
		telefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, "telefono");
		imagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, "imagen");
		contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contraseña");
		estado = servPersistencia.recuperarPropiedadEntidad(eUsuario, "estado");
		fecha = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha"));
		premium = Boolean.valueOf(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));

		// Crear el objeto Usuario
		Usuario usuario = new Usuario(telefono, nombre, imagen, contraseña, fecha, estado, null);
		usuario.setPremium(premium);
		usuario.setId(codigo);

		// IMPORTANTE: Añadir el usuario al pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// Recuperar propiedades que son objetos llamando a adaptadores
		// Contactos
		String codigosContactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
		List<ContactoIndividual> contactos = obtenerContactosDesdeCodigos(codigosContactos);
		for (ContactoIndividual c : contactos)
			usuario.addContacto(c);

		// Grupos
		List<Grupo> grupos = obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "grupos"));
		for (Grupo g : grupos)
			usuario.addGrupo(g);

		return usuario;
	}

	/**
	 * Método para poder recuperar todos los Usuarios
	 * 
	 * @return devuelve una lista con todos los usuarios
	 */
	public List<Usuario> recuperarTodosUsuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}

	/**
	 * Método auxiliar que obtiene una lista de contactos a partir de los códigos.
	 * 
	 * @param codigos La cadena con los códigos de los contactos.
	 * @return Una lista de objetos {@link ContactoIndividual} correspondientes a
	 *         los códigos.
	 */
	private List<ContactoIndividual> obtenerContactosDesdeCodigos(String codigos) {
		AdaptadorContactoIndividual adaptadorContactos = AdaptadorContactoIndividual.getUnicaInstancia();

		return Arrays.stream(codigos.split(",")).map(code -> {
			try {
				return adaptadorContactos.recuperarContacto(Integer.valueOf(code));
			} catch (NumberFormatException e) {
				return null;
			}
		}).filter(contacto -> contacto != null).collect(Collectors.toList());
	}

	/**
	 * Método auxiliar que obtiene una lista de grupos a partir de los códigos.
	 * 
	 * @param codigos La cadena con los códigos de los grupos.
	 * @return Una lista de objetos {@link Grupo} correspondientes a los códigos.
	 */
	private List<Grupo> obtenerGruposDesdeCodigos(String codigos) {
		List<Grupo> grupos = new ArrayList<>();
		if (codigos == null || codigos.isEmpty()) {
			return grupos;
		}

		for (String codigo : codigos.split(",")) {
			Grupo grupo = AdaptadorGrupo.getUnicaInstancia().recuperarGrupo(Integer.parseInt(codigo));
			if (grupo != null) {
				grupos.add(grupo);
			}
		}

		return grupos;
	}
}