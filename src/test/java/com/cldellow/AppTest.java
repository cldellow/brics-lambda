package com.cldellow;

import static org.junit.Assert.*;

import org.junit.Test;

public class AppTest 
{
    @Test
    public void noMatches()
    {
        String rv = (new App()).match("haystack", "needle", null);
        assertEquals("0\t", rv);
    }

    @Test
    public void someMatches()
    {
        String rv = (new App()).match("Donald", "[Dd]", null);
        assertEquals("2\tD", rv);
    }

    @Test
    public void badRegex()
    {
        String rv = (new App()).match("Donald", "(", null);
        assertEquals("-1\t", rv);
    }
}
