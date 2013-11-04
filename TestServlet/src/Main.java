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

/*
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import protocol.HttpRequest;


public class MainTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Main foo = new Main();
		File file = new File("C:\\Users\\sohlbehd\\workspace\\TestServlet\\src\\getRequest");
		InputStream inputStream = null;
		HttpRequest request = null;
		try {
			inputStream = new FileInputStream(file);
			 request = HttpRequest.read(inputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//foo.service(request, null);
	}

}
*/