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
		foo.service(request, null);
	}

}
