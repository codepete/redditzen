package com.peterung.redditzen.data.repository;


import com.f2prateek.rx.preferences.Preference;
import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.api.model.Account;
import com.peterung.redditzen.data.db.EntityConvert;
import com.peterung.redditzen.data.db.entities.AccountEntity;

import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AccountRepository {
    private RedditService redditService;
    private SingleEntityStore<Persistable> database;
    private CompositeSubscription compositeSubscription;
    private Preference<String> username;

    public AccountRepository(RedditService redditService,
                             SingleEntityStore<Persistable> database,
                             Preference<String> username) {
        this.compositeSubscription = new CompositeSubscription();
        this.redditService = redditService;
        this.database = database;
        this.username = username;
    }

    public Observable<AccountEntity> addAccount(Account account) {
        return database.upsert(EntityConvert.convert(account)).toObservable();
    }

    public Observable<AccountEntity> getAccount() {
        return redditService.getAccount()
                .observeOn(Schedulers.io())
                .concatMap(account -> addAccount(account));
    }

}
