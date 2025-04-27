package lanzador;

import cargador.CargarAppChat;
import ventanas.VentanaLogin;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase que actúa como punto de entrada para iniciar la aplicación de chat,
 * incluyendo la configuración y arranque de un servidor de persistencia de
 * datos y la carga de la aplicación principal.
 * 
 * También registra algunos usuarios de ejemplo y lanza la ventana de login para
 * la interacción con el usuario.
 */

public class Lanzador {

	/**
	 * Constructor por defecto.
	 */
	public Lanzador() {
	}

	/**
	 * Método principal que se ejecuta al iniciar la aplicación.
	 * 
	 * @param args Argumentos de línea de comando.
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// Configurar el look and feel de JTattoo
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// quitar una vez lanzado por primera vez
		//CargarAppChat.main(null);

		VentanaLogin login = new VentanaLogin();
		login.main(null);
	}
}
