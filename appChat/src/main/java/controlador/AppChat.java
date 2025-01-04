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

    public boolean hacerLogin(int tel, String contraseña) {
        Usuario usuario = repo.getUsuario(tel);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            usuarioLogueado = usuario;
            return true;
        }
        return false;
    }

    // Le pasamos un usuario, creado en la ventanaRegistro, para poder registrarlo en la base de datos
    public boolean crearCuentaUsuario(int movil, String nombre, String imagen, String contrasena, LocalDate fecha, String estado) {
        // Primero creamos un nuevo usuario con los datos metidos en la Ventana Registro
        Usuario nuevoUsuario = new Usuario(movil, nombre, imagen, contrasena, fecha, estado, null);
        // Comprobamos que no hay ningún usuario con ese número de teléfono
        if (!repo.contains(nuevoUsuario)) {
            // Conexión con la persistencia y registrar usuario
            repo.addUsuario(nuevoUsuario);
            adaptadorUsuario.registrarUsuario(nuevoUsuario);
            return hacerLogin(movil, contrasena);
        }
        // En caso de que haya un usuario con ese teléfono, devolvemos false para que mande un mensaje de error
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

    public ContactoIndividual agregarContacto(String nombre, int telefono) {
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