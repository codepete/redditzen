package com.peterung.redditzen.data.db.entities;


import java.util.Date;

import io.requery.Entity;
import io.requery.Key;

@Entity(name = "PostEntity")
abstract class AbstractPost {
    @Key
    public String id;
    public String subredditId;
    public String bannedBy;
    public String domain;
    public String subreddit;
    public String selftext;
    public String suggestedSort;
    public String author;
    public String name;
    public String approvedBy;
    public String thumbnail;
    public String url;
    public String authorFlairText;
    public String linkFlairText;
    public String title;
    public int numComments;
    public int gilded;
    public int score;
    public int ups;
    public int downs;
    public boolean over18;
    public boolean hidden;
    public boolean contestMode;
    public boolean saved;
    public boolean archived;
    public boolean isSelf;
    public boolean hideScore;
    public boolean locked;
    public boolean stickied;
    public boolean quarantine;
    public boolean visited;
    public Boolean likes;
    public Date created;
}
