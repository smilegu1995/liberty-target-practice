package io.openliberty.sentry.demo.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.*;

import io.openliberty.sentry.demo.model.game.stat.GameStat;

public class MongoDBConnector {

	private static final String DBNAME = "demodb"; 
	private static final String COLLECTION = "gamestats"; 
	private static MongoDBConnector instance;
	private MongoDatabase database;
	private MongoClient mongoClient;
	private MongoCollection statsCollection;
	private ArrayList<MongoGameStat> TopFiveList;
	
	public MongoDBConnector() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase(DBNAME);
		TopFiveList = new ArrayList<MongoGameStat>();
		if (!!!isCollectionExist(COLLECTION)) {
			database.createCollection(COLLECTION);
		}
		statsCollection = database.getCollection(COLLECTION);
		statsCollection.drop();
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
		inserttodb(mongoStat);
	}
	
	public void inserttodb(MongoGameStat stat){
		Document data = new Document();
		data.put("_id", stat.getId());
		data.put("name", stat.getPlayerId());
		data.put("score", stat.getScore());
		this.statsCollection.insertMany(Arrays.asList(data));
	}
	
	public GameStat getStat() {
		return null;
	}
	
	public JsonObject topfive(){
		TopFiveList.clear();
		AggregateIterable<Document> output = this.statsCollection.aggregate(Arrays.asList(
		        //new Document("$group", new Document("_id","$_id").append("score", new Document("$max","$score"))),
		        new Document("$project",new Document("_id","$_id").append("name", "$name").append("score", 1)),
		        new Document("$sort", new Document("score", -1)),
		        new Document("$limit", 5)
				));
		
		
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (Document dbObject : output)
		{
		    System.out.println(dbObject);
		    TopFiveList.add(new MongoGameStat(dbObject.get("name").toString(),dbObject.getObjectId("_id"),Integer.parseInt(dbObject.get("score").toString())));
		    builder.add(dbObject.get("name").toString(), dbObject.get("score").toString());
			
		}
   	 	return builder.build();

	}
	
	public ArrayList<MongoGameStat> topfivelist(){
		return TopFiveList;
	}
	
	
}
