package com.peterung.redditzen.data.api.model;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.Instant;

public class Comment extends RedditObject {
    public String id;
    public String name;
    public String subredditId;
    public String subject;
    public int ups;
    public int downs;
    public int score;
    public boolean likes;
    public boolean scoreHidden;

    @SerializedName("new")
    public boolean unread;

    public Instant created;
    public Instant createdUtc;

    public String approvedBy;
    public String author;
    public String authorFlairCssClass;
    public String authorFlairText;
    public String bannedBy;
    public String body;
    public String bodyHtml;
    public boolean edited;

    public int gilded;
//    public String linkAuthor;
    public String linkId;
    public String linkTitle;
    public String linkUrl;
    public int numReports;
    public String parentId;
    public RedditObject replies;
    public int depth;

}
