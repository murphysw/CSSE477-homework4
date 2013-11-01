import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;


public class PluginManagerUI extends JFrame {

	private ExecutionPanel executionPanel;
	private ListingPanel listingPanel;
	private StatusPanel statusPanel;
	private PanelMessenger messenger;
	
	public PluginManagerUI(){
		super("Plugin Manager");
		this.messenger = new PanelMessenger();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700,700);
		this.setLocation(400,200);
		this.setBackground(Color.black);
		executionPanel = new ExecutionPanel();
		listingPanel = new ListingPanel();
		statusPanel = new StatusPanel();
		messenger.setPanels(executionPanel, listingPanel, statusPanel);
		executionPanel.setMessenger(messenger);
		listingPanel.setMessenger(messenger);
		statusPanel.setMessenger(messenger);
		this.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.2;
		c.weighty = 0.5;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		this.getContentPane().add(listingPanel, c);
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 2;
		this.getContentPane().add(executionPanel,c);
		c.weighty = 0.2;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		this.getContentPane().add(statusPanel,c);
		this.setVisible(true);
	}
	
	//updates the list of files on the listing panel
	public void updatePluginFolder(ArrayList<String> files){
		this.listingPanel.updateListOfPlugins(files);
		
	}
}
