package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

enum FireMode {
	LASER,
	NERF
}

public class SentryTurret extends IoTObject {

	public SentryTurret(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public void fireLaser(){
		
	}
	
	public void fireNerf(){
		
	}
	
	public void fire(FireMode mode){
		
	}
	

}
