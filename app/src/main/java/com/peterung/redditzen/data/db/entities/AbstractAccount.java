package com.peterung.redditzen.data.db.entities;


import java.util.Date;

import io.requery.Entity;
import io.requery.Key;

@Entity(name = "AccountEntity")
abstract class AbstractAccount {

    @Key
    String id;
    int commentKarma;
    int linkKarma;
    boolean hasMail;
    boolean hasModMail;
    boolean hasVerifiedEmail;
    boolean isFriend;
    boolean isMod;
    boolean over18;
    int inboxCount;
    String modhash;
    String name;
    Date created;

}
