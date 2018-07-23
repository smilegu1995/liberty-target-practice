package io.openliberty.sentry.demo.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import io.openliberty.sentry.demo.model.Ship;
import io.openliberty.sentry.demo.tcp.TCPCommand;
import io.openliberty.sentry.demo.tcp.TCPUtils;

@ServerEndpoint(value = "/shipsocket")
public class ShipSocket {
	
	Ship spaceShip = null;
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) throws Exception {
		// (lifecycle) Called when the connection is opened
		System.out.println("Websocket open!");
		spaceShip = Ship.getInstance();
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// (lifecycle) Called when the connection is closed
		System.out.println("Websocket closed!");
	}

	@OnMessage
	public void receiveMessage(String message, Session session) throws Exception {
		// Called when a message is received. 
		// Single endpoint per connection by default --> @OnMessage methods are single threaded!
		// Endpoint/per-connection instances can see each other through sessions.

		System.out.println("Got a message: " + message);
		
		if (spaceShip != null) {
			if (message.equals("startShip")) {
				spaceShip.startShip();
			} 
			else if (message.equals("stopShip")) {
				spaceShip.stopShip();	
			}
			else if (message.contains("H=")) {
				String panAngle = message.substring(2).trim();
				System.out.println("PanAngle: " + panAngle);
				spaceShip.sendCommand(TCPCommand.S_PANSHIP, panAngle);
			}
			else if (message.contains("V=")) {
				String tiltAngle = message.substring(2).trim();
				System.out.println("PanAngle: " + tiltAngle);
				spaceShip.sendCommand(TCPCommand.S_TILTSHIP, tiltAngle);
			}
			else if (message.equals("fireLaser")) {
				spaceShip.sendCommand(TCPCommand.S_FIRELASER);
			} 
		}	
	}

	@OnError
	public void onError(Throwable t) {
		// (lifecycle) Called if/when an error occurs and the connection is disrupted
		System.out.println("Something went wrong! : " + t.getMessage());
	}
}
