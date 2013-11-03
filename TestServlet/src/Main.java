import java.io.File;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class Main extends AbstractServlet {

	private static final String SUPPORTS = "GET, PUT";	
	
	@Override
	public HttpResponse doDelete(HttpRequest request) {
		return HttpResponseFactory.create405MethodNotAllowed(SUPPORTS ,Protocol.CLOSE);
	}

	@Override
	public HttpResponse doGet(HttpRequest request) {
		return HttpResponseFactory.create200OK(null, Protocol.CLOSE);		
	}

	@Override
	public HttpResponse doPost(HttpRequest request) {
		return HttpResponseFactory.create405MethodNotAllowed(SUPPORTS ,Protocol.CLOSE);			
	}

	@Override
	public HttpResponse doPut(HttpRequest request) {
		return HttpResponseFactory.create200OK(null, Protocol.CLOSE);		
	}

}