package io.openliberty.sentry.demo.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import io.openliberty.sentry.demo.model.game.stat.GameStat;

public class MongoDBConnector {

	private static final String DBNAME = "demodb"; 
	private static final String COLLECTION = "gamestats"; 
	private static final String TESTDBNAME = "testdb"; 
	private static MongoDBConnector instance;
	private MongoDatabase database;
	private MongoClient mongoClient;
	private MongoCollection<Document> statsCollection;
	
	private MongoDBConnector(boolean testDB) {
		mongoClient = new MongoClient("localhost", 27017);
		if (testDB)
			database = mongoClient.getDatabase(TESTDBNAME);
		else
			database = mongoClient.getDatabase(DBNAME);
		
		if (!!!isCollectionExist(COLLECTION)) {
			database.createCollection(COLLECTION);
		}
		statsCollection = database.getCollection(COLLECTION);
	}
	
	public static MongoDBConnector getInstance(boolean testDB) {
		if (instance == null) {
			instance = new MongoDBConnector(testDB);
		}
		return instance;
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
		inserttodb(mongoStat);
	}
	
	private void inserttodb(MongoGameStat stat){
		Document data = new Document();
		data.put("_id", stat.getId());
		data.put("playerId", stat.getPlayerId());
		data.put("score", stat.getScore());
		this.statsCollection.insertOne(data);;
	}
	
	public GameStat getStat() {
		return null;
	}
	
	public void dropTestCollection() {
		if (database.getName().equals(TESTDBNAME)) {
			statsCollection.drop();
		}
	}
	
	public List<MongoGameStat> topfive(){
		AggregateIterable<Document> output = this.statsCollection.aggregate(Arrays.asList(
		        //new Document("$group", new Document("_id","$_id").append("score", new Document("$max","$score"))),
		        new Document("$project",new Document("_id","$_id").append("playerId", "$playerId").append("score", 1)),
		        new Document("$sort", new Document("score", -1)),
		        new Document("$limit", 5)
				));
		
		List<MongoGameStat> topFive = new ArrayList<>();
		for (Document dbObject : output)
		{
		    System.out.println(dbObject);
		    topFive.add(new MongoGameStat(dbObject.get("playerId").toString(),dbObject.getObjectId("_id"),Integer.parseInt(dbObject.get("score").toString())));
			
		}
   	 	return topFive;

	}
	
	
}
