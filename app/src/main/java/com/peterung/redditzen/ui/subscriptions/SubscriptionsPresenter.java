package com.peterung.redditzen.ui.subscriptions;


import com.peterung.redditzen.data.api.model.Subreddit;
import com.peterung.redditzen.data.repository.SubredditRepository;
import com.peterung.redditzen.utils.RedditObjectConverter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class SubscriptionsPresenter implements SubscriptionsContract.UserActionsListener {

    SubredditRepository subredditRepository;
    CompositeSubscription compositeSubscription;
    SubscriptionsContract.View view;
    String after;

    public SubscriptionsPresenter(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Override
    public void loadSubscriptions() {
        Subscription subscription = subredditRepository.getSubscribedSubreddits(after)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        redditResponse -> {
                            List<Subreddit> subreddits = RedditObjectConverter.convertToSubreddits(redditResponse.data.children);
                            view.showSubscriptions(subreddits, after != null);
                            after = redditResponse.data.after;
                            Timber.d("Success in loading subscriptions");
                        },
                        error -> {
                            Timber.e("Error in loading subscriptions", error);
                        }
                );

        compositeSubscription.add(subscription);
    }

    @Override
    public void loadMore() {
        if (after == null) {
            return;
        }

        loadSubscriptions();
    }

    @Override
    public void initialize(SubscriptionsContract.View view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void release() {
        view = null;
        after = null;
        compositeSubscription.unsubscribe();
    }

    @Override
    public void goToSubreddit(Subreddit subreddit) {
        view.viewSubreddit(subreddit);
    }

}
