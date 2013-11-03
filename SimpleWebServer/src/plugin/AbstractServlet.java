package plugin;

import protocol.HttpRequest;
import protocol.HttpResponse;


public abstract class AbstractServlet {
	
	public abstract HttpResponse doDelete(HttpRequest request);
	
	public abstract HttpResponse doGet(HttpRequest request);	
	
	public abstract HttpResponse doPost(HttpRequest request);
	
	public abstract HttpResponse doPut(HttpRequest request);
	
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
