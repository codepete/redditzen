package com.peterung.redditzen.utils;


import java.util.HashMap;

public class RecentActions {
    private static HashMap<String, Boolean>  vote = new HashMap<>();
    private static HashMap<String, Boolean>  saved = new HashMap<>();
    
    public static void vote(String name, Boolean likes) {
        vote.put(name, likes);
    }
    
    public static void save(String name, boolean save) {
        saved.put(name, save);
    }
    
    public static Boolean isSaved(String name) {
        return saved.get(name); 
    }
    
    public static Boolean getVote(String name) {
        return vote.get(name);
    }
    
    public static boolean recentlyVoted(String name) {
        return vote.containsKey(name);
    }
    
    public static boolean recentlySaved(String name) {
        return saved.containsKey(name);
    }
    
}
