package appChat.Ventanas;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.itextpdf.text.DocumentException;
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

	private JScrollPane scrollPanelEmojis;
	private JPanel panelEmojis;

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

		JButton btnExportarPDF = new JButton("");
		btnExportarPDF.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/enviar-mensaje.png")));
		btnExportarPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean exito;
				try {
					exito = AppChat.getUnicaInstancia().exportarPDF();
					if (exito) {
						JOptionPane.showMessageDialog(frame, "Exportación a PDF realizada con éxito.", "Información",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frame, "No se pudo exportar a PDF.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}

			}

		});
		panelBotones.add(btnExportarPDF);

		JButton btnBuscarMensajes = new JButton("");
		btnBuscarMensajes.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/lupa.png")));
		btnBuscarMensajes.addActionListener(ev -> {
			VentanaBuscar buscar = new VentanaBuscar();
			buscar.setVisible(true);
		});
		panelBotones.add(btnBuscarMensajes);

		JButton btnCrearGrupo = new JButton("Nuevo Grupo");
		btnCrearGrupo.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/nuevo-grupo.png")));
		btnCrearGrupo.addActionListener(ev -> crearGrupo());

		// Botón para crear un nuevo contacto
		JButton btnCrearContacto = new JButton("Nuevo Contacto");
		btnCrearContacto.setToolTipText("Crear nuevo contacto"); // Tooltip para mayor claridad
		btnCrearContacto.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/nuevo-contacto.png")));
		btnCrearContacto.addActionListener(ev -> crearContacto());

		JButton btnAnadirContacto = new JButton("Añadir contacto");
		btnAnadirContacto
				.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/nuevo-contacto-grupo.png")));
		btnAnadirContacto.addActionListener(ev -> anadirContacto());

		panelBotones.add(btnCrearContacto);
		panelBotones.add(btnCrearGrupo);
		panelBotones.add(btnAnadirContacto);

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
		
		// Aqui cogemos la imagen del usuario y comprobamos que este, en caso de no
		// tener se le asignará la de robohash
		String imagen = appchat.getUsuarioLogueado().getImagen();
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
			System.err.println("No se pudo cargar la imagen, estableciendo imagen por defecto.");
		}

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
				}
			}
		});
		
		// Boton para editar contacto no agregado
		listaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int index = listaContactos.locationToIndex(e.getPoint());
		        if (index >= 0) {
		            Rectangle bounds = listaContactos.getCellBounds(index, index);
		            Contacto contacto = listaContactos.getModel().getElementAt(index);

		            // Verificar si es un contacto inverso sin nombre
		            if (contacto instanceof ContactoIndividual && ((ContactoIndividual) contacto).isContactoInverso()) {
		                // Verificar si el clic fue en la celda
		                if (bounds.contains(e.getPoint())) {
		                    String nuevoNombre = JOptionPane.showInputDialog(VentanaPrincipal.this,
		                            "Introduce un nombre para el contacto:", "Editar Nombre",
		                            JOptionPane.PLAIN_MESSAGE);

		                    if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
		                        // Actualizar el nombre del contacto
		                        appchat.actualizarNombreContacto(contacto, nuevoNombre);
		                        actualizarListaContactos(); // Refrescar la lista de contactos
		                    }
		                }
		            }
		        }
		    }
		});

		// Agregar la lista al panel
		panelLista.add(new JScrollPane(listaContactos)); // Se recomienda usar JScrollPane para listas grandes

		cargarPanelEmojis();

		JPanel enviar = new JPanel();
		chatActual.add(enviar, BorderLayout.SOUTH);
		enviar.setLayout(new BoxLayout(enviar, BoxLayout.X_AXIS));

		JButton botonEmojis = new JButton("");
		botonEmojis.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagenes/diablo.png")));
		botonEmojis.addActionListener(ev -> abrirPanelEmojis());
		enviar.add(botonEmojis);

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

	// FUNCIONES AUXILIARES

	// Método privado que servira para carar el panel de los emoticonos
	private void cargarPanelEmojis() {
		// Crear el panel de emojis al inicializar la ventana (oculto por defecto)
		panelEmojis = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelEmojis.setBackground(Color.LIGHT_GRAY); // O usa tu constante CHAT_COLOR

		// Cargar los emojis una sola vez
		for (int i = 0; i <= BubbleText.MAXICONO; i++) {
			final int emojiId = i;
			JLabel labelIcono = new JLabel(BubbleText.getEmoji(i));
			labelIcono.setName(Integer.toString(i));
			labelIcono.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			labelIcono.setToolTipText("Emoji " + i);

			labelIcono.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					enviarMensajeEmoji(emojiId);
					mensaje.setText(""); // Limpia el campo de texto
				}
			});

			panelEmojis.add(labelIcono);
		}

		scrollPanelEmojis = new JScrollPane(panelEmojis);
		scrollPanelEmojis.setBorder(null);
		scrollPanelEmojis.setPreferredSize(new Dimension(400, 75));
		scrollPanelEmojis.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanelEmojis.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		// Inicialmente oculto
		scrollPanelEmojis.setVisible(false);

		// Añadir el panel justo encima del área de entrada (debajo del chat)
		contentPane.add(scrollPanelEmojis, BorderLayout.SOUTH);

	}

	// Método privado que te permitirá abrir el panel de emoticonos
	private void abrirPanelEmojis() {
		boolean visible = scrollPanelEmojis.isVisible();
		scrollPanelEmojis.setVisible(!visible);
		contentPane.revalidate();
		contentPane.repaint();
	}

	// Método privado que servirá como dialogo para crear el contacto al pulsar el
	// boton de Crear Contacto
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

	// Método privado que servirá para crear el grupo

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

		// Panel para la imagen con el path arriba y el botón debajo
		JPanel panelImagen = new JPanel();
		panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
		JTextField campoRutaImagen = new JTextField();
		campoRutaImagen.setEditable(false);
		JButton botonSeleccionarImagen = new JButton("Seleccionar imagen");

		botonSeleccionarImagen.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				campoRutaImagen.setText(file.getAbsolutePath());
			}
		});

		campoRutaImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
		botonSeleccionarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelImagen.add(campoRutaImagen);
		panelImagen.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre campo y botón
		panelImagen.add(botonSeleccionarImagen);

		Object[] mensaje = { "Nombre del grupo:", nombreGrupo, "Selecciona miembros:", scroll, "Imagen del grupo:",
				panelImagen };

		int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Nuevo Grupo", JOptionPane.OK_CANCEL_OPTION);

		if (opcion == JOptionPane.OK_OPTION) {
			String nombre = nombreGrupo.getText().trim();
			List<String> seleccionados = listaNombres.getSelectedValuesList();
			String rutaImagen = campoRutaImagen.getText().trim();

			if (nombre.isEmpty() || seleccionados.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Debes ingresar un nombre y seleccionar al menos un contacto.");
				return;
			}

			appchat.agregarGrupo(nombre, seleccionados, rutaImagen);
			actualizarListaContactos(); // Refresca la UI
		}
	}

	// Método privado que servirá para actualizar la lista de los contactos
	private void actualizarListaContactos() {
		List<Contacto> contactos = appchat.getContactosUsuarioActual();
		DefaultListModel<Contacto> modeloActualizado = new DefaultListModel<>();

		for (Contacto c : contactos) {
			modeloActualizado.addElement(c);
		}

		listaContactos.setModel(modeloActualizado);
	}

	// Mérodo privado que servirá para saber si un mensaje es un mensaje de texto o
	// un emoji
	private void comprobarEmojioTexto(String mensajeTexto) {
		if (esEntero(mensajeTexto)) {
			enviarMensajeEmoji(Integer.parseInt(mensajeTexto));
		} else {
			enviarMensajeTexto(mensajeTexto);
		}

	}

	// Método privado que servirá para enviar un mensaje de emoji
	private void enviarMensajeEmoji(int emoji) {
		if (appchat.getChatActual() == null)
			return;
		if (appchat.getChatActual() instanceof ContactoIndividual) {
			appchat.enviarMensajeEmoticonoContacto((ContactoIndividual) appchat.getChatActual(), emoji,
					TipoMensaje.ENVIADO);
		} else {
			appchat.enviarMensajeEmoticonoGrupo((Grupo) appchat.getChatActual(), emoji, TipoMensaje.ENVIADO);
		}

		Mensaje nuevoMensaje = new Mensaje(emoji, TipoMensaje.ENVIADO, LocalDateTime.now());
		BubbleText burbuja = crearBurbuja(nuevoMensaje);

		ChatBurbujas chatActual = chatsRecientes.get(appchat.getChatActual());
		if (chatActual != null) {
			chatActual.agregarBurbuja(burbuja);
			scrollBarChatBurbujas.getViewport().revalidate();
			scrollBarChatBurbujas.getViewport().repaint();
		}
	}

	// Método privado que servirña para enviar un mensaje de texto
	private void enviarMensajeTexto(String mensaje) {
		if (appchat.getChatActual() instanceof ContactoIndividual) {
			appchat.enviarMensajeTextoContacto((ContactoIndividual) appchat.getChatActual(), mensaje,
					TipoMensaje.ENVIADO);
		} else {
			appchat.enviarMensajeTextoGrupo((Grupo) appchat.getChatActual(), mensaje, TipoMensaje.ENVIADO);
		}
	}

	// Método privado que servirá para saber si el mensaje enviado es un entero o no
	private static boolean esEntero(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Método privado que servirá para poder cargar el chat del contacto pasado como
	// parámetro
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

		if (m.getTexto() == null) {
			return new BubbleText(chat, m.getEmoticono(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
		}
		return new BubbleText(chat, m.getTexto(), colorBurbuja, emisor, direccionMensaje, TAMANO_MENSAJE);
	}

	// Método privado que te permitirá añadir un contacto individual a un grupo en
	// concreto
	private void anadirContacto() {
		// Obtener solo nombres de contactos individuales
		List<Contacto> contactos = appchat.getContactosUsuarioActual();
		DefaultListModel<String> modelNombres = new DefaultListModel<>();

		for (Contacto c : contactos) {
			if (c instanceof ContactoIndividual) {
				modelNombres.addElement(c.getNombre());
			}
		}

		// Lista visual para seleccionar contacto
		JList<String> listaNombres = new JList<>(modelNombres);
		listaNombres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaNombres.setVisibleRowCount(8);
		JScrollPane scroll = new JScrollPane(listaNombres);

		// Obtener solo nombres de grupos
		DefaultListModel<String> nombreGrupos = new DefaultListModel<>();
		for (Contacto c : contactos) {
			if (c instanceof Grupo) {
				nombreGrupos.addElement(c.getNombre()); // ✅ Aquí estaba el fallo
			}
		}

		// Lista visual para seleccionar grupo
		JList<String> listaGrupos = new JList<>(nombreGrupos);
		listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaGrupos.setVisibleRowCount(8);
		JScrollPane scrollGrupos = new JScrollPane(listaGrupos);

		// Mostrar panel con ambas listas
		Object[] mensaje = { "Selecciona un contacto:", scroll, "Selecciona el grupo al que añadirlo:", scrollGrupos };

		int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Añadir contacto a grupo",
				JOptionPane.OK_CANCEL_OPTION);

		if (opcion == JOptionPane.OK_OPTION) {
			String nombreContacto = listaNombres.getSelectedValue();
			String nombreGrupo = listaGrupos.getSelectedValue();

			if (nombreContacto == null || nombreGrupo == null) {
				JOptionPane.showMessageDialog(this, "Debes seleccionar un contacto y un grupo.");
				return;
			}

			// Buscar el contacto y grupo seleccionados
			ContactoIndividual contactoIndividual = null;
			Grupo grupo = null;

			for (Contacto c : contactos) {
				if (c instanceof ContactoIndividual && c.getNombre().equals(nombreContacto)) {
					contactoIndividual = (ContactoIndividual) c;
				} else if (c instanceof Grupo && c.getNombre().equals(nombreGrupo)) {
					grupo = (Grupo) c;
				}
			}

			if (contactoIndividual != null && grupo != null) {
				grupo.agregarContacto(contactoIndividual);
				actualizarListaContactos();
			} else {
				JOptionPane.showMessageDialog(this, "No se pudo encontrar el contacto o el grupo.");
			}
		}
	}
}
