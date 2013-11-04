package server;

import java.io.File;
import java.net.Socket;

import protocol.HttpRequest;
import protocol.HttpResponse;

public class AuditLog {
	private String rootDir;
	private int recordCounter;
	private File currentFile;
	
	public AuditLog(String rootDir) {
		this.recordCounter = 0;
		this.rootDir = rootDir;
	}
	
	public void newRecord(Socket socket, HttpRequest request, HttpResponse response) {
		
	}
}
