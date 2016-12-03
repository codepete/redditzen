package com.peterung.redditzen.data.api.model;

import java.util.List;

public class Listing extends RedditObject {
    public String before;
    public String after;
    public String modhash;
    public List<RedditObject> children;
}
