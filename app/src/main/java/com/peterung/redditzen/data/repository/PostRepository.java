package com.peterung.redditzen.data.repository;

import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.RedditResponse;
import com.peterung.redditzen.data.db.EntityConvert;
import com.peterung.redditzen.data.db.entities.PostEntity;
import com.peterung.redditzen.utils.Strings;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.rx.SingleEntityStore;
import rx.Observable;
import rx.schedulers.Schedulers;

public class PostRepository {
    private RedditService redditService;
    private SingleEntityStore<Persistable> database;
    private final int PAGE_SIZE = 20;

    public PostRepository(RedditService redditService, SingleEntityStore<Persistable> database) {
        this.redditService = redditService;
        this.database = database;
    }

    public Observable<RedditResponse<Listing>> getPosts(String subreddit, String sort, int limit, String after) {
        if (Strings.isBlank(subreddit)) {
            return redditService.getFrontpage(sort, limit, after)
                    .subscribeOn(Schedulers.io());
        } else {
            return redditService.getPosts(subreddit, sort, limit, after)
                    .subscribeOn(Schedulers.io());
        }
    }

    public Observable<Result<PostEntity>> getSavedPosts(int page)  {
        return database.select(PostEntity.class)
                .limit(PAGE_SIZE)
                .offset((page - 1) * PAGE_SIZE)
                .get()
                .toSelfObservable();
    }

    public Observable<PostEntity> addPost(PostEntity post) {
        return database.upsert(post).toObservable();
    }

    public Observable<Void> removePost(PostEntity post) {
        return database.delete(post).toObservable();
    }

    public Observable<Void> upvote(String postName) {
        return redditService.vote(postName, 1)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> downvote(String postName) {
        return redditService.vote(postName, -1)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Void> removeVote(String postName) {
        return redditService.vote(postName, 0)
                .subscribeOn(Schedulers.io());
    }

    public Observable<PostEntity> save(Post post) {
        return redditService.save(post.name, null)
                .subscribeOn(Schedulers.io())
                .concatMap(success -> addPost(EntityConvert.convert(post)));

    }

    public Observable<Void> unsave(Post post) {
        return redditService.unsave(post.name, null)
                .subscribeOn(Schedulers.io())
                .concatMap(success -> removePost(EntityConvert.convert(post)));
    }

}
