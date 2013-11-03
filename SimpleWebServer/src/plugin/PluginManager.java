/*
 * PluginManager.java
 * Nov 1, 2013
 *
 * Simple Web Server (SWS) for EE407/507 and CS455/555
 * 
 * Copyright (C) 2011 Chandan Raj Rupakheti, Clarkson University
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 * Contact Us:
 * Chandan Raj Rupakheti (rupakhcr@clarkson.edu)
 * Department of Electrical and Computer Engineering
 * Clarkson University
 * Potsdam
 * NY 13699-5722
 * http://clarkson.edu/~rupakhcr
 */

package plugin;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import plugin.PluginInterface;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;

/**
 * 
 * @author Spencer Murphy
 */
public class PluginManager implements Runnable {
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private Map<String, PluginInterface> plugins;

	public PluginManager(String path) throws IOException {
		Path dir = Paths.get("plugins");
		this.plugins = new HashMap<String, PluginInterface>();
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

		registerAll(dir);
		for (File file : dir.toFile().listFiles()){
			if(file.toString().endsWith(".jar")){
				loadJar(file.toString());
			}
		}

	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * 
	 */
	public void watch() {
		for (;;) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);

				// print out event
				System.out.format("%s: %s\n", event.kind().name(), child);

				if (kind == ENTRY_CREATE) {
					if (child.toString().endsWith(".jar")){
						loadJar(child.toString());
					}
					System.out.println("added");
				} else if (kind == ENTRY_MODIFY) {
					if (child.toString().endsWith(".jar")){
						this.plugins.remove(child.toString());
						loadJar(child.toString());
					}
					System.out.println("modified");
				} else if (kind == ENTRY_DELETE) {
					if (child.toString().endsWith(".jar")){
						this.plugins.remove(child.toString());
					}
					System.out.println("deleted");
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}

	}

	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,
				ENTRY_MODIFY);
		Path prev = keys.get(key);
		if (prev == null) {
			System.out.format("register: %s\n", dir);
		} else {
			if (!dir.equals(prev)) {
				System.out.format("update: %s -> %s\n", prev, dir);
			}
		}
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private void loadJar(String jarName) {
		try{
			File file = new File(jarName);
	
			URI uri = file.toURI();
			URL url = new URL("jar:" + uri + "!/");
			URL[] urls = { url };
			URLClassLoader classLoader = new URLClassLoader(urls);
			JarURLConnection uc = (JarURLConnection) url.openConnection();
			String main = uc.getMainAttributes().getValue(
					Attributes.Name.MAIN_CLASS);
			if (main != null) {
				Class<?> aClass = classLoader.loadClass(main);
				PluginInterface plugin = (PluginInterface) aClass.newInstance();
				this.plugins.put(jarName, plugin);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		watch();

	}

}
