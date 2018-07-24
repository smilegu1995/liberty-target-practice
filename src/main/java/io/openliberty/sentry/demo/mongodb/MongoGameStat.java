package io.openliberty.sentry.demo.mongodb;

import java.util.Date;

import org.bson.types.ObjectId;

import io.openliberty.sentry.demo.model.game.stat.GameStat;

public class MongoGameStat {
	
	private ObjectId id;
	private String playerId;
	private int score;
	
	public MongoGameStat(GameStat stat) {
		id = new ObjectId(new Date(System.currentTimeMillis()), stat.getGameId());
		playerId = stat.getPlayerId();
		score = stat.getScore();
	}
	
	public MongoGameStat(String playername, ObjectId oid, int playerscore) {
		id = oid;
		playerId = playername;
		score = playerscore;
	}
	
	public ObjectId getId() {
		return id;
	}
	public String getPlayerId() {
		return playerId;
	}
	public int getScore() {
		return score;
	}	
	public int getGameStatGameId() {
		return id.getCounter();
	}

}
