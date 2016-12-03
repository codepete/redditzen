package com.peterung.redditzen.ui.subreddit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.peterung.redditzen.R;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.utils.RecentActions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubredditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Post> posts;
    private Context context;
    private SubredditPresenter subredditPresenter;

    public SubredditAdapter(Context context, SubredditPresenter subredditPresenter) {
        this.context = context;
        this.subredditPresenter = subredditPresenter;
        posts = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);
        PostViewHolder postViewHolder = (PostViewHolder) holder;
        post.likes = RecentActions.recentlyVoted(post.name) ? RecentActions.getVote(post.name) : post.likes;
        post.saved = RecentActions.recentlySaved(post.name) ? RecentActions.isSaved(post.name) : post.saved;
        postViewHolder.title.setText(post.title);
        postViewHolder.upvote.setChecked(post.likes != null && post.likes);
        postViewHolder.downvote.setChecked(post.likes != null && !post.likes);
        postViewHolder.save.setChecked(post.saved);
        postViewHolder.info.setText(String.format("%s | %s | %s", post.author, DateUtils.getRelativeTimeSpanString(post.created.toEpochMilli()), post.subreddit));

        if (post.preview != null && post.preview.images != null) {
            postViewHolder.image.setVisibility(View.VISIBLE);
            String url = post.preview.images.get(0).source.url;
            url = url.replace("&amp;", "&");

            Glide.with(getContext())
                    .load(url)
                    .into(postViewHolder.image);
        } else {
            postViewHolder.image.setVisibility(View.GONE);
        }
        postViewHolder.setData(post);
    }

    @Override
    public int getItemCount() {
        if (posts == null) {
            return 0;
        }

        return posts.size();
    }

    public void addItems(List<Post> posts) {
        this.posts.addAll(posts);
    }

    public void setData(List<Post> posts) {
        this.posts = posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.info) TextView info;
        @BindView(R.id.details) View details;
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.save) CheckBox save;
        @BindView(R.id.upvote) CheckBox upvote;
        @BindView(R.id.downvote) CheckBox downvote;
        @BindView(R.id.counts) TextView counts;
        @BindView(R.id.title) TextView title;
        Post post;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Post post) {
            this.post = post;
        }

        @OnClick(R.id.save)
        void saveListener() {
            if (this.post.saved) {
                subredditPresenter.unsave(this.post);
            } else {
                subredditPresenter.save(this.post);
            }
        }

        @OnClick(R.id.upvote)
        void upvoteListener() {
            if (this.downvote.isChecked()) {
                this.downvote.setChecked(false);
            }

            if (this.upvote.isChecked()) {
                subredditPresenter.upvote(post);
            } else {
                subredditPresenter.removeVote(post);
            }
        }

        @OnClick(R.id.downvote)
        void downvoteListener() {
            if (this.upvote.isChecked()) {
                this.upvote.setChecked(false);
            }

            if (this.downvote.isChecked()) {
                subredditPresenter.downvote(post);
            } else {
                subredditPresenter.removeVote(post);
            }
        }

        @OnClick(R.id.details)
        void goToPostListener() {
            subredditPresenter.goToPost(this.post);
        }

        @OnClick(R.id.image)
        void goToUrl() {
            if (post.isSelf) {
                subredditPresenter.goToPost(post);
                return;
            }

            subredditPresenter.goToUrl(post.url);
        }

    }
}
