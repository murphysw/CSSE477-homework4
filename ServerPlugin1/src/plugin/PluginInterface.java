
 
package plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URLClassLoader;

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
	public abstract void setUpHash(URLClassLoader classLoader, InputStream stream);
}
