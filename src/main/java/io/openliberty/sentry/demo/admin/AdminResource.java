package io.openliberty.sentry.demo.admin;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.openliberty.sentry.demo.model.TargetArray;

@ApplicationScoped
@Path("admin")
public class AdminResource {

	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public JsonObject getDevicesStat() {
    	
        // tag::method-contents[]
    	
    	//return target and ship ip and port and connection status
    	String targets_ip = "n/a";
    	int targets_port = -1;
    	boolean targets_connected = false;
    	TargetArray targets = TargetArray.getInstance();
    	if (targets != null) {
        	targets_ip = targets.getIP();
        	targets_port = targets.getPort();
        	targets_connected = targets.isConnected();
    	}


    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	builder.add("targets_ip", targets_ip);
    	builder.add("targets_port", String.valueOf(targets_port));
    	builder.add("targets_connected", String.valueOf(targets_connected));
		return builder.build();
	}
    
    @POST
    @Path("txcmd/{device}/{cmd}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject postEspCmd(@PathParam("device")String device, @PathParam("cmd")String cmd) {
        // tag::method-contents[]
    	 JsonObjectBuilder builder = Json.createObjectBuilder();
    	 builder.add("result", "success");
    	 builder.add("cmd", cmd);
    	 builder.add("device", device);
    	 
		return builder.build();    	
    }
}
