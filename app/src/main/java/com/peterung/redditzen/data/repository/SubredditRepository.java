package com.peterung.redditzen.data.repository;

import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.RedditResponse;

import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import rx.Observable;
import rx.schedulers.Schedulers;

public class SubredditRepository {
    private RedditService redditService;
    private SingleEntityStore<Persistable> database;
    private final int PAGE_SIZE = 20;

    public SubredditRepository(RedditService redditService, SingleEntityStore<Persistable> database) {
        this.redditService = redditService;
        this.database = database;
    }

    public Observable<RedditResponse<Listing>> getSubreddits(String query, String sort, String after) {
        return redditService.searchSubreddits(query, sort, after)
                .subscribeOn(Schedulers.io());

    }

    public Observable<RedditResponse<Listing>> getSubscribedSubreddits(String after) {
        return redditService.getSubcribedSubreddits(PAGE_SIZE, after)
                .subscribeOn(Schedulers.io());
    }

//    public Observable<SubredditEntity> addSubreddit(SubredditEntity subreddit) {
//        return database.upsert(subreddit).toObservable();
//    }
//
//    public Observable<Iterable<SubredditEntity>> addSubreddits(List<SubredditEntity> subreddits) {
//        return database.upsert(subreddits).toObservable();
//    }
//
//    public Observable<Void> removeSubreddit(SubredditEntity subreddit) {
//        return database.delete(subreddit).toObservable();
//    }


}
