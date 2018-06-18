package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Game implements Runnable{
	
	private static TargetArray targets;
	
	private boolean running = false;
	
	private AtomicBoolean iswaiting = new AtomicBoolean(false);
	
	public static final int GAMETIME = 60000;
	
	private static Game gameinstance = new Game();
	
	public Game() {
		try {
			targets = new TargetArray();
			targets.setHost(InetAddress.getByName("192.168.0.11"), 80);
			//targets.setHost(InetAddress.getByName("localhost"), 58784);
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
		if (!!!gameinstance.isRunning()) {
			gameinstance.start();
		}
		return gameinstance;
	}
	
	public boolean test() throws IOException {
		return targets.ping();
	}
	
	
    public boolean isRunning() {
        return running;
    }
    
    public void stopGameCycle() {
    	running = false;
    	iswaiting.set(false);;
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
	
	public synchronized void waitForHitUpdate(){
        try {
        	System.out.println("putting the thread to wait");
        	iswaiting.set(true);
        	wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Start running on new thread " + String.valueOf(running));
		while (running){
			if (iswaiting.get()) {
				try {
					String rxData = targets.getData();
					System.out.println("received rxData: "+ rxData);
					if (rxData != null) {
			            synchronized(this) {
			            	iswaiting.set(false);;
			                this.notifyAll();
			            }
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		targets.stopGameCycle();
		//read tcp message in while loop
	}
}
