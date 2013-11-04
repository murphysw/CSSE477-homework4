
 
package plugin;

import java.io.File;
import java.io.InputStream;

import protocol.HttpRequest;
import protocol.HttpResponse;

/**
 * 
 * @author Team Otherguys
 */
public interface PluginInterface {
	
	public abstract String getConfigFile();
	
	public abstract HttpResponse service(HttpRequest request);

	/**
	 * @param stream
	 */
	public abstract void setUpHash(InputStream stream);
}
