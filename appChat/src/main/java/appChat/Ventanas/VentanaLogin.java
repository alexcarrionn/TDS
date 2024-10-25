package appChat.Ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaLogin {
    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame frame = new JFrame("AppChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(651, 416);
        frame.getContentPane().setLayout(new BorderLayout());

        // Crear el panel superior para el título
        JLabel titleLabel = new JLabel("AppChat", JLabel.CENTER);
        titleLabel.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/whastapp.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);

        // Crear el panel central para el formulario
        JPanel panelCentral = new JPanel();
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{0, 271, 20, 0};
        gbl_panelCentral.rowHeights = new int[]{0, 0, 0, 100, 0, 0, 0, 0};
        gbl_panelCentral.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);

        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);

        // Etiqueta y campo de teléfono
        JLabel phoneLabel = new JLabel("Teléfono:");
        phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 15));
        GridBagConstraints gbc_phoneLabel = new GridBagConstraints();
        gbc_phoneLabel.fill = GridBagConstraints.BOTH;
        gbc_phoneLabel.insets = new Insets(0, 0, 5, 5);
        gbc_phoneLabel.gridx = 1;
        gbc_phoneLabel.gridy = 4;
        gbc_phoneLabel.anchor = GridBagConstraints.CENTER; // Centrar la etiqueta
        panelCentral.add(phoneLabel, gbc_phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_phoneField = new GridBagConstraints();
        gbc_phoneField.anchor = GridBagConstraints.CENTER; // Centrar el campo
        gbc_phoneField.fill = GridBagConstraints.VERTICAL;
        gbc_phoneField.insets = new Insets(0, 0, 5, 0);
        gbc_phoneField.gridx = 2;
        gbc_phoneField.gridy = 4;
        panelCentral.add(phoneField, gbc_phoneField);

        // Etiqueta y campo de contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
        gbc_passwordLabel.fill = GridBagConstraints.BOTH;
        gbc_passwordLabel.insets = new Insets(0, 0, 0, 5);
        gbc_passwordLabel.gridx = 1;
        gbc_passwordLabel.gridy = 6;
        gbc_passwordLabel.anchor = GridBagConstraints.CENTER; // Centrar la etiqueta
        panelCentral.add(passwordLabel, gbc_passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_passwordField = new GridBagConstraints();
        gbc_passwordField.anchor = GridBagConstraints.CENTER; // Centrar el campo
        gbc_passwordField.fill = GridBagConstraints.VERTICAL;
        gbc_passwordField.gridx = 2;
        gbc_passwordField.gridy = 6;
        panelCentral.add(passwordField, gbc_passwordField);

        // Crear el panel inferior para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        JButton registerButton = new JButton("Registrar");
        registerButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/avatar.png")));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Acción del botón Registrar
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/cancelar.png")));

        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        acceptButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/aceptar.png")));

        panelBotones.add(registerButton);
        panelBotones.add(cancelButton);
        panelBotones.add(acceptButton);

        frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Hacer la ventana visible
        frame.setVisible(true);
    }
}
