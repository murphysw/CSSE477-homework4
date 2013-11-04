package mine;

import java.io.File;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class GetServlet extends AbstractServlet {

	@Override
	public String getSupportedRequests() {
		return "GET";
	}
	@Override
	public HttpResponse doGet(HttpRequest request) {
		HttpResponse response = null;
		String complete = "C:\\Users\\hoorncj\\Documents\\Courses\\CSSE477\\HW4\\CSSE477-homework4\\ServerPlugin1\\src\\mine\\1.html";
		File file = new File(complete);
		if(file.exists()) {
			response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
		}
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
		return response;
	}

}
