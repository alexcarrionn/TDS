package appChat.Ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaGrupo {
    private static DefaultListModel<String> contactosModel = new DefaultListModel<>();
    private static DefaultListModel<String> grupoModel = new DefaultListModel<>();
    private static JList<String> listaContactos;
    private static JList<String> listaGrupo;
    private JFrame frame;
    
    public VentanaGrupo() {
        frame = new JFrame("Gestión de Grupos");

        // Configurar la operación de cierre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Establecer el tamaño de la ventana
        frame.setSize(600, 400);
        GridBagLayout gridBagLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gridBagLayout);

        // Crear lista de contactos
        contactosModel.addElement("contacto1");
        contactosModel.addElement("contacto2");
        contactosModel.addElement("grupo1");
        contactosModel.addElement("contacto3");
        contactosModel.addElement("grupo2");

        listaContactos = new JList<>(contactosModel);
        JScrollPane scrollContactos = new JScrollPane(listaContactos);
        GridBagConstraints gbc_scrollContactos = new GridBagConstraints();
        gbc_scrollContactos.insets = new Insets(5, 5, 5, 5);
        gbc_scrollContactos.fill = GridBagConstraints.BOTH;
        gbc_scrollContactos.gridx = 2;
        gbc_scrollContactos.gridy = 2;
        gbc_scrollContactos.gridheight = 3;
        frame.getContentPane().add(scrollContactos, gbc_scrollContactos);

        // Crear lista de grupo
        listaGrupo = new JList<>(grupoModel);
        JScrollPane scrollGrupo = new JScrollPane(listaGrupo);
        GridBagConstraints gbc_scrollGrupo = new GridBagConstraints();
        gbc_scrollGrupo.insets = new Insets(5, 5, 5, 5);
        gbc_scrollGrupo.fill = GridBagConstraints.BOTH;
        gbc_scrollGrupo.gridx = 4;
        gbc_scrollGrupo.gridy = 2;
        gbc_scrollGrupo.gridheight = 3;
        frame.getContentPane().add(scrollGrupo, gbc_scrollGrupo);

        // Botón para mover contacto al grupo
        JButton btnAgregarAlGrupo = new JButton(">>");
        btnAgregarAlGrupo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedContact = listaContactos.getSelectedValue();
                if (selectedContact != null && !grupoModel.contains(selectedContact)) {
                    grupoModel.addElement(selectedContact);
                    contactosModel.removeElement(selectedContact);
                }
            }
        });
        GridBagConstraints gbc_btnAgregarAlGrupo = new GridBagConstraints();
        gbc_btnAgregarAlGrupo.insets = new Insets(5, 5, 5, 5);
        gbc_btnAgregarAlGrupo.gridx = 3;
        gbc_btnAgregarAlGrupo.gridy = 3;
        frame.getContentPane().add(btnAgregarAlGrupo, gbc_btnAgregarAlGrupo);

        // Botón para quitar contacto del grupo
        JButton btnQuitarDelGrupo = new JButton("<<");
        btnQuitarDelGrupo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedGroupContact = listaGrupo.getSelectedValue();
                if (selectedGroupContact != null) {
                    contactosModel.addElement(selectedGroupContact);
                    grupoModel.removeElement(selectedGroupContact);
                }
            }
        });
        GridBagConstraints gbc_btnQuitarDelGrupo = new GridBagConstraints();
        gbc_btnQuitarDelGrupo.insets = new Insets(5, 5, 5, 5);
        gbc_btnQuitarDelGrupo.gridx = 3;
        gbc_btnQuitarDelGrupo.gridy = 4;
        frame.getContentPane().add(btnQuitarDelGrupo, gbc_btnQuitarDelGrupo);

        // Botón para añadir contacto
        JButton btnAñadirContacto = new JButton("Añadir Contacto");
        btnAñadirContacto.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 JPanel panel = new JPanel(new GridLayout(0, 1));
        	        panel.add(new JLabel("Introduzca el nombre del contacto y su teléfono"));

        	        // Crear campos de texto
        	        JTextField nombreField = new JTextField();
        	        JTextField telefonoField = new JTextField();
        	        panel.add(new JLabel("Nombre:"));
        	        panel.add(nombreField);
        	        panel.add(new JLabel("Teléfono:"));
        	        panel.add(telefonoField);

        	        // Mostrar el cuadro de diálogo
        	        int result = JOptionPane.showConfirmDialog(frame, panel, "Alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        	        if (result == JOptionPane.OK_OPTION) {
        	            String nombre = nombreField.getText();
        	            String telefono = telefonoField.getText();

        	            // Verificar que los campos no estén vacíos
        	            if (!nombre.isEmpty() && !telefono.isEmpty()) {
        	                // Agregar el nuevo contacto a la lista de contactos
        	                contactosModel.addElement(nombre);
        	                /*else if(telefono not in Usuarios){
        	                 * JOptionPane.showMessageDialog(frame, "El Telefono indicado no existe", "Error", JOptionPane.ERROR_MESSAGE);
        	            }*/    
        	            } else {
        	                JOptionPane.showMessageDialog(frame, "Debe completar ambos campos", "Error", JOptionPane.ERROR_MESSAGE);
        	            }
        	        }
        	    }
        });
        
        GridBagConstraints gbc_btnAñadirContacto = new GridBagConstraints();
        gbc_btnAñadirContacto.insets = new Insets(5, 5, 5, 5);
        gbc_btnAñadirContacto.gridx = 2;
        gbc_btnAñadirContacto.gridy = 6;
        frame.getContentPane().add(btnAñadirContacto, gbc_btnAñadirContacto);

        // Botón para añadir grupo
        JButton btnAñadirGrupo = new JButton("Añadir Grupo");
       
        GridBagConstraints gbc_btnAñadirGrupo = new GridBagConstraints();
        gbc_btnAñadirGrupo.insets = new Insets(5, 5, 5, 5);
        gbc_btnAñadirGrupo.gridx = 4;
        gbc_btnAñadirGrupo.gridy = 6;
        frame.getContentPane().add(btnAñadirGrupo, gbc_btnAñadirGrupo);

        // Hacer visible la ventana
        frame.setVisible(true);
    }
    
    public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		frame.setVisible(b);
	}
}
