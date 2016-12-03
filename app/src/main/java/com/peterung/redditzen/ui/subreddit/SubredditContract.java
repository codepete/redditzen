package com.peterung.redditzen.ui.subreddit;

import com.peterung.redditzen.data.api.model.Post;

import java.util.List;


public interface SubredditContract {
    interface View {
        void showPosts(List<Post> posts, boolean append);
        void viewPost(Post post);
        void viewUrl(String url);
    }

    interface UserActionsListener {
        void loadMore();
        void loadPosts(String subreddit, String sort);
        void initialize(View view);
        void save(Post post);
        void unsave(Post post);
        void upvote(Post post);
        void downvote(Post post);
        void removeVote(Post post);
        void goToPost(Post post);
        void goToUrl(String url);
        void release();
    }

}
