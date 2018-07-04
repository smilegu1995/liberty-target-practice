package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import javax.enterprise.inject.Model;

import io.openliberty.sentry.demo.tcp.TCPCommand;

@Model
public class TargetArray extends IoTObject {

	private static TargetArray instance; 
	
	private TargetArray(){
		super();
	}
	
	private TargetArray(InetAddress serverAddress, int serverPort) {
		super(serverAddress, serverPort);
		// TODO Auto-generated constructor stub
	}
	
	public static TargetArray getInstance() {
		//boolean pingSuccessful = false;
		while (instance == null || !!!instance.isConnected()) {
			try {
				instance = new TargetArray();
				instance.setHost(InetAddress.getByName("192.168.0.103"), 5045);
				instance.connect();
				//pingSuccessful = instance.ping();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return instance;
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
		disconnect();
	}
	
	public void testGameCycle() throws Exception {
		sendCommand(TCPCommand.TXTEST);
	}

}
