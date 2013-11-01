import javax.swing.*;
import java.awt.*;
import java.util.Observable;


public abstract class PluginInterface extends Observable{
	
	public abstract Component getComponents();
	
	public void notify(Object o) {
        this.setChanged();
        this.notifyObservers(o);
    }
	
	public void postStatus(String s) {
		notify(s);
	}

}
