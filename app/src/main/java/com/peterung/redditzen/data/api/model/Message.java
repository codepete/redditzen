package com.peterung.redditzen.data.api.model;


import com.google.gson.annotations.SerializedName;

import org.threeten.bp.Instant;

public class Message {
    public String name;
    public String author;
    public Instant created;
    public String body;
    public String bodyHtml;
    public String firstMessageName;
    public boolean likes;
    public String linkTitle;
    @SerializedName("new")
    public boolean isNew;
    public String parentId;
    public String replies;
    public String subject;
    public String subreddit;
    public boolean wasComment;
}
