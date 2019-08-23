package com.cldellow;

public class Request {
    String needle;
    String haystack;

    public String getNeedle() { return needle; }
    public String getHaystack() { return haystack; }

    public void setNeedle(String needle) { this.needle = needle; }
    public void setHaystack(String haystack) { this.haystack = haystack; }

    public Request(String needle, String haystack) {
        this.needle = needle;
        this.haystack = haystack;
    }

    public Request() {
    }
}
