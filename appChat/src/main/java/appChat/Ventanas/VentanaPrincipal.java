package appChat.Ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.AppChat;
import modelo.Mensaje;

import javax.swing.JLabel;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTextField;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField mensaje;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 852, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelBotones = new JPanel();
		contentPane.add(panelBotones, BorderLayout.NORTH);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
		
		JComboBox<String> comboUsuarioReceptor = new JComboBox<String>();
		comboUsuarioReceptor.setEditable(true);
		comboUsuarioReceptor.setModel(new DefaultComboBoxModel<String>(new String[] {"Contacto Javier", "Contacto Ana"}));
		panelBotones.add(comboUsuarioReceptor);
		
		JButton btnEnviar = new JButton("");
		btnEnviar.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/enviar-mensaje.png")));
		panelBotones.add(btnEnviar);
		
		JButton btnBuscarMensajes = new JButton("");
		btnBuscarMensajes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/lupa.png")));
		panelBotones.add(btnBuscarMensajes);
		
		JButton contactos = new JButton("Contactos");
		contactos.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/reunion.png")));
		contactos.addActionListener(ev -> {
			VentanaGrupo grupo = new VentanaGrupo(); 
			grupo.setVisible(true);
		}); 
		panelBotones.add(contactos);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panelBotones.add(horizontalGlue);
		
		JButton premium = new JButton("Premium");
		premium.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/calidad-premium.png")));
		panelBotones.add(premium);
		
		JLabel labelUsuarioActual = new JLabel("Usuario Actual");
		panelBotones.add(labelUsuarioActual);
		
		JLabel labelImagenUsuarioActual = new JLabel("");
		labelImagenUsuarioActual.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/avatar.png")));
		panelBotones.add(labelImagenUsuarioActual);
		
		JPanel panelLista = new JPanel();
		contentPane.add(panelLista, BorderLayout.WEST);
		panelLista.setLayout(new BorderLayout(0, 0));
		
		JList<Mensaje> listaChatsRecientes = new JList<Mensaje>();
		listaChatsRecientes.setCellRenderer(new MensajeCellRender()); 
		List<Mensaje> mensajes = AppChat.ObtenerMensajesReMensaje(); 
		DefaultListModel<Mensaje> modelo = new DefaultListModel<Mensaje>(); 
		for (Mensaje men : mensajes) {
			modelo.addElement(men); 
		}
		listaChatsRecientes.setModel(modelo); 
		
		panelLista.add(listaChatsRecientes);
		
		JPanel chatActual = new JPanel();
		contentPane.add(chatActual, BorderLayout.CENTER);
		chatActual.setLayout(new BorderLayout(0, 0));
		
		JPanel chat = new JPanel();
		chatActual.add(chat, BorderLayout.CENTER);
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
		
		JPanel enviar = new JPanel();
		chatActual.add(enviar, BorderLayout.SOUTH);
		enviar.setLayout(new BoxLayout(enviar, BoxLayout.X_AXIS));
		
		mensaje = new JTextField();
		enviar.add(mensaje);
		mensaje.setColumns(10);
		
		JButton botonEnviarMensaje = new JButton("");
		botonEnviarMensaje.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/enviar-mensaje.png")));
		botonEnviarMensaje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		enviar.add(botonEnviarMensaje);
		
		
		//DONDE SE HAGA EL CHAT
		//AppChat.getMensajes(); 
		
	}

}
