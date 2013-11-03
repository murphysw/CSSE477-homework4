package mine;

import java.io.File;

import plugin.AbstractPlugin;


public class Plugin1Main extends AbstractPlugin {
	
	private final String pathToConfigFile = "Plugin1ServletConfigFile.txt";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
	}

	@Override
	public File getConfigFile() {
		return new File(pathToConfigFile);
	}

}
