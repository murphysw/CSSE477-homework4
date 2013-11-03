package mine;

import java.io.File;

import plugin.AbstractPlugin;


public class Plugin1Main extends AbstractPlugin {
	
	private final String pathToConfigFile = "Plugin1ServletConfigFile.txt";

	@Override
	public File getConfigFile() {
		return new File(pathToConfigFile);
	}

}
