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
	
	static Ship spaceShip = null;
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		// (lifecycle) Called when the connection is opened
		System.out.println("I'm open!");
		spaceShip = Ship.getInstance();
		
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		// (lifecycle) Called when the connection is closed
		System.out.println("I'm closed!");
	}

	@OnMessage
	public void receiveMessage(String message, Session session) throws IOException {
		// Called when a message is received. 
		// Single endpoint per connection by default --> @OnMessage methods are single threaded!
		// Endpoint/per-connection instances can see each other through sessions.

		System.out.println("I got a message: " + message);
		// Send something back to the client for feedback
		if (spaceShip != null) {
			if (message.equals("fireLaser")) {
				spaceShip.sendCommand(TCPCommand.S_FIRELASER);
			} else {
				spaceShip.sendCommand(TCPCommand.S_PANSHIP, message);
			}
		}
			
					
	}

	@OnError
	public void onError(Throwable t) {
		// (lifecycle) Called if/when an error occurs and the connection is disrupted
		System.out.println("oops " + t.getMessage());
	}
}
