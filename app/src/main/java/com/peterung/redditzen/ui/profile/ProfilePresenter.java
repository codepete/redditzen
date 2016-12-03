package com.peterung.redditzen.ui.profile;

import com.peterung.redditzen.data.repository.AccountRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class ProfilePresenter implements ProfileContract.UserActionsListener {

    ProfileContract.View view;
    CompositeSubscription compositeSubscription;
    AccountRepository accountRepository;

    public ProfilePresenter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void loadProfile() {
        Subscription subscription = accountRepository.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountEntity -> {
                            view.showProfile(accountEntity);
                            Timber.d("Successfully pulled account information");
                        },
                        error -> {
                            Timber.e("Error retrieving account information", error);
                        }
                );

        compositeSubscription.add(subscription);
    }

    @Override
    public void initialize(ProfileContract.View view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void release() {
        view = null;
        compositeSubscription.unsubscribe();
    }
}
