package appChat.Ventanas; 

import javax.swing.*;

import controlador.AppChat;

import java.awt.*;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class VentanaGrupo extends JFrame {
    private DefaultListModel<String> mainListModel;
    private DefaultListModel<String> groupCreationListModel;
    private JList<String> mainList;
    private JList<String> groupCreationList;
    private Map<String, DefaultListModel<String>> groupsMap;
    private final AppChat controlador=AppChat.getUnicaInstancia();
    
    public VentanaGrupo() {
    	
        setTitle("Gestor de Contactos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(653, 378);
        getContentPane().setLayout(new BorderLayout());

        groupsMap = new HashMap<>();

        // Panel principal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Lista principal de contactos y grupos
        mainListModel = new DefaultListModel<>();
        mainList = new JList<>(mainListModel);
        JScrollPane mainListScrollPane = new JScrollPane(mainList);

        // Panel de lista principal
        JPanel mainListPanel = new JPanel(new BorderLayout());
        mainListPanel.setBorder(BorderFactory.createTitledBorder("Lista de Contactos"));
        mainListPanel.add(mainListScrollPane, BorderLayout.CENTER);

        // Botón Añadir Contacto
        JButton addContactButton = new JButton("Añadir Contacto");
        addContactButton.addActionListener(e -> addContact());
        mainListPanel.add(addContactButton, BorderLayout.SOUTH);

        // Lista para crear un nuevo grupo
        groupCreationListModel = new DefaultListModel<>();
        groupCreationList = new JList<>(groupCreationListModel);
        JScrollPane groupCreationListScrollPane = new JScrollPane(groupCreationList);

        // Panel para la lista de creación de grupos
        JPanel groupCreationPanel = new JPanel(new BorderLayout());
        groupCreationPanel.setBorder(BorderFactory.createTitledBorder("Grupo"));
        groupCreationPanel.add(groupCreationListScrollPane, BorderLayout.CENTER);

        // Botón para crear un grupo
        JButton createGroupButton = new JButton("Crear Grupo");
        createGroupButton.addActionListener(e -> createGroup());
        groupCreationPanel.add(createGroupButton, BorderLayout.SOUTH);

        // Botones para mover elementos entre listas
        JPanel buttonPanel = new JPanel();
                GridBagLayout gbl_buttonPanel = new GridBagLayout();
                gbl_buttonPanel.columnWidths = new int[]{20, 47, 0, 0, 0};
                gbl_buttonPanel.rowHeights = new int[]{70, 21, 21, 0, 0, 0, 0, 0};
                gbl_buttonPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
                gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
                buttonPanel.setLayout(gbl_buttonPanel);

        mainPanel.add(mainListPanel);
        mainPanel.add(buttonPanel);
        JButton removeFromGroupCreationButton = new JButton("<<");
        removeFromGroupCreationButton.addActionListener(e -> removeFromGroupCreation());
        JButton addToGroupCreationButton = new JButton(">>");
        addToGroupCreationButton.addActionListener(e -> addToGroupCreation());
        GridBagConstraints gbc_addToGroupCreationButton = new GridBagConstraints();
        gbc_addToGroupCreationButton.fill = GridBagConstraints.BOTH;
        gbc_addToGroupCreationButton.insets = new Insets(0, 0, 5, 0);
        gbc_addToGroupCreationButton.gridx = 3;
        gbc_addToGroupCreationButton.gridy = 3;
        buttonPanel.add(addToGroupCreationButton, gbc_addToGroupCreationButton);
        GridBagConstraints gbc_removeFromGroupCreationButton = new GridBagConstraints();
        gbc_removeFromGroupCreationButton.fill = GridBagConstraints.BOTH;
        gbc_removeFromGroupCreationButton.gridx = 3;
        gbc_removeFromGroupCreationButton.gridy = 6;
        buttonPanel.add(removeFromGroupCreationButton, gbc_removeFromGroupCreationButton);
        mainPanel.add(groupCreationPanel);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // Método para añadir un contacto a la lista principal
    private void addContact() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        Object[] message = {
            "Nombre:", nameField,
            "Teléfono:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Añadir Contacto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (!name.isEmpty() && !phone.isEmpty()) {
                mainListModel.addElement(name);
                controlador.agregarContacto(name, phone);
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre y el teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para añadir un elemento seleccionado a la lista de creación de grupos
    private void addToGroupCreation() {
        String selected = mainList.getSelectedValue();
        if (selected != null && !groupCreationListModel.contains(selected)) {
            groupCreationListModel.addElement(selected);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto o grupo que no esté en la lista de creación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para remover un elemento de la lista de creación de grupos y devolverlo a la lista principal
    private void removeFromGroupCreation() {
        String selected = groupCreationList.getSelectedValue();
        if (selected != null) {
            groupCreationListModel.removeElement(selected);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un elemento para remover de la lista de creación.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para crear un grupo basado en los elementos de la lista de creación de grupos
    private void createGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Ingrese el nombre del grupo:");
        if (groupName != null && !groupName.trim().isEmpty()) {
            if (groupsMap.containsKey(groupName)) {
                JOptionPane.showMessageDialog(this, "El grupo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                DefaultListModel<String> groupContacts = new DefaultListModel<>();
                for (int i = 0; i < groupCreationListModel.size(); i++) {
                    groupContacts.addElement(groupCreationListModel.getElementAt(i));
                }
                groupsMap.put(groupName, groupContacts);
                mainListModel.addElement(groupName);
                groupCreationListModel.clear();  // Limpiar la lista de creación después de crear el grupo
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaGrupo manager = new VentanaGrupo();
            manager.setVisible(true);
        });
    }
}
