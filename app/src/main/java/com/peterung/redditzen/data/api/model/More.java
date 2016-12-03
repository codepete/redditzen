package com.peterung.redditzen.data.api.model;

import java.util.List;

public class More extends RedditObject {
    public int count;
    public String parentId;
    public String id;
    public String name;
    public int depth;
    public String linkId;
    public List<String> children;
}
