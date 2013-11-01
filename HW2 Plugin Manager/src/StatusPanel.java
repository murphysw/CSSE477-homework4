import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class StatusPanel extends JPanel {
	
	JTextArea statusArea;
	JScrollPane scrollPanel;
	PanelMessenger messenger;
	public StatusPanel() {
		super();
		setBackground(Color.YELLOW);
		this.statusArea = new JTextArea();
		this.statusArea.setColumns(10);
        this.statusArea.setLineWrap(true);
        this.statusArea.setRows(5);
        this.statusArea.setWrapStyleWord(true);
        this.statusArea.setEnabled(false); 
        
        scrollPanel = new JScrollPane(this.statusArea);
        
        this.setLayout(new BorderLayout());
		this.add(scrollPanel,BorderLayout.CENTER);
        this.setVisible(true);
	}
	
	public void addStringToStatus(String message) {
		this.statusArea.append(message + "\n");
	}

	public void setMessenger(PanelMessenger messenger) {
		this.messenger = messenger;		
	}

}
