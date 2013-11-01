import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ListingPanel extends JPanel implements ListSelectionListener {
	DefaultListModel<String> listModel;
	JList<String> list;
	PanelMessenger messenger;
	public ListingPanel() {
		super();
		
		setBackground(Color.RED);
		this.setVisible(true);
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setVisible(true);
		listScrollPane.setSize(200,200);
		this.setLayout(new BorderLayout());
		this.add(listScrollPane,BorderLayout.CENTER);
	}

	
	public void updateListOfPlugins(ArrayList<String> files) {
		listModel = new DefaultListModel<String>();
		for(String file : files){
			listModel.addElement(file);
			System.out.println(file.toString());
		}
		list.setModel(listModel);
		this.paint(this.getGraphics());
		this.messenger.sendMessageToStatus("The plugins folder has been updated!");
		return;
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (!arg0.getValueIsAdjusting()){
			@SuppressWarnings("unchecked")
			JList<String> jList = (JList<String>)(arg0.getSource());
			String file = jList.getSelectedValue();
			try {
				this.messenger.switchToPlugin(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public void setMessenger(PanelMessenger messenger) {
		this.messenger = messenger;
	}

}
