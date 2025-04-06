package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Descuento;
import modelo.DescuentoFactory;
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

public class AppChat {
    private static AppChat unicaInstancia;
    
    //Usuario actual
    private Usuario usuarioLogueado;
    
    //Adapatadores
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private IAdaptadorContactoIndividualDAO adaptadorContacto;
    private IAdaptadorGrupoDAO adaptadorGrupo;
    
    //Chat Seleccionado
    private Contacto chatActual;
    
    //catalogo de usuarios
    private RepositorioUsuario repo; 
    
    private AppChat() {
        inicializarAdaptadores();
        inicializarRepositorio();       
    }
    
    //aplicamos el patrón Singleton
    public static AppChat getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AppChat();
        }
        return unicaInstancia;
    }
    
    //inicializamos Repositorio
    private void inicializarRepositorio() {
        repo = RepositorioUsuario.getUnicaInstancia();
    }

    // Inicializamos los adaptadores
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

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
    
	public double getDescuento() {
    	return usuarioLogueado.getPrecio(); 
    }

    public boolean hacerLogin(String tel, String contraseña) {
        Usuario usuario = repo.getUsuario(tel);
        if (usuario == null || !usuario.getContraseña().equals(contraseña)) {
            return false;
        }
        usuarioLogueado = usuario;
        return true;
    }

    
    public boolean registrarUsuario(String telefono, String nombre,String foto, String contraseña,LocalDate fecha,String estado) {
		Usuario usuarioExistente = repo.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}
		
		if(fecha ==null) {fecha = LocalDate.now();}

		Usuario nuevoUsuario = new Usuario(telefono, nombre, foto, contraseña, fecha, estado, null);

		// si no esta registrado, lo añadimos al catalogo de usuarios
		if (!repo.contains(nuevoUsuario)) {
			repo.addUsuario(nuevoUsuario);
			adaptadorUsuario.registrarUsuario(nuevoUsuario);

			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña()); // comprueba que se haya registrado
																		// correctamente
		}
		return false;

	}

    // Función para obtener los mensajes
    public List<Mensaje> getMensajes(Contacto contacto) {
        // Caso 1: Si el contacto es IndividualContact y no es el usuario actual
        if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioLogueado)) {
        	ContactoIndividual individual = (ContactoIndividual) contacto;

            // Combinar, ordenar y filtrar los mensajes usando Stream.of() y flatMap()
            return Stream.of(
                        individual.getMensajes(),
                        individual.getMensajesRecibidos(Optional.of(usuarioLogueado))
                    )
                    .flatMap(Collection::stream) // Aplanar la lista de listas en un solo Stream<Message>
                    .sorted() // Ordenar los mensajes
                    .filter(m -> !(m.getTipo().equals(TipoMensaje.ENVIADO) && m.isGrupo())) // Filtrar mensajes grupales enviados por el usuario
                    .collect(Collectors.toList());
        }

        // Caso 2: Si el contacto es un grupo, devolver directamente los mensajes enviados
        return contacto.getMensajes();
    }

	public List<Contacto> getContactosUsuarioActual() {
		List<Contacto> contactosActual = usuarioLogueado.getContactos(); 
		return contactosActual;
	}
	
	public Contacto getChatActual() {
		return chatActual;
	}

	public void setChatActual(Contacto contactoActual) {
		chatActual = contactoActual;	
	}

	/*
    public List<Contacto> buscarContactos(String texto, String telefono) {
        // Crear variables locales para las versiones procesadas
        final String textoFiltrado = (texto != null) ? texto.trim().toLowerCase() : "";
        final String telefonoFiltrado = (telefono != null) ? telefono.trim() : "";

		// Usar estas variables en la lambda
        return usuarioLogueado.getContactos().stream()
            .filter(c -> (textoFiltrado.isEmpty() || c.getNombre().toLowerCase().contains(textoFiltrado)) &&
                         (telefonoFiltrado.isEmpty() || String.valueOf(c.getTelefono()).contains(telefonoFiltrado)))
            .collect(Collectors.toList());
    }
*/
	
	public Optional<Usuario> buscarUsuario(String telefono) {
		return repo.obtenerUsuarioPorTelefono(telefono);
	}
	
	//Funcion para agregar los contactos
	public ContactoIndividual agregarContacto(String nombre, String telefono) {
	    if (usuarioLogueado.contieneContacto(nombre)) {
	        return null; // Ya existe el contacto
	    }
	    
	    //buscamos en el repositorio el usuario
	    return buscarUsuario(telefono)
	        .map(usuario -> crearYRegistrarContacto(nombre, telefono, usuario))
	        .orElse(null);
	}

	//Funcion que te permite crear y registar un contacto 
	private ContactoIndividual crearYRegistrarContacto(String nombre, String telefono, Usuario usuario) {
	    ContactoIndividual nuevoContacto = usuarioLogueado.crearContactoIndividual(nombre, telefono, usuario); 
	    usuarioLogueado.addContacto(nuevoContacto);

	    adaptadorContacto.registrarContacto(nuevoContacto);
	    adaptadorUsuario.modificarUsuario(usuarioLogueado);

	    return nuevoContacto;
	}

        
    //Funcion para poder agregar un Grupo
	public Grupo agregarGrupo(String groupName, List<String> contactNames) {
	    //Si ya existe el grupo 
		if (usuarioLogueado.contieneGrupo(groupName)) {
	        System.out.println("El grupo ya existe: " + groupName);
	        //retorna null
	        return null;
	    }
		//si no existe coge la lista de contactos pasados para crear el grupo
	    List<ContactoIndividual> contactos = contactNames.stream()
	        .map(this::getContactoPorNombre)
	        .filter(Objects::nonNull) // Filtra los contactos no encontrados
	        .toList(); // Devuelve una lista inmutable
	    //si no hay ninguno retorna null
	    if (contactos.isEmpty()) {
	        System.out.println("No se encontraron contactos válidos para el grupo: " + groupName);
	        return null;
	    }
	    //Crea el nuevo grupo
	    return crearYRegistrarGrupo(groupName, contactos);
	}
	
	//Funcion para poder crear y registrar el gurpo 
	private Grupo crearYRegistrarGrupo(String groupName, List<ContactoIndividual> contactos) {
	    //creamos el grupo y lo añadimos a los contactos del usuario
		Grupo nuevoGrupo = usuarioLogueado.crearGrupo(groupName, contactos);
	    usuarioLogueado.addGrupo(nuevoGrupo);
	    //registramos el grupo y modificamos el grupo en la bbdd
	    adaptadorGrupo.registrarGrupo(nuevoGrupo);
	    adaptadorUsuario.modificarUsuario(usuarioLogueado);

	    System.out.println("Grupo creado: " + groupName + " con contactos: " + contactos);
	    return nuevoGrupo;
	}

    
    
    private ContactoIndividual getContactoPorNombre(String nombre) {
        List<Contacto> contactos = usuarioLogueado.getContactos();
        for (Contacto contacto : contactos) {
            if (contacto instanceof ContactoIndividual && contacto.getNombre().equals(nombre)) {
                return (ContactoIndividual) contacto;
            }
        }
        return null;
    }
    
    //Eliminar un contacto
    public void eliminarContacto(Contacto contacto) {
        usuarioLogueado.removeContacto(contacto);
    }
    
    //Función para obtener todos los contactos
    public List<Contacto> obtenerTodosContactos() {
    	if (usuarioLogueado == null)
            return new LinkedList<Contacto>();
        
        return usuarioLogueado.getContactos();
    }
    
  //ENVIAR MENSAJE DE TEXTO
    
    // Método para crear un mensaje entre usuarios
	@SuppressWarnings("null")
	private void crearMensajeTextoUsuarioContacto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
		//Cogemos el usuario receptor
		Usuario usuarioReceptor = contacto.getUsuario();
        //Vemos el contacto que se lo envio
		ContactoIndividual contactoInverso = usuarioReceptor.getContactoIndividual(usuarioLogueado.getTelefono());
		
		//Si el usuario no existe entre los contactos del usuario, creamos uno vacio
        if (contactoInverso == null) {
            contactoInverso = usuarioReceptor.crearContactoIndividual("", contacto.getMovil(),usuarioLogueado);
            adaptadorContacto.registrarContacto(contactoInverso);
            adaptadorUsuario.modificarUsuario(usuarioReceptor);
        }
        
        //Creamos el nuevo mensaje como recibido
        Mensaje mensajeRecibido = contactoInverso.creaMensajeTexto(texto,TipoMensaje.RECIBIDO);
        adaptadorMensaje.registrarMensaje(mensajeRecibido);
        adaptadorContacto.modificarContacto(contactoInverso);
    }
	
    // Método para crear un mensaje entre usuarios
	@SuppressWarnings("null")
	private void crearMensajeEmoticonoUsuarioContacto(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
		//Cogemos el usuario receptor
		Usuario usuarioReceptor = contacto.getUsuario();
        //Vemos el contacto que se lo envio
		ContactoIndividual contactoInverso = usuarioReceptor.getContactoIndividual(usuarioLogueado.getTelefono());
		
		//Si el usuario no existe entre los contactos del usuario, creamos uno vacio
        if (contactoInverso == null) {
            contactoInverso = usuarioReceptor.crearContactoIndividual("", contactoInverso.getMovil(),usuarioLogueado);
            adaptadorContacto.registrarContacto(contactoInverso);
            adaptadorUsuario.modificarUsuario(usuarioReceptor);
        }
        
        //Creamos el nuevo mensaje como recibido
        Mensaje mensajeRecibido = contactoInverso.creaMensajeEmoticono(emoticono,TipoMensaje.RECIBIDO);
        adaptadorMensaje.registrarMensaje(mensajeRecibido);
        adaptadorContacto.modificarContacto(contactoInverso);
    }
    
	//Metodo para crear un mensaje de texto para el contacto
    private void crearMensajeContactoTexto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeTexto(texto, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    //Metedo para crear Mensaje con emoticono para el contacto
    private void crearMensajeContactoEmoticono(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeEmoticono(emoticono, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    // Método para enviar un mensaje a un contacto individual
    public void enviarMensajeTextoContacto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
        this.crearMensajeContactoTexto(contacto, texto, tipo);
        this.crearMensajeTextoUsuarioContacto(contacto, texto, tipo);
    }
    
    //Metodo para enviar un mendaje de emoticono a un contacto individual
    public void enviarMensajeEmoticonoContacto(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
        this.crearMensajeContactoEmoticono(contacto, emoticono, tipo);
        this.crearMensajeEmoticonoUsuarioContacto(contacto, emoticono, tipo);
    }
  
    
    //ENVIAR MENSAJE DE EMOJI A UN GRUPO 
    
    // Método para enviar un mensaje de texto a un grupo
    public void enviarMensajeTextoGrupo(Grupo grupo, String texto, TipoMensaje tipo) {
        this.crearMensajeTextoGrupo(grupo, texto, tipo);
        grupo.getContactos().forEach(c -> this.crearMensajeTextoUsuarioContacto(c, texto, tipo));
    }
    
    // Método para enviar un mensaje con emoji a un grupo
    public void enviarMensajeEmoticonoGrupo(Grupo grupo, int emoticono, TipoMensaje tipo) {
        this.crearMensajeEmojiGrupo(grupo, emoticono, tipo);
        grupo.getContactos().forEach(c -> this.crearMensajeEmoticonoUsuarioContacto(c, emoticono, tipo));
    }
    
    public void crearMensajeTextoGrupo(Grupo g, String texto, TipoMensaje tipo) {
    	 Mensaje mensaje = g.creaMensajeTexto(texto, tipo);
         adaptadorMensaje.registrarMensaje(mensaje);
         adaptadorGrupo.modificarGrupo(g);
    }
    
    public void crearMensajeEmojiGrupo(Grupo g, int emoticono, TipoMensaje tipo) {
    	Mensaje mensaje = g.creaMensajeEmoticono(emoticono, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorGrupo.modificarGrupo(g);
    }
    
    //esta funcion te aplica el descuento concreto si se cumple la condicion
    public void aplicarDescuento(String tipoDescuento) {
            Descuento nuevoDescuento = DescuentoFactory.crearDescuento(tipoDescuento);
            usuarioLogueado.setDescuento(Optional.of(nuevoDescuento));
    }
    
    //Esta funcion te permite saber si un usuario cumple las condiciones o no de ser premium
    public boolean hacerPremium(String tipo) {     
        //Si cumple la condicion revolverá true y hará al usuario Premium
        if (cumpleCondicion(tipo)) {
            usuarioLogueado.setPremium(true);
            adaptadorUsuario.modificarUsuario(usuarioLogueado);
            return true;
        }
        //Sino, devolverá false
        return false;
    }
    
    private boolean cumpleCondicion(String tipo) {
    	//Fijamos las dos fechas de inicio  y fin donde se aplicará el descuento
    	LocalDate inicioDescuento = LocalDate.of(2024, 12, 24);
        LocalDate finDescuento = LocalDate.of(2025, 1, 6);
        //Vemos si se cumple la condicion de que sea Premium según sea Descuento por mensaje o por Fecha
        switch (tipo) {
        case "Descuento Mensajes":
            return usuarioLogueado.getNumMensajes() >= 100 && !usuarioLogueado.isPremium();

        case "Descuento Fecha":
            LocalDate fechaUsuario = usuarioLogueado.getFecha();
            boolean enRangoFechas = !fechaUsuario.isBefore(inicioDescuento) && !fechaUsuario.isAfter(finDescuento);
            return enRangoFechas && !usuarioLogueado.isPremium();

        default:
            return false;
        }
	}

	//Sirve para devolver la lista de mensajes entre el usuarioLogueado y el contactoseleccionado
    public List<Mensaje> obtenerMensajesReMensaje() {
        Contacto contactoActual = getChatActual();
        if (contactoActual == null) {
            return new ArrayList<>(); // Retorna lista vacía si no hay contacto
        }
        return usuarioLogueado.getMensajes(contactoActual);
    }
    

    //Funcion utilizada para saber si existe o no un contacto en la lista de contactos del usuarioLogueado
    public boolean validarContacto(String numero) {
        return usuarioLogueado.getContactos().stream()
            .filter(ContactoIndividual.class::isInstance) // Filtrar solo ContactoIndividual
            .map(ContactoIndividual.class::cast)         // Hacer cast automáticamente
            .anyMatch(contacto -> contacto.getMovil().equals(numero)); // Comparar número
    }
    
    public boolean desactivarPremium() {
		if(usuarioLogueado.isPremium()) {
			usuarioLogueado.setPremium(false);
			return true;
		}
		return false;
	}
}