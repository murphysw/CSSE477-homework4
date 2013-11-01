import java.io.File;
import java.util.ArrayList;


public class PluginManagerMain {
	
	public static void main(String[] args)
	{
		PluginManagerUI mUI = new PluginManagerUI();
		ArrayList<String> files = new ArrayList<String>();
		
		while(true){
			ArrayList<String> newFiles = getListOfPlugins();
			if (newFiles.toString().compareTo( files.toString()) != 0){
				mUI.updatePluginFolder(newFiles);
				files = newFiles;
			}
		}
	}

	// pulls the list of files from the plugins folder
	private static ArrayList<String> getListOfPlugins() {
		ArrayList<String> plugins = new ArrayList<String>();
		final File directory = new File("plugins");
		for (final File file : directory.listFiles()){
			if (file.getName().endsWith(".jar")){
				plugins.add(file.getName());
			}
		}
		
		return plugins;
	}
}
