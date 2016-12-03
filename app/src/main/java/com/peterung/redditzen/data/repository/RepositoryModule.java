package com.peterung.redditzen.data.repository;


import com.f2prateek.rx.preferences.Preference;
import com.peterung.redditzen.data.annotations.Username;
import com.peterung.redditzen.data.api.RedditService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    PostRepository providePostRepository(RedditService redditService, SingleEntityStore<Persistable> database) {
        return new PostRepository(redditService, database);
    }

    @Provides
    @Singleton
    CommentRepository provideCommentRepository(RedditService redditService) {
        return new CommentRepository(redditService);
    }

    @Provides
    @Singleton
    AccountRepository provideAccountRepository(RedditService redditService, SingleEntityStore<Persistable> database, @Username Preference<String> username) {
        return new AccountRepository(redditService, database, username);
    }

    @Provides
    @Singleton
    SubredditRepository provideSubredditRepository(RedditService redditService, SingleEntityStore<Persistable> database, @Username Preference<String> username) {
        return new SubredditRepository(redditService, database);
    }


}
