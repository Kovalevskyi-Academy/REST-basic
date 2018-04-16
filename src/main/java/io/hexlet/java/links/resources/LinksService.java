package io.hexlet.java.links.resources;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface LinksService {

    @LambdaFunction(functionName="linksAdder")
    String addLink(String link);

    @LambdaFunction(functionName="getLink")
    String getLink(String id);
}
