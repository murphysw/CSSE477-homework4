package plugin;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public abstract class AbstractServlet {
	
	public abstract String getSupportedRequests();
	
	public HttpResponse doDelete(HttpRequest request) {
		return HttpResponseFactory.create405MethodNotAllowed(getSupportedRequests() ,Protocol.CLOSE);
	}
	
	public HttpResponse doGet(HttpRequest request){
		return HttpResponseFactory.create405MethodNotAllowed(getSupportedRequests() ,Protocol.CLOSE);
	}	
	
	public HttpResponse doPost(HttpRequest request){
		return HttpResponseFactory.create405MethodNotAllowed(getSupportedRequests() ,Protocol.CLOSE);
	}
	
	public HttpResponse doPut(HttpRequest request){
		return HttpResponseFactory.create405MethodNotAllowed(getSupportedRequests() ,Protocol.CLOSE);
	}
	
	public HttpResponse service(HttpRequest request) {
		String method = request.getMethod().toUpperCase();
		if (method.equals("DELETE"))
			return doDelete(request);
		else if (method.equals("GET"))
			return doGet(request);
		else if (method.equals("POST"))
			return doPost(request);
		else if (method.equals("PUT"))
			return doPut(request);
		else 
			System.out.println("[AbstractServlet] Requested method " + method + " not supported");
		return null;
	}
	
}
