package mine;
import java.io.File;
import java.io.InputStream;

import plugin.AbstractPlugin;
import protocol.HttpRequest;
import protocol.HttpResponse;


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

}
