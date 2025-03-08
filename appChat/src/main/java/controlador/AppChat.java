package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Descuento;
import modelo.DescuentoFactory;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.Usuario;
import persistencia.AdaptadorContactoIndividual;
import persistencia.AdaptadorGrupo;
import persistencia.AdaptadorUsuario;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public class AppChat {
    private static AppChat unicaInstancia;
    
    //Usuario actual
    private Usuario usuarioLogueado;
    
    //Adapatadores
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    
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
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean hacerLogin(String tel, String contraseña) {
        Usuario usuario = repo.getUsuario(tel);
        if (usuario == null) {
            return false;
        }
        if (!usuario.getContraseña().equals(contraseña)) {
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
		
		if(fecha ==null) {
		fecha = LocalDate.now();}

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
    	//TODO
        return null; // Devuelve una copia de la lista de mensajes
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


    public List<Contacto> buscarContactos(String texto, String telefono) {
        // Crear variables locales para las versiones procesadas
        final String textoFiltrado = (texto != null) ? texto.trim().toLowerCase() : "";
        final String telefonoFiltrado = (telefono != null) ? telefono.trim() : "";

        List<Contacto> listaContactos = usuarioLogueado.getContactos();
		// Usar estas variables en la lambda
        return listaContactos.stream()
            .filter(c -> (textoFiltrado.isEmpty() || c.getNombre().toLowerCase().contains(textoFiltrado)) &&
                         (telefonoFiltrado.isEmpty() || String.valueOf(c.getTelefono()).contains(telefonoFiltrado)))
            .collect(Collectors.toList());
    }

    public ContactoIndividual agregarContacto(String nombre, String telefono) {
        // Si no tiene el contacto guardado lo guarda
        if (!usuarioLogueado.contieneContacto(nombre)) {
            Optional<Usuario> usuarioOpt = repo.buscarUsuario(telefono);

            if (usuarioOpt.isPresent()) {
                ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, telefono, usuarioOpt.get());
                usuarioLogueado.addContacto(nuevoContacto);

                AdaptadorContactoIndividual.getUnicaInstancia().registrarContacto(nuevoContacto);

                adaptadorUsuario.modificarUsuario(usuarioLogueado);
                return nuevoContacto;
            }
        }
        return null;
    }
    
    public Grupo agregarGrupo(String groupName, List<String> contactNames) {
        // Si no tiene el grupo guardado lo guarda
        if (!usuarioLogueado.contieneGrupo(groupName)) {
            // Crear una lista de ContactoIndividual a partir de los nombres proporcionados
            List<ContactoIndividual> contactos = new ArrayList<>();
            for (String nombre : contactNames) {
                ContactoIndividual contacto = getContactoPorNombre(nombre);
                if (contacto != null) {
                    contactos.add(contacto);
                } else {
                    System.out.println("Contacto no encontrado: " + nombre);
                }
            }

            // Crear un nuevo grupo con los contactos proporcionados
            Grupo nuevoGrupo = new Grupo(groupName, contactos);
            
            // Agregar el nuevo grupo al usuario logueado
            usuarioLogueado.addGrupo(nuevoGrupo);

            // Registrar el grupo en el adaptador (si es necesario)
            AdaptadorGrupo.getUnicaInstancia().registrarGrupo(nuevoGrupo);

            // Modificar el usuario en el repositorio
            adaptadorUsuario.modificarUsuario(usuarioLogueado);
            
            System.out.println("Grupo creado: " + groupName + " con contactos: " + contactos);
            return nuevoGrupo;
        }
        System.out.println("El grupo ya existe: " + groupName);
        return null; // Retorna null si el grupo ya existe
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

    public void eliminarContacto(Contacto contacto) {
        usuarioLogueado.removeContacto(contacto);
    }

    public List<Contacto> obtenerTodosContactos() {
    	if (usuarioLogueado == null)
            return new LinkedList<Contacto>();
        
        return usuarioLogueado.getContactos();
    }
    
      
    public void enviarMensaje(Contacto contacto, String mensajeEnviar) {
		Mensaje mensaje = new Mensaje(mensajeEnviar, usuarioLogueado, contacto,LocalDate.now());
		contacto.addMensaje(mensaje);

		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof ContactoIndividual) {
			AdaptadorContactoIndividual.getUnicaInstancia().modificarContacto((ContactoIndividual) contacto);
		} else {
			AdaptadorGrupo.modificarGrupo((Grupo) contacto);
		}
	}


    public void enviarMensaje(Contacto contacto, int emoji) {
		Mensaje mensaje = new Mensaje(emoji, usuarioLogueado, contacto, LocalDate.now());
		contacto.addMensaje(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof ContactoIndividual) {
			AdaptadorContactoIndividual.getUnicaInstancia().modificarContacto((ContactoIndividual) contacto);
		} else {
			AdaptadorGrupo.modificarGrupo((Grupo) contacto);
		}
	}
    
    public double getDescuento() {
    	return usuarioLogueado.getPrecio(); 
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
        LocalDate inicio = LocalDate.of(2024, 12, 24);
        LocalDate fin = LocalDate.of(2025, 1, 6);
        
        if ("Descuento Mensajes".equals(tipo)) {
            if(usuarioLogueado.getNumMensajes() >= 100) {
                usuarioLogueado.setPremium();
                AdaptadorUsuario.getUnicaInstancia().modificarUsuario(usuarioLogueado);
                return true;
            }
        } else if("Descuento Fecha".equals(tipo)) {
            if((usuarioLogueado.getFecha().isAfter(inicio) || usuarioLogueado.getFecha().equals(inicio)) 
               && (usuarioLogueado.getFecha().isBefore(fin) || usuarioLogueado.getFecha().equals(fin))) {
                usuarioLogueado.setPremium();
                AdaptadorUsuario.getUnicaInstancia().modificarUsuario(usuarioLogueado);
                return true;
            }
        }
        return false;
    }

}