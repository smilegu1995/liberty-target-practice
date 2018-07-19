package io.openliberty.sentry.demo.model.game.stat;

import java.util.concurrent.atomic.AtomicInteger;

public class GameStat implements Comparable<GameStat>{
	
	private static final AtomicInteger gameCounter = new AtomicInteger(0); 
	private int gameId;
	private int score;
	private String playerId;
	private int rank;
	
	public GameStat(String playerId, int score) {
		gameId = gameCounter.incrementAndGet();
		this.score = score;
		this.playerId = playerId;
	}
	
	public int getGameId() {
		return gameId;
	}

	public int getScore() {
		return score;
	}

	public String getPlayerId() {
		return playerId;
	}

	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	@Override
	public int compareTo(GameStat o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
