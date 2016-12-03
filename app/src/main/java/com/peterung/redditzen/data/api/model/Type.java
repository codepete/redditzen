package com.peterung.redditzen.data.api.model;


public enum Type {
    t1(Comment.class),
    t3(Post.class),
    t5(Subreddit.class),
    Listing(Listing.class),
    more(More.class);


    private final Class cls;

    Type(Class cls) {
        this.cls = cls;
    }

    public Class getRedditClass() {
        return cls;
    }

}
