
 
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
//		servlets = new HashMap<String,AbstractServlet>();
//		File servletsFile = getConfigFile();
//		Scanner scan;
//		try{
//			scan = new Scanner(servletsFile);
//		}catch(FileNotFoundException ex){
//			System.out.println("This plugin has not defined a config file");
//			return;
//		}
//		while(scan.hasNext()){
//			String servletConfiguration = scan.nextLine();
//			StringTokenizer st = new StringTokenizer(servletConfiguration," ");
//			String requestType = st.nextToken();
//			String servletName = st.nextToken();
//			String servletClass = st.nextToken();
//			/*try{
//				File file = new File(servletPath); //This path will likely need to include .../plugins/<Plugin>/
//		        
//		        URI uri = file.toURI();
//		        URL url = new URL("jar:" + uri + "!/");
//		        URL[] urls = {url};
//		        URLClassLoader classLoader = new URLClassLoader(urls);
//		        JarURLConnection uc = (JarURLConnection)url.openConnection();
//		        String main = uc.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
//		        if(main != null)
//		        {
//					Class<?> aClass = classLoader.loadClass(main);
//					AbstractServlet servlet = (AbstractServlet)aClass.newInstance();
//					servlets.put(servletName, servlet);
//		        }
//		        classLoader.close(); //This might remove the class, but it warns us if we don't have it
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    } */
//			try {
//				Class sClass = Class.forName(servletClass);
//				AbstractServlet servlet = (AbstractServlet) sClass.newInstance();
//				servlets.put(servletName, servlet);
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	/* (non-Javadoc)
	 * @see plugin.PluginInterface#getConfigFile()
	 */
	public abstract String getConfigFile();

	public HttpResponse service(HttpRequest request){
		String uri = request.getUri();
		String servletKey = uri.substring(uri.lastIndexOf('/'));
		AbstractServlet servlet = servlets.get(servletKey);
		return servlet.service(request);
	}
	
	public void setUpHash(URLClassLoader classLoader, InputStream stream){
		InputStreamReader inStreamReader = new InputStreamReader(stream);
		BufferedReader reader = new BufferedReader(inStreamReader);
		
		//First Request Line: GET /somedir/page.html HTTP/1.1
		try {
			String line = reader.readLine();
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // A line ends with either a \r, or a \n, or both
		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
