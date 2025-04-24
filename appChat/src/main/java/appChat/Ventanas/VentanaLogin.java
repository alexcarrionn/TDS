package appChat.Ventanas;

import javax.swing.*;
import controlador.AppChat;
import java.awt.*;


/**
 * Clase que representa la ventana de inicio de sesión para la aplicación.
 * Contiene campos para ingresar el teléfono y la contraseña, así como botones
 * para registrar un nuevo usuario, aceptar o cancelar.
 * 
 * Utiliza el patrón Singleton para acceder al controlador principal.
 * Implementa la interfaz gráfica utilizando Swing.
 * 
 */

public class VentanaLogin {
    private JFrame frame;
    private JTextField phoneField;
    private JPasswordField contrasena;
    
    /**
     * Hacemos referencia al controlador 
     */
    private final AppChat controlador;

    /**
     * Constructor que inicializa la ventana y el controlador
     * @wbp.parser.entryPoint
     */
    public VentanaLogin() {
        controlador = AppChat.getUnicaInstancia(); // Inicialización del controlador
        initialize();
    }

    /**
     * Método principal para lanzar la aplicación
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaLogin window = new VentanaLogin();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Método para manejar el inicio de sesión
     */
    private void login() {
        String telefono = phoneField.getText();
        String clave = new String(contrasena.getPassword());

        if (telefono.isBlank()) {
            JOptionPane.showMessageDialog(frame, "El campo de Teléfono tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (clave.isBlank()) {
            JOptionPane.showMessageDialog(frame, "Debes indicar la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean login = controlador.hacerLogin(telefono, clave);

            if (login) {
                VentanaPrincipal vp = new VentanaPrincipal();
                vp.setVisible(true);
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "El Usuario no está registrado o la contraseña es incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método para inicializar los componentes de la ventana
     */
    private void initialize() {
        frame = new JFrame("AppChat");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/imagenes/AppChatLogo.png")));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(651, 416);
        frame.getContentPane().setLayout(new BorderLayout());
        //obtenermos la imagen de nuestra aplicación 
        ImageIcon originalIcon = new ImageIcon(VentanaLogin.class.getResource("/imagenes/AppChatLogo.png"));
        Image image = originalIcon.getImage();
        Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	     ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Panel central para el formulario
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridBagLayout());
        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);
        
                // Panel superior para el título
                JLabel titleLabel = new JLabel("AppChat", JLabel.CENTER);
                GridBagConstraints gbc_titleLabel = new GridBagConstraints();
                gbc_titleLabel.insets = new Insets(0, 0, 5, 0);
                gbc_titleLabel.gridx = 1;
                gbc_titleLabel.gridy = 0;
                panelCentral.add(titleLabel, gbc_titleLabel);
                
        titleLabel.setIcon(scaledIcon);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Etiqueta y campo de teléfono
        JLabel phoneLabel = new JLabel("Teléfono:");
        phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 15));
        GridBagConstraints gbc_phoneLabel = new GridBagConstraints();
        gbc_phoneLabel.anchor = GridBagConstraints.NORTH;
        gbc_phoneLabel.insets = new Insets(10, 0, 5, 5);
        gbc_phoneLabel.gridx = 0;
        gbc_phoneLabel.gridy = 2;
        panelCentral.add(phoneLabel, gbc_phoneLabel);

        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_phoneField = new GridBagConstraints();
        gbc_phoneField.insets = new Insets(0, 0, 15, 0);
        gbc_phoneField.gridx = 1;
        gbc_phoneField.gridy = 2;
        gbc_phoneField.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(phoneField, gbc_phoneField);

        // Etiqueta y campo de contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
        gbc_passwordLabel.anchor = GridBagConstraints.NORTH;
        gbc_passwordLabel.insets = new Insets(10, 0, 0, 5);
        gbc_passwordLabel.gridx = 0;
        gbc_passwordLabel.gridy = 4;
        panelCentral.add(passwordLabel, gbc_passwordLabel);

        contrasena = new JPasswordField();
        contrasena.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_contrasena = new GridBagConstraints();
        gbc_contrasena.insets = new Insets(0, 0, 15, 0);
        gbc_contrasena.gridx = 1;
        gbc_contrasena.gridy = 4;
        gbc_contrasena.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(contrasena, gbc_contrasena);

        // Panel inferior para los botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Registrar");
        registerButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/avatar.png")));
        registerButton.addActionListener(e -> {
            VentanaRegistro registro = new VentanaRegistro();
            registro.setVisible(true);
            frame.setVisible(false);
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/cancelar.png")));
        cancelButton.addActionListener(e -> System.exit(0));

        JButton acceptButton = new JButton("Aceptar");
        acceptButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/aceptar.png")));
        acceptButton.addActionListener(e -> login());

        panelBotones.add(registerButton);
        panelBotones.add(cancelButton);
        panelBotones.add(acceptButton);

        frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }
}
