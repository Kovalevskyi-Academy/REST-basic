package io.hexlet.java.links.resources;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

// my.site.com/api/links
@Path("links")
public class LinkResource {

    private static final LinksService LINKS_SERVICE;

    static {
        LINKS_SERVICE = LambdaInvokerFactory.builder()
                .lambdaClient(AWSLambdaClientBuilder.defaultClient())
                .build(LinksService.class);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public Response getUrlById(final @PathParam("id") String id) {
        final String url = LINKS_SERVICE.getLink(id);
        if (url == null || url == "") {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(url).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response shortUrl(final String url) {
        final String id = LINKS_SERVICE.addLink(url);
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(id).build();
    }
}
