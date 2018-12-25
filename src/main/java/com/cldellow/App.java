package com.cldellow;

import java.util.ArrayList;
import java.util.Arrays;
import dk.brics.automaton.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class App implements RequestHandler<Request, Response>
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

        String[] strings = Arrays.copyOf(hits.toArray(), hits.size(), String[].class);
        return new Response(strings, null);
    }

    public Response handleRequest(Request request, Context context) {
        return match(request.getHaystack(), request.getNeedle(), context);
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
