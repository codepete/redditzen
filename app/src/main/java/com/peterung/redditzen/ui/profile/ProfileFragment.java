package com.peterung.redditzen.ui.profile;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.db.entities.AccountEntity;
import com.peterung.redditzen.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileContract.View {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.started_date)
    TextView startedDate;
    @BindView(R.id.link_karma)
    TextView linkKarma;
    @BindView(R.id.comment_karma)
    TextView commentKarma;
    @Inject ProfilePresenter profilePresenter;

    Tracker tracker;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        RedditZenApplication.getComponent().inject(this);

        profilePresenter.initialize(this);
        profilePresenter.loadProfile();
        setupAnalytics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tracker.setScreenName("profile");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Profile");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profilePresenter.release();
    }

    @Override
    public void showProfile(AccountEntity accountEntity) {
        username.setText(accountEntity.getName());
        linkKarma.setText(String.format("%d", accountEntity.getLinkKarma()));
        commentKarma.setText(String.format("%d", accountEntity.getCommentKarma()));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String date = formatter.format(accountEntity.getCreated());
        startedDate.setText(String.format("member since %s", date));
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getActivity().getApplication();
        tracker = app.getDefaultTracker();
    }
}
