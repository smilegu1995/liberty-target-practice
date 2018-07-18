package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.net.InetAddress;

import javax.enterprise.inject.Model;

import io.openliberty.sentry.demo.tcp.TCPClient;
import io.openliberty.sentry.demo.tcp.TCPCommand;

@Model
public abstract class IoTObject implements IoTConnection{
	
	private TCPClient tcpClient;
	private InetAddress host;
	private int port;
	
	public IoTObject(){
		
	}
	
	public IoTObject(InetAddress serverAddress, int serverPort){
		host = serverAddress;
		port = serverPort;
	}
	
	public void setHost(InetAddress serverAddress, int serverPort) {
		host = serverAddress;
		port = serverPort;
	}
	
	public String getIP() {
		return host == null? null : host.getHostAddress();
	}
	
	public int getPort() {
		return port;
	}
	
	public void connect() throws Exception{
		if (tcpClient == null)
			tcpClient = new TCPClient(host, port);
	}
	
	public void disconnect() throws IOException {
		if (tcpClient != null) {
			tcpClient.close();
			tcpClient = null;
		}
	}
	public boolean ping() throws IOException {
		if (tcpClient != null) {
			String response = tcpClient.sendCommand("ping");
			if (response != null && response.contains(TCPClient.TCP_OK)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isConnected() {
		try {
			return ping();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public void sendCommand(TCPCommand c) {
		String rawtcp = null;
		if (c == TCPCommand.GAMESTART)
			rawtcp = "GSTR";
		else if (c == TCPCommand.PING)
			rawtcp = "ping";
		else if (c == TCPCommand.GAMESTOP)
			rawtcp = "GG";
		else if (c == TCPCommand.GUNSTART)
			rawtcp = "START";
		else if (c == TCPCommand.TXTEST)
			rawtcp = "T_X";
		try {
			String response = tcpClient.sendCommand(rawtcp);
			while (response.contains(TCPClient.TCP_NC)) {
				System.out.println("response is not ok, resending command " + response);
				response = tcpClient.sendCommand(rawtcp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to send message to tcpClient due to an error " + e.getMessage());
		}
		
	}
	
	public String getData() throws IOException{
		int count = 0;
		String rxData = null;
		while ((rxData = tcpClient.getData()) == null  && count < 2) {
			count++;
		}
        return rxData;
	}
}
