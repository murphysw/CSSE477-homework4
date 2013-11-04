package mine;

import java.io.File;

import plugin.AbstractServlet;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class DeleteServlet extends AbstractServlet {
	
	@Override
	public String getSupportedRequests() {
		return "DELETE";
	}
	
	
	@Override
	public HttpResponse doDelete(HttpRequest request) {
		HttpResponse response = null;
		String complete = "C:\\Users\\hoorncj\\Documents\\Courses\\CSSE477\\HW4\\CSSE477-homework4\\ServerPlugin1\\src\\mine\\1.html";
		File file = new File(complete);
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

}
