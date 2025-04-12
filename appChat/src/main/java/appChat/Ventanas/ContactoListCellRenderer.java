package appChat.Ventanas;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import modelo.Contacto;
import modelo.ContactoIndividual;

import java.awt.*;
import java.io.File;

public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;
	private static final Border SELECCIONADO = BorderFactory.createLineBorder(Color.BLUE, 2);
	private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private JLabel lblImagen;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JLabel lblSaludo;

	public ContactoListCellRenderer() {
		setLayout(new BorderLayout(10, 10)); // Espaciado entre imagen y texto

		lblImagen = new JLabel();
		lblNombre = new JLabel();
		lblTelefono = new JLabel();
		lblSaludo = new JLabel();

		lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
		lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSaludo.setFont(new Font("Arial", Font.ITALIC, 12));

		JPanel panelTexto = new JPanel(new GridLayout(3, 1)); // Para organizar los textos verticalmente
		panelTexto.add(lblNombre);
		panelTexto.add(lblTelefono);
		panelTexto.add(lblSaludo);

		add(lblImagen, BorderLayout.WEST); // Imagen a la izquierda
		add(panelTexto, BorderLayout.CENTER); // Texto a la derecha
	}

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

	// Método para obtener la foto del grupo si es necesario
	private String getFotoGrupo(Contacto contacto) {
		String fotoGrupo = contacto.getFoto();
		return (fotoGrupo == null || fotoGrupo.isEmpty()) ? "https://robohash.org/grupo" : fotoGrupo;
	}

	// Método auxiliar para cargar la imagen y escalarla
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