package io.openliberty.sentry.demo.model;

import java.net.InetAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Game {
	
	@Inject
	private TargetArray targets;
	
	public boolean test() throws Exception{
		targets.setHost(InetAddress.getByName("localhost"), 62679);
		targets.connect();
		return targets.ping();
	}
}
