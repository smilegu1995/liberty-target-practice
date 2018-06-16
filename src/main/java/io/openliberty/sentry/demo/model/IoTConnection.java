package io.openliberty.sentry.demo.model;

import java.io.IOException;

public interface IoTConnection {

	public void connect() throws Exception;
	public void disconnect() throws IOException;
	public boolean ping() throws IOException;
}
