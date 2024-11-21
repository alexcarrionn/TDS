package appChat.Ventanas;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class VentanaBuscar {

    private JFrame frame;

    /**
     * @wbp.parser.entryPoint
     */
    public VentanaBuscar() {
        // Crear el JFrame dentro de la clase
        frame = new JFrame("Buscar Ventana");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);  // Tamaño más grande de la ventana
        frame.setLocationRelativeTo(null);

        // Añadir un margen general alrededor del contenedor principal
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crear un panel para el icono fuera del borde del panel de búsqueda
        JPanel panelIcono = new JPanel();
        JLabel iconoLabel = new JLabel(new ImageIcon(VentanaLogin.class.getResource("/imagenes/buscar.png"))); // Cambia la ruta del icono
        panelIcono.add(iconoLabel);

        // Crear el panel superior principal con un borde titulado
        JPanel panelSuperior = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Buscar");
        title.setTitleJustification(TitledBorder.LEFT); // Justificación a la izquierda
        panelSuperior.setBorder(title);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        // Panel para el campo de texto (en la parte superior del panel de búsqueda)
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.X_AXIS));
        JTextField textoField = new JTextField();
        addPlaceholder(textoField, "texto"); // Añadir placeholder
        panelTexto.add(textoField);

        // Panel para teléfono, contacto y botón de buscar (en la parte inferior del panel de búsqueda)
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.X_AXIS));

        // Campo de teléfono con placeholder
        JTextField telefonoField = new JTextField();
        addPlaceholder(telefonoField, "teléfono");
        panelCampos.add(telefonoField);
        panelCampos.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre campos

        // Campo de contacto con placeholder
        JTextField contactoField = new JTextField();
        addPlaceholder(contactoField, "contacto");
        panelCampos.add(contactoField);
        panelCampos.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre campos

        // Botón de buscar
        JButton buscarButton = new JButton("Buscar");
        buscarButton.setIcon(new ImageIcon(VentanaBuscar.class.getResource("/imagenes/buscar_boton.png")));
        buscarButton.setSelectedIcon(null);
        panelCampos.add(buscarButton);
        
        //TODO evento del boton
        

        // Añadir ambos paneles (texto y campos) al panel superior
        panelSuperior.add(panelTexto);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre panelTexto y panelCampos
        panelSuperior.add(panelCampos);

        // Crear el panel inferior con borde y área de desplazamiento
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Área de texto dentro de un JScrollPane, que se agrandará con la ventana
        JTextArea mensajesArea = new JTextArea();
        mensajesArea.setEditable(false); // Para que solo muestre mensajes
        JScrollPane scrollPane = new JScrollPane(mensajesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        // Añadir los paneles al contenedor principal
        contenedorPrincipal.add(panelIcono, BorderLayout.NORTH); // Icono fuera del panel de búsqueda
        contenedorPrincipal.add(panelSuperior, BorderLayout.CENTER);
        contenedorPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Ajustes de tamaño y expansión para el panelInferior
        panelInferior.setPreferredSize(new Dimension(600, 250)); // Tamaño inicial más grande

        // Añadir el contenedor principal al JFrame
        frame.getContentPane().add(contenedorPrincipal);

        // Hacer el frame visible
        frame.setVisible(true);
    }

    // Método para agregar placeholders en los campos de texto
    private void addPlaceholder(JTextField textField, String placeholderText) {
        textField.setText(placeholderText);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholderText)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholderText);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
