package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import io.openliberty.sentry.demo.tcp.TCPCommand;

public class Ship extends IoTObject {

	private static Ship instance; 
	
	public Ship(){
		super();
	}
	
	public Ship(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public static Ship getInstance() {
		//boolean pingSuccessful = false;
		int retry = 2;
		while ((instance == null || !!!instance.isConnected()) && retry > 0) {
			try {
				instance = new Ship();
				
				String ip = System.getProperty("ship.ip");
				int port = Integer.valueOf(System.getProperty("ship.port"));
				
				instance.setHost(InetAddress.getByName(ip), port);
				instance.connect();
				retry--;
				//pingSuccessful = instance.ping();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retry--;
			}
		}

		return instance.isConnected() ? instance : null;
	}
	
	public void startShip() throws Exception {
		sendCommand(TCPCommand.T_GAMESTART);
	}
	
	public void panShip(String value) throws Exception {
		sendCommand(TCPCommand.S_PANSHIP, value);
	}
	
	public void tiltShip(String value) throws Exception {
		sendCommand(TCPCommand.S_TILTSHIP, value);
	}
	
	public void fireLaser(){
		sendCommand(TCPCommand.S_FIRELASER);		
	}
	
	public void stopShip() throws Exception {
		sendCommand(TCPCommand.T_GAMESTOP);
		disconnect();
	}
}
