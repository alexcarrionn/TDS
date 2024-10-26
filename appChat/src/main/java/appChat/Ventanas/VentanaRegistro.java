package appChat.Ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import com.toedter.calendar.JDateChooser;

public class VentanaRegistro {
    private static GridBagConstraints gbc_1;
    private static GridBagConstraints gbc_2;
    private static GridBagConstraints gbc_3;
    private static GridBagConstraints gbc_5;
    private static GridBagConstraints gbc_6;
    private static GridBagConstraints gbc_7;
    private static GridBagConstraints gbc_8;
    private static GridBagConstraints gbc_4;
    private static GridBagConstraints gbc_9;
    private static GridBagConstraints gbc_10;
    private static GridBagConstraints gbc_11;

    public static void main(String[] args) {
        // Crear la ventana principal
        JFrame frame = new JFrame("Ventana registro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(582, 394);
        frame.getContentPane().setLayout(new BorderLayout());

        // Crear el panel central para el formulario
        JPanel panelCentral = new JPanel();
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.rowHeights = new int[]{0, 0, 0, 0, 31, 0, 0};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gbl_panelCentral.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
        gbl_panelCentral.columnWidths = new int[]{0, 135, 0, 139};
        panelCentral.setLayout(gbl_panelCentral);

        // Etiqueta y campo de nombre
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);  // Espaciado entre componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(new JLabel("Nombre:"), gbc);
        
        gbc_2 = new GridBagConstraints();
        gbc_2.anchor = GridBagConstraints.WEST;
        gbc_2.insets = new Insets(5, 5, 5, 0);
        gbc_2.gridx = 1;
        gbc_2.gridy = 0;
        gbc_2.gridwidth = 3;
        JTextField nombreField = new JTextField(20);
        panelCentral.add(nombreField, gbc_2);

        // Etiqueta y campo de apellidos
        gbc_8 = new GridBagConstraints();
        gbc_8.anchor = GridBagConstraints.WEST;
        gbc_8.insets = new Insets(5, 5, 5, 5);
        gbc_8.gridx = 0;
        gbc_8.gridy = 1;
        panelCentral.add(new JLabel("Apellidos:"), gbc_8);
        
        gbc_3 = new GridBagConstraints();
        gbc_3.anchor = GridBagConstraints.WEST;
        gbc_3.insets = new Insets(5, 5, 5, 0);
        gbc_3.gridx = 1;
        gbc_3.gridy = 1;
        gbc_3.gridwidth = 3;
        JTextField apellidosField = new JTextField(20);
        panelCentral.add(apellidosField, gbc_3);

        // Etiqueta y campo de teléfono
        gbc_7 = new GridBagConstraints();
        gbc_7.anchor = GridBagConstraints.WEST;
        gbc_7.insets = new Insets(5, 5, 5, 5);
        gbc_7.gridx = 0;
        gbc_7.gridy = 2;
        panelCentral.add(new JLabel("Teléfono:"), gbc_7);
        
        gbc_10 = new GridBagConstraints();
        gbc_10.anchor = GridBagConstraints.WEST;
        gbc_10.insets = new Insets(5, 5, 5, 5);
        gbc_10.gridx = 1;
        gbc_10.gridy = 2;
        JTextField telefonoField = new JTextField(10);
        panelCentral.add(telefonoField, gbc_10);

        // Etiqueta y campos de contraseña
        gbc_6 = new GridBagConstraints();
        gbc_6.anchor = GridBagConstraints.WEST;
        gbc_6.insets = new Insets(5, 5, 5, 5);
        gbc_6.gridx = 0;
        gbc_6.gridy = 3;
        panelCentral.add(new JLabel("Contraseña:"), gbc_6);
        
        gbc_9 = new GridBagConstraints();
        gbc_9.anchor = GridBagConstraints.WEST;
        gbc_9.insets = new Insets(5, 5, 5, 5);
        gbc_9.gridx = 1;
        gbc_9.gridy = 3;
        JPasswordField passwordField1 = new JPasswordField(10);
        panelCentral.add(passwordField1, gbc_9);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 2;
        gbc.gridy = 3;
        panelCentral.add(new JLabel("Repetir:"), gbc);
        
        gbc_11 = new GridBagConstraints();
        gbc_11.anchor = GridBagConstraints.WEST;
        gbc_11.insets = new Insets(5, 5, 5, 0);
        gbc_11.gridx = 3;
        gbc_11.gridy = 3;
        JPasswordField passwordField2 = new JPasswordField(10);
        panelCentral.add(passwordField2, gbc_11);

        // Etiqueta y campo de fecha
        gbc_1 = new GridBagConstraints();
        gbc_1.anchor = GridBagConstraints.WEST;
        gbc_1.insets = new Insets(5, 5, 5, 5);
        gbc_1.gridx = 0;
        gbc_1.gridy = 4;
        panelCentral.add(new JLabel("Fecha:"), gbc_1);
        
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        GridBagConstraints gbc_dateChooser = new GridBagConstraints();
        gbc_dateChooser.fill = GridBagConstraints.BOTH;
        gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
        gbc_dateChooser.gridx = 1;
        gbc_dateChooser.gridy = 4;
        panelCentral.add(dateChooser, gbc_dateChooser);

        // Etiqueta y espacio para la imagen
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 3;
        gbc.gridy = 4;
        panelCentral.add(new JLabel("Imagen:"), gbc);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 3;
        gbc.gridy = 5;
        JLabel imagenLabel = new JLabel(new ImageIcon(VentanaRegistro.class.getResource("/imagenes/agregar-usuario.png")));
        panelCentral.add(imagenLabel, gbc);

        // Botón para cargar la imagen
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 0, 0);
        gbc.gridx = 3;
        gbc.gridy = 6;
        JButton cargarImagenButton = new JButton("Añadir Imagen");
        panelCentral.add(cargarImagenButton, gbc);

        // Acción del botón para cargar la imagen
        cargarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crea un JFileChooser para seleccionar la imagen
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(frame);

                // Si se selecciona un archivo
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Establecer la nueva imagen en el JLabel
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    imagenLabel.setIcon(imageIcon);
                }
            }
        });

        // Etiqueta y campo de saludo
        gbc_5 = new GridBagConstraints();
        gbc_5.anchor = GridBagConstraints.WEST;
        gbc_5.insets = new Insets(5, 5, 5, 5);
        gbc_5.gridx = 0;
        gbc_5.gridy = 5;
        panelCentral.add(new JLabel("Estado:"), gbc_5);
        
        gbc_4 = new GridBagConstraints();
        gbc_4.anchor = GridBagConstraints.WEST;
        gbc_4.insets = new Insets(5, 5, 5, 5);
        gbc_4.gridx = 1;
        gbc_4.gridy = 5;
        gbc_4.gridwidth = 2;
        JTextArea saludoArea = new JTextArea(3, 20);
        saludoArea.setLineWrap(true);
        panelCentral.add(saludoArea, gbc_4);

        // Añadir panel central al frame
        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);

        // Crear el panel inferior para los botones
        JPanel panelBotones = new JPanel();
        JButton cancelButton = new JButton("Cancelar");
        JButton acceptButton = new JButton("Aceptar");

        panelBotones.add(cancelButton);
        panelBotones.add(acceptButton);

        // Añadir panel de botones al frame
        frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Hacer la ventana visible
        frame.setVisible(true);
    }
}
