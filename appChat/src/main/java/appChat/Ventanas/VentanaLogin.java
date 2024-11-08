package appChat.Ventanas;

import javax.swing.*;
import controlador.AppChat;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaLogin {
    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame frame = new JFrame("AppChat");
        frame.setResizable(false);
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
        panelCentral.setLayout(gbl_panelCentral);

        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);
        
                // Etiqueta y campo de teléfono
                JLabel phoneLabel = new JLabel("Teléfono:");
                phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
                phoneLabel.setFont(new Font("Arial", Font.BOLD, 15));
                GridBagConstraints gbc_phoneLabel = new GridBagConstraints();
                gbc_phoneLabel.anchor = GridBagConstraints.NORTH;
                gbc_phoneLabel.insets = new Insets(10, 0, 5, 5);
                gbc_phoneLabel.gridx = 0;
                gbc_phoneLabel.gridy = 1;
                panelCentral.add(phoneLabel, gbc_phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_phoneField = new GridBagConstraints();
        gbc_phoneField.insets = new Insets(0, 0, 15, 0);
        gbc_phoneField.gridx = 1;
        gbc_phoneField.gridy = 1;
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
                gbc_passwordLabel.gridy = 3;
                panelCentral.add(passwordLabel, gbc_passwordLabel);

        JPasswordField contrasena = new JPasswordField();
        contrasena.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints gbc_contrasena = new GridBagConstraints();
        gbc_contrasena.insets = new Insets(0, 0, 15, 0);
        gbc_contrasena.gridx = 1;
        gbc_contrasena.gridy = 3;
        gbc_contrasena.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(contrasena, gbc_contrasena);

        // Crear el panel inferior para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        JButton registerButton = new JButton("Registrar");
        registerButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/avatar.png")));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Acción del botón Registrar
            	VentanaRegistro registro = new VentanaRegistro(); 
            	registro.setVisible(true); 
            	frame.setVisible(false);
            }
        
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e->System.exit(0));
        cancelButton.setIcon(new ImageIcon(VentanaLogin.class.getResource("/imagenes/cancelar.png")));

        JButton acceptButton = new JButton("Aceptar");
        acceptButton.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
                // Acción del botón Aceptar
            	String telefono =phoneField.getText(); 
          	  	String clave = contrasena.getText(); 
            	if (telefono.isBlank()) {
        			JOptionPane.showMessageDialog(frame, "El campo de Telefono tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
        		}
            	else if (clave.isBlank()) {
            		JOptionPane.showMessageDialog(frame, "Debes de indicar la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            	}
            	else {//recupero los datos de pantalla
            	  
            	  
            	 // ejecutamos el negocio a traves del CONTROLADOR
            	  
            	  boolean login = AppChat.hacerLogin(telefono, clave);
            	  
            	  //Actuo en la pantalla segun el resultado 
            	   
            	   if (login) {
            	   VentanaPrincipal vp= new VentanaPrincipal(); 
            	   vp.setVisible(true);
            	   frame.setVisible(false);}
            	   else{
            	   JOptionPane.showMessageDialog(frame, "El telefono no está registrado", "Error", JOptionPane.ERROR_MESSAGE);
            	   }
            	
            }}
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
