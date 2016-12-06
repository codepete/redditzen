package com.peterung.redditzen.ui.subscriptions;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.api.model.Subreddit;
import com.peterung.redditzen.ui.misc.EndlessScrollListener;
import com.peterung.redditzen.ui.subreddit.SubredditActivity;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionsFragment extends Fragment implements SubscriptionsContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @Inject
    SubscriptionsPresenter subscriptionsPresenter;
    LinearLayoutManager linearLayoutManager;
    SubscriptionsAdapter subscriptionsAdapter;
    Parcelable layoutState;
    Tracker tracker;

    public SubscriptionsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        RedditZenApplication.getComponent().inject(this);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        subscriptionsAdapter = new SubscriptionsAdapter(getContext(), subscriptionsPresenter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(subscriptionsAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                subscriptionsPresenter.loadMore();
            }
        });

        subscriptionsPresenter.initialize(this);
        subscriptionsPresenter.loadSubscriptions();
        setupAnalytics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tracker.setScreenName("subscriptions");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptionsPresenter.release();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Subscriptions");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("state", linearLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            layoutState = savedInstanceState.getParcelable("state");
        }
    }

    @Override
    public void showSubscriptions(List<Subreddit> subreddits, boolean append) {
        if (append) {
            subscriptionsAdapter.addItems(subreddits);
        } else {
            subscriptionsAdapter.setData(subreddits);
        }
        subscriptionsAdapter.notifyDataSetChanged();

        if (layoutState != null) {
            linearLayoutManager.onRestoreInstanceState(layoutState);
            layoutState = null;
        }
    }

    @Override
    public void viewSubreddit(Subreddit subreddit) {
        Intent intent = new Intent(getContext(), SubredditActivity.class);
        intent.putExtra("subreddit", Parcels.wrap(subreddit));
        startActivity(intent);
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getActivity().getApplication();
        tracker = app.getDefaultTracker();
    }


}
