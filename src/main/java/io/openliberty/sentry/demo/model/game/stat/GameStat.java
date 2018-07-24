package io.openliberty.sentry.demo.model.game.stat;

import java.util.concurrent.atomic.AtomicInteger;

import io.openliberty.sentry.demo.mongodb.MongoGameStat;

public class GameStat {
	
	private static final AtomicInteger gameCounter = new AtomicInteger(0); 
	private int gameId;
	private int score = 0;
	private String playerId;
	private int rank;
	
	public GameStat(String playerId) {
		gameId = gameCounter.incrementAndGet();
		this.playerId = playerId;
	}
	
	public GameStat(MongoGameStat ms) {
		gameId = ms.getId().getCounter();
		playerId = ms.getPlayerId();
		score = ms.getScore();
	}
	
	/**
	 * for testing only
	 * @param playerId
	 * @param score
	 */
	public GameStat(String playerId, int score) {
		gameId = gameCounter.incrementAndGet();
		this.playerId = playerId;
		this.score = score;
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

	public void incrementScore() {
		this.score += 100;
	}
		
}
