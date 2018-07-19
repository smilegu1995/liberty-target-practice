package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import io.openliberty.sentry.demo.tcp.TCPCommand;

enum FireMode {
	LASER,
	NERF
}

public class Ship extends IoTObject {

	public Ship(){
		super();
	}
	
	public Ship(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public void startShip() throws Exception {
	}
	
	public void fireLaser(){
		
	}
	

}
