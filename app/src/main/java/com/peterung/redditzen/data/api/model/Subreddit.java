package com.peterung.redditzen.data.api.model;

import org.parceler.Parcel;
import org.threeten.bp.Instant;

@Parcel
public class Subreddit extends RedditObject {
    public String id;
    public boolean publicTraffic;
    public Instant created;
    public int accountsActive;
    public int subscribers;
    public String description;
    public String publicDescription;
    public String displayName;
    public String submissionType;
    public boolean over18;
    public boolean userIsBanned;
    public boolean userIsModerator;
    public boolean userIsSubscriber;
    public String subredditType;
    public String title;
    public String url;

    public Subreddit() {};
}
