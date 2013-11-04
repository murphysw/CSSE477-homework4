package mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class PostServlet extends AbstractServlet {

	@Override
	public String getSupportedRequests() {
		return "POST";
	}
	
	@Override
	public HttpResponse doPost(HttpRequest request) {
		HttpResponse response = null;
		String uri = request.getUri();
		String cwd = System.getProperty("user.dir");
		File file = new File(cwd + uri);
		if(file.exists()) {
			if(file.isDirectory()) {
				// Look for default index.html file in a directory
				String location = cwd + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
				file = new File(location);
				if(file.exists()) {
					// use the post information
					PrintWriter writer;
					try {
						writer = new PrintWriter(location + ".put.txt", "UTF-8");
						writer.println(request.getBody());
						writer.close();
					} catch (FileNotFoundException
							| UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					// Lets create 200 OK response
					response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
				}
				else {
					// File does not exist so lets create 404 file not found code
					response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
				}
			}
			else { // Its a file
				// write put info to a file to be used later
				PrintWriter writer;
				try {
					writer = new PrintWriter(file.getAbsolutePath() + ".post.txt", "UTF-8");
					writer.println(request.getBody());
					writer.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}				
				// Lets create 200 OK response
				response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
			}
		}
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
		return response;
	}

}
