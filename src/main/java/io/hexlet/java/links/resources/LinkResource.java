package io.hexlet.java.links.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// my.site.com/api/links
@Path("links")
public class LinkResource {

    private static final AtomicInteger currentId = new AtomicInteger();

    private static final Map<String, String> links = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public Response getUrlById(final @PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final String url = links.get(id);
        if (url == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(url).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response shortUrl(final String url) {
        final int id = currentId.getAndIncrement();
        links.put(String.valueOf(id), url);
        return Response.ok(id).build();
    }
}
