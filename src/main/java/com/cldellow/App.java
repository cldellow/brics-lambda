package com.cldellow;

import dk.brics.automaton.*;
import com.amazonaws.services.lambda.runtime.Context;

public class App 
{
    public String match(String haystack, String needle, Context context) {
        String rv = "";
        RegExp r;
        try {
            r = new RegExp(needle);
        } catch (IllegalArgumentException iae) {
            return "-1\t";
        }

        RunAutomaton a = new RunAutomaton(r.toAutomaton());

        AutomatonMatcher m = a.newMatcher(haystack);
        int results = 0;
        while(m.find()) {
            if(rv == "")
                rv = m.group();
            results++;
        }

        return results + "\t" + rv;
    }

    public static void main( String[] args )
    {
        String haystack = args[0];
        String needle = args[1];
        System.out.println((new App()).match(haystack, needle, null));
    }
}
