package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Game implements Runnable{
	
	private TargetArray targets;
	
	private boolean running = false;
	
	private long lastScoreTime;
	private AtomicBoolean iswaiting = new AtomicBoolean(false);
	private AtomicInteger score = new AtomicInteger(0);
	
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
    	System.out.println("Stop game cycle");
    	running = false;
    	iswaiting.set(false);;
    	targets.stopGameCycle();
    }
	
    public void startGameCycle() {
    	if (!!!running)
    		running = true;
		targets.startGameCycle();
    }
    
    public void testGameCycle() {
		targets.testGameCycle();
    }
    
    public void start() {
    	running = true;
    	lastScoreTime = System.currentTimeMillis();
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
		System.out.println("Start game on new thread " + String.valueOf(running));
		while (running){
			if (iswaiting.get()) {
				System.out.println("Detect the other thread in wait");
				try {
					System.out.println("Entering try block");
					String rxData = targets.getData();
					System.out.println("received rxData: "+ rxData);
					if (rxData != null && rxData.contains("hit")) {
			            synchronized(this) {
			            	iswaiting.set(false);
			            	updateScore();
			            	lastScoreTime = System.currentTimeMillis();
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
	
	public synchronized void updateScore(){
		long scoreInterval = (System.currentTimeMillis() - lastScoreTime) / 1000;
		int timeBonus = 10;
		if (scoreInterval != 0)
			timeBonus += (int) (100 / scoreInterval);
		System.out.println("Score 50 + Time Bonus " + timeBonus);
		score.addAndGet(50);
		score.addAndGet(timeBonus);
	}
	
	public int getScore(){
		return score.get();
	}
}
