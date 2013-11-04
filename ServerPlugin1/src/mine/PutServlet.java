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


public class PutServlet extends AbstractServlet {

	@Override
	public String getSupportedRequests() {
		return "PUT";
	}

	@Override
	public HttpResponse doPut(HttpRequest request) {
		HttpResponse response = null;
		String uri = request.getUri();
		String cwd = System.getProperty("user.dir");
		File file = new File(cwd + uri);
		
		if(file.exists()) {
			response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
		}
		else {					
			response = HttpResponseFactory.create201Created(file, Protocol.CLOSE);
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
			System.out.println(request.getBody());
			writer.println(request.getBody());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return response;	
	}

}
