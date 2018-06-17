package io.openliberty.sentry.demo.game;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

	@Inject
	private Game game;
	
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
			result = String.valueOf(game.test());
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
    	game.startGameCycle();
    	
        Runnable r = new Runnable() {
            @Override
            public void run() {
            	long start = System.currentTimeMillis();
            	long end = start + Game.GAMETIME * 1000;
            	int hitCount = 0;
            	while (System.currentTimeMillis() < end){
                    game.waitForHitUpdate();
                    OutboundSseEvent event = sse.newEventBuilder()
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(String.class, "hit")
                            .build();
                        eventSink.send(event);
                        System.out.println("Sending data "+ "hit");
            	}
            }
        };
        new Thread(r).start();   	
    }
}
