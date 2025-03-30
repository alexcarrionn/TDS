package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	//Funcion para agregar los contactos
	public ContactoIndividual agregarContacto(String nombre, String telefono) {
	    if (usuarioLogueado.contieneContacto(nombre)) {
	        return null; // Ya existe el contacto
	    }
	    
	    //buscamos en el repositorio el usuario
	    return repo.buscarUsuario(telefono)
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
    
  //ENVIAR MENSAJE
    
    // Método para crear un mensaje entre usuarios
    private void crearMensajeUsuarioContacto(ContactoIndividual contacto, String texto, int emoticono, TipoMensaje tipo) {
        Usuario usuarioReceptor = contacto.getUsuario();
        ContactoIndividual contactoInverso = usuarioReceptor.getContactoIndividual(usuarioLogueado.getTelefono());

        if (contactoInverso == null) {
            contactoInverso = usuarioReceptor.nuevoContacto("", usuarioLogueado);
            adaptadorContacto.registrarContacto(contactoInverso);
            adaptadorUsuario.modificarUsuario(usuarioReceptor);
        }

        Mensaje mensajeRecibido = contactoInverso.nuevoMensaje(texto, emoticono, TipoMensaje.RECIBIDO);
        mensajeDAO.registrarMensaje(mensajeRecibido);
        contactoIndividualDAO.modificarContacto(contactoInverso);
    }
    
    private void crearMensajeContactoTexto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeTexto(texto, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    private void crearMensajeContactoEmoticono(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeEmoticono(emoticono, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    // Método para enviar un mensaje a un contacto individual
    public void enviarMensajeContacto(ContactoIndividual contacto, String texto, int emoticono, TipoMensaje tipo) {
        this.crearMensajeContactoTexto(contacto, texto, emoticono, tipo);
        this.crearMensajeUsuarioContacto(contacto, texto, emoticono, tipo);
    }
    
        
 // Método para obtener el contacto de un usuario dentro de un grupo
    private ContactoIndividual getContactoUsuarioGrupo(ContactoIndividual c) {
        for (Contacto contacto : usuarioLogueado.getContactos()) {
            if (contacto instanceof ContactoIndividual && contacto.getId() == c.getId()) {
                return (ContactoIndividual) contacto;
            }
        }
        return null;
    }

 // Método para crear un contacto anónimo si no existe
    private void crearContactoAnonimo(ContactoIndividual contacto) {
        Optional<Usuario> usuarioOpt = repo.buscarUsuario(contacto.getMovil());
        
        if (usuarioOpt.isPresent()) {
            ContactoIndividual nuevoContacto = usuarioLogueado.crearContactoIndividual(usuarioOpt.get().getNombre(), usuarioOpt.get().getTelefono(), usuarioOpt.get());
            contacto.getUsuario().addContacto(nuevoContacto);
            adaptadorContacto.registrarContacto(nuevoContacto);
            adaptadorUsuario.modificarUsuario(usuarioOpt.get());
        }
    }
    
	//Busca un contacto que este dentro de la lista de los cotnactos del usuario logueado y retorna true si lo encuentra
	private boolean isEnListaContactos(Contacto contacto) {
		return usuarioLogueado.getContactos().stream()
											 .anyMatch(c -> c.getId()==contacto.getId());
	}
    
    
    //esta funcion te aplica el descuento concreto si se cumple la condicion
    public void aplicarDescuento(String tipoDescuento) {
        // Solo aplicamos el descuento si cumple las condiciones
        if (hacerPremium(tipoDescuento)) {
            Descuento nuevoDescuento = DescuentoFactory.crearDescuento(tipoDescuento);
            usuarioLogueado.setDescuento(Optional.of(nuevoDescuento));
        } else {
            // Si no cumple, eliminamos cualquier descuento existente
            usuarioLogueado.setDescuento(Optional.empty());
        }
    }
    
    //Esta funcion te permite saber si un usuario cumple las condiciones o no de ser premium
    public boolean hacerPremium(String tipo) {
    	//Fijamos las dos fechas de inicio  y fin donde se aplicará el descuento
        LocalDate inicioDescuento = LocalDate.of(2024, 12, 24);
        LocalDate finDescuento = LocalDate.of(2025, 1, 6);
        //Vemos si se cumple la condicion de que sea Premium según sea Descuento por mensaje o por Fecha
        boolean cumpleCondicion = switch (tipo) {
            case "Descuento Mensajes" -> usuarioLogueado.getNumMensajes() >= 100 && !usuarioLogueado.isPremium();
            case "Descuento Fecha" -> 
                !usuarioLogueado.getFecha().isBefore(inicioDescuento) && 
                !usuarioLogueado.getFecha().isAfter(finDescuento) && !usuarioLogueado.isPremium();
            default -> false;
        };
        //Si cumple la condicion revolverá true y hará al usuario Premium
        if (cumpleCondicion) {
            usuarioLogueado.setPremium();
            adaptadorUsuario.modificarUsuario(usuarioLogueado);
            return true;
        }
        //Sino, devolverá false
        return false;
    }

    //Funcion para obtener los ids de los mensajes 
    public String obtenerIdsMensajes(List<Mensaje> mensajes) {
        // Usando Java Streams 
        return mensajes.stream()
                      .map(mensaje -> String.valueOf(mensaje.getId()))
                      .collect(Collectors.joining(","));}
    
    //Funcion para obtener los mensajes desde los ids
    public List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {      
        return Arrays.stream(codigos.split(" "))
                     .map(code -> {
                         try {
                             return adaptadorMensaje.recuperarMensaje(Integer.valueOf(code));
                         } catch (NumberFormatException e) {
                             return null;
                         }
                     })
                     .filter(mensaje -> mensaje != null)
                     .collect(Collectors.toList());
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


}