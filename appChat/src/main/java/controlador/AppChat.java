package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import filtrosStrategy.FiltroCombinado;
import filtrosStrategy.FiltroFechaHora;
import filtrosStrategy.FiltroTexto;
import filtrosStrategy.FiltroTipoMensaje;
import modelo.Contacto;
import modelo.ContactoIndividual;
import descuentoStrategy.Descuento;
import descuentoStrategy.DescuentoPorFecha;
import descuentoStrategy.DescuentoPorMensaje;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.TipoMensaje;
import modelo.Usuario;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorContactoIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Controlador que gestiona la interacción entre los usuarios, contactos, grupos y mensajes.
 * Proporciona métodos para enviar mensajes, aplicar descuentos, activar premium, exportar conversaciones y más.
 */

public class AppChat {
	private static final int NUM_MENSAJES_DESCUENTO = 1;
	private static final LocalDate INICIO_DESCUENTO = LocalDate.of(2024, 12, 24);
	private static final LocalDate FIN_DESCUENTO = LocalDate.of(2025, 1, 6);

	private static AppChat unicaInstancia;

	// Usuario actual
	private Usuario usuarioLogueado;

	// Adapatadores
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorContactoIndividualDAO adaptadorContacto;
	private IAdaptadorGrupoDAO adaptadorGrupo;

	// Chat Seleccionado
	private Contacto chatActual;

	// catalogo de usuarios
	private RepositorioUsuario repo;
	
	/**
	 * Constructor donde inicializaremos los adaptadores y el repositorio
	 */
	private AppChat() {
		inicializarAdaptadores();
		inicializarRepositorio();
	}

