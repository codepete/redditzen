package com.peterung.redditzen.ui.post;


import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.More;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.RedditObject;

import java.util.List;

public interface PostContract {
    interface View {
        void showPost(Listing listing);
        void showComments(List<RedditObject> listing, boolean append);
    }

    interface UserActionsListener {
        void initialize(View view);
        void release();
        void loadPost(String subreddit, String postId);
        void loadMore(More more);
        void upvote(Post post);
        void downvote(Post post);
        void removeVote(Post post);
        void save(Post post);
        void unsave(Post post);
        void upvote(Comment comment);
        void downvote(Comment comment);
        void removeVote(Comment comment);
        void goToUrl(String url);

    }

}
