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
		String complete = "C:\\Users\\hoorncj\\Documents\\Courses\\CSSE477\\HW4\\CSSE477-homework4\\ServerPlugin1\\src\\mine\\1.html";
		File file = new File(complete);
		if(file.exists()) {
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
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
		return response;
	}

}
