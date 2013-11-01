import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Observable;
import java.util.Observer;
import java.util.jar.Attributes;

import javax.swing.JPanel;


public class ExecutionPanel extends JPanel implements Observer {
	private PanelMessenger messenger;
	public ExecutionPanel() {
		super();

		setBackground(Color.BLUE);
		setLayout(new BorderLayout());
		setVisible(true);
	}
	
	public void setMessenger(PanelMessenger messenger) {
		this.messenger = messenger;		
	}
	
	public boolean updateExecutedPlugin(String jarName) throws IOException{
	    try {
	        File file = new File("plugins", jarName);
	        
	        URI uri = file.toURI();
	        URL url = new URL("jar:" + uri + "!/");
	        URL[] urls = {url};
	        URLClassLoader classLoader = new URLClassLoader(urls);
	        JarURLConnection uc = (JarURLConnection)url.openConnection();
	        String main = uc.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
	        if(main != null)
	        {
				Class<?> aClass = classLoader.loadClass(main);
				PluginInterface plugin = (PluginInterface)aClass.newInstance();
				plugin.addObserver(this);
				Component c = plugin.getComponents();
				this.removeAll();
				this.add(c,BorderLayout.CENTER);
				this.updateUI();
				return true;
	        }
			
			
	        
	        return false;
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof PluginInterface) {
			messenger.sendMessageToStatus((String)arg1);
		}
	}
}
