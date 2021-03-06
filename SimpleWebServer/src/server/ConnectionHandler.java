/*
 * ConnectionHandler.java
 * Oct 7, 2012
 *
 * Simple Web Server (SWS) for CSSE 477
 * 
 * Copyright (C) 2012 Chandan Raj Rupakheti
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 * 
 */
 
package server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import plugin.PluginInterface;
import plugin.PluginManager;
import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.HttpResponseValidator;
import protocol.Protocol;
import protocol.ProtocolException;

/**
 * This class is responsible for handling a incoming request
 * by creating a {@link HttpRequest} object and sending the appropriate
 * response be creating a {@link HttpResponse} object. It implements
 * {@link Runnable} to be used in multi-threaded environment.
 * 
 * @author Chandan R. Rupakheti (rupakhet@rose-hulman.edu)
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket socket;
	private PluginManager manager;
	private AuditLog auditLog;
	private HttpResponseValidator validator;
	
	public ConnectionHandler(Server server, Socket socket, PluginManager manager, AuditLog log) {
		this.server = server;
		this.socket = socket;
		this.manager = manager;
		this.auditLog = log;
		this.validator = new HttpResponseValidator();
	}
	
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}


	/**
	 * The entry point for connection handler. It first parses
	 * incoming request and creates a {@link HttpRequest} object,
	 * then it creates an appropriate {@link HttpResponse} object
	 * and sends the response back to the client (web browser).
	 */
	public void run() {
		// Get the start time
		long start = System.currentTimeMillis();
		
		InputStream inStream = null;
		OutputStream outStream = null;
		
		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		}
		catch(Exception e) {
			// Cannot do anything if we have exception reading input or output stream
			// May be have text to log this for further analysis?
			e.printStackTrace();
			
			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end-start);
			return;
		}
		// At this point we have the input and output stream of the socket
		// Now lets create a HttpRequest object
		HttpRequest request = null;
		HttpResponse response = null;
		try {
			request = HttpRequest.read(inStream);
			System.out.println(request);
		}
		catch(ProtocolException pe) {
			// We have some sort of protocol exception. Get its status code and create response
			// We know only two kind of exception is possible inside fromInputStream
			// Protocol.BAD_REQUEST_CODE and Protocol.NOT_SUPPORTED_CODE
			int status = pe.getStatus();
			if(status == Protocol.BAD_REQUEST_CODE) {
				response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
			}
			// DONE: Handle version not supported code as well
			else if(status == Protocol.NOT_SUPPORTED_CODE) {
				response = HttpResponseFactory.create505NotSupported(Protocol.CLOSE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			// For any other error, we will create bad request response as well
			response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
		}
		
		if(response != null) {
			// Means there was an error, now write the response object to the socket
			try {
				response.write(outStream);
			}
			catch(Exception e){
				// We will ignore this exception
				e.printStackTrace();
			}

			// Increment number of connections by 1
			server.incrementConnections(1);
			// Get the end time
			long end = System.currentTimeMillis();
			this.server.incrementServiceTime(end-start);
			return;
		}
		
		// We reached here means no error so far, so lets process further
		try {
			// Fill in the code to create a response for version mismatch.
			// You may want to use constants such as Protocol.VERSION, Protocol.NOT_SUPPORTED_CODE, and more.
			// You can check if the version matches as follows
			if(!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
				// Here you checked that the "Protocol.VERSION" string is not equal to the  
				// "request.version" string ignoring the case of the letters in both strings
				// DONE: Fill in the rest of the code here
				response = HttpResponseFactory.create505NotSupported(Protocol.CLOSE);
			}
			else{
				boolean plugin = checkForPlugin(request);
				if(plugin){
					response = handlePluginRequest(request);
					response = validator.checkResponse(response);
				}
				else {
					if(request.getMethod().equalsIgnoreCase(Protocol.GET)) {
		//				Map<String, String> header = request.getHeader();
		//				String date = header.get("if-modified-since");
		//				String hostName = header.get("host");
		//				
						// Handling GET request here
						// Get relative URI path from request
						String uri = request.getUri();
						// Get root directory path from server
						String rootDirectory = server.getRootDirectory();
						// Combine them together to form absolute file path
						File file = new File(rootDirectory + uri);
						// Check if the file exists
						if(file.exists()) {
							if(file.isDirectory()) {
								// Look for default index.html file in a directory
								String location = rootDirectory + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
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
					}
					else if(request.getMethod().equalsIgnoreCase(Protocol.POST)) {
						// Get relative uri path from Request
						String uri = request.getUri();
						// Get root directory from server
						String rootDirectory = server.getRootDirectory();
						// Combine them together to form absolute file path
						File file = new File(rootDirectory + uri);
						// Check if file exists
						if(file.exists()) {
							if(file.isDirectory()) {
								// Look for default index.html file in a directory
								String location = rootDirectory + uri + System.getProperty("file.separator") + Protocol.DEFAULT_FILE;
								file = new File(location);
								if(file.exists()) {
									// use the post information
									PrintWriter writer = new PrintWriter(location + ".put.txt", "UTF-8");
									writer.println(request.getBody());
									writer.close();
									
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
								PrintWriter writer = new PrintWriter(file.getAbsolutePath() + ".post.txt", "UTF-8");
								writer.println(request.getBody());
								writer.close();
								
								// Lets create 200 OK response
								response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
							}
						}
						else {
							// File does not exist so lets create 404 file not found code
							response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
						}
					}
					else if(request.getMethod().equalsIgnoreCase(Protocol.PUT)) {
						// Get relative uri path from Request
						String uri = request.getUri();
						// Get root directory from server
						String rootDirectory = server.getRootDirectory();
						// Combine them together to form absolute file path
						File file = new File(rootDirectory + uri);
						
						// Check if file exists
						if(file.exists()) {
							// Lets create 200 OK response
							response = HttpResponseFactory.create200OK(file, Protocol.CLOSE);
						}
						else {					
							// Lets create 201 Created response
							response = HttpResponseFactory.create201Created(file, Protocol.CLOSE);
						}
						
						// put the body of the request into the file location
						PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
						writer.println(request.getBody());
						writer.close();
					}
					else if(request.getMethod().equalsIgnoreCase(Protocol.DELETE)) {
						// Get relative uri path from Request
						String uri = request.getUri();
						// Get root directory from server
						String rootDirectory = server.getRootDirectory();
						// Combine them together to form absolute file path
						File file = new File(rootDirectory + uri);
						// Check if file exists
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
					}
					else {
						// File does not exist so lets create 404 file not found code
						response = HttpResponseFactory.create404NotFound(Protocol.CLOSE);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

//		// DONE: So far response could be null for protocol version mismatch.
//		// So this is a temporary patch for that problem and should be removed
//		// after a response object is created for protocol version mismatch.
//		if(response == null) {
//			response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
//		}
		
		try{
			// Write response and we are all done so close the socket
			response.write(outStream);
			socket.close();
		}
		catch(Exception e){
			// We will ignore this exception
			e.printStackTrace();
		} 
		
		// Increment number of connections by 1
		server.incrementConnections(1);
		// Get the end time
		long end = System.currentTimeMillis();
		this.server.incrementServiceTime(end-start);
		server.incrementConnections(-1);
		auditLog.newRecord(socket, request, response);
	}

	/**
	 * @param request
	 * @return
	 */
	private HttpResponse handlePluginRequest(HttpRequest request) {
		String pluginName;
		try{
			 pluginName = request.getUri().split("/")[1];
			
		}
		catch (IndexOutOfBoundsException e){
			return HttpResponseFactory.create404NotFound(Protocol.CLOSE);
		}
	
		PluginInterface plugin = manager.getPlugins().get(pluginName);
		plugin.toString();
		return plugin.service(request);
	}

	/**
	 * @param request
	 * @return
	 */
	private boolean checkForPlugin(HttpRequest request) {
		String plugin;
		try{
		plugin = request.getUri().split("/")[1];
		}
		catch (IndexOutOfBoundsException e){
			return false;
		}
		return manager.checkForPlugin(plugin);
	}
}
