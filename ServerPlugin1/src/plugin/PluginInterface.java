
 
package plugin;

import java.io.File;

import protocol.HttpRequest;
import protocol.HttpResponse;

/**
 * 
 * @author Team Otherguys
 */
public interface PluginInterface {
	
	public abstract File getConfigFile();
	
	public abstract HttpResponse service(HttpRequest request);
}
