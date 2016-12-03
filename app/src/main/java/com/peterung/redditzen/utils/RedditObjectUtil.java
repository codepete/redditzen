package com.peterung.redditzen.utils;

import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.More;
import com.peterung.redditzen.data.api.model.RedditObject;

public class RedditObjectUtil {
    public static String getName(RedditObject redditObject) {
        if (redditObject instanceof Comment) {
            return ((Comment) redditObject).name;
        } else if (redditObject instanceof More){
            return ((More) redditObject).name;
        }

        return null;
    }

    public static String getParentId(RedditObject redditObject) {
        if (redditObject instanceof Comment) {
            return ((Comment) redditObject).parentId;
        } else if (redditObject instanceof More){
            return ((More) redditObject).parentId;
        }

        return null;
    }

    public static void setDepth(RedditObject redditObject, int depth) {
        if (redditObject instanceof Comment) {
            ((Comment) redditObject).depth = depth;
        } else if (redditObject instanceof More){
            ((More) redditObject).depth = depth;
        }
    }

    public static void setLinkId(RedditObject redditObject, String linkId) {
        if (redditObject instanceof Comment) {
            ((Comment) redditObject).linkId = linkId;
        } else if (redditObject instanceof More){
            ((More) redditObject).linkId = linkId;
        }
    }

}
