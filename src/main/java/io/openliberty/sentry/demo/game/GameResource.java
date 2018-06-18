package io.openliberty.sentry.demo.game;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import io.openliberty.sentry.demo.model.Game;

//tag::header[]
//tag::cdi-scope[]
//end::cdi-scope[]
@ApplicationScoped
@Path("game")
public class GameResource {

	static Game game;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public JsonObject getGameStat() {
    	
        // tag::method-contents[]
    	 JsonObjectBuilder builder = Json.createObjectBuilder();
    	 builder.add("game", "getGameStat");
    	 
		return builder.build();
	}
	
    // tag::listContents[]
    @GET
    @Path("leaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject listLeaderBoard() {
        // tag::method-contents[]
    	 JsonObjectBuilder builder = Json.createObjectBuilder();
    	 builder.add("game", "listleaderboard");
    	 return builder.build();

    	 // end::method-contents[]
    }
    // end::listContents[]
    
    @POST
    public JsonObject newGame(){
        // tag::method-contents[]
    	 JsonObjectBuilder builder = Json.createObjectBuilder();
    	 String result = "no result";
    	 try {
			//result = String.valueOf(game.test());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 builder.add("game", "newGame");
    	 builder.add("result", result);
    	 return builder.build();
    }
    
    @GET
    @Path("gamestream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void gameDataStream(@Context SseEventSink eventSink, @Context Sse sse){
        Runnable r = new Runnable() {
            @Override
            public void run() {
            	game = Game.getInstance();
            	game.startGameCycle();
            	long start = System.currentTimeMillis();
            	long end = start + Game.GAMETIME;
            	int hitcount = 0;
            	while (System.currentTimeMillis() < end && hitcount < 5){
                    game.waitForHitUpdate();
                    hitcount++;
                    OutboundSseEvent event = sse.newEventBuilder()
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(String.class, "score " + game.getScore())
                            .build();
                    eventSink.send(event);
                    System.out.println("Sending data "+ "hit" + hitcount);
            	}
            	game.stopGameCycle();
            }
        };
        new Thread(r).start();   	
    }
    
    @GET
    @Path("gamestreamtest")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void gameDataStreamTest(@Context SseEventSink eventSink, @Context Sse sse){
        Runnable r = new Runnable() {
            @Override
            public void run() {
            	long start = System.currentTimeMillis();
            	long end = start + Game.GAMETIME;
            	int hitcount = 0;
            	int score = 0;
            	while (System.currentTimeMillis() < end && hitcount < 5){
                    score += (hitcount * 50);
                    OutboundSseEvent event = sse.newEventBuilder()
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(String.class, "score " + score)
                            .build();
                    eventSink.send(event);
                    System.out.println("Sending data "+ "hit" + hitcount);
                    try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
            	}
                hitcount++;
            }
        };
        new Thread(r).start();   	
    }
}
