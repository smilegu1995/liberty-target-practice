package io.openliberty.sentry.demo.model.game.stat;

import java.util.ArrayList;
import java.util.List;

import io.openliberty.sentry.demo.mongodb.MongoDBConnector;
import io.openliberty.sentry.demo.mongodb.MongoGameStat;

public class GameStatsManager {

	private MongoDBConnector mongoConnector;
	private static GameStatsManager instance;
	
	private GameStatsManager() {
		mongoConnector = MongoDBConnector.getInstance(false);
	}
	
	public static GameStatsManager getInstance() {
		if (instance == null) {
			instance = new GameStatsManager();
		}
		return instance;
	}
	
	public void writeGameStat(GameStat stat) {
		mongoConnector.insertStat(stat);
	}
	
	public List<GameStat> getTopFiveScoreStats() {
		List<GameStat> topFive = new ArrayList<>();
		List<MongoGameStat> topFiveMongo = mongoConnector.topfive(); 
		for (MongoGameStat ms: topFiveMongo) {
			topFive.add(new GameStat(ms));
		}
		
		return topFive;
	}
}
