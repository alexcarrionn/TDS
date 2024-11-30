package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.Usuario;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public class AppChat {
    private static AppChat unicaInstancia;
    private Usuario usuarioLogueado;
    private RepositorioUsuario repo; 
    private List<Contacto> listaContactos;
    private List<Mensaje> mensajes; // Lista para almacenar los mensajes
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    
    private AppChat() {
        inicializarAdaptadores();
        inicializarRepositorio();
        // Inicializar la lista de mensajes
        mensajes = new ArrayList<>();
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
        Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(tel);
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
    public List<Mensaje> obtenerMensajes() {
        return new ArrayList<>(mensajes); // Devuelve una copia de la lista de mensajes
    }

    public List<Contacto> buscarContactos(String texto, String telefono) {
        // Crear variables locales para las versiones procesadas
        final String textoFiltrado = (texto != null) ? texto.trim().toLowerCase() : "";
        final String telefonoFiltrado = (telefono != null) ? telefono.trim() : "";

        // Usar estas variables en la lambda
        return listaContactos.stream()
            .filter(c -> (textoFiltrado.isEmpty() || c.getNombre().toLowerCase().contains(textoFiltrado)) &&
                         (telefonoFiltrado.isEmpty() || String.valueOf(c.getTelefono()).contains(telefonoFiltrado)))
            .collect(Collectors.toList());
    }

    public ContactoIndividual agregarContacto(String nombre, int telefono) {
        ContactoIndividual contacto = new ContactoIndividual(nombre, telefono);
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
        } else {
            System.out.println("El contacto ya existe.");
        }
        return contacto;
    }

    public void eliminarContacto(Contacto contacto) {
        listaContactos.remove(contacto);
    }

    public List<Contacto> obtenerTodosContactos() {
        return new ArrayList<>(listaContactos);
    }
}

    
    /*    
    public void enviarMensaje(Contacto contacto, String mensajeEnviar) {
		Mensaje mensaje = new Mensaje(mensajeEnviar, LocalDateTime.now(), usuarioActual, contacto);
		contacto.enviarMensaje(mensaje);

		adaptadormensaje.registrarMensaje(mensaje);

		if (contacto instanceof IndividualContact) {
			adaptadorContactoIndividual.modificarContacto((IndividualContact) contacto);
		} else {
			adaptadorGrupo.modificarGrupo((Group) contacto);
		}
	}


    public void enviarMensaje(Contacto contacto, int emoji) {
		Mensaje mensaje = new Mensaje(emoji, LocalDateTime.now(), usuarioActual, contacto);
		contacto.sendMessage(mensaje);
		adaptadorMensaje.registrarMensaje(mensaje);

		if (contacto instanceof IndividualContact) {
			adaptadorContactoIndividual.modificarContacto((IndividualContact) contacto);
		} else {
			adaptadorGrupo.modificarGrupo((Group) contacto);
		}
	}
*/
