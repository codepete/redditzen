package com.peterung.redditzen.ui.post;


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
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.RedditObject;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostFragment extends Fragment implements PostContract.View {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Parcelable layoutState;
    PostAdapter postAdapter;
    Post post;
    @Inject PostPresenter postPresenter;
    Tracker tracker;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        RedditZenApplication.getComponent().inject(this);

        Bundle args = getArguments();
        post = Parcels.unwrap(args.getParcelable("post"));

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        postAdapter = new PostAdapter(getContext(), postPresenter, post);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(postAdapter);

        postPresenter.initialize(this);
        postPresenter.loadPost(post.subreddit, post.id);
        setupAnalytics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tracker.setScreenName("post");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postPresenter.release();
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
    public void showPost(Listing listing) {
        Post post = (Post) listing.children.get(0); // ugh yuck
        getActivity().setTitle(post.title);
        postAdapter.setHeaderData(post);
        postAdapter.notifyDataSetChanged();

        if (layoutState != null) {
            linearLayoutManager.onRestoreInstanceState(layoutState);
        }
    }

    @Override
    public void showComments(List<RedditObject> listing, boolean append) {
        if (append) {
            postAdapter.addData(listing);
        } else {
            postAdapter.setData(listing, 0);
        }
        postAdapter.notifyDataSetChanged();
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getActivity().getApplication();
        tracker = app.getDefaultTracker();
    }
}
