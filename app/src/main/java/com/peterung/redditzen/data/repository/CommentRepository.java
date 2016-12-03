package com.peterung.redditzen.data.repository;


import android.text.TextUtils;

import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.More;
import com.peterung.redditzen.data.api.model.MoreChildrenResponse;
import com.peterung.redditzen.data.api.model.RedditResponse;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CommentRepository {
    private CompositeSubscription compositeSubscription;
    private RedditService redditService;

    public CommentRepository(RedditService redditService) {
        this.redditService = redditService;
        this.compositeSubscription = new CompositeSubscription();
    }

    public Observable<List<RedditResponse<Listing>>> getComments(String subreddit, String postId, String sort) {
        return redditService.getComments(subreddit, postId, sort)
                .subscribeOn(Schedulers.io());
    }

    public Observable<MoreChildrenResponse> getComments(More more) {
        String children = TextUtils.join(",", more.children);
        return redditService.getMoreChildren(more.linkId, children, "json", more.name, "confidence")
                .subscribeOn(Schedulers.io());
    }
}
