package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Contacto;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.Usuario;
import persistencia.AdaptadorUsuario;

//Para poder usar la clase AppChat tiene que ser Singleton , para ello lo que hacemos es poner el contructor en privado y mas tarde creamos una 
//funcion estatica que nos devuelva una única instacia de la clase

public class AppChat {
    private static AppChat unicaInstancia;
    private static Usuario usuarioLogueado;
    private static RepositorioUsuario repo; 
    private List<Contacto> listaContactos;

    private AppChat() {
        this.listaContactos = new ArrayList<>();
    }

    public static AppChat getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AppChat();
        }
        return unicaInstancia;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static boolean hacerLogin(int tel, String contraseña) {
        Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(tel);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            usuarioLogueado = usuario;
            return true;
        }
        return false;
    }
    
    //Le pasamos un usuario, creado en la ventanaRegistro, para poder registrarlo en la base de datos
    public static boolean crearCuentaUsuario(int movil, String nombre, String imagen, String contrasena, LocalDate fecha, String estado) {
    	//Primero creamos un nuevo usuario con los datos metidos en la Ventana Registro
    	Usuario nuevoUsuario = new Usuario(movil, nombre, imagen, contrasena, fecha, estado, null);
		//Comprobamos que no hay Ningun Usuario con ese numero de telefono
    	if (!repo.contains(nuevoUsuario)) {
			// Conexion con la persistencia y registrar usuario
			repo.addUsuario(nuevoUsuario);
			AdaptadorUsuario.getUnicaInstancia().registrarUsuario(nuevoUsuario);
			return hacerLogin(movil, contrasena);
		}
    	//en caso de que haya un usuario con ese telefono cogemos y devolvemos false para que mande un mensaje de error
		return false;
    }

    
    public static List<Mensaje> obtenerMensajesReMensaje() {
        Usuario javi = new Usuario(98523, "Javi", "", "", "", null);
        Usuario ana = new Usuario(98524, "Ana", "", "", "", null);

        List<Mensaje> resultado = new ArrayList<>();
        resultado.add(new Mensaje("Hola Javier", ana, javi, "", "", ""));
        resultado.add(new Mensaje("Hola Ana", javi, ana, "", "", ""));
        //TODO
        return resultado;
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

    public void agregarContacto(Contacto contacto) {
        if (!listaContactos.contains(contacto)) {
            listaContactos.add(contacto);
        } else {
            System.out.println("El contacto ya existe.");
        }
    }

    public void eliminarContacto(Contacto contacto) {
        listaContactos.remove(contacto);
    }

    public List<Contacto> obtenerTodosContactos() {
        return new ArrayList<>(listaContactos);
    }
}