	/**
	 * Obtiene la instancia única del Controlador (Singleton).
     * @return La instancia única del Controlador.
	 */
	public static AppChat getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppChat();
		}
		return unicaInstancia;
	}

	/**
	 *  inicializamos Repositorio
	 */
	private void inicializarRepositorio() {
		repo = RepositorioUsuario.getUnicaInstancia();
	}

	/**
	 *  Inicializamos los adaptadores
	 */
	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorMensaje = factoria.getMensajeDAO();
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorContacto = factoria.getContactoIndividualDAO();
		adaptadorGrupo = factoria.getGrupoDAO();
	}
	
	/**
	 * Método para conseguir el UsuarioLogueado
	 * @return Usuario Logueado
	 */
	public Usuario getUsuarioLogueado() {
		return usuarioLogueado;
	}

	/**
	 * Método para conseguir el Precio tras aplicar el Descuento
	 * @return precio
	 */
	public double getDescuento() {
		return usuarioLogueado.getPrecio();
	}

	/**
	 * Metodo para poder comprobar el login
	 * 
	 * @param tel telefono con el que se intenta hacer el login
	 * @param contraseña contraseña con la que se intenta hacer el login
	 * @return true si se ha conseguido hacer el login, false en caso contrario
	 */
	public boolean hacerLogin(String tel, String contraseña) {
		Usuario usuario = repo.getUsuario(tel);
		if (usuario == null || !usuario.getContraseña().equals(contraseña)) {
			return false;
		}
		usuarioLogueado = usuario;
		return true;
	}

	/**
	 * Metodo que sirve para registrar a un nuevo usuario
	 * 
	 * @param telefono   telefono del usuario que se quiere registrar
	 * @param nombre     nombre del usuario que se quiere registrar
	 * @param foto       foto del usuario que se quiere registrar
	 * @param contraseña contraseña del usuario que se quiere registar
	 * @param fecha      fecha del usuario que se quiere registrar
	 * @param estado     estado del usuario que se quiere registrar
	 * @return true si se ha podido registrar al usuario con exito, false en caso
	 *         contrario
	 */
	public boolean registrarUsuario(String telefono, String nombre, String foto, String contraseña, LocalDate fecha,
			String estado) {
		Usuario usuarioExistente = repo.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}

		if (fecha == null) {
			fecha = LocalDate.now();
		}

		Usuario nuevoUsuario = new Usuario(telefono, nombre, foto, contraseña, fecha, estado, null);

		// si no esta registrado, lo añadimos al catalogo de usuarios
		if (!repo.contains(nuevoUsuario)) {
			repo.addUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);

			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña()); // comprueba que se haya
																							// registrado correctamente
		}
		return false;

	}

	/**
	 * Método para obtener los mensajes
	 * 
	 * @param contacto del que se quiere obtener los mensajes
	 * @return Lista de mensajes del contacto
	 */
	public List<Mensaje> getMensajes(Contacto contacto) {
		// Caso 1: Si el contacto es IndividualContact y no es el usuario actual
		if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioLogueado)) {
			ContactoIndividual individual = (ContactoIndividual) contacto;

			// Combinar, ordenar y filtrar los mensajes usando Stream.of() y flatMap()
			return Stream.of(individual.getMensajes(), individual.getMensajesRecibidos(Optional.of(usuarioLogueado)))
					.flatMap(Collection::stream) // Aplanar la lista de listas en un solo Stream<Message>
					.sorted() // Ordenar los mensajes
					.filter(m -> !(m.getTipo().equals(TipoMensaje.ENVIADO) && m.isGrupo())) // Filtrar mensajes grupales
																							// enviados por el usuario
					.collect(Collectors.toList());
		}

		// Caso 2: Si el contacto es un grupo, devolver directamente los mensajes
		// enviados
		return contacto.getMensajes();
	}

	/**
	 * Obtiene los contactos del usuario Actual
	 * 
	 * @return lista del contactos del usuarioLogueado
	 */
	public List<Contacto> getContactosUsuarioActual() {
		return new ArrayList<>(usuarioLogueado.getContactos());
	}

	/**
	 * Retorna el chat en el que se encuentra el usuario
	 * 
	 * @return Contacto con el que esta hablando
	 */
	public Contacto getChatActual() {
		return chatActual;
	}
	
	/**
	 * Cambia el chat Actual
	 * @param contactoActual contacto del chat al que se quiere cambiar
	 */
	public void setChatActual(Contacto contactoActual) {
		chatActual = contactoActual;
	}

	/**
	 * Método que sirve para porder buscar a un usuario a traves de su telefono
	 * 
	 * @param telefono telefono del usuario a encontrar
	 * @return Retorna el usuario si se encontro
	 */
	public Optional<Usuario> buscarUsuario(String telefono) {
		return repo.obtenerUsuarioPorTelefono(telefono);
	}

	/**
	 * Método para agregar los contactos
	 * 
	 * @param nombre   nombre del contacto
	 * @param telefono telefono del contacto
	 * @return retorna el ContactoIndividual creado
	 */
	public ContactoIndividual agregarContacto(String nombre, String telefono) {
		if (usuarioLogueado.contieneContacto(nombre)) {
			return null; // Ya existe el contacto
		}

		// buscamos en el repositorio el usuario
		return buscarUsuario(telefono).map(usuario -> crearYRegistrarContacto(nombre, telefono, usuario)).orElse(null);
	}

	/**
	 *  Funcion que te permite crear y registar un contacto
	 * @param nombre nombre del nuevo contacto
	 * @param telefono telefono del nuevo contacto
	 * @param usuario usuario del nuevo contactp
	 * @return nuevo contacto
	 */
	private ContactoIndividual crearYRegistrarContacto(String nombre, String telefono, Usuario usuario) {
		ContactoIndividual nuevoContacto = usuarioLogueado.crearContactoIndividual(nombre, telefono, usuario);
		usuarioLogueado.addContacto(nuevoContacto);

		adaptadorContacto.registrarContacto(nuevoContacto);
		adaptadorUsuario.modificarUsuario(usuarioLogueado);

		return nuevoContacto;
	}

	/**
	 * Método para poder agregar un Grupo
	 * 
	 * @param groupName    nombre del grupo
	 * @param contactNames lista de los nombres de los contactos que se quieren
	 *                     meter al grupo
	 * @param rutaImagen   imagen del grupo
	 * @return nuevo grupo
	 */
	public Grupo agregarGrupo(String groupName, List<String> contactNames, String rutaImagen) {
		// Si ya existe el grupo
		if (usuarioLogueado.contieneGrupo(groupName)) {
			System.out.println("El grupo ya existe: " + groupName);
			// retorna null
			return null;
		}
		// si no existe coge la lista de contactos pasados para crear el grupo
		List<ContactoIndividual> contactos = contactNames.stream().map(this::getContactoPorNombre)
				.filter(Objects::nonNull) // Filtra los contactos no encontrados
				.collect(Collectors.toList()); // Devuelve una lista inmutable
		// si no hay ninguno retorna null
		if (contactos.isEmpty()) {
			System.out.println("No se encontraron contactos válidos para el grupo: " + groupName);
			return null;
		}
		// Crea el nuevo grupo
		return crearYRegistrarGrupo(groupName, contactos, rutaImagen);
	}

	/**
	 * Método privado para poder crear y registrar el gurpo
	 * @param groupName nombre del grupo
	 * @param contactos lista de contactos del grupo
	 * @param rutaImagen imagen del grupo
	 * @return nuevo grupo 
	 */ 
	
	private Grupo crearYRegistrarGrupo(String groupName, List<ContactoIndividual> contactos, String rutaImagen) {
		// creamos el grupo y lo añadimos a los contactos del usuario
		Grupo nuevoGrupo = usuarioLogueado.crearGrupo(groupName, contactos, rutaImagen);
		usuarioLogueado.addGrupo(nuevoGrupo);
		// registramos el grupo y modificamos el grupo en la bbdd
		adaptadorGrupo.registrarGrupo(nuevoGrupo);
		adaptadorUsuario.modificarUsuario(usuarioLogueado);

		System.out.println("Grupo creado: " + groupName + " con contactos: " + contactos);
		return nuevoGrupo;
	}
	
	/**
	 * Método privado para conseguir un contacto por nombre
	 * @param nombre del contacto a conseguir 
	 * @return contacto con el nombre 
	 */
	
	private ContactoIndividual getContactoPorNombre(String nombre) {
		List<Contacto> contactos = usuarioLogueado.getContactos();
		for (Contacto contacto : contactos) {
			if (contacto instanceof ContactoIndividual && contacto.getNombre().equals(nombre)) {
				return (ContactoIndividual) contacto;
			}
		}
		return null;
	}


	/**
	 * Método para crear un mensaje entre usuarios
	 * @param contacto contacto receptor del mensaje
	 * @param texto texto del mensaje
	 * @param tipo tipo del mensaje
	 */
	private void crearMensajeTextoUsuarioContacto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
		// Cogemos el usuario receptor
		Usuario usuarioReceptor = contacto.getUsuario();
		// Vemos el contacto que se lo envio
		ContactoIndividual contactoInverso = usuarioReceptor.getContactoIndividual(usuarioLogueado.getTelefono());

		// Si el usuario no existe entre los contactos del usuario, creamos uno vacio
		if (contactoInverso == null) {
			contactoInverso = usuarioReceptor.crearContactoIndividual(" ", usuarioLogueado.getTelefono(), usuarioLogueado);
			usuarioReceptor.addContacto(contactoInverso);
			adaptadorContacto.registrarContacto(contactoInverso);
			adaptadorUsuario.modificarUsuario(usuarioReceptor);
		}

		// Creamos el nuevo mensaje como recibido
		Mensaje mensajeRecibido = contactoInverso.creaMensajeTexto(texto, TipoMensaje.RECIBIDO);
		adaptadorMensaje.registrarMensaje(mensajeRecibido);
		adaptadorContacto.modificarContacto(contactoInverso);
	}

	/**
	 * Método para crear un mensaje entre usuarios
	 * @param contacto contacto receptor del mensaje
	 * @param emoticono emoticono del mensaje
	 * @param tipo tipo del mensaje
	 */

	private void crearMensajeEmoticonoUsuarioContacto(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
		// Cogemos el usuario receptor
		Usuario usuarioReceptor = contacto.getUsuario();
		// Vemos el contacto que se lo envio
		ContactoIndividual contactoInverso = usuarioReceptor.getContactoIndividual(usuarioLogueado.getTelefono());

		// Si el usuario no existe entre los contactos del usuario, creamos uno vacio
		if (contactoInverso == null) {
			contactoInverso = usuarioReceptor.crearContactoIndividual(" ", usuarioLogueado.getTelefono(), usuarioLogueado);
			usuarioReceptor.addContacto(contactoInverso);
			adaptadorContacto.registrarContacto(contactoInverso);
			adaptadorUsuario.modificarUsuario(usuarioReceptor);
		}

		// Creamos el nuevo mensaje como recibido
		Mensaje mensajeRecibido = contactoInverso.creaMensajeEmoticono(emoticono, TipoMensaje.RECIBIDO);
		adaptadorMensaje.registrarMensaje(mensajeRecibido);
		adaptadorContacto.modificarContacto(contactoInverso);
	}

	/**
	 *  Metodo para crear un mensaje de texto para el contacto
	 * @param contacto 
	 * @param texto texto del mensaje 
	 * @param tipo tipo del mensaje
	 */
	private void crearMensajeContactoTexto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
		Mensaje mensaje = contacto.creaMensajeTexto(texto, tipo);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);
	}

	/**
	 * Método para crear Mensaje con emoticono para el contacto
	 * @param contacto
	 * @param emoticono emoticono del mensaje
	 * @param tipo tipo del mensaje
	 */
	private void crearMensajeContactoEmoticono(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = contacto.creaMensajeEmoticono(emoticono, tipo);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorContacto.modificarContacto(contacto);
	}

	/**
	 * Método para enviar un mensaje a un contacto individual
	 * 
	 * @param contacto contacto 
	 * @param texto texto del mensaje a enviar 
	 * @param tipo tipo del mensaje 
	 */
	public void enviarMensajeTextoContacto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
		this.crearMensajeContactoTexto(contacto, texto, tipo);
		this.crearMensajeTextoUsuarioContacto(contacto, texto, tipo);
	}

	/**
	 * Metodo para enviar un mendaje de emoticono a un contacto individual
	 * 
	 * @param contacto contacto 
	 * @param emoticono emoticono del mensaje a enviar 
	 * @param tipo tipo del mensaje
	 */
	public void enviarMensajeEmoticonoContacto(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
		this.crearMensajeContactoEmoticono(contacto, emoticono, tipo);
		this.crearMensajeEmoticonoUsuarioContacto(contacto, emoticono, tipo);
	}


	/**
	 * Método para enviar un mensaje de texto a un grupo
	 * 
	 * @param grupo de los contactos a los que se les quiere mandar el mensaje
	 * @param texto texto del mensaje
	 * @param tipo  tipo del mensaje
	 */
	public void enviarMensajeTextoGrupo(Grupo grupo, String texto, TipoMensaje tipo) {
		this.crearMensajeTextoGrupo(grupo, texto, tipo);
		grupo.getContactos().forEach(c -> this.crearMensajeTextoUsuarioContacto(c, texto, tipo));
	}

	/**
	 * Método para enviar un mensaje con emoji a un grupo
	 * 
	 * @param grupo     de los contactos a los que se les quiere mandar el mensaje
	 * @param emoticono emoji del mensaje
	 * @param tipo      tipo del mensaje
	 */
	public void enviarMensajeEmoticonoGrupo(Grupo grupo, int emoticono, TipoMensaje tipo) {
		this.crearMensajeEmojiGrupo(grupo, emoticono, tipo);
		grupo.getContactos().forEach(c -> this.crearMensajeEmoticonoUsuarioContacto(c, emoticono, tipo));
	}

	/**
	 * Método para registar el mensaje de texto
	 * 
	 * @param g grupo
	 * @param texto texto del mensaje
	 * @param tipo tipo del mensaje
	 */
	public void crearMensajeTextoGrupo(Grupo g, String texto, TipoMensaje tipo) {
		Mensaje mensaje = g.creaMensajeTextoGrupo(texto, tipo);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorGrupo.modificarGrupo(g);
	}

	/**
	 * Método para registar el mensaje
	 * 
	 * @param g grupo 
	 * @param emoticono emoticono del mensaje
	 * @param tipo tipo del mensaje
	 */
	public void crearMensajeEmojiGrupo(Grupo g, int emoticono, TipoMensaje tipo) {
		Mensaje mensaje = g.creaMensajeEmoticonoGrupo(emoticono, tipo);
		adaptadorMensaje.registrarMensaje(mensaje);
		adaptadorGrupo.modificarGrupo(g);
	}

	/**
	 * Método que aplica el descuento concreto si se cumple la condicion
	 * 
	 * @param tipoDescuento tipo de descuento que se va a aplicar
	 */

	public void aplicarDescuento(String tipoDescuento) {
		Descuento descuento;

		// Aquí decides manualmente qué implementación usar
		if ("Descuento Fecha".equals(tipoDescuento)) {
			descuento = new DescuentoPorFecha();
			usuarioLogueado.setDescuento(descuento);
		} else if ("Descuento Mensajes".equals(tipoDescuento)) {
			descuento = new DescuentoPorMensaje();
			usuarioLogueado.setDescuento(descuento);
		} else {
			throw new IllegalArgumentException("Tipo de descuento no válido");
		}
	}

	/**
	 * Esta funcion te permite saber si un usuario cumple las condiciones o no de
	 * ser premium
	 * 
	 * @param tipo de descuento seleccionado
	 * @return devuelve true si se ha podido hacer premium y false en caso contrario
	 */
	public boolean hacerPremium(String tipo) {
		// Si cumple la condicion revolverá true y hará al usuario Premium
		if (cumpleCondicion(tipo)) {
			usuarioLogueado.setPremium(true);
			adaptadorUsuario.modificarUsuario(usuarioLogueado);
			return true;
		}
		// Sino, devolverá false
		return false;
	}

	/**
	 * Metodo privado que sirva para saber si el usuario cumple con la condicion
	 * dependiendo del tipo que haya escogido
	 * 
	 * @param tipo tipo de descuento seleccionado
	 */
	private boolean cumpleCondicion(String tipo) {
		// Vemos si se cumple la condicion de que sea Premium según sea Descuento por
		// mensaje o por Fecha
		switch (tipo) {
		case "Descuento Mensajes":
			return usuarioLogueado.getNumMensajes() >= NUM_MENSAJES_DESCUENTO && !usuarioLogueado.isPremium();

		case "Descuento Fecha":
			LocalDate fechaUsuario = usuarioLogueado.getFecha();
			boolean enRangoFechas = !fechaUsuario.isBefore(INICIO_DESCUENTO) && !fechaUsuario.isAfter(FIN_DESCUENTO);
			return enRangoFechas && !usuarioLogueado.isPremium();

		default:
			return false;
		}
	}

	/**
	 * Metodo que sirve para devolver la lista de mensajes entre el usuarioLogueado
	 * y el contactoseleccionado
	 * 
	 * @return devuelve una lista de mensajes
	 */
	public List<Mensaje> obtenerMensajesReMensaje() {
		Contacto contactoActual = getChatActual();
		if (contactoActual == null) {
			return new ArrayList<>(); // Retorna lista vacía si no hay contacto
		}
		return usuarioLogueado.getMensajes(contactoActual);
	}

	/**
	 * Método utilizadp para saber si existe o no un contacto en la lista de
	 * contactos del usuarioLogueado
	 * 
	 * @param numero del usuario que se quiere comprobar
	 * @return devuelve segun exista o no el usuario en la lista de contactos
	 */
	public boolean validarContacto(String numero) {
		return usuarioLogueado.getContactos().stream().filter(ContactoIndividual.class::isInstance) // Filtrar solo
																									// ContactoIndividual
				.map(ContactoIndividual.class::cast) // Hacer cast automáticamente
				.anyMatch(contacto -> contacto.getMovil().equals(numero)); // Comparar número
	}

	/**
	 * Metodo que sirve para desactivar el premium del usuario
	 * 
	 * @return true si se ha podido desactivar el premium y false en caso contrario
	 */
	public boolean desactivarPremium() {
		if (usuarioLogueado.isPremium()) {
			usuarioLogueado.setPremium(false);
			return true;
		}
		return false;
	}

	/**
	 * Método que sirve para actualizar la foto de perfil del usuario
	 * 
	 * @param string Cadena de la foto que quiere actualizar
	 */
	public void actualizarFoto(String string) {
		usuarioLogueado.setImagen(string);
		adaptadorUsuario.modificarUsuario(usuarioLogueado);
	}

	/**
	 * Método que sirve para poder Buscar Mensajes específico despendiendo del
	 * filtro que corresponda
	 * 
	 * @param texto texto del mensaje a buscar 
	 * @param tipo tipo del mensaje a buscar 
	 * @param desde fecha desde la que se quiere buscar 
	 * @param hasta fecha hasta la que se quiere buscar 
	 * @return Lista de Mensajes que se hayan encontrado
	 */
	public List<Mensaje> buscarMensajes(String texto, TipoMensaje tipo, LocalDate desde, LocalDate hasta) {
		// Primero cogemos todos los mensajes que existen
		List<Mensaje> mensajes = getContactosUsuarioActual().stream().flatMap(c -> getMensajes(c).stream())
				.collect(Collectors.toList());

		// Creamos un filtroCombiando
		FiltroCombinado filtro = new FiltroCombinado();

		// Añadimos los filtros en caso de que queramos

		// Primero el del texto
		if (texto != null && !texto.isEmpty())
			filtro.anadirFiltro(new FiltroTexto(texto));

		// Despues haremos el del tipo
		if (tipo != null)
			filtro.anadirFiltro(new FiltroTipoMensaje(tipo));

		// Por último el de la fecha y la hora
		if (desde != null && hasta != null)
			filtro.anadirFiltro(new FiltroFechaHora(desde, hasta));

		return filtro.filtrarMensaje(mensajes);
	}

	/**
	 * Método que sirve para exportar una conversacíon a PDF
	 * 
	 * @throws DocumentException 
	 * @throws FileNotFoundException
	 */

	public boolean exportarPDF() throws FileNotFoundException, DocumentException {
		boolean comprobacion;
		if (usuarioLogueado.isPremium()) {
			crearPDF();
			comprobacion = true;
		} else
			comprobacion = false;
		return comprobacion;
	}

	/**
	 * Método para poder crear el pdf con los usuarios, grupos y los mensajes de
	 * estos
	 * 
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public void crearPDF() throws FileNotFoundException, DocumentException {
		// Crear directorio si no existe
		crearDirectorioSiNoExiste("exportaciones");

		// Configurar documento
		Document documento = new Document();
		PdfWriter.getInstance(documento, new FileOutputStream("exportaciones/UsuariosYgrupos.pdf"));
		documento.open();

		// Añadir título
		agregarTitulo(documento, "Reporte de Usuarios y Grupos");

		// Sección de usuarios
		List<ContactoIndividual> usuarios = usuarioLogueado.recuperarTodosLosContactos();
		generarSeccionUsuarios(documento, usuarios);

		// Sección de mensajes individuales
		generarSeccionMensajesIndividuales(documento, usuarios);

		// Sección de grupos y sus mensajes
		List<Grupo> grupos = usuarioLogueado.recuperarTodosGrupos();
		generarSeccionGrupos(documento, grupos);

		// Cerrar el documento
		documento.close();
	}

	/**
	 *  Método privado para crear el directorio donde se pondrá el archivo en caso de no existir
	 * @param rutaDirectorio ruta del directorio a crear 
	 */

	private void crearDirectorioSiNoExiste(String rutaDirectorio) {
		File directorio = new File(rutaDirectorio);
		if (!directorio.exists()) {
			directorio.mkdirs();
		}
	}

	/**
	 * Metodo privado para agregar el titulo a un parrafo en el documento
	 * @param documento documento a asignar el titulo
	 * @param titulo titulo del documento
	 * @throws DocumentException
	 */
	private void agregarTitulo(Document documento, String titulo) throws DocumentException {
		documento.add(new Paragraph(titulo));
		documento.add(Chunk.NEWLINE);
	}

	/**
	 * Método privado para generar la sección de Usuarios en el documento
	 * @param documento documento a modificar
	 * @param usuarios usuarios que estarán en la sección 
	 * @throws DocumentException
	 */
	private void generarSeccionUsuarios(Document documento, List<ContactoIndividual> usuarios)
			throws DocumentException {
		documento.add(new Paragraph("Usuarios: "));
		documento.add(Chunk.NEWLINE);

		PdfPTable tablaUsuarios = new PdfPTable(2);
		tablaUsuarios.addCell("Nombre");
		tablaUsuarios.addCell("Teléfono");

		for (ContactoIndividual usuario : usuarios) {
			tablaUsuarios.addCell(usuario.getNombre());
			tablaUsuarios.addCell(usuario.getMovil());
		}

		documento.add(tablaUsuarios);
		documento.add(Chunk.NEWLINE);
	}

	/**
	 *  Método privado para generar la sección de los mensajes con lo usuarios en el  documento
	 * @param documento documento a modificar
	 * @param usuarios usuarios de los que se quiere los mensajes 
	 * @throws DocumentException
	 */
	private void generarSeccionMensajesIndividuales(Document documento, List<ContactoIndividual> usuarios)
			throws DocumentException {
		documento.add(new Paragraph("Mensajes con Contactos:"));
		documento.add(Chunk.NEWLINE);

		for (ContactoIndividual usuario : usuarios) {
			documento.add(new Paragraph("Conversación con: " + usuario.getNombre()));
			documento.add(Chunk.NEWLINE);

			List<Mensaje> mensajes = obtenerMensajesConContacto(usuario);
			if (mensajes != null && !mensajes.isEmpty()) {
				agregarTablaMensajes(documento, mensajes);
			} else {
				documento.add(new Paragraph("No hay mensajes con este contacto."));
			}
			documento.add(Chunk.NEWLINE);
		}
	}

	/**
	 *  Método privado para generar la sección de Grupos en el documento
	 * @param documento documento a modificar
	 * @param grupos grupos que se quiere en la sección
	 * @throws DocumentException
	 */
	private void generarSeccionGrupos(Document documento, List<Grupo> grupos) throws DocumentException {
		for (Grupo grupo : grupos) {
			documento.add(new Paragraph("Grupo: " + grupo.getNombre()));
			documento.add(Chunk.NEWLINE);

			// Tabla de miembros del grupo
			PdfPTable tablaGrupo = new PdfPTable(2);
			tablaGrupo.addCell("Nombre del Miembro");
			tablaGrupo.addCell("Teléfono");

			List<ContactoIndividual> contactos = grupo.getContactos();
			for (ContactoIndividual contacto : contactos) {
				tablaGrupo.addCell(contacto.getNombre());
				tablaGrupo.addCell(contacto.getMovil());
			}
			documento.add(tablaGrupo);
			documento.add(Chunk.NEWLINE);

			// Mensajes del grupo
			documento.add(new Paragraph("Mensajes del grupo " + grupo.getNombre() + ":"));
			documento.add(Chunk.NEWLINE);

			List<Mensaje> mensajesGrupo = obtenerMensajesDeGrupo(grupo);
			if (mensajesGrupo != null && !mensajesGrupo.isEmpty()) {
				agregarTablaMensajes(documento, mensajesGrupo);
			} else {
				documento.add(new Paragraph("No hay mensajes en este grupo."));
			}
			documento.add(Chunk.NEWLINE);
		}
	}

	/**
	 *  Método privado para generar la sección de los mensajes con los grupos en el documento
	 * @param documento docuemnto a modificar
	 * @param mensajes mensajes que se quiere en la tabla
	 * @throws DocumentException
	 */

	private void agregarTablaMensajes(Document documento, List<Mensaje> mensajes) throws DocumentException {
		PdfPTable tablaMensajes = new PdfPTable(3);
		tablaMensajes.addCell("Tipo");
		tablaMensajes.addCell("Fecha");
		tablaMensajes.addCell("Contenido");

		for (Mensaje mensaje : mensajes) {
			tablaMensajes.addCell(String.valueOf(mensaje.getTipo()));
			tablaMensajes.addCell(String.valueOf(mensaje.getHora()));
			tablaMensajes.addCell(mensaje.getTexto());
		}

		documento.add(tablaMensajes);
	}

	/**
	 *  Método privado para obtener los Mensajes con el contacto especificado
	 * @param contacto del que se quiere obtener los mensajes
	 * @return
	 */
	private List<Mensaje> obtenerMensajesConContacto(ContactoIndividual contacto) {
		return usuarioLogueado.getMensajes(contacto);
	}

	/**
	 *  Método privado para obtener los mensajes con el grupo
	 * @param grupo grupo del que se quiere obtener los mensajes
	 * @return
	 */
	private List<Mensaje> obtenerMensajesDeGrupo(Grupo grupo) {
		return usuarioLogueado.getMensajes(grupo);
	}

	/**
	 * Método que te permite actualizar el nombre de un contacto sin nombre
	 * @param contacto contacto al que se le va a actualizar el nombre
	 * @param nuevoNombre nombre del contacto
	 */
	public void actualizarNombreContacto(Contacto contacto, String nuevoNombre) {
	    if (contacto instanceof ContactoIndividual) {
	        ((ContactoIndividual) contacto).setNombre(nuevoNombre);
	        // Guardar cambios en la persistencia
	        adaptadorContacto.modificarContacto((ContactoIndividual)contacto);
	    }
	}
}