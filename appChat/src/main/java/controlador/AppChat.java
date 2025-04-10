package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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
	
	/**
	 * Metodo para poder comprobar el login
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
     * @param telefono telefono del usuario que se quiere registrar
     * @param nombre nombre del usuario que se quiere registrar
     * @param foto foto del usuario que se quiere registrar
     * @param contraseña contraseña del usuario que se quiere registar
     * @param fecha fecha del usuario que se quiere registrar
     * @param estado estado del usuario que se quiere registrar
     * @return true si se ha podido registrar al usuario con exito, false en caso contrario
     */
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

			return hacerLogin(nuevoUsuario.getTelefono(), nuevoUsuario.getContraseña()); // comprueba que se haya registrado correctamente
		}
		return false;

	}

    
    /**
     * Método para obtener los mensajes
     * @param contacto del que se quiere obtener los mensajes
     * @return Lista de mensajes del contacto
     */
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

    
    /**
     * Obtiene los contactos del usuario Actual
     * @return lista del contactos del usuarioLogueado
     */
	public List<Contacto> getContactosUsuarioActual() {
		List<Contacto> contactosActual = usuarioLogueado.getContactos(); 
		return contactosActual;
	}
	
	/**
	 * Retorna el chat en el que se encuentra el usuario
	 * @return Contacto con el que esta hablando
	 */
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
	/**
	 * Método que sirve para porder buscar a un usuario a traves de su telefono
	 * @param telefono telefono del usuario a encontrar
	 * @return Retorna el usuario si se encontro
	 */
	public Optional<Usuario> buscarUsuario(String telefono) {
		return repo.obtenerUsuarioPorTelefono(telefono);
	}
	
	/**
	 * Método para agregar los contactos
	 * @param nombre nombre del contacto
	 * @param telefono telefono del contacto
	 * @return retorna el ContactoIndividual creado
	 */
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

        
    /**
     * Método para poder agregar un Grupo
     * @param groupName nombre del grupo
     * @param contactNames lista de los nombres de los contactos que se quieren meter al grupo
     * @param rutaImagen imagen del grupo
     * @return nuevo grupo
     */
	public Grupo agregarGrupo(String groupName, List<String> contactNames, String rutaImagen) {
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
	    return crearYRegistrarGrupo(groupName, contactos, rutaImagen);
	}
	
	//Funcion para poder crear y registrar el gurpo 
	private Grupo crearYRegistrarGrupo(String groupName, List<ContactoIndividual> contactos, String rutaImagen) {
	    //creamos el grupo y lo añadimos a los contactos del usuario
		Grupo nuevoGrupo = usuarioLogueado.crearGrupo(groupName, contactos, rutaImagen);
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
    
    /*
    //Eliminar un contacto
    public void eliminarContacto(Contacto contacto) {
        usuarioLogueado.removeContacto(contacto);
    }*/
    
  //ENVIAR MENSAJE DE TEXTO
    
    /**
     *  Método para crear un mensaje entre usuarios
     * @param contacto contacto al que se le quiere enviar el mensaje
     * @param texto mensaje que se le quiere pasar al contacto
     * @param tipo tipo del mensaje
     */
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
	
    /**
     *  Método para crear un mensaje entre usuarios
     * @param contacto contacto al que se le quiere enviar el mensaje
     * @param emoticono emoji que se le quiere pasar al contacto
     * @param tipo tipo del mensaje
     */
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
    
	/**
	 * Metodo para crear un mensaje de texto para el contacto
	 * @param contacto
	 * @param texto
	 * @param tipo
	 */
    private void crearMensajeContactoTexto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeTexto(texto, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    /**
     * Metedo para crear Mensaje con emoticono para el contacto
     * @param contacto
     * @param emoticono
     * @param tipo
     */
    private void crearMensajeContactoEmoticono(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
        Mensaje mensaje = contacto.creaMensajeEmoticono(emoticono, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorContacto.modificarContacto(contacto);
    }
    
    /**
     *  Método para enviar un mensaje a un contacto individual
     * @param contacto
     * @param texto
     * @param tipo
     */
    public void enviarMensajeTextoContacto(ContactoIndividual contacto, String texto, TipoMensaje tipo) {
        this.crearMensajeContactoTexto(contacto, texto, tipo);
        this.crearMensajeTextoUsuarioContacto(contacto, texto, tipo);
    }
    
    /**
     * Metodo para enviar un mendaje de emoticono a un contacto individual
     * @param contacto
     * @param emoticono
     * @param tipo
     */
    public void enviarMensajeEmoticonoContacto(ContactoIndividual contacto, int emoticono, TipoMensaje tipo) {
        this.crearMensajeContactoEmoticono(contacto, emoticono, tipo);
        this.crearMensajeEmoticonoUsuarioContacto(contacto, emoticono, tipo);
    }
  
    
    //ENVIAR MENSAJE DE EMOJI A UN GRUPO 
    
    /**
     *  Método para enviar un mensaje de texto a un grupo
     * @param grupo de los contactos a los que se les quiere mandar el mensaje
     * @param texto texto del mensaje
     * @param tipo tipo del mensaje
     */
    public void enviarMensajeTextoGrupo(Grupo grupo, String texto, TipoMensaje tipo) {
        this.crearMensajeTextoGrupo(grupo, texto, tipo);
        grupo.getContactos().forEach(c -> this.crearMensajeTextoUsuarioContacto(c, texto, tipo));
    }
    
    /**
     *  Método para enviar un mensaje con emoji a un grupo
     * @param grupo de los contactos a los que se les quiere mandar el mensaje 
     * @param emoticono emoji del mensaje
     * @param tipo tipo del mensaje 
     */
    public void enviarMensajeEmoticonoGrupo(Grupo grupo, int emoticono, TipoMensaje tipo) {
        this.crearMensajeEmojiGrupo(grupo, emoticono, tipo);
        grupo.getContactos().forEach(c -> this.crearMensajeEmoticonoUsuarioContacto(c, emoticono, tipo));
    }
    
    /**
     * Método para registar el mensaje de texto
     * @param g
     * @param texto
     * @param tipo
     */
    public void crearMensajeTextoGrupo(Grupo g, String texto, TipoMensaje tipo) {
    	 Mensaje mensaje = g.creaMensajeTexto(texto, tipo);
         adaptadorMensaje.registrarMensaje(mensaje);
         adaptadorGrupo.modificarGrupo(g);
    }
    
    /**
     * Método para registar el mensaje
     * @param g
     * @param emoticono
     * @param tipo
     */
    public void crearMensajeEmojiGrupo(Grupo g, int emoticono, TipoMensaje tipo) {
    	Mensaje mensaje = g.creaMensajeEmoticono(emoticono, tipo);
        adaptadorMensaje.registrarMensaje(mensaje);
        adaptadorGrupo.modificarGrupo(g);
    }
    
    /**
     * Método que aplica el descuento concreto si se cumple la condicion
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
     * Esta funcion te permite saber si un usuario cumple las condiciones o no de ser premium
     * @param tipo de descuento seleccionado
     * @return devuelve true si se ha podido hacer premium y false en caso contrario
     */
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
    
    /**
     * Metodo privado que sirva para saber si el usuario cumple con la condicion dependiendo del tipo que haya escogido
     * @param tipo
     * @return devuelve true en caso de que cumpla la condicion y false en caso contrario
     */
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

	/**
	 * Metodo que sirve para devolver la lista de mensajes entre el usuarioLogueado y el contactoseleccionado
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
     * Método utilizadp para saber si existe o no un contacto en la lista de contactos del usuarioLogueado
     * @param numero del usuario que se quiere comprobar
     * @return devuelve segun exista o no el usuario en la lista de contactos
     */
    public boolean validarContacto(String numero) {
        return usuarioLogueado.getContactos().stream()
            .filter(ContactoIndividual.class::isInstance) // Filtrar solo ContactoIndividual
            .map(ContactoIndividual.class::cast)         // Hacer cast automáticamente
            .anyMatch(contacto -> contacto.getMovil().equals(numero)); // Comparar número
    }
    
    /**
     * Metodo que sirve para desactivar el premium del usuario 
     * @return true si se ha podido desactivar el premium y false en caso contrario
     */
    public boolean desactivarPremium() {
		if(usuarioLogueado.isPremium()) {
			usuarioLogueado.setPremium(false);
			return true;
		}
		return false;
	}
    
    /**
     * Método que sirve para actualizar la foto de perfil del usuario
     * @param string Cadena de la foto que quiere actualizar 
     */
	public void actualizarFoto(String string) {
		usuarioLogueado.setImagen(string);
		adaptadorUsuario.modificarUsuario(usuarioLogueado);
	}
	
	/**
	 * Método que sirve para poder Buscar Mensajes específico despendiendo del filtro que corresponda
	 * @param texto
	 * @param tipo
	 * @param desde
	 * @param hasta
	 * @return Lista de Mensajes que se hayan encontrado
	 */
	public List<Mensaje> buscarMensajes(String texto, TipoMensaje tipo, LocalDateTime desde, LocalDateTime hasta) {
		//Primero cogemos todos los mensajes que existen
		List<Mensaje> mensajes = getContactosUsuarioActual().stream()
								.flatMap(c -> getMensajes(c).stream())
								.collect(Collectors.toList());
		
		//Creamos un filtroCombiando
		FiltroCombinado filtro = new FiltroCombinado(); 
		
		//Añadimos los filtros en caso de que queramos 
		
		//Primero el del texto
		if(texto != null && !texto.isEmpty())
			filtro.anadirFiltro(new FiltroTexto(texto));
		
		//Despues haremos el del tipo
		if(tipo != null)
			filtro.anadirFiltro(new FiltroTipoMensaje(tipo));
		
		//Por último el de la fecha y la hora 
		if (desde != null && hasta != null)
			filtro.anadirFiltro(new FiltroFechaHora(desde, hasta));
		
		return filtro.filtrarMensaje(mensajes);
	}
	
	/**
	 * Método que sirve para exportar una conversacíon a PDF 
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
	 */
	
	public boolean exportarPDF() throws FileNotFoundException, DocumentException {
		boolean comprobacion;
		if(usuarioLogueado.isPremium()) {
			crearPDF();
			comprobacion=true;
		}else 
			comprobacion= false;
		return comprobacion;
	}
	
	
	public void crearPDF()throws FileNotFoundException, DocumentException {
			FileOutputStream archivo = new FileOutputStream("");
			Document documento = new Document();
			PdfWriter.getInstance(documento, archivo);
			documento.open();
			documento.add(new Paragraph("Reporte de Usuarios y Grupos"));
			documento.add(Chunk.NEWLINE);
			
			//Listamos a los usuarios
			documento.add(new Paragraph("Usuarios: ")); 
			 PdfPTable tablaUsuarios = new PdfPTable(2);
		     tablaUsuarios.addCell("Nombre");
		     tablaUsuarios.addCell("Teléfono");
		     
		     List<Usuario> usuarios = repo.getUsuarios();
		     for (Usuario u: usuarios) {
		    	 tablaUsuarios.addCell(u.getNombre());
		    	 tablaUsuarios.addCell(u.getTelefono());
		     }
		     documento.add(tablaUsuarios);
		     documento.add(Chunk.NEWLINE);
		     
		     List<Grupo> grupos = usuarioLogueado.recuperarTodosGrupos();
		     for (Grupo g: grupos) {
		    	 documento.add(new Paragraph("Grupo: " + g.getNombre()));
		            PdfPTable tablaGrupo = new PdfPTable(2);
		            tablaGrupo.addCell("Nombre del Miembro");
		            tablaGrupo.addCell("Teléfono");
		            
		            List<ContactoIndividual> contactos = g.getContactos();
		            for (ContactoIndividual ci: contactos) {
		            	tablaGrupo.addCell(ci.getNombre());
		            	tablaGrupo.addCell(ci.getMovil());
		            }
		            documento.add(tablaGrupo);
		            documento.add(Chunk.NEWLINE);
		            
		     }
		  // Cerrar el documento
			documento.close();
			
	}

}