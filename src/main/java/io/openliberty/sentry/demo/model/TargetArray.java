package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

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
		int retry = 2;
		while ((instance == null || !!!instance.isConnected()) && retry > 0) {
			try {
				instance = new TargetArray();
				
				String ip = System.getProperty("targets.ip");
				int port = Integer.valueOf(System.getProperty("targets.port"));
				
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
