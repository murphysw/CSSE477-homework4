import java.io.File;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class Main extends AbstractServlet {

	private static final String SUPPORTS = "GET, PUT";	
	
	@Override
	public void doDelete(HttpRequest request, HttpResponse response) {
		response = HttpResponseFactory.create405MethodNotAllowed(SUPPORTS ,Protocol.CLOSE);		
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		response = HttpResponseFactory.create200OK(null, Protocol.CLOSE);		
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		response = HttpResponseFactory.create405MethodNotAllowed(SUPPORTS ,Protocol.CLOSE);			
	}

	@Override
	public void doPut(HttpRequest request, HttpResponse response) {
		response = HttpResponseFactory.create200OK(null, Protocol.CLOSE);		
	}

}
