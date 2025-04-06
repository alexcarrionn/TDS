package appChat.Ventanas;

import javax.swing.*;
import javax.swing.border.*;

import controlador.AppChat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import modelo.Mensaje;
import modelo.TipoMensaje;

public class VentanaBuscar {

    private JFrame frame;
    private JTextArea mensajesArea;
    private AppChat appchat= AppChat.getUnicaInstancia();

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
        JTextField tipoField = new JTextField();
        addPlaceholder(tipoField, "Tipo");
        panelCampos.add(tipoField);
        panelCampos.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre campos

     // Campo de fecha desde
        JTextField desdeField = new JTextField();
        addPlaceholder(desdeField, "Desde (YYYY-MM-DDTHH:MM)");
        panelCampos.add(desdeField);
        panelCampos.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre campos

        // Campo de fecha hasta
        JTextField hastaField = new JTextField();
        addPlaceholder(hastaField, "Hasta (YYYY-MM-DDTHH:MM)");
        panelCampos.add(hastaField);
        panelCampos.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre campos


        // Botón de buscar
        JButton buscarButton = new JButton("Buscar");
        buscarButton.setIcon(new ImageIcon(VentanaBuscar.class.getResource("/imagenes/buscar_boton.png")));
        buscarButton.setSelectedIcon(null);
        panelCampos.add(buscarButton);
        
        buscarButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String texto = textoField.getText().trim();
        		LocalDateTime desde = null;
        		LocalDateTime hasta = null;
        		TipoMensaje tipo = null;

        		try {
        		    // --- Tipo de mensaje ---
        		    String tipoTexto = tipoField.getText().trim();
        		    if (!tipoTexto.isEmpty() && !tipoTexto.equalsIgnoreCase("Tipo")) {
        		        tipo = TipoMensaje.valueOf(tipoTexto.toUpperCase()); // Asegura que coincida con los enums
        		    }

        		    // --- Fecha Desde ---
        		    String desdeTexto = desdeField.getText().trim();
        		    if (!desdeTexto.isEmpty() && !desdeTexto.equalsIgnoreCase("Desde (YYYY-MM-DDTHH:MM)")) {
        		        desde = LocalDateTime.parse(desdeTexto);
        		    }

        		    // --- Fecha Hasta ---
        		    String hastaTexto = hastaField.getText().trim();
        		    if (!hastaTexto.isEmpty() && !hastaTexto.equalsIgnoreCase("Hasta (YYYY-MM-DDTHH:MM)")) {
        		        hasta = LocalDateTime.parse(hastaTexto);
        		    }

        		    // Llamada a la función de búsqueda
        		    buscarMensaje(texto, tipo, desde, hasta);

        		} catch (IllegalArgumentException ex) {
        		    JOptionPane.showMessageDialog(frame, "Tipo de mensaje no válido. Usa uno de: " + Arrays.toString(TipoMensaje.values()), "Error", JOptionPane.ERROR_MESSAGE);
        		} catch (DateTimeParseException ex) {
        		    JOptionPane.showMessageDialog(frame, "Formato de fecha inválido. Usa el formato: YYYY-MM-DDTHH:MM (Ej: 2025-04-06T15:30)", "Error", JOptionPane.ERROR_MESSAGE);
        		}
        	}});
        

        // Añadir ambos paneles (texto y campos) al panel superior
        panelSuperior.add(panelTexto);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre panelTexto y panelCampos
        panelSuperior.add(panelCampos);

        // Crear el panel inferior con borde y área de desplazamiento
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Área de texto dentro de un JScrollPane, que se agrandará con la ventana
        mensajesArea = new JTextArea();
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

    private void buscarMensaje(String texto,TipoMensaje tipo, LocalDateTime desde, LocalDateTime hasta) {
    	mensajesArea.removeAll();
    
    	List<Mensaje> mensajes = appchat.buscarMensajes(texto,tipo,desde, hasta);
    	
     	for(Mensaje m: mensajes) {
     		mensajesArea.add(CrearMensaje(m));
     	}
     	
     	mensajesArea.revalidate();
     	mensajesArea.repaint();
	}
    
    //Metodo para crear el Mensaje que se verá en el Panel de los mensajes
    private JPanel CrearMensaje(Mensaje m) {
        // Crear un panel para el mensaje
        JPanel panelMensaje = new JPanel();
        panelMensaje.setLayout(new BoxLayout(panelMensaje, BoxLayout.Y_AXIS)); // Organiza los elementos verticalmente
        panelMensaje.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Añadir un poco de espacio al borde

        // Mostrar el texto del mensaje
        JLabel textoLabel = new JLabel(m.getTexto());
        textoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear a la izquierda
        panelMensaje.add(textoLabel);

        // Mostrar la hora del mensaje
        JLabel horaLabel = new JLabel("Hora: " + m.getHora().toLocalTime().toString());
        horaLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        horaLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear a la izquierda
        panelMensaje.add(horaLabel);

        // Si el mensaje tiene emoticono, lo mostramos también
        if (m.getEmoticono() != 0) {
            JLabel emoticonoLabel = new JLabel(new ImageIcon("ruta/a/tus/emoticonos/" + m.getEmoticono() + ".png"));
            emoticonoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinear a la izquierda
            panelMensaje.add(emoticonoLabel);
        }

        // Agregar un separador si lo deseas entre mensajes
        panelMensaje.add(Box.createRigidArea(new Dimension(0, 10)));

        return panelMensaje;
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
