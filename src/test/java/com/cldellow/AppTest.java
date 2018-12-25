package com.cldellow;

import static org.junit.Assert.*;

import org.junit.Test;

public class AppTest 
{
    @Test
    public void noMatches()
    {
        Response rv = (new App()).match("haystack", "needle", null);
        assertEquals(0, rv.getHits().length);
        assertEquals(null, rv.getError());
    }

    @Test
    public void someMatches()
    {
        Response rv = (new App()).match("Donald", "[Dd]", null);
        assertEquals(2, rv.hits.length);
        assertEquals("D", rv.hits[0]);
        assertEquals("d", rv.hits[1]);
    }

    @Test
    public void badRegex()
    {
        Response rv = (new App()).match("Donald", "(", null);
        assertEquals(null, rv.hits);
        assertEquals("unexpected end-of-string", rv.error);
    }
}
