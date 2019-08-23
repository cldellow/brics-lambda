package com.cldellow;

import java.util.ArrayList;
import com.github.openjson.*;
import java.util.Arrays;
import java.util.HashMap;
import dk.brics.automaton.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    public Response match(String haystack, String needle, Context context) {
        ArrayList<String> hits = new ArrayList<String>();
        RegExp r;
        try {
            r = new RegExp(needle);
        } catch (IllegalArgumentException iae) {
            return new Response(null, iae.getMessage());
        }

        RunAutomaton a = new RunAutomaton(r.toAutomaton());

        AutomatonMatcher m = a.newMatcher(haystack);
        while(m.find()) {
            hits.add(m.group());
        }

        int len = hits.size();
        if(len > 10)
            len = 10;
        String[] strings = Arrays.copyOf(hits.toArray(), len, String[].class);
        return new Response(strings, null);
    }

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent gwRequest, Context context) {

        System.out.println(gwRequest.getBody());
        JSONObject request = new JSONObject(gwRequest.getBody());
        Response r = match(request.get("haystack").toString(), request.get("needle").toString(), context);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");

        APIGatewayProxyResponseEvent gwResponse = new APIGatewayProxyResponseEvent();

        gwResponse.withStatusCode(200);
        gwResponse.withHeaders(headers);


        JSONObject body = new JSONObject();
        if(r.error != null) {
            gwResponse.withStatusCode(400);
            body.put("error", r.error);
        } else {
            JSONArray hits = new JSONArray();
            for(int i = 0; i < r.hits.length; i++) {
                hits.put(r.hits[i]);
            }
            body.put("hits", hits);
        }

        gwResponse.withBody(body.toString());
        return gwResponse;
    }

    public static void main( String[] args )
    {
        String haystack = args[0];
        String needle = args[1];
        Response rv = (new App()).match(haystack, needle, null);

        if(rv.error != null) {
            System.err.println(rv.error);
            System.exit(1);
        }

        for(int i = 0; i < rv.getHits().length; i++) {
            System.out.println(rv.getHits()[i]);
        }
    }
}
