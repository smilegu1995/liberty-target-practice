package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.net.InetAddress;

import javax.enterprise.inject.Model;

import io.openliberty.sentry.demo.tcp.TCPCommand;

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
	
	public void startGameCycle() throws Exception {
		int count = 2;
		while (count != 0) {
			sendCommand(TCPCommand.GAMESTART);
			count--;
		}
		
	}
	
	public void stopGameCycle() throws Exception {
		sendCommand(TCPCommand.GAMESTOP);
	}
	
	public void testGameCycle() throws Exception {
		sendCommand(TCPCommand.TXTEST);
	}

}
