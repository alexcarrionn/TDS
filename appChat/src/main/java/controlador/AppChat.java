package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modelo.Contacto;
import modelo.Mensaje;
import modelo.RepositorioUsuario;
import modelo.Usuario;

public class AppChat {
    private Usuario usuarioLogueado;
    private List<Contacto> listaContactos;

    public AppChat() {
        this.listaContactos = new ArrayList<>();
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean hacerLogin(String tel, String contraseña) {
        Usuario usuario = RepositorioUsuario.getUnicaInstancia().getUsuario(Integer.parseInt(tel));
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            usuarioLogueado = usuario;
            return true;
        }
        return false;
    }

    public List<Mensaje> obtenerMensajesReMensaje() {
        Usuario javi = new Usuario(98523, "Javi", "", "", "");
        Usuario ana = new Usuario(98524, "Ana", "", "", "");

        List<Mensaje> resultado = new ArrayList<>();
        resultado.add(new Mensaje("Hola Javier", ana, javi, "", "", ""));
        resultado.add(new Mensaje("Hola Ana", javi, ana, "", "", ""));

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
