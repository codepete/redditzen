package com.peterung.redditzen.data.api.model;


import org.threeten.bp.Instant;

public class Account {
    public String id;
    public int commentKarma;
    public int linkKarma;
    public boolean hasMail;
    public boolean hasModMail;
    public boolean hasVerifiedEmail;
    public boolean isFriend;
    public boolean isMod;
    public boolean over18;
    public int inboxCount;
    public String modhash;
    public String name;
    public Instant created;
}
