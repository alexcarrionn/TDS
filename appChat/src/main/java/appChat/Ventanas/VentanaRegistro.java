package appChat.Ventanas;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaRegistro {
    private static GridBagConstraints gbc_1;
    private static GridBagConstraints gbc_2;
    private static GridBagConstraints gbc_3;
    private static GridBagConstraints gbc_5;
    private static GridBagConstraints gbc_6;
    private static GridBagConstraints gbc_7;
    private static GridBagConstraints gbc_8;
    private static GridBagConstraints gbc_AreaEstado;
    private static JTextField imagen;
    private static GridBagConstraints gbc_labelImagen;
    private static JTextField repetirContra;
    private static GridBagConstraints gbc_repetirLabel;
    private static JTextField contraseña;
    private static JTextField telefono;
    private JLabel imagenLabel; // Hacer imagenLabel accesible en toda la clase
    private JFrame frame;

    public VentanaRegistro() {
        // Crear la ventana principal
        frame = new JFrame("Ventana registro");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(582, 394);
        frame.getContentPane().setLayout(new BorderLayout());

        // Crear el panel central para el formulario
        JPanel panelCentral = new JPanel();
        panelCentral.setForeground(Color.GREEN);
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.rowHeights = new int[]{0, 0, 0, 0, 31, 0, 30};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gbl_panelCentral.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gbl_panelCentral.columnWidths = new int[]{0, 234, 0, 0, 139, 0};
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
        gbc_2.insets = new Insets(5, 5, 5, 5);
        gbc_2.gridx = 1;
        gbc_2.gridy = 0;
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
        gbc_3.insets = new Insets(5, 5, 5, 5);
        gbc_3.gridx = 1;
        gbc_3.gridy = 1;
        JTextField apellidosField = new JTextField(20);
        panelCentral.add(apellidosField, gbc_3);

        // Etiqueta y campo de teléfono
        gbc_7 = new GridBagConstraints();
        gbc_7.anchor = GridBagConstraints.WEST;
        gbc_7.insets = new Insets(5, 5, 5, 5);
        gbc_7.gridx = 0;
        gbc_7.gridy = 2;
        panelCentral.add(new JLabel("Teléfono:"), gbc_7);

        telefono = new JTextField(20);
        GridBagConstraints gbc_textField_3 = new GridBagConstraints();
        gbc_textField_3.insets = new Insets(5, 5, 5, 5);
        gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_3.gridx = 1;
        gbc_textField_3.gridy = 2;
        panelCentral.add(telefono, gbc_textField_3);

        // Etiqueta y campos de contraseña
        gbc_6 = new GridBagConstraints();
        gbc_6.anchor = GridBagConstraints.WEST;
        gbc_6.insets = new Insets(5, 5, 5, 5);
        gbc_6.gridx = 0;
        gbc_6.gridy = 3;
        panelCentral.add(new JLabel("Contraseña:"), gbc_6);

        contraseña = new JTextField(20);
        GridBagConstraints gbc_contraseña = new GridBagConstraints();
        gbc_contraseña.insets = new Insets(5, 5, 5, 5);
        gbc_contraseña.fill = GridBagConstraints.HORIZONTAL;
        gbc_contraseña.gridx = 1;
        gbc_contraseña.gridy = 3;
        panelCentral.add(contraseña, gbc_contraseña);

        gbc_repetirLabel = new GridBagConstraints();
        gbc_repetirLabel.anchor = GridBagConstraints.EAST;
        gbc_repetirLabel.insets = new Insets(5, 5, 5, 5);
        gbc_repetirLabel.gridx = 3;
        gbc_repetirLabel.gridy = 3;
        JLabel repetirLabel = new JLabel("Repetir:");
        panelCentral.add(repetirLabel, gbc_repetirLabel);

        repetirContra = new JTextField();
        repetirContra.setColumns(10);
        GridBagConstraints gbc_repetirContra = new GridBagConstraints();
        gbc_repetirContra.gridwidth = 2;
        gbc_repetirContra.insets = new Insets(0, 0, 5, 0);
        gbc_repetirContra.fill = GridBagConstraints.HORIZONTAL;
        gbc_repetirContra.gridx = 4;
        gbc_repetirContra.gridy = 3;
        panelCentral.add(repetirContra, gbc_repetirContra);

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
        gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateChooser.insets = new Insets(5, 5, 5, 5);
        gbc_dateChooser.gridx = 1;
        gbc_dateChooser.gridy = 4;
        panelCentral.add(dateChooser, gbc_dateChooser);
        
        // Etiqueta y espacio para la imagen
        gbc_labelImagen = new GridBagConstraints();
        gbc_labelImagen.anchor = GridBagConstraints.EAST;
        gbc_labelImagen.insets = new Insets(5, 5, 5, 5);
        gbc_labelImagen.gridx = 3;
        gbc_labelImagen.gridy = 4;
        JLabel labelImagen = new JLabel("Imagen:");
        panelCentral.add(labelImagen, gbc_labelImagen);

        imagen = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.gridwidth = 2;
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 4;
        gbc_textField.gridy = 4;
        panelCentral.add(imagen, gbc_textField);
        imagen.setColumns(10);
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 4;
        gbc.gridy = 5;
        imagenLabel = new JLabel(new ImageIcon(VentanaRegistro.class.getResource("/imagenes/agregar-usuario.png")));
        panelCentral.add(imagenLabel, gbc);

        // Botón para cargar imagen
        JButton btnCargarImagen = new JButton("Cargar Imagen");
        btnCargarImagen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileChooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(file.getPath());
                    imagenLabel.setIcon(imageIcon); // Actualiza el JLabel con la nueva imagen
                }
            }
        });

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 4;
        gbc.gridy = 6;
        panelCentral.add(btnCargarImagen, gbc);

        // Etiqueta y campo de saludo
        gbc_5 = new GridBagConstraints();
        gbc_5.anchor = GridBagConstraints.WEST;
        gbc_5.insets = new Insets(5, 5, 5, 5);
        gbc_5.gridx = 0;
        gbc_5.gridy = 5;
        panelCentral.add(new JLabel("Estado:"), gbc_5);
        
        gbc_AreaEstado = new GridBagConstraints();
        gbc_AreaEstado.fill = GridBagConstraints.HORIZONTAL;
        gbc_AreaEstado.insets = new Insets(5, 5, 5, 5);
        gbc_AreaEstado.gridx = 1;
        gbc_AreaEstado.gridy = 5;
        
        JTextArea AreaEstado = new JTextArea(5, 20);
        AreaEstado.setWrapStyleWord(true);
        AreaEstado.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(AreaEstado);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        panelCentral.add(scrollPane, gbc_AreaEstado);
        
        // Agregar panel al marco
        frame.getContentPane().add(panelCentral, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 0, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 6;
        panelCentral.add(panel, gbc_panel);
        
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose(); // Cierra la ventana actual
                VentanaLogin.main(new String[]{}); // Abre la ventana de login	
            }
        });
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nombreField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "El campo de Nombre tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (apellidosField.getText().isBlank()){
                    JOptionPane.showMessageDialog(frame, "El campo de Apellido tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (telefono.getText().isBlank()){
                    JOptionPane.showMessageDialog(frame, "El campo de Telefono tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (repetirContra.getText().isBlank()){
                    JOptionPane.showMessageDialog(frame, "El campo de Contraseña tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (contraseña.getText().isBlank()){
                    JOptionPane.showMessageDialog(frame, "El campo de Repetir Contraseña tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (AreaEstado.getText().isBlank()) {
                    JOptionPane.showMessageDialog(frame, "El campo de Estado tiene que estar relleno", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!repetirContra.getText().equals(contraseña.getText())) {
                    JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden.", "No Contraseñas", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(btnAceptar);
        
        Component horizontalGlue = Box.createHorizontalGlue();
        panel.add(horizontalGlue);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(ev -> {
            frame.dispose(); // Cierra la ventana actual
            VentanaLogin.main(new String[]{}); // Abre la ventana de login
        });
        panel.add(btnCancelar);
        
    }

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        frame.setVisible(b);
    }
}