package plugin;

import protocol.HttpRequest;
import protocol.HttpResponse;


public abstract class AbstractServlet {
	
	public abstract void doDelete(HttpRequest request, HttpResponse response);
	
	public abstract void doGet(HttpRequest request, HttpResponse response);	
	
	public abstract void doPost(HttpRequest request, HttpResponse response);
	
	public abstract void doPut(HttpRequest request, HttpResponse response);
	
	public void service(HttpRequest request, HttpResponse response) {
		String method = request.getMethod().toUpperCase();
		if (method.equals("DELETE"))
			doDelete(request, response);
		else if (method.equals("GET"))
			doGet(request, response);
		else if (method.equals("POST"))
			doPost(request, response);
		else if (method.equals("PUT"))
			doPut(request, response);
		else 
			System.out.println("[AbstractServlet] Requested method " + method + " not supported");		
	}
	
}
