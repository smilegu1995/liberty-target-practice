package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Game implements Runnable{
	
	private TargetArray targets;
	
	private boolean running;
	
	public static final int GAMETIME = 60000;
	
	public static Game gameinstance = new Game();
	
	public Game() {
		try {
			targets = new TargetArray();
			targets.setHost(InetAddress.getByName("192.168.0.11"), 80);
			targets.connect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to create a game due to incorrect target host and ip");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to create a game due to incorrect target host and ip");
			e.printStackTrace();
		}

	}
	
	public synchronized static Game getInstance() {
		if (gameinstance == null) {
			return new Game();
		}
		return gameinstance;
	}
	
	public boolean test() throws IOException {
		return targets.ping();
	}
	
	public void startGameCycle() {
		if (!!!gameinstance.isRunning()) {
			gameinstance.start();
		}
	}
	
    public boolean isRunning() {
        return running;
    }
    
    public void stopGameCycle() {
    	running = false;
    }
	
    public void start() {
        running = true;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
	
	public synchronized void waitForHitUpdate(){
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		targets.startGameCycle();
		while (running){
			try {
				String rxData = targets.getData();
				if (rxData != null) {
		            synchronized(this) {
		                this.notifyAll();
		            }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		targets.stopGameCycle();
		//read tcp message in while loop
	}
}
