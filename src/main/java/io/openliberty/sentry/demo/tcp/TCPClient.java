package io.openliberty.sentry.demo.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient {
	private InetAddress host;
	private int port;
	
	private Socket socket;
    private String latestResponse;
    private PrintWriter out;
    private BufferedReader in;
    public static final String TCP_NC = "NC";
    public static final String TCP_OK = "ok";
    
    
    
    public TCPClient(InetAddress serverAddress, int serverPort) throws Exception {
    	host = serverAddress;
    	port = serverPort;
        this.socket = new Socket();
        this.socket.setTcpNoDelay(true);
        this.socket.connect(new InetSocketAddress(host, port), 5000);
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
            try {
            	response = in.readLine();
            	attempts ++;
            } catch (SocketException se) {
            	if (se.getMessage().contains("reset")) {
                    this.socket = new Socket(host, port);
                    out = new PrintWriter(this.socket.getOutputStream(), true);
                    in = new BufferedReader(
                                new InputStreamReader(this.socket.getInputStream()));
            	}
            }
            
    		
    	}
    	
    	if (response != null)
    		latestResponse = response;
    	else
    		latestResponse = TCP_NC;
    	
 
    	return latestResponse;
    }
    
	public String getData() throws IOException{
		System.out.println("get Data on TCPClient");
		String rxData = in.readLine();
		System.out.println("got Data on TCPClient " + rxData);
        return rxData;

	}
    
    public boolean isConnected(){
    	return this.socket.isConnected();
    }
    
    public void close() throws IOException{
    	this.socket.close();
    	in.close();
    	out.close();
    }
    
    public String getLatestResponse(){
    	return latestResponse;
    }
    
}
