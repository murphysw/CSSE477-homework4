package mine;
import java.io.File;

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
	public File getConfigFile() {
		return new File(configFile);
	}

}
