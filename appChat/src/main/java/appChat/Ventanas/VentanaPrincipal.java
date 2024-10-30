package appChat.Ventanas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal {

    private JFrame frame;
    /**
     * @wbp.parser.entryPoint
     */
    public VentanaPrincipal() {
        // Crear el JFrame
        frame = new JFrame("Ventana Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Configuración del panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Barra superior de navegación
        JPanel barraSuperior = new JPanel();
        barraSuperior.setLayout(new BoxLayout(barraSuperior, BoxLayout.X_AXIS));
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Componentes de la barra superior
        JComboBox<String> contactoOtelefono = new JComboBox<>(new String[] {"Seleccione o introduzca un teléfono"});
        contactoOtelefono.setFont(new Font("Tahoma", Font.PLAIN, 11));
        JButton btnIniciarConversacion = new JButton("");
        btnIniciarConversacion.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/mostrar.png")));
        JButton btnBuscar = new JButton("");
        btnBuscar.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/buscar_principal.png")));
        JButton btnContactos = new JButton("Contactos");
        btnContactos.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/contactos.png")));
        JButton btnPremium = new JButton("Premium");
        btnPremium.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/premium.png")));

        // Espacios para nombre e imagen del usuario
        JTextField nombreUsuario = new JTextField("Nombre del Usuario");
        nombreUsuario.setEditable(false); // Solo lectura

        JLabel imagenUsuario = new JLabel();
        imagenUsuario.setPreferredSize(new Dimension(40, 40)); // Tamaño para la imagen
        imagenUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borde alrededor de la imagen

        // Añadir componentes a la barra superior
        barraSuperior.add(contactoOtelefono);
        
        Component horizontalStrut = Box.createHorizontalStrut(5);
        barraSuperior.add(horizontalStrut);
        barraSuperior.add(btnIniciarConversacion);
        barraSuperior.add(Box.createHorizontalStrut(10));
        barraSuperior.add(btnBuscar);
        barraSuperior.add(Box.createHorizontalStrut(10));
        barraSuperior.add(btnContactos);
        barraSuperior.add(Box.createHorizontalStrut(10));
        barraSuperior.add(btnPremium);
        barraSuperior.add(Box.createHorizontalStrut(10));
        barraSuperior.add(nombreUsuario);
        barraSuperior.add(Box.createHorizontalStrut(5));
        barraSuperior.add(imagenUsuario);

        // Panel de contactos (izquierda)
        JPanel panelContactos = new JPanel();
        panelContactos.setLayout(new BoxLayout(panelContactos, BoxLayout.Y_AXIS));
        panelContactos.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        panelContactos.setPreferredSize(new Dimension(250, 0));

        // Panel de chat (derecha)
        JPanel panelChat = new JPanel(new BorderLayout());
        panelChat.setBorder(BorderFactory.createTitledBorder("Mensajes con contacto"));

        // Añadir componentes al panel principal
        panelPrincipal.add(barraSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelContactos, BorderLayout.WEST);
        
        JScrollPane scrollPane = new JScrollPane();
        panelContactos.add(scrollPane);
        panelPrincipal.add(panelChat, BorderLayout.CENTER);

        // Añadir el panel principal al JFrame
        frame.getContentPane().add(panelPrincipal);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
