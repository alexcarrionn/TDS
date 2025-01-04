package controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.Usuario;
import persistencia.AdaptadorContactoIndividual;
import persistencia.AdaptadorGrupo;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public class AppChat {
    private static AppChat unicaInstancia;
    private Usuario usuarioLogueado;
    private RepositorioUsuario repo; 
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private Contacto chatActual;
    
    private AppChat() {
        inicializarAdaptadores();
        inicializarRepositorio();       
    }

    public static AppChat getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AppChat();
        }
        return unicaInstancia;
    }

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
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            usuarioLogueado = usuario;
            return true;
        }
        return false;
    }
    
    public boolean registrarUsuario(String telefono, String nombre,String foto, String contraseña,LocalDate fecha,String estado) {
		Usuario usuarioExistente = repo.getUsuario(telefono);
		if (usuarioExistente != null) {
			return false;
		}

		LocalDate fechaRegistro = LocalDate.now();

		Usuario nuevoUsuario = new Usuario(telefono, nombre, foto, contraseña, fechaRegistro,estado, null);

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
		// TODO Auto-generated method stub
		return null;
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
        ContactoIndividual contacto = new ContactoIndividual(nombre, telefono);
        if (!usuarioLogueado.getContactos().contains(contacto)) {
            usuarioLogueado.addContacto(contacto);
        } else {
            System.out.println("El contacto ya existe.");
        }
        return contacto;
    }

    public void eliminarContacto(Contacto contacto) {
        usuarioLogueado.removeContacto(contacto);
    }

    public List<Contacto> obtenerTodosContactos() {
        return usuarioLogueado.getContactos();
    }
      
    public void enviarMensaje(Contacto contacto, String mensajeEnviar) {
		Mensaje mensaje = new Mensaje(mensajeEnviar, usuarioLogueado, contacto,LocalDate.now());
		contacto.addMensaje(mensaje);

		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof ContactoIndividual) {
			AdaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
		} else {
			AdaptadorGrupo.modificarGrupo((Grupo) contacto);
		}
	}


    public void enviarMensaje(Contacto contacto, int emoji) {
		Mensaje mensaje = new Mensaje(emoji, usuarioLogueado, contacto, LocalDate.now());
		contacto.addMensaje(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof ContactoIndividual) {
			AdaptadorContactoIndividual.modificarContacto((ContactoIndividual) contacto);
		} else {
			AdaptadorGrupo.modificarGrupo((Grupo) contacto);
		}
	}
}