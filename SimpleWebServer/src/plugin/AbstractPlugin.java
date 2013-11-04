
 
package plugin;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.jar.Attributes;

import protocol.HttpRequest;
import protocol.HttpResponse;


/**
 * 
 * @author Team Otherguys
 */
public abstract class AbstractPlugin implements PluginInterface {
	
	HashMap<String,AbstractServlet> servlets;
	
	public AbstractPlugin(){
		servlets = new HashMap<String,AbstractServlet>();
	}

	/* (non-Javadoc)
	 * @see plugin.PluginInterface#getConfigFile()
	 */
	public abstract String getConfigFile();

	public HttpResponse service(HttpRequest request){
		String uri = request.getUri();
		String servletKey = uri.substring(uri.lastIndexOf('/')+1);
		AbstractServlet servlet = servlets.get(servletKey);
		return servlet.service(request);
	}
	
	public void setUpHash(URLClassLoader classLoader, InputStream stream){
		InputStreamReader inStreamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(inStreamReader);
		
		//First Request Line: GET /somedir/page.html HTTP/1.1
		try {
			String line = reader.readLine();
			while(line != null){
				StringTokenizer st = new StringTokenizer(line," ");
				String servletName = st.nextToken();
				String servletClass = st.nextToken();
				Class aClass = classLoader.loadClass(servletClass);
				try {
					AbstractServlet servlet = (AbstractServlet) aClass.newInstance();
					servlets.put(servletName, servlet);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				line = reader.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // A line ends with either a \r, or a \n, or both
		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // A line ends with either a \r, or a \n, or both
		
	}
}
