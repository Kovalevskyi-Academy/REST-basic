package io.hexlet.java.links.resources;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Random;

// my.site.com/api/links
@Path("links")
public class LinkResource {

    private static final Table LINKS_TABLE;

    static {
        final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        final DynamoDB dynamoDB = new DynamoDB(client);

        LINKS_TABLE = dynamoDB.getTable("Links");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public Response getUrlById(final @PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Item item = LINKS_TABLE.getItem("id", id);
        final String url = item.getString("url");
        if (url == null || url == "") {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(url).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response shortUrl(final String url) {
        int attempt = 0;
        while (attempt < 5) {
            final String id = getRandomId();
            final Item urlRecord = new Item()
                    .withPrimaryKey("id", id)
                    .withString("url", url);
            try {
                LINKS_TABLE.putItem(
                        new PutItemSpec()
                                .withConditionExpression("attribute_not_exists(id)")
                                .withItem(urlRecord));
                return Response.ok(id).build();
            } catch (ConditionalCheckFailedException e) {
                // attempt to write failed, ID - exists.
            }
            attempt++;
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    private static String getRandomId() {
        String possibleCharacters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder idBuilder = new StringBuilder();
        Random rnd = new Random();
        while (idBuilder.length() < 5) {
            int index = (int) (rnd.nextFloat() * possibleCharacters.length());
            idBuilder.append(possibleCharacters.charAt(index));
        }
        return idBuilder.toString();
    }
}
