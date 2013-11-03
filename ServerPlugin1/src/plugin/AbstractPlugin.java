
 
package plugin;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		File servletsFile = getConfigFile();
		Scanner scan;
		try{
			scan = new Scanner(servletsFile);
		}catch(FileNotFoundException ex){
			System.out.println("This plugin has not defined a config file");
			return;
		}
		while(scan.hasNext()){
			String servletConfiguration = scan.nextLine();
			StringTokenizer st = new StringTokenizer(servletConfiguration," ");
			String requestType = st.nextToken();
			String servletName = st.nextToken();
			String servletPath = st.nextToken();
			try{
				File file = new File(servletPath); //This path will likely need to include .../plugins/<Plugin>/
		        
		        URI uri = file.toURI();
		        URL url = new URL("jar:" + uri + "!/");
		        URL[] urls = {url};
		        URLClassLoader classLoader = new URLClassLoader(urls);
		        JarURLConnection uc = (JarURLConnection)url.openConnection();
		        String main = uc.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
		        if(main != null)
		        {
					Class<?> aClass = classLoader.loadClass(main);
					AbstractServlet servlet = (AbstractServlet)aClass.newInstance();
					servlets.put(servletName, servlet);
		        }
		        classLoader.close(); //This might remove the class, but it warns us if we don't have it
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 
		}
	}

	/* (non-Javadoc)
	 * @see plugin.PluginInterface#getConfigFile()
	 */
	public abstract File getConfigFile();

	public void service(HttpRequest request, HttpResponse response){
		String uri = request.getUri();
		String servletKey = uri.substring(uri.lastIndexOf('/'));
		AbstractServlet servlet = servlets.get(servletKey);
		servlet.service(request,response);
	}
}
