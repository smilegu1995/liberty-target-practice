package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import io.openliberty.sentry.demo.tcp.TCPCommand;

enum FireMode {
	LASER,
	NERF
}

public class SentryTurret extends IoTObject {

	public SentryTurret(){
		super();
	}
	
	public SentryTurret(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public void startGun() throws Exception {
		sendCommand(TCPCommand.GUNSTART);
	}
	
	public void fireLaser(){
		
	}
	
	public void fireNerf(){
		
	}
	
	public void fire(FireMode mode){
		
	}
	

}
