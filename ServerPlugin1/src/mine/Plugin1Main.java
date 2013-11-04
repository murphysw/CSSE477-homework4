package mine;
import java.io.File;
import java.io.InputStream;
import java.net.URLClassLoader;

import plugin.AbstractPlugin;
import protocol.HttpRequest;
import protocol.HttpResponse;
import sun.print.resources.serviceui;


public class Plugin1Main extends AbstractPlugin {
	
	private String configFile = "Plugin1ServletConfigFile.txt";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("hello plugin");
	}

	@Override
	public String getConfigFile() {
		return "Plugin1ServletConfigFile.txt";
	}
	
	@Override
	public HttpResponse service(HttpRequest request) {
		// TODO Auto-generated method stub
		HttpResponse response = super.service(request);
		response.put("Plugin:","Plugin1");
		return response;
	}

}
