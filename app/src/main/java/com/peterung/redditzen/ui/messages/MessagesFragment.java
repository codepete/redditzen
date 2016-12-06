package com.peterung.redditzen.ui.messages;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.db.schematic.MessagesProvider;
import com.peterung.redditzen.data.sync.SyncUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.list) ListView listView;
    @BindView(R.id.empty) TextView emptyView;
    MessagesAdapter adapter;
    SyncUtil syncUtil;
    Parcelable listState;
    Tracker tracker;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);
        syncUtil = new SyncUtil(getContext());
        setupAnalytics();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Messages");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(10, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncUtil.syncMessagesImmediately();

        tracker.setScreenName("messages");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("state", listView.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("state");
        }
    }

//    @OnItemClick(R.id.list) void onListClicked(int position) {
//        adapter.getItem(position);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MessagesProvider.Messages.CONTENT_URI, MessagesAdapter.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter == null) {
            adapter = new MessagesAdapter(getActivity(), data);
            listView.setAdapter(adapter);
            listView.setEmptyView(emptyView);

            if (listState != null) {
                listView.onRestoreInstanceState(listState);
                listState = null;
            }
        } else {
            adapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getActivity().getApplication();
        tracker = app.getDefaultTracker();
    }
}
