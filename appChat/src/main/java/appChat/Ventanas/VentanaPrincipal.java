package appChat.Ventanas;

import java.awt.Image;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import javax.swing.JFileChooser;
import javax.swing.DefaultListModel;
//import javax.swing.Icon;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import controlador.AppChat;
import modelo.Contacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.TipoMensaje;
import tds.BubbleText;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final float TAMANO_MENSAJE = 12;
	private JPanel contentPane;
	private JTextField mensaje;
	static VentanaPrincipal frame;
	private AppChat appchat;
	private ChatBurbujas chat;
	private JList<Contacto> listaContactos;

	private Map<Contacto, ChatBurbujas> chatsRecientes;
	private JScrollPane scrollBarChatBurbujas;

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		// Llamamos a la instancia del controlador
		appchat = AppChat.getUnicaInstancia();
		this.chatsRecientes = new HashMap<>();
		scrollBarChatBurbujas = new JScrollPane();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 852, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelBotones = new JPanel();
		contentPane.add(panelBotones, BorderLayout.NORTH);
		panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

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

		JButton btnContactos = new JButton("Nuevo Grupo");
		btnContactos.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/nuevo-grupo.png")));
		btnContactos.addActionListener(ev -> crearGrupo());

		// Botón para crear un nuevo contacto
		JButton btnCrearContacto = new JButton("Nuevo Contacto");
		btnCrearContacto.setToolTipText("Crear nuevo contacto"); // Tooltip para mayor claridad
		btnCrearContacto.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/nuevo-contacto.png")));
		btnCrearContacto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Llamar a la función crearContacto()
				crearContacto();
			}
		});
		panelBotones.add(btnCrearContacto);
		panelBotones.add(btnContactos);

		/*
		 * JLabel labelImagenUsuarioActual = new JLabel(""); String imagen =
		 * appchat.getUsuarioLogueado().getImagen(); if (imagen == null ||
		 * imagen.isEmpty()) { // Si no hay imagen, usa RoboHash con el nombre del
		 * usuario try { imagen = "https://robohash.org/" +
		 * appchat.getUsuarioLogueado().getNombre(); // Convertir la URL en un objeto
		 * ImageIcon URL imageUrl = new URL(imagen); ImageIcon imageIcon = new
		 * ImageIcon(imageUrl); Image image = imageIcon.getImage().getScaledInstance(50,
		 * 50, Image.SCALE_AREA_AVERAGING); ImageIcon imageResized = new
		 * ImageIcon(image); labelImagenUsuarioActual.setIcon(imageResized); } catch
		 * (MalformedURLException e) { e.printStackTrace(); // Manejar el caso de error
		 * en la URL, puedes poner una imagen predeterminada aquí. } } else { // Si hay
		 * imagen, usa la imagen del usuario ImageIcon imageIcon = new
		 * ImageIcon(imagen); Image image = imageIcon.getImage().getScaledInstance(50,
		 * 50, Image.SCALE_AREA_AVERAGING); ImageIcon imageResized = new
		 * ImageIcon(image); labelImagenUsuarioActual.setIcon(imageResized); }
		 * 
		 * panelBotones.add(labelImagenUsuarioActual);
		 */

		/*
		 * JLabel labelImagenUsuarioActual = new JLabel(""); ImageIcon imageIcon = new
		 * ImageIcon(appchat.getUsuarioLogueado().getImagen()); Image image =
		 * imageIcon.getImage().getScaledInstance(50,50, Image.SCALE_AREA_AVERAGING);
		 * ImageIcon imageResized= new ImageIcon(image);
		 * labelImagenUsuarioActual.setIcon(imageResized);
		 * panelBotones.add(labelImagenUsuarioActual);
		 */

		JButton botonPremium = new JButton("Premium");
		botonPremium.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/calidad-premium.png")));
		botonPremium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mostrar la ventana premium, independientemente del estado
				VentanaPremium ventana = new VentanaPremium();
				ventana.setVisible(true);
			}
		});

		// Agregar el botón al panel
		panelBotones.add(botonPremium);

		Component horizontalGlue = Box.createHorizontalGlue();
		panelBotones.add(horizontalGlue);

		JLabel labelUsuarioActual = new JLabel(appchat.getUsuarioLogueado().getNombre());
		panelBotones.add(labelUsuarioActual);
		
		//TODO Refactorizar
		//Aqui cogemos la imagen del usuario y comprobamos que este, en caso de no tener se le asignará la de robohash
		String imagen = appchat.getUsuarioLogueado().getImagen();
		if (imagen == null || imagen.isEmpty()) {
			imagen = "https://robohash.org/" + appchat.getUsuarioLogueado().getNombre();
		}

		ImageIcon imageIcon = null;

		// Verificar si la imagen es una URL o un archivo local
		if (imagen.startsWith("http://") || imagen.startsWith("https://")) {
			try {
				// Cargar imagen desde URL externa
				imageIcon = new ImageIcon(new java.net.URL(imagen));
			} catch (Exception e) {
				System.err.println("No se pudo cargar la imagen desde la URL: " + imagen);
				e.printStackTrace();
			}
		} else {
			// Cargar imagen desde archivo local
			File file = new File(imagen);
			if (file.exists()) {
				imageIcon = new ImageIcon(file.getAbsolutePath());
			} else {
				System.err.println("La imagen no existe en la ruta: " + imagen);
			}
		}

		// Si la imagen se carga correctamente, redimensionarla
		if (imageIcon != null) {
			Image image = imageIcon.getImage().getScaledInstance(45, 45, Image.SCALE_AREA_AVERAGING);
			ImageIcon imageResized = new ImageIcon(image);

			// Crear el botón con la imagen redimensionada
			JButton botonImagenUsuarioActual = new JButton();
			botonImagenUsuarioActual.setIcon(imageResized);
			panelBotones.add(botonImagenUsuarioActual);

			// Acción para cambiar la foto de perfil
			botonImagenUsuarioActual.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Crear un JFileChooser para seleccionar una nueva imagen
					JFileChooser fileChooser = new JFileChooser();

					// Filtrar solo imágenes (opcional)
					fileChooser.setFileFilter(
							new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "gif"));

					// Mostrar el diálogo para seleccionar el archivo
					int result = fileChooser.showOpenDialog(null);

					// Si el usuario selecciona un archivo
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();

						// Crear un ImageIcon con la imagen seleccionada
						ImageIcon selectedImageIcon = new ImageIcon(selectedFile.getAbsolutePath());
						Image selectedImage = selectedImageIcon.getImage().getScaledInstance(50, 50,
								Image.SCALE_AREA_AVERAGING);
						ImageIcon resizedImageIcon = new ImageIcon(selectedImage);

						// Establecer la imagen en el botón
						botonImagenUsuarioActual.setIcon(resizedImageIcon);

						// Actualizar la foto en la aplicación
						appchat.actualizarFoto(selectedFile.getAbsolutePath());
					}
				}
			});
		} else {
			// Si no se pudo cargar la imagen, puedes establecer una imagen por defecto
			System.err.println("No se pudo cargar la imagen, estableciendo imagen por defecto.");
			// Aquí puedes agregar código para mostrar una imagen por defecto
		}

		/*
		 * String imagen = appchat.getUsuarioLogueado().getImagen(); if (imagen == null
		 * || imagen.isEmpty()) { imagen =
		 * "\TDS\\appChat\\src\\main\\java\\imagenes\\contacto.png"; } ImageIcon
		 * imageIcon = new ImageIcon(imagen); Image image =
		 * imageIcon.getImage().getScaledInstance(45, 45, Image.SCALE_AREA_AVERAGING);
		 * ImageIcon imageResized = new ImageIcon(image);
		 * 
		 * JButton botonImagenUsuarioActual = new JButton();
		 * botonImagenUsuarioActual.setIcon(imageResized);
		 * panelBotones.add(botonImagenUsuarioActual);
		 * 
		 * //hacemos click en el boton para que el usuario
		 * botonImagenUsuarioActual.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // Crear un
		 * JFileChooser para seleccionar la imagen JFileChooser fileChooser = new
		 * JFileChooser();
		 * 
		 * // Filtrar solo imágenes (opcional) fileChooser.setFileFilter(new
		 * javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png",
		 * "gif"));
		 * 
		 * // Mostrar el diálogo para seleccionar el archivo int result =
		 * fileChooser.showOpenDialog(null);
		 * 
		 * // Si el usuario selecciona un archivo if (result ==
		 * JFileChooser.APPROVE_OPTION) { File selectedFile =
		 * fileChooser.getSelectedFile();
		 * 
		 * // Crear un ImageIcon con la imagen seleccionada ImageIcon selectedImageIcon
		 * = new ImageIcon(selectedFile.getAbsolutePath()); Image selectedImage =
		 * selectedImageIcon.getImage().getScaledInstance(50, 50,
		 * Image.SCALE_AREA_AVERAGING); ImageIcon resizedImageIcon = new
		 * ImageIcon(selectedImage);
		 * 
		 * // Establecer la imagen en el botón
		 * botonImagenUsuarioActual.setIcon(resizedImageIcon);
		 * appchat.actualizarFoto(selectedFile.getAbsolutePath()); }} });
		 */

		JPanel panelLista = new JPanel();
		contentPane.add(panelLista, BorderLayout.WEST);
		panelLista.setLayout(new BorderLayout(0, 0));

		JList<Mensaje> listaChatsRecientes = new JList<Mensaje>();
		listaChatsRecientes.setCellRenderer(new MensajeCellRender());
		List<Mensaje> mensajes = AppChat.getUnicaInstancia().obtenerMensajesReMensaje();
		DefaultListModel<Mensaje> modelo = new DefaultListModel<Mensaje>();
		for (Mensaje men : mensajes) {
			modelo.addElement(men);
		}
		listaChatsRecientes.setModel(modelo);

		panelLista.add(new JScrollPane(listaChatsRecientes));

		JPanel chatActual = new JPanel();
		contentPane.add(chatActual, BorderLayout.CENTER);
		chatActual.setLayout(new BorderLayout(0, 0));
		chatActual.add(scrollBarChatBurbujas, BorderLayout.CENTER);

		// Obtener la lista de contactos del usuario
		List<Contacto> contactos = appchat.getContactosUsuarioActual();

		// Convertimos la lista de Contacto a ContactoIndividual si es necesario
		DefaultListModel<Contacto> modeloLista = new DefaultListModel<>();

		for (Contacto c : contactos) {
			modeloLista.addElement(c);
		}

		// Crear la JList con el modelo
		listaContactos = new JList<>(modeloLista);
		listaContactos.setBorder(null);
		listaContactos.setCellRenderer(new ContactoListCellRenderer());

		listaContactos.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Contacto contactoActual = listaContactos.getSelectedValue();
				if (contactoActual != null) {
					cargarChat(contactoActual);
					appchat.setChatActual(contactoActual);
					// botonImagenUsuarioActual.setIcon(resizeIcon(contactoActual.getFoto(),
					// ICON_SIZE_MINI));
				}
			}
		});

		// Agregar la lista al panel
		panelLista.add(new JScrollPane(listaContactos)); // Se recomienda usar JScrollPane para listas grandes

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
				String mensajeTexto = mensaje.getText().trim();
				if (!mensajeTexto.isEmpty()) {
					// Obtener contacto actual y enviar mensaje
					Contacto contactoActual = appchat.getChatActual();

					// Comprobar y ver si el mensaje es un emoticono o un texto y enviar el mensaje
					comprobarEmojioTexto(mensajeTexto);

					// Crear burbuja y añadirla al chat
					Mensaje nuevoMensaje = new Mensaje(mensajeTexto, TipoMensaje.ENVIADO, LocalDateTime.now());
					BubbleText burbuja = crearBurbuja(nuevoMensaje);

					// Obtener el chat actual y actualizar la UI
					ChatBurbujas chatActual = chatsRecientes.get(contactoActual);
					if (chatActual != null) {
						chatActual.agregarBurbuja(burbuja); // Usa el método de ChatBurbujas
						scrollBarChatBurbujas.getViewport().revalidate();
						scrollBarChatBurbujas.getViewport().repaint();
					}

					// Limpiar campo de texto
					mensaje.setText("");
				}
			}
		});
		enviar.add(botonEnviarMensaje);
	}

	// Dialogo para crear el contacto al pulsar el boton de Crear Contacto
	private ContactoIndividual crearContacto() {
		JTextField nombreContacto = new JTextField();
		JTextField telefonoContacto = new JTextField();

		Object[] mensaje = { "Nombre", nombreContacto, "Teléfono", telefonoContacto };

		int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Contacto", JOptionPane.OK_CANCEL_OPTION);
		if (opcion != JOptionPane.OK_OPTION) {
			return null; // Se pulsó cancelar
		}

		String nombre = nombreContacto.getText();
		String telefono = telefonoContacto.getText();

		if (nombre.isEmpty() || telefono.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Uno de las entradas está vacía", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (appchat.validarContacto(telefono)) {
			JOptionPane.showMessageDialog(this, "Ya existe un contacto con ese número");
			return null;
		}

		ContactoIndividual nuevoContacto = appchat.agregarContacto(nombre, telefono);
		actualizarListaContactos();
		return nuevoContacto;
	}
	
	private void crearGrupo() {
	    JTextField nombreGrupo = new JTextField();

	    // Obtener solo nombres de contactos individuales
	    List<Contacto> contactos = appchat.getContactosUsuarioActual();
	    DefaultListModel<String> modelNombres = new DefaultListModel<>();

	    for (Contacto c : contactos) {
	        if (c instanceof ContactoIndividual) {
	            modelNombres.addElement(c.getNombre());
	        }
	    }

	    JList<String> listaNombres = new JList<>(modelNombres);
	    listaNombres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    listaNombres.setVisibleRowCount(8);
	    JScrollPane scroll = new JScrollPane(listaNombres);

	    Object[] mensaje = {
	        "Nombre del grupo:", nombreGrupo,
	        "Selecciona miembros:", scroll
	    };

	    int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Grupo", JOptionPane.OK_CANCEL_OPTION);

	    if (opcion == JOptionPane.OK_OPTION) {
	        String nombre = nombreGrupo.getText().trim();
	        List<String> seleccionados = listaNombres.getSelectedValuesList();

	        if (nombre.isEmpty() || seleccionados.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre y seleccionar al menos un contacto.");
	            return;
	        }

	        appchat.agregarGrupo(nombre, seleccionados);
	        actualizarListaContactos(); // Refresca la UI
	    }
	}


	// FUNCIONES AUXILIARES

	private void actualizarListaContactos() {
		List<Contacto> contactos = appchat.getContactosUsuarioActual();
		DefaultListModel<Contacto> modeloActualizado = new DefaultListModel<>();

		for (Contacto c : contactos) {
			modeloActualizado.addElement(c);
		}

		listaContactos.setModel(modeloActualizado);
	}

	private void comprobarEmojioTexto(String mensajeTexto) {
		if (esEntero(mensajeTexto)) {
			enviarMensajeEmoji(Integer.parseInt(mensajeTexto));
		} else {
			enviarMensajeTexto(mensajeTexto);
		}

	}

	private void enviarMensajeEmoji(int emoji) {
		if (appchat.getChatActual() instanceof ContactoIndividual) {
			appchat.enviarMensajeEmoticonoContacto((ContactoIndividual) appchat.getChatActual(), emoji,
					TipoMensaje.ENVIADO);
		} else {
			appchat.enviarMensajeEmoticonoGrupo((Grupo) appchat.getChatActual(), emoji, TipoMensaje.ENVIADO);
		}
	}

	private void enviarMensajeTexto(String mensaje) {
		if (appchat.getChatActual() instanceof ContactoIndividual) {
			appchat.enviarMensajeTextoContacto((ContactoIndividual) appchat.getChatActual(), mensaje,
					TipoMensaje.ENVIADO);
		} else {
			appchat.enviarMensajeTextoGrupo((Grupo) appchat.getChatActual(), mensaje, TipoMensaje.ENVIADO);
		}
	}

	private static boolean esEntero(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/*
	 * private Icon resizeIcon(String foto, int iconSizeMini) { try { Image img =
	 * new ImageIcon(foto).getImage(); // Cargar la imagen Image resizedImg =
	 * img.getScaledInstance(iconSizeMini, iconSizeMini, Image.SCALE_SMOOTH); return
	 * new ImageIcon(resizedImg); // Devolver la imagen redimensionada como Icon }
	 * catch (Exception e) { e.printStackTrace(); return null; // Si falla, devuelve
	 * null } }
	 */

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
			System.out.println(appchat.getMensajes(contacto));

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
		nuevoChat.setBackground(Color.LIGHT_GRAY);
		nuevoChat.setLayout(new BoxLayout(nuevoChat, BoxLayout.Y_AXIS));
		nuevoChat.setSize(400, 700);
		return nuevoChat;
	}

	// Método para configurar un chat existente
	private void configurarChatExistente(ChatBurbujas chat) {
		// Configuración básica
		chat.setBackground(Color.LIGHT_GRAY);
		chat.setOpaque(true); // Asegura que el fondo se pinte

		// Layout dinámico
		chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));

		// Tamaño preferido (en lugar de setSize)
		chat.setSize(400, 700);

		// Configuración del scroll
		scrollBarChatBurbujas.getViewport().setBackground(Color.PINK);
		scrollBarChatBurbujas.getViewport().setOpaque(true);

		// Actualización visual
		chat.revalidate();
		chat.repaint();
	}

	// Método para crear una burbuja de mensaje
	private BubbleText crearBurbuja(Mensaje m) {
		String emisor;
		int direccionMensaje;
		Color colorBurbuja;

		if (m.getTipo().equals(TipoMensaje.ENVIADO)) {
			colorBurbuja = Color.GREEN;
			emisor = "You";
			direccionMensaje = BubbleText.SENT;
		} else {
			colorBurbuja = Color.BLUE;
			emisor = "Other";
			direccionMensaje = BubbleText.RECEIVED;
		}

		if (m.getTexto().isEmpty()) {
			return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
		}
		return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
	}

}
