package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;
import modelo.Contactos;
import modelo.Usuario;


//Usa un pool para evitar problemas doble referencia con ventas
public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public static AdaptadorUsuario getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuario();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuario() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra un cliente se le asigna un identificador ï¿½nico */
	public void registrarUsuario(Usuario user) {
		Entidad eUsuario = null;

		// Si la entidad esta registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());
		} catch (NullPointerException e) {}
		if (eUsuario != null) return;

		/* registrar primero los atributos que son objetos
		AdaptadorVentaTDS adaptadorVenta = AdaptadorVentaTDS.getUnicaInstancia();
		for (Venta v : cliente.getVentas())
			adaptadorVenta.registrarVenta(v);*/

		// crear entidad usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(null);
		// registrar entidad usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		user.setTelefono(eUsuario.getId());
	}

	public void borrarUsuario(Usuario user) {
		// No se comprueban restricciones de integridad con Contacto
		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario user) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getTelefono());

		/*for (Propiedad prop : eCliente.getPropiedades()) {
			if (prop.getNombre().equals("codigo")) {
				prop.setValor(String.valueOf(user.getTelefono()));
			} else if (prop.getNombre().equals("dni")) {
				prop.setValor(user.getDni());
			} else if (prop.getNombre().equals("nombre")) {
				prop.setValor(user.getNombre());
			} else if (prop.getNombre().equals("ventas")) {
				String ventas = obtenerCodigosVentas(user.getVentas());
				prop.setValor(ventas);
			}
			servPersistencia.modificarPropiedad(prop);
		}*/

	}
	@Override
	public Usuario recuperarUsuario(int id) {
		 if (PoolUsuarios.INSTANCE.containsUsuario(id)) {
		return PoolUsuarios.INSTANCE.getUsuario(id);
		 }
		 Entidad entidadUsuario = servPersistencia.recuperarEntidad(id);
		 if (entidadUsuario == null) return null;
		 String nombre = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "nombre");
		 String movil = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "movil");
		 String contrasena = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "contrasena");
		 String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(entidadUsuario,
		"fechaNacimiento");
		 String imagen = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "imagen");
		 String saludo = servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "saludo");
		 boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(entidadUsuario,
		"isPremium"));
		 Usuario usuario = new Usuario(nombre, movil, contrasena,LocalDate.parse(fechaNacimientoStr), imagen, saludo);
		 usuario.setId(id);
		 usuario.setPremium(premium);
		 PoolUsuarios.INSTANCE.addUsuario(usuario.getId(), usuario);
		 usuario.addAllContactos(obtenerContactos(
		servPersistencia.recuperarPropiedadEntidad(entidadUsuario, "contactos")));
		 return usuario;
		}
	
	public List<Usuario> recuperarTodosUsuarios() {

		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}
}