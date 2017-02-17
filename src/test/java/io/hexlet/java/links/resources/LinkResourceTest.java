package io.hexlet.java.links.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.hexlet.java.links.Main;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LinkResourceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testUrlCreation() {
        final String url = "url";
        final String id = target
                .path("links")
                .request()
                .put(Entity.entity(url, MediaType.TEXT_PLAIN))
                .readEntity(String.class);
        final String resultUrl = target
                .path(String.format("links/%s", id))
                .request()
                .get(String.class);
        assertEquals(url, resultUrl);
    }
}
