package io.hexlet.java.links.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class LinkResourceTest {

    public static final String BASE_URI = "http://localhost:8080/";

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = startServer();
        final Client c = ClientBuilder.newClient();
        target = c.target(BASE_URI);
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

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    private static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("io.hexlet.java.links.resources");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
}
