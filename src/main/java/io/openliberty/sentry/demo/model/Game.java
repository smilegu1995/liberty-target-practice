package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Game implements Runnable{
	
	@Inject
	private TargetArray targets;
	
	private boolean running;
	
	public static final int GAMETIME = 60000;
	
	public boolean test() throws Exception{
		targets.setHost(InetAddress.getByName("localhost"), 62679);
		targets.connect();
		return targets.ping();
	}
	
	public void startGameCycle() {
		targets.startGameCycle();
	}
	
    public void start() {
        running = true;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
	
	public void waitForHitUpdate(){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		targets.startGameCycle();
		while (running){
			
		}
		
		//read tcp message in while loop
	}
}
