package mine;

import java.util.HashMap;
import java.util.Map;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;


public class BadServlet extends AbstractServlet {

	@Override
	public String getSupportedRequests() {
		return "GET";
	}

	@Override
	public HttpResponse doGet(HttpRequest request) {
		HttpResponse response = new HttpResponse("1.5", 1337, "Elite", new HashMap<String, String>(), null);
		response.put("Bad-Header", "Actually virus");
		return response;
	}
}
