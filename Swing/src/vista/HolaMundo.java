package vista;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HolaMundo {

	public static void main(String[] args) {
		JFrame ventana = new JFrame();
		ventana.setTitle("Hola mundo");
		ventana.setSize(800,300); //SIRVE PARA DARLE UN TAMAÑO 
		ventana.setLocation(300,150);	//SIRVE PARA PONERLO EN UN LUGAR ESPECIFICO
		ventana.setVisible(true);	//PARA QUE SEA VISIBLE
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		ventana.getContentPane().add(btnNewButton, BorderLayout.SOUTH);
		
	}

}
