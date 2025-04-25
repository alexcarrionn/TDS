package ventanas;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import controlador.AppChat;
import modelo.Contacto;
import modelo.ContactoIndividual;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * Clase que actúa como renderer para los elementos de una JList de contactos en la aplicación de chat.
 * Muestra la imagen del contacto, su nombre, teléfono y saludo. Además, permite editar el nombre 
 * si se trata de un contacto inverso.
 * 
 * Implementa {@code ListCellRenderer<Contacto>} y extiende {@code JPanel}.
 * 
 */


public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {

	private static final long serialVersionUID = 1L;
	private static final Border SELECCIONADO = BorderFactory.createLineBorder(Color.BLUE, 2);
	private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private JLabel lblImagen;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblSaludo;
	private JButton btnEditar;
	
	/**
	 * Referencia al controlador principal de la aplicación para realizar operaciones como la edición de contactos.
	 */
	private AppChat appchat;
	
	private Contacto contactoActual;
	
	/**
	 * Constructor de la clase.
	 * Inicializa y organiza los componentes gráficos necesarios para representar un contacto.
	 */

	public ContactoListCellRenderer() {
	    setLayout(new BorderLayout(10, 10)); // Espaciado entre imagen y texto

	    lblImagen = new JLabel();
	    lblNombre = new JLabel();
	    lblTelefono = new JLabel();
	    lblSaludo = new JLabel();
	    btnEditar = new JButton("");

	    btnEditar.setIcon(new ImageIcon(ContactoListCellRenderer.class.getResource("/imagenes/boton-mas.png")));
	    btnEditar.setPreferredSize(new Dimension(20, 20)); // Tamaño cuadrado
	    btnEditar.setVisible(false); // Oculto por defecto
	    btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor al pasar sobre el ícono

	    lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
	    lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
	    lblSaludo.setFont(new Font("Arial", Font.ITALIC, 12));

	    JPanel panelTexto = new JPanel(new GridLayout(3, 1)); // Para organizar los textos verticalmente
	    panelTexto.add(lblNombre);
	    panelTexto.add(lblTelefono);
	    panelTexto.add(lblSaludo);

	    // Crear un panel vertical para el botón con espaciado arriba y abajo
	    JPanel panelBoton = new JPanel();
	    panelBoton.setLayout(new BoxLayout(panelBoton, BoxLayout.Y_AXIS)); // Diseño vertical
	    panelBoton.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio arriba
	    panelBoton.add(btnEditar); // Añadir el botón
	    panelBoton.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio abajo

	    add(lblImagen, BorderLayout.WEST); // Imagen a la izquierda
	    add(panelTexto, BorderLayout.CENTER); // Texto en el centro
	    add(panelBoton, BorderLayout.EAST); // Botón a la derecha
	}
	
	/**
	 * Devuelve el componente que se usará para renderizar cada celda de la lista de contactos.
	 * 
	 * @param listacontactos la lista a la que pertenece el contacto.
	 * @param contacto el objeto contacto que se está renderizando.
	 * @param index el índice de la celda en la lista.
	 * @param isSelected true si la celda está seleccionada.
	 * @param cellHasFocus true si la celda tiene el foco.
	 * @return el componente component que representa visualmente el contacto.
	 */

	@Override
	public Component getListCellRendererComponent(JList<? extends Contacto> listacontactos, Contacto contacto,
			int index, boolean isSelected, boolean cellHasFocus) {

		// Cargar y establecer la imagen
		String rutaImagen = contacto instanceof ContactoIndividual ? contacto.getFoto() : getFotoGrupo(contacto);
		ImageIcon icono = cargarImagen(rutaImagen);
		lblImagen.setIcon(icono);

		// Configuración del texto
		lblNombre.setText(contacto.getNombre());
		if (contacto instanceof ContactoIndividual) {
			ContactoIndividual contactoInd = (ContactoIndividual) contacto;
			lblTelefono.setText("Tel: " + contactoInd.getMovil());
			lblSaludo.setText(contactoInd.getEstado());
		} else {
			lblTelefono.setText("Grupo");
			lblSaludo.setText("");
		}

		// Mostrar el botón "Editar" si es un contacto inverso sin nombre
        if (contacto instanceof ContactoIndividual && ((ContactoIndividual) contacto).isContactoInverso()) {
            btnEditar.setVisible(true);
        } else {
            btnEditar.setVisible(false);
        }
		
        this.contactoActual = contacto;

        // Limpiar listeners anteriores para evitar acumulación
        for (ActionListener al : btnEditar.getActionListeners()) {
        	btnEditar.removeActionListener(al);
        }
        
        btnEditar.addActionListener(ev->{
        	String nuevoNombre = JOptionPane.showInputDialog(this, "Introduce el nuevo nombre para el contacto:", contactoActual.getNombre());
        	
        	// Agregar nuevo listener con el contacto actual
        	if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
        		appchat.actualizarNombreContacto(contactoActual, nuevoNombre.trim());
        		listacontactos.revalidate();
        		listacontactos.repaint();
        	}
        	
        });
        
        
		// Configuración de colores para selección
		if (isSelected) {
			setBackground(new Color(184, 207, 229)); // Color de fondo para selección
			setBorder(SELECCIONADO); // Borde azul para mostrar selección
		} else {
			setBackground(Color.WHITE); // Fondo blanco cuando no está seleccionado
			setBorder(NO_SELECCIONADO); // Sin borde cuando no está seleccionado
		}

		setOpaque(true); // Para que el fondo se muestre correctamente
		return this;
	}

	/**
	 * Devuelve la ruta de la foto asociada a un grupo. Si no existe una ruta válida,
	 * se devuelve una URL por defecto.
	 * 
	 * @param contacto el objeto {@code Contacto} del cual se obtiene la foto.
	 * @return la ruta o URL de la imagen.
	 */

	private String getFotoGrupo(Contacto contacto) {
		String fotoGrupo = contacto.getFoto();
		return (fotoGrupo == null || fotoGrupo.isEmpty()) ? "https://robohash.org/grupo" : fotoGrupo;
	}

	/**
	 * Carga y escala una imagen desde una ruta local o una URL remota.
	 * 
	 * @param rutaImagen la ruta local o URL de la imagen.
	 * @return un objeto  escalado a 50x50 píxeles o null si no se pudo cargar.
	 */

	private ImageIcon cargarImagen(String rutaImagen) {
		if (rutaImagen == null || rutaImagen.isEmpty()) {
			return null;
		}

		ImageIcon icono = null;
		try {
			if (rutaImagen.startsWith("http://") || rutaImagen.startsWith("https://")) {
				// Si es una URL remota
				icono = new ImageIcon(new java.net.URL(rutaImagen));
			} else {
				// Si es una ruta local
				File file = new File(rutaImagen);
				if (file.exists()) {
					icono = new ImageIcon(file.getAbsolutePath());
				} else {
					System.err.println("La imagen no existe en la ruta: " + rutaImagen);
				}
			}
		} catch (Exception e) {
			System.err.println("No se pudo cargar la imagen: " + rutaImagen);
			e.printStackTrace();
		}

		// Escalar la imagen si se ha cargado correctamente
		if (icono != null && icono.getImage() != null) {
			int anchoDeseado = 50;
			int altoDeseado = 50;
			Image imagenOriginal = icono.getImage();
			Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
			return new ImageIcon(imagenEscalada);
		}

		return null;
	}

}