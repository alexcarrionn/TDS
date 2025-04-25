package ventanas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import modelo.Mensaje;
import modelo.TipoMensaje;

/**
 * Render personalizado para los elementos de una lista de mensajes.
 * Muestra el texto del mensaje, el nombre del usuario ("You" u "Other"),
 * y una imagen generada desde un servicio externo.
 * 
 * Implementa {@code ListCellRenderer<Mensaje>} y extiende {@code JPanel}.
 * 
 * Cada celda incluye una imagen, el nombre del usuario y el texto del mensaje.
 * 
 */

public class MensajeCellRender extends JPanel implements ListCellRenderer<Mensaje> {
	private static final long serialVersionUID = 1L;
	private JLabel usuarioLabel;
	private JLabel imageLabel;
	private JLabel textolabel;
	private JPanel panelCentro;
	
	/**
	 * Constructor principal de la clase.
	 * Inicializa los componentes gráficos y organiza la disposición
	 * de los elementos dentro del panel.
	 */
	public MensajeCellRender() {
		setLayout(new BorderLayout(5, 5));
		usuarioLabel = new JLabel();
		imageLabel = new JLabel();
		textolabel = new JLabel();
		panelCentro = new JPanel();
		panelCentro.setLayout(new BorderLayout(2, 2));
		panelCentro.add(usuarioLabel, BorderLayout.NORTH);
		panelCentro.add(textolabel, BorderLayout.SOUTH);
		add(imageLabel, BorderLayout.WEST);
		add(panelCentro, BorderLayout.CENTER);
	}
	
	/**
	 * Método que devuelve el componente que se usará para renderizar
	 * cada celda de la lista de mensajes.
	 * 
	 * @param list la lista que contiene los mensajes.
	 * @param Mensaje el mensaje a renderizar.
	 * @param index el índice del mensaje en la lista.
	 * @param isSelected true si la celda está seleccionada.
	 * @param cellHasFocus true si la celda tiene el foco.
	 * @return el componente component configurado para representar visualmente el mensaje.
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje Mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		textolabel.setText(Mensaje.getTexto());
		String usuario = "";

		if (Mensaje.getTipo().equals(TipoMensaje.ENVIADO)) {
			usuario = "You";
		} else {
			usuario = "Other";
		}
		usuarioLabel.setText(usuario);
		// Load the image from a random URL (for example, using "https://robohash.org")
		try {
			URL imageUrl = new URL("https://robohash.org/" + usuario + "?size=50x50");
			Image image = ImageIO.read(imageUrl);
			ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			imageLabel.setIcon(imageIcon);
		} catch (IOException e) {
			e.printStackTrace();
			imageLabel.setIcon(null); // Default to no image if there was an issue
		}
		// Set background and foreground based on selection
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			panelCentro.setBackground(list.getSelectionBackground());
			panelCentro.setForeground(list.getSelectionBackground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			panelCentro.setBackground(list.getBackground());
			panelCentro.setForeground(list.getForeground());
		}
		return this;
	}
}
