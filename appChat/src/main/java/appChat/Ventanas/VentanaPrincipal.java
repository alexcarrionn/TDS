package appChat.Ventanas;

import java.awt.EventQueue;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JTextField;

import controlador.AppChat;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Mensaje;
import tds.BubbleText;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
	private static final float TAMANO_MENSAJE = 12;
	private static final int ICON_SIZE_MINI = 12;
    private JPanel contentPane;
    private JTextField mensaje;
    static VentanaPrincipal frame;
    private AppChat appchat;
    private JList<Contacto> listaContactos;
    private ChatBurbujas chat;
    
	private Map<Contacto, ChatBurbujas> chatsRecientes;
	private JScrollPane scrollBarChatBurbujas;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new VentanaPrincipal();
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
    	//Llamamos a la instancia del controlador
    	appchat=AppChat.getUnicaInstancia();
    	
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
        comboUsuarioReceptor.setModel(new DefaultComboBoxModel<String>(new String[] { "Contacto Javier", "Contacto Ana" }));
        panelBotones.add(comboUsuarioReceptor);

        JButton btnEnviar = new JButton("");
        btnEnviar.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/enviar-mensaje.png")));
        panelBotones.add(btnEnviar);

        JButton btnBuscarMensajes = new JButton("");
        btnBuscarMensajes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/lupa.png")));
        btnBuscarMensajes.addActionListener(ev -> {
            VentanaBuscar buscar = new VentanaBuscar();
            buscar.setVisible(true);
        });
        panelBotones.add(btnBuscarMensajes);

        JButton btnContactos = new JButton("Contactos");
        btnContactos.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/reunion.png")));
        btnContactos.addActionListener(ev -> {
            VentanaGrupo grupo = new VentanaGrupo();
            grupo.setVisible(true);
        });
        panelBotones.add(btnContactos);

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
        
        JPanel chatActual = new JPanel();
        contentPane.add(chatActual, BorderLayout.CENTER);
        chatActual.setLayout(new BorderLayout(0, 0));

        JPanel chat = new JPanel();
        chatActual.add(chat, BorderLayout.CENTER);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        
		// Se extraen los contactos del usuario
		List<Contacto> contactos = appchat.getContactosUsuarioActual();
		
		// Crear una lista genérica de Contacto
		JList<ContactoIndividual> listaContactos = new JList<>();
		
		listaContactos.setBorder(null);
		listaContactos.setCellRenderer(new ContactoListCellRenderer());
		listaContactos.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Contacto contactoActual = listaContactos.getSelectedValue();
				if (contactoActual != null) {
					cargarChat(contactoActual);
					appchat.setChatActual(contactoActual);
					labelUsuarioActual.setText(contactoActual.getNombre());
					labelImagenUsuarioActual.setIcon(resizeIcon(contactoActual.getFoto(), ICON_SIZE_MINI));
				}
			}

		});

        panelLista.add(listaContactos);

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
                // Acción para enviar mensaje
                String mensajeTexto = mensaje.getText();
                if (!mensajeTexto.isBlank()) {
                    // Aquí puedes agregar la lógica para enviar el mensaje
                    // y actualizar la interfaz según sea necesario.
                }
            }
        });
        enviar.add(botonEnviarMensaje);
    }
    
    //FUNCIONES AUXILIARES
    

	private Icon resizeIcon(String foto, int iconSizeMini) {
		// TODO Auto-generated method stub
		return null;
	}

	private void cargarChat(Contacto contacto) {
        if (contacto == null) {
            return; // Salir si el contacto es nulo
        }

        // Obtener el chat asociado al contacto
        chat = chatsRecientes.get(contacto);

        if (chat == null) {
            // Crear un nuevo panel de chat si no existe
            chat = crearNuevoChat();
            scrollBarChatBurbujas.setViewportView(chat);

            // Añadir burbujas de mensajes al chat
            appchat.getMensajes(contacto).forEach(m -> chat.add(crearBurbuja(m)));

            // Guardar el nuevo chat en la caché
            chatsRecientes.put(contacto, chat);
        } else {
            // Mostrar el chat existente
            configurarChatExistente(chat);
            scrollBarChatBurbujas.setViewportView(chat);
        }
    }

    // Método para crear un nuevo chat
    private ChatBurbujas crearNuevoChat() {
        ChatBurbujas nuevoChat = new ChatBurbujas();
        nuevoChat.setBackground(Color.pink);
        nuevoChat.setLayout(new BoxLayout(nuevoChat, BoxLayout.Y_AXIS));
        nuevoChat.setSize(400, 700);
        return nuevoChat;
    }

    // Método para configurar un chat existente
    private void configurarChatExistente(ChatBurbujas chat) {
        chat.setBackground(Color.pink);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setSize(400, 700);
        scrollBarChatBurbujas.getViewport().setBackground(Color.pink);
    }

    // Método para crear una burbuja de mensaje
    private BubbleText crearBurbuja(Mensaje m) {
        String emisor;
        int direccionMensaje;
        Color colorBurbuja;

        if (m.getEmisor().equals(appchat.getUsuarioLogueado())) {
            colorBurbuja = Color.GREEN;
            emisor = "You";
            direccionMensaje = BubbleText.SENT;
        } else {
            colorBurbuja = Color.DARK_GRAY;
            emisor = m.getEmisor().getNombre();
            direccionMensaje = BubbleText.RECEIVED;
        }

        if (m.getTexto().isEmpty()) {
            return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
        }
        return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
    }

}
