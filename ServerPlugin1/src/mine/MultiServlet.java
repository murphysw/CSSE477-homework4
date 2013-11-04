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


public class MultiServlet extends AbstractServlet {
	
	@Override
	public String getSupportedRequests() {
		return "DELETE, GET, POST, PUT";
	}
	
	@Override
	public HttpResponse doDelete(HttpRequest request) {
		HttpResponse response = null;
		String uri = request.getUri();
		String cwd = System.getProperty("user.dir");
		File file = new File(cwd + uri);
		if(file.exists()) {
			// Its a file
			//delete the file
			try{			 
	    		if(file.delete()){
	    			// Lets create 200 OK response
					response = HttpResponseFactory.create200OK(null, Protocol.CLOSE);
				}else{
					
					// For any other error, we will create bad request response as well
					response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
	    		}
	 
	    	}catch(Exception e){
	 
	    		e.printStackTrace();
	    		// For any other error, we will create bad request response as well
				response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
	    	}
		}
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
	return response;
	}

	@Override
	public HttpResponse doGet(HttpRequest request) {
		HttpResponse response = null;
		String uri = request.getUri();
		String cwd = System.getProperty("user.dir");
		File file = new File(cwd + uri);
		if(file.exists()) {
			if(file.isDirectory()) {
				// Look for default index.html file in a directory
				String location = cwd + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
				file = new File(location);
				if(file.exists()) {
					// Lets create 200 OK response
					response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
				}
				else {
					// File does not exist so lets create 404 file not found code
					response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
				}
			}
			else { // Its a file
				// Lets create 200 OK response
				response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
			}
		}
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
		return response;
	}


	@Override
	public HttpResponse doPost(HttpRequest request) {
		HttpResponse response = null;
		String uri = request.getUri();
		String cwd = System.getProperty("user.dir");
		File file = new File(cwd + uri);
		if(file.exists()) {
			if(file.isDirectory()) {
				// Look for default index.html file in a directory
				String location = cwd + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
				file = new File(location);
				if(file.exists()) {
					// use the post information
					PrintWriter writer;
					try {
						writer = new PrintWriter(location + ".put.txt", "UTF-8");
						writer.println(request.getBody());
						writer.close();
					} catch (FileNotFoundException
							| UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					// Lets create 200 OK response
					response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
				}
				else {
					// File does not exist so lets create 404 file not found code
					response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
				}
			}
			else { // Its a file
				// write put info to a file to be used later
				PrintWriter writer;
				try {
					writer = new PrintWriter(file.getAbsolutePath() + ".post.txt", "UTF-8");
					writer.println(request.getBody());
					writer.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}				
				// Lets create 200 OK response
				response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
			}
		}
		else {
			// File does not exist so lets create 404 file not found code
			response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
		return response;
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
			writer.println(request.getBody());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return response;	
	}

}
