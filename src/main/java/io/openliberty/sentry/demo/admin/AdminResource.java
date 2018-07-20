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

import io.openliberty.sentry.demo.model.Ship;
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
    		String ship_ip = "n/a";
        	int ship_port = -1;
        	boolean ship_connected = false;
        	Ship spaceShip = Ship.getInstance();
        	if (spaceShip != null) {
            	ship_ip = spaceShip.getIP();
            	ship_port = spaceShip.getPort();
            	ship_connected = spaceShip.isConnected();
        	}
        	
        	builder.add("result", "success");
        	builder.add("ship_ip", ship_ip);
        	builder.add("ship_port", String.valueOf(ship_port));
        	builder.add("ship_connected", String.valueOf(ship_connected));
    		return builder.build();
    		
    	}

		return builder.build();
	}
    
    @POST
    @Path("txcmd/{device}/{cmd}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject postEspCmd(@PathParam("device")String device, @PathParam("cmd")String cmd) {
        // tag::method-contents[]
    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	//corner cases
    	if (device == null || device.isEmpty()) {
        	builder.add("result", "failed");
        	builder.add("reason", "device not specified");
        	return builder.build();   
    	}
    	
    	if (!!!device.equals("targets") && !!!device.equals("ship")) {
        	builder.add("result", "failed");
        	builder.add("reason", "device unknown");
        	return builder.build();   
    	}
    	
    	if (device.equals("targets")) {
    		TCPCommand tcmd = TCPUtils.convertRequestCmdStringToTCPCommand(device, cmd);
    		if (tcmd != null) {
    			TargetArray targets = TargetArray.getInstance();
    			targets.sendCommand(tcmd);
    		} else {
    			builder.add("result", "failed");
            	builder.add("reason", "targets command unknown");
            	return builder.build();
    		}
    	}
    	
    	if (device.equals("ships")) {
    		TCPCommand tcmd = TCPUtils.convertRequestCmdStringToTCPCommand(device, cmd);
    		if (tcmd != null) {
    			Ship spaceShip = Ship.getInstance();
    			spaceShip.sendCommand(tcmd);
    		} else {
    			builder.add("result", "failed");
            	builder.add("reason", "targets command unknown");
            	return builder.build();
    		}
    	}
    	
    	 
    	builder.add("result", "success");
    	builder.add("cmd", cmd);
    	builder.add("device", device);
    	 
		return builder.build();    	
    }
}
