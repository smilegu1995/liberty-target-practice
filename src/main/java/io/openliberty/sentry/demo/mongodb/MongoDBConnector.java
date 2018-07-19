package io.openliberty.sentry.demo.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import io.openliberty.sentry.demo.model.game.stat.GameStat;

public class MongoDBConnector {

	private static final String DBNAME = "demodb"; 
	private static final String COLLECTION = "gamestats"; 
	private static MongoDBConnector instance;
	private MongoDatabase database;
	private MongoClient mongoClient;
	private  MongoCollection statsCollection;
	
	private MongoDBConnector() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase(DBNAME);
		if (!!!isCollectionExist(COLLECTION)) {
			database.createCollection(COLLECTION);
		}
		statsCollection = database.getCollection(COLLECTION);
	}
	
	private boolean isCollectionExist(final String cname) {
	    MongoIterable<String> collectionNames = database.listCollectionNames();
	    for (final String name : collectionNames) {
	        if (name.equalsIgnoreCase(cname)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void insertStat(GameStat stat) {
		MongoGameStat mongoStat = new MongoGameStat(stat);
	}
	
	public GameStat getStat() {
		return null;
	}
	
	
}
