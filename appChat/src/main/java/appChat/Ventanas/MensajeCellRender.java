package appChat.Ventanas;

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
import controlador.AppChat;

public class MensajeCellRender extends JPanel implements ListCellRenderer<Mensaje> {
	private static final long serialVersionUID = 1L;
	private JLabel usuarioLabel;
	private JLabel imageLabel;
	private JLabel textolabel;
	private JPanel panelCentro;

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

	@Override
	public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje Mensaje, int index,
			boolean isSelected, boolean cellHasFocus) {
		// Set the text
		textolabel.setText(Mensaje.getTexto());
		String usuario = "";

		if (Mensaje.getEmisor().getNombre().equals(AppChat.getUnicaInstancia().obtenerMensajesReMensaje())) {
			usuario = Mensaje.getReceptor().getNombre();
		} else {
			usuario = Mensaje.getEmisor().getNombre();
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
