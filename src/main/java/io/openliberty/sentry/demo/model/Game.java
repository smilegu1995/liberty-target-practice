package io.openliberty.sentry.demo.model;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;

import io.openliberty.sentry.demo.model.game.stat.GameStat;
import io.openliberty.sentry.demo.model.game.stat.GameStatsManager;


@ApplicationScoped
public class Game implements Runnable{
	
	private TargetArray targets;
	
	private boolean running = false;
	
	private AtomicBoolean iswaiting = new AtomicBoolean(false);
	private GameStat stat;
	private GameStatsManager statsManager;
	boolean isPracticeGame = false;
	
	public static final int GAMETIME = 60000;
	
	public Game(GameStat stat) throws Exception {
		this(stat, false);
	}
	
	public Game(GameStat stat, boolean isPractice) throws Exception {
		this.stat = stat;
		targets = TargetArray.getInstance();
		if (targets == null) {
			throw new Exception("Targets array is not connected. Game cannot be started");
		}
		isPracticeGame = isPractice;
	}
	
	public boolean test() throws IOException {
		return targets.ping();
	}
	
	public boolean isPracticeGame() {
		return isPracticeGame;
	}
	
    public boolean isRunning() {
        return running;
    }
    
    public void stopGameCycle() throws Exception {
    	System.out.println("Stop game cycle");
    	if (!!!isPracticeGame)
    		writeScore();
    	running = false;
    	iswaiting.set(false);
    	targets.stopGameCycle();
    }
	
    public void startGameCycle() throws Exception {
    	if (!!!running)
    		running = true;
		try {
			targets.connect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		targets.startGameCycle();
    }
    
    public void testGameCycle() throws Exception {
		targets.testGameCycle();
    }
    
    public void start() {
    	running = true;
    	targets.cycleAllTargets();
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
    
    public void reset() throws Exception{
    	System.out.println("resetting the game");
    	running = false;
        synchronized(this) {
        	iswaiting.set(false);
            this.notifyAll();
        }
    	targets.stopGameCycle();
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
					if (rxData != null) {
						if (rxData.contains("hit")) {
				            synchronized(this) {
				            	iswaiting.set(false);
				            	updateScore();
				                this.notifyAll();
				            }
						} else if (rxData.contains("end")){
				            synchronized(this) {
				            	iswaiting.set(false);
				            	running = false;
				                this.notifyAll();
				            }
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					running = false;
				}				
			}
		}
		System.out.println("finished running on game thread");
	}
	
	private void writeScore() {
		statsManager = GameStatsManager.getInstance();
		statsManager.writeGameStat(stat);
	}
	
	public synchronized void updateScore(){
		stat.incrementScore();
	}
	
	public int getScore(){
		return stat.getScore();
	}
}
