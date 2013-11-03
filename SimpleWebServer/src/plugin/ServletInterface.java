/* A simple servlet interface to support DELETE, GET, POST, and PUT HTTP requests
 */
package plugin;

import protocol.HttpRequest;
import protocol.HttpResponse;

public interface ServletInterface {

	public void doDelete(HttpRequest request, HttpResponse response);
	
	public void doGet(HttpRequest request, HttpResponse response);	
	
	public void doPost(HttpRequest request, HttpResponse response);
	
	public void doPut(HttpRequest request, HttpResponse response);
	
	public void service(HttpRequest request, HttpResponse response);
	
}
