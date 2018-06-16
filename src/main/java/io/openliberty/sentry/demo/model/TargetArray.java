package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import javax.enterprise.inject.Model;

@Model
public class TargetArray extends IoTObject {

	public TargetArray(){
		super();
	}
	
	public TargetArray(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public void activateAllTargets(){
		
	}
	
	public void deactivateAllTargets(){
		
	}
	
	public void cycleAllTargets(){
		
	}
	
	public void setTargetState(){
		
	}

}
