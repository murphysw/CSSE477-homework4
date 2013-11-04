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
	private HttpResponseValidator validator;
	private AuditLog auditLog;
	
	public ConnectionHandler(Server server, Socket socket, PluginManager manager, AuditLog log) {
		this.server = server;
		this.socket = socket;
		this.manager = manager;
		this.validator = new HttpResponseValidator();
		this.auditLog = log;
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
		long start = System.currentTimeMillis();		
		InputStream inStream = null;
		OutputStream outStream = null;		
		HttpRequest request = null;
		HttpResponse response = null;
		
		try {
			inStream = this.socket.getInputStream();
			outStream = this.socket.getOutputStream();
		}
		catch(Exception e) {
			// end request without writing anything to socket
			endRequest(response, start, outStream);
			return;
		}
		
		try {
			request = HttpRequest.read(inStream);
		}
		catch(ProtocolException pe) {
			int status = pe.getStatus();
			if(status == Protocol.BAD_REQUEST_CODE) {
				response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
			}
			else if(status == Protocol.NOT_SUPPORTED_CODE) {
				response = HttpResponseFactory.create505NotSupported(Protocol.CLOSE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			response = HttpResponseFactory.create400BadRequest(Protocol.CLOSE);
		}
		
		// No errors so far, try dispatch to plugin
		if(response == null) {		
			try {
				if(!request.getVersion().equalsIgnoreCase(Protocol.VERSION)) {
					response = HttpResponseFactory.create505NotSupported(Protocol.CLOSE);
				}
				else{
					boolean plugin = checkForPlugin(request);
					if(plugin){
						response = handlePluginRequest(request);
						response = validator.checkResponse(response);
					}
				}			
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// Finally
		auditLog.newRecord(socket, request, response);
		endRequest(response, start, outStream);
	}
	
	private void endRequest(HttpResponse response, long start, OutputStream outStream) {
		if (response != null) {
			try{
				response.write(outStream);
				socket.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		server.incrementConnections(1);
		long end = System.currentTimeMillis();
		this.server.incrementServiceTime(end-start);
	}

	/**
	 * @param request
	 * @return
	 */
	private HttpResponse handlePluginRequest(HttpRequest request) {
		String pluginName = request.getUri().split("/")[0];
		
		PluginInterface plugin = manager.getPlugins().get(pluginName);
		return plugin.service(request);
	}

	/**
	 * @param request
	 * @return
	 */
	private boolean checkForPlugin(HttpRequest request) {
		String plugin = request.getUri().split("/")[0];
		return manager.checkForPlugin(plugin);
	}
}
