package com.peterung.redditzen.ui.subscriptions;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peterung.redditzen.R;
import com.peterung.redditzen.data.api.model.Subreddit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscriptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Subreddit> data;
    SubscriptionsPresenter presenter;

    public SubscriptionsAdapter(Context context, SubscriptionsPresenter subscriptionsPresenter) {
        this.context = context;
        this.presenter = subscriptionsPresenter;
    }

    public Context getContext() {
        return context;
    }

    public void setData(List<Subreddit> data) {
        this.data = data;
    }

    public void addItems(List<Subreddit> data) {
        this.data.addAll(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.item_subscription, parent, false);
        return new SubredditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Subreddit subreddit = data.get(position);

        SubredditViewHolder viewHolder = (SubredditViewHolder) holder;
        viewHolder.subreddit.setText(subreddit.displayName);
        viewHolder.data = subreddit;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class SubredditViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.subreddit)
        TextView subreddit;
        Subreddit data;


        public SubredditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.subreddit)
        void subredditListener() {
            presenter.goToSubreddit(data);
        }

    }
}
