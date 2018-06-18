package io.openliberty.sentry.demo.game.sse.client;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

@Path("/gamestreamclient")
public class GameStreamClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
	public Response runAsClient() {
    	List<String> eventList = new ArrayList<String>();
        String port = System.getProperty("default.http.port");
        String war = System.getProperty("app.context.root");
        
        Client client = ClientBuilder.newClient();
        client.register(StringReader.class);
        WebTarget target = client.target("http://localhost:" + port + "/" + war + "/gameapp/game/gamestreamtest");
        try (SseEventSource source = SseEventSource.target(target).build()) {
            source.register(new Consumer<InboundSseEvent>(){

            @Override
            public void accept(InboundSseEvent event) {
                System.out.println("Received new event: " + event);
                // called when we receive a new event
                String hitEvent = event.readData(String.class);
                eventList.add(hitEvent);
                System.out.println("New Event " + hitEvent);
            }}, new Consumer<Throwable>(){

              @Override
              public void accept(Throwable t) {
                  // called when something went wrong
                  t.printStackTrace();
              }}, new Runnable() {

              @Override
              public void run() {
                    // called when our connection is closed
                  System.out.println("All done for now!");
              }});

            source.open();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return toResponse(eventList);
	}
	
    private static Response toResponse(List<String> playList) {
        StringBuilder sb = new StringBuilder("Target Hits:");
        for (String s : playList) {
            sb.append(s);
        }
        return Response.ok(sb.toString()).build();
    }
}
