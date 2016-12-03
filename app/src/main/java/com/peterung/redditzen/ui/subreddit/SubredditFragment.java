package com.peterung.redditzen.ui.subreddit;


import android.content.Context;
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
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.Subreddit;
import com.peterung.redditzen.ui.MainActivity;
import com.peterung.redditzen.ui.misc.EndlessScrollListener;
import com.peterung.redditzen.ui.post.PostActivity;
import com.peterung.redditzen.ui.web.WebActivity;
import com.peterung.redditzen.utils.Strings;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditFragment extends Fragment implements SubredditContract.View {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @Inject SubredditPresenter subredditPresenter;
    LinearLayoutManager linearLayoutManager;
    SubredditAdapter subredditAdapter;
    String subreddit;
    Parcelable layoutState;
    Tracker tracker;

    public SubredditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subreddit, container, false);
        ButterKnife.bind(this, view);
        RedditZenApplication.getComponent().inject(this);

        Bundle args = getArguments();
        if (args != null) {
            Subreddit subredditData = Parcels.unwrap(args.getParcelable("subreddit"));
            subreddit = subredditData.displayName;
        }

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        subredditAdapter = new SubredditAdapter(getContext(), subredditPresenter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(subredditAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                subredditPresenter.loadMore();
            }
        });

        subredditPresenter.initialize(this);
        subredditPresenter.loadPosts(subreddit, "hot");
        setupAnalytics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tracker.setScreenName("subreddit");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.setToolbarTitle("Front Page");
        } else {
            getActivity().setTitle(Strings.isBlank(subreddit) ? "Reddit Zen" : subreddit);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        subredditPresenter.release();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            layoutState = savedInstanceState.getParcelable("state");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("state", linearLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showPosts(List<Post> posts, boolean append) {
        if (append) {
            subredditAdapter.addItems(posts);
        } else {
            subredditAdapter.setData(posts);
        }
        subredditAdapter.notifyDataSetChanged();

        if (layoutState != null) {
            linearLayoutManager.onRestoreInstanceState(layoutState);
        }
    }

    @Override
    public void viewPost(Post post) {
        Intent intent = new Intent(getActivity(), PostActivity.class);
        intent.putExtra("post", Parcels.wrap(post));
        startActivity(intent);
    }

    @Override
    public void viewUrl(String url) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getActivity().getApplication();
        tracker = app.getDefaultTracker();
    }


}
