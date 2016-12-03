package com.peterung.redditzen.utils;


import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.RedditObject;
import com.peterung.redditzen.data.api.model.Subreddit;

import java.util.ArrayList;
import java.util.List;

public class RedditObjectConverter {
    public static List<Post> convertToPosts(List<RedditObject> redditObjects) {
        List<Post> posts = new ArrayList<>();
        for (RedditObject redditObject : redditObjects) {
            posts.add((Post)redditObject);
        }

        return  posts;
    }

    public static List<Subreddit> convertToSubreddits(List<RedditObject> redditObjects) {
        List<Subreddit> subreddits = new ArrayList<>();
        for (RedditObject redditObject : redditObjects) {
            subreddits.add((Subreddit) redditObject);
        }

        return  subreddits;
    }

    public static List<Comment> convertToComments(List<RedditObject> redditObjects) {
        List<Comment> comments = new ArrayList<>();
        for (RedditObject redditObject : redditObjects) {
            comments.add((Comment) redditObject);
        }

        return comments;
    }
}
