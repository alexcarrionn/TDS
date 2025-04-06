package appChat.Ventanas;

import javax.swing.*;
import controlador.AppChat;
import java.awt.*;

@SuppressWarnings("serial")
public class VentanaPremium extends JFrame {
    public VentanaPremium() { 
        // Crear el frame
        setTitle("Seleccionar Descuento Premium");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        getContentPane().setLayout(new GridLayout(3, 1, 10, 10));

        // Panel superior para el combo box
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaSeleccion = new JLabel("Selecciona un descuento:");
        String[] descuentos = {"Descuento Fecha", "Descuento Mensajes"};
        JComboBox<String> comboBoxDescuentos = new JComboBox<>(descuentos);
        panelSuperior.add(etiquetaSeleccion);
        panelSuperior.add(comboBoxDescuentos);

        // Panel central para la cantidad a pagar
        JPanel panelCentral = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCantidad = new JLabel("Cantidad a pagar: ");
        panelCentral.add(lblCantidad);
        

        // Panel inferior para los botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.addActionListener(e -> {
            String tipoDescuentoSeleccionado = (String) comboBoxDescuentos.getSelectedItem();
            
            
            boolean comprobacion = AppChat.getUnicaInstancia().hacerPremium(tipoDescuentoSeleccionado); 
            if (!comprobacion) {
            	 JOptionPane.showMessageDialog(this, "No cumple los requisitos para el descuento aplicado o ya es premium", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Actualizar el texto de la etiqueta en lugar de agregar una nueva
            AppChat.getUnicaInstancia().aplicarDescuento(tipoDescuentoSeleccionado);
            lblCantidad.setText("Cantidad a pagar: " + AppChat.getUnicaInstancia().getDescuento());
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        
        JButton btnDesactivar = new JButton("Desactivar");
        btnDesactivar.addActionListener(e-> {
        	boolean comprobacion = AppChat.getUnicaInstancia().desactivarPremium();
        	if (!comprobacion) {
        		JOptionPane.showMessageDialog(this,"No eres premium no puede desactivar la cuenta", "Error", JOptionPane.ERROR_MESSAGE);
        	}else {
        		JOptionPane.showMessageDialog(this,"Has desactivado Premium", "Information", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        panelInferior.add(btnDesactivar);

        panelInferior.add(botonAceptar);
        panelInferior.add(botonCancelar);

        // Agregar los paneles al frame
        getContentPane().add(panelSuperior);
        getContentPane().add(panelCentral);
        getContentPane().add(panelInferior);

        // Configurar visibilidad del frame
        setLocationRelativeTo(null);
    }
}
