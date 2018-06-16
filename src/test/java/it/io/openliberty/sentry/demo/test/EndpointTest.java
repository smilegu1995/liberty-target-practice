package it.io.openliberty.sentry.demo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.Test;

public class EndpointTest {
	
    @Test
    public void testGetProperties() {
        // tag::systemProperties[]
        String port = System.getProperty("liberty.test.port");
        String war = System.getProperty("war.name");
        String url = "http://localhost:" + port + "/" + war + "/";
        System.out.println(url);
        // end::systemProperties[]

        // tag::clientSetup[]
        Client client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
        // end::clientSetup[]

        // tag::request[]
        WebTarget target = client.target(url + "sentry-challenge/properties");
        Response response = target.request().get();
        // end::request[]

        // tag::response[]
        assertEquals("Incorrect response code from " + url, 200, response.getStatus());
        // end::response[]

        // tag::body[]
        JsonObject obj = response.readEntity(JsonObject.class);

        assertEquals("The system property for the local and remote JVM should match",
                     System.getProperty("os.name"),
                     obj.getString("os.name"));
        // end::body[]
        response.close();
    }
    
    @Test
    public void testGameEndpoints() {
        // tag::systemProperties[]
        String port = System.getProperty("liberty.test.port");
        String war = System.getProperty("war.name");
        String url = "http://localhost:" + port + "/" + war + "/";
        System.out.println(url);
        // end::systemProperties[]

        // tag::clientSetup[]
        Client client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
        // end::clientSetup[]

        // tag::request[]
        WebTarget target = client.target(url + "gameapp/game");
        Response response = target.request().get();
        // end::request[]

        // tag::response[]
        assertEquals("Incorrect response code from " + url, 200, response.getStatus());
        // end::response[]

        // tag::body[]
        JsonObject obj = response.readEntity(JsonObject.class);

        assertEquals("The json object should contain getGameStat",
                     "getGameStat",
                     obj.getString("game"));
        // end::body[]
        response.close();
    }
    /*
    @Test
    public void testGameDataStream() throws Throwable {
        String port = System.getProperty("liberty.test.port");
        String war = System.getProperty("war.name");
        String url = "http://localhost:" + port + "/" + war + "/";
        System.out.println("Trying to invoke: " + url);
        GetMethod get = new GetMethod(url + "gameapp/game/gamestream");
        HttpClient httpclient = new HttpClient();
        int result = httpclient.executeMethod(get);

        InputStream inputStream = get.getResponseBodyAsStream();

        byte[] buffer = new byte[1024 * 1024 * 5];
        String path = null;
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            path = new String(buffer);
        }
        System.out.println("Response of service: " + path);
        System.out.println("==================");

        assertTrue(200 == result);
    }*/
}
