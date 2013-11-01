import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Plugin extends PluginInterface {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
	}
	
	public Component getComponents(){
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Hello");
		JButton button = new JButton();
		button.setBackground(Color.GREEN);
		button.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((JButton)e.getSource()).setBackground(Color.RED);
				postStatus("hello");
			}
		});
		
		JButton button2 = new JButton();
		button2.setBackground(Color.GREEN);
		button2.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((JButton)e.getSource()).setBackground(Color.RED);
				postStatus("hi");
			}
		});
		
		JButton button3 = new JButton();
		button3.setBackground(Color.GREEN);
		button3.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((JButton)e.getSource()).setBackground(Color.RED);
				postStatus("bye");
			}
		});
		panel.add(label);
		panel.add(button);
		panel.add(button2);
		panel.add(button3);
		return panel;
	}
	
	

}
