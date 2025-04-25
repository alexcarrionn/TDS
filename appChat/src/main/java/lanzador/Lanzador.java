package lanzador;

import java.io.IOException;

import cargador.CargarAppChat;
import ventanas.VentanaLogin;

import java.io.File;

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
		ProcessBuilder terminalPersistencia = new ProcessBuilder("cmd.exe", "/k",
				"java -jar ServidorPersistenciaH2.jar");

		// IMPORTANTE CADA VEZ QUE SE QUIERA EJECUTAR EN UN EQUIPO DIFERENTE, CAMBIAR LA
		// RUTA DEL ARCHIVO
		terminalPersistencia.directory(new File("C:\\Users\\acarr\\Downloads\\ServidorPersistenciaH2"));
		try {
			terminalPersistencia.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// quitar una vez lanzado por primera vez
		CargarAppChat.main(null);

		VentanaLogin login = new VentanaLogin();
		login.main(null);

	}
}
