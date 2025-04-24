package appChat.Ventanas;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import tds.BubbleText;

public class ChatBurbujas extends JPanel {
    
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor principal 
	 */
	public ChatBurbujas() {
        super(); // Llama al constructor de JPanel
        inicializarPanel();
    }

    // Método para configurar el panel
    private void inicializarPanel() {
        // Establecer el layout para apilar elementos verticalmente
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Personalización adicional del panel
        setOpaque(true); // Asegura que el fondo del panel sea visible
    }

    /**
     * Este método se podría usar para añadir una "burbuja de mensaje".
     * @param burbuja Componente gráfico que representa un mensaje.
     */
    public void agregarBurbuja(BubbleText burbuja) {
        add(burbuja); // Añade la burbuja al panel
        revalidate(); // Actualiza el layout del panel
        repaint(); // Redibuja el panel
    }
}
