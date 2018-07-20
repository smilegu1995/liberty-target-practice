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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.openliberty.sentry.demo.model.TargetArray;
import io.openliberty.sentry.demo.tcp.TCPCommand;
import io.openliberty.sentry.demo.tcp.TCPUtils;

@ApplicationScoped
@Path("admin")
public class AdminResource {

	
    @GET
    @Path("devices/{device}")
    @Produces(MediaType.APPLICATION_JSON)
	public JsonObject getDevicesStat(@PathParam("device")String device) {
    	
        // tag::method-contents[]
    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	//return target and ship ip and port and connection status
    	if (device.equals("targets")) {
        	String targets_ip = "n/a";
        	int targets_port = -1;
        	boolean targets_connected = false;
        	TargetArray targets = TargetArray.getInstance();
        	if (targets != null) {
            	targets_ip = targets.getIP();
            	targets_port = targets.getPort();
            	targets_connected = targets.isConnected();
        	}
        	
        	builder.add("result", "success");
        	builder.add("targets_ip", targets_ip);
        	builder.add("targets_port", String.valueOf(targets_port));
        	builder.add("targets_connected", String.valueOf(targets_connected));
    		return builder.build();
    	}
    	
    	if (device.equals("ship")) {
    		
    	}

		return builder.build();
	}
    
    @POST
    @Path("txcmd/{device}/{cmd}/{value}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postEspCmd(@PathParam("device")String device, @PathParam("cmd")String cmd, @PathParam("value")String value) {
        // tag::method-contents[]
    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	//corner cases
    	if (device == null || device.isEmpty()) {
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: Device is not specified")
                        .build();
    	}
    	
    	if (!!!device.equals("targets") && !!!device.equals("ship")) {
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: Device unknown")
                        .build();  
    	}
    	
    	if (device.equals("targets")) {
    		TCPCommand tcmd = TCPUtils.convertRequestCmdStringToTCPCommand(device, cmd);
    		if (tcmd != null) {
    			TargetArray targets = TargetArray.getInstance();
    			if (targets != null)
    				targets.sendCommand(tcmd);
    			else
    				return Response.status(Response.Status.SERVICE_UNAVAILABLE)
    	                     .entity("ERROR: Device is not connected")
    	                         .build();
    		} else {
    			return Response.status(Response.Status.SERVICE_UNAVAILABLE)
	                     .entity("ERROR: unknown command")
	                         .build();
    		}
    	}
    	/*
    	if (device.equals("ships") && !!!targets.isCmdValid(cmd)) {
    		
    	}*/
    	
    	builder.add("result", "success"); 
    	return Response.ok(builder.build()).build(); 	
    }
}
