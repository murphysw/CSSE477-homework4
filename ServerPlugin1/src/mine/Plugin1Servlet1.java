package mine;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;

public class Plugin1Servlet1 extends AbstractServlet {

	@Override
	public HttpResponse doDelete(HttpRequest request) {
		System.out.println("delete");
		return HttpResponseFactory.create304NotModified(Protocol.CLOSE);
	}

	@Override
	public HttpResponse doGet(HttpRequest request) {
		System.out.println("get");
		return HttpResponseFactory.create304NotModified(Protocol.CLOSE);
	}

	@Override
	public HttpResponse doPost(HttpRequest request) {
		System.out.println("post");
		return HttpResponseFactory.create304NotModified(Protocol.CLOSE);
	}

	@Override
	public HttpResponse doPut(HttpRequest request) {
		System.out.println("put");
		return HttpResponseFactory.create304NotModified(Protocol.CLOSE);
	}

}
