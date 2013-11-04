package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import protocol.HttpRequest;
import protocol.HttpResponse;

public class AuditLog {
	private String rootDir;
	private int recordCounter;
	private int logSize, duplicateDateCounter;
	private String currentFile;
	
	public AuditLog(String rootDir, int logSize) {
		this.recordCounter = 0;
		this.logSize = logSize;
		this.rootDir = rootDir;
		createNewFile();
	}
	
	public void newRecord(Socket socket, HttpRequest request, HttpResponse response) {
		if (socket == null || request == null || response == null)
			return;
		if (recordCounter > logSize ) {
			createNewFile();
			recordCounter = 0;
		}
		PrintWriter writer;
		String socketString = socket.getInetAddress().toString() + " ";
		String requestString = request.getMethod() + " " + request.getUri() + " ";
		String responseString = response.getStatus() + " " + response.getPhrase();
		try {
			writer = new PrintWriter(new FileOutputStream(new File(currentFile + ".txt"),true));
			writer.println("[" + getDate(false) + "] " + socketString + requestString + responseString);
			writer.close();
			recordCounter++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String getDate(boolean fileName) {
		Date now = new Date();
		String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(now);
		if (fileName) {
			StringBuilder sb = new StringBuilder();
			date = getChoppedDate(date, "/");
			date = getChoppedDate(date, ":");
			date = getChoppedDate(date, " ");
			date = date.substring(0, date.length()-4);
			sb.append(date);
			sb.deleteCharAt(date.length()-3);
			return sb.toString();
		}
		return DateFormat.getTimeInstance().format(now);
	}
	
	private String getChoppedDate(String date, String delim) {
		StringTokenizer st = new StringTokenizer(date, delim);
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreElements()) {
			if (delim.equals(" "))
				sb.append(st.nextElement() + "__");
			else
				sb.append(st.nextElement() + "_");
	    }
	 return sb.toString();
	}
	
	private void createNewFile() {
		String name = rootDir + "\\" + getDate(true);
		if (name.equals(currentFile)) {
			name += "_" + Integer.toString(duplicateDateCounter);
			duplicateDateCounter++;
		}
		else
			duplicateDateCounter = 1;
		System.out.println(name);
		this.currentFile = name;
	}
}
