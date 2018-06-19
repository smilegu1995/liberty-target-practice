package io.openliberty.sentry.demo.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
	private InetAddress host;
	private int port;
	
	private Socket socket;
    private String latestResponse;
    private PrintWriter out;
    private BufferedReader in;
    public static final String TCP_ERROR = "tcp_error";
    public static final String TCP_OK = "ok";
    
    
    public TCPClient(InetAddress serverAddress, int serverPort) throws Exception {
    	host = serverAddress;
    	port = serverPort;
        this.socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(this.socket.getOutputStream(), true);
        in = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream()));
    }
    
    public String sendCommand(String c) throws IOException{
    	String response = null;
    	int attempts = 0;
    	while (response == null && attempts < 5){
            out.println(c);
            out.flush();
            response = in.readLine();
    		attempts ++;
    	}
    	
    	if (response != null)
    		latestResponse = response;
    	else
    		latestResponse = TCP_ERROR;
    	
 
    	return latestResponse;
    }
    
	public String getData() throws IOException{
		System.out.println("get Data on TCPClient");
        return in.readLine();
	}
    
    public boolean isConnected(){
    	return this.socket.isConnected();
    }
    
    public void close() throws IOException{
    	this.socket.close();
    }
    
    public String getLatestResponse(){
    	return latestResponse;
    }
    
}
