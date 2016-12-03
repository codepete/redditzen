package com.peterung.redditzen.ui.post;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.peterung.redditzen.R;
import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.Listing;
import com.peterung.redditzen.data.api.model.More;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.api.model.RedditObject;
import com.peterung.redditzen.utils.RecentActions;
import com.peterung.redditzen.utils.RedditObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LinkedHashMap<String, List<RedditObject>>  commentsTracker;
    List<RedditObject> data;
    HashMap<String, RedditObject> lookup;
    PostPresenter postPresenter;
    Post post;
    final int COMMENT_TYPE = 0;
    final int MORE_TYPE = 1;
    final int HEADER = 3;


    public PostAdapter(Context context, PostPresenter postPresenter, Post post) {
        this.context = context;
        this.post = post;
        this.commentsTracker = new LinkedHashMap<>();
        this.lookup = new HashMap<>();
        this.data = new ArrayList<>();
        this.data.add(post);
        this.postPresenter = postPresenter;

    }

    @SuppressWarnings("Java8CollectionsApi")
    public void setData(List<RedditObject> data, int depth) {
        if (data == null) {
            return;
        }

        data.removeAll(Collections.singleton(null));
        for (RedditObject redditObject : data) {
            RedditObjectUtil.setDepth(redditObject, depth);
            // Making sure all objects have linkId set
            RedditObjectUtil.setLinkId(redditObject, post.name);
            this.data.add(redditObject);

            String name = RedditObjectUtil.getName(redditObject);
            String parentId = RedditObjectUtil.getParentId(redditObject);

            updateTracker(parentId, redditObject);
            lookup.put(name, redditObject);

            if (redditObject instanceof Comment) {
                Comment comment = (Comment) redditObject;
                if (comment.replies != null) {
                    Listing listing = (Listing) comment.replies;
                    setData(listing.children, depth + 1);
                }
            }
        }
    }


    @SuppressWarnings("Java8CollectionsApi")
    public void setHeaderData(Post post) {
        this.post = post;
        data = new ArrayList<>();
        data.add(0, post);

        lookup.put(post.name, post);
    }

    public void addData(List<RedditObject> comments) {
        for (RedditObject redditObject : comments) {
            String parentId = RedditObjectUtil.getParentId(redditObject);
            String name = RedditObjectUtil.getName(redditObject);

            // Making sure all objects have linkId set
            RedditObjectUtil.setLinkId(redditObject, post.name);

            if (parentId == null) {
                return;
            }

            if (parentId.equals(post.name)) {
                data.add(redditObject); // just append at end if parent is post
            } else {
                int insertIndex = findInsertionIndex(parentId);

                Comment parentComment = (Comment) lookup.get(parentId);
                RedditObjectUtil.setDepth(redditObject, parentComment.depth + 1);
                data.add(insertIndex+1, redditObject);
            }

            lookup.put(name, redditObject);
            updateTracker(parentId, redditObject);
        }
    }

    @SuppressWarnings("Java8CollectionsApi")
    public void updateTracker(String name, RedditObject redditObject) {
        if (commentsTracker.get(name) == null) {
            commentsTracker.put(name, new ArrayList<>());
        }

        commentsTracker.get(name).add(redditObject);
    }

    public int findInsertionIndex(String parentId) {
        int childCount = getThreadChildCount(parentId);
        if (childCount == 0) {
            return data.indexOf(lookup.get(parentId));
        }

        RedditObject redditObject = commentsTracker.get(parentId).get(childCount-1);
        return findInsertionIndex(RedditObjectUtil.getName(redditObject));
    }

    public int getThreadChildCount(String parentId) {
        if (commentsTracker.get(parentId) == null) {
            return 0;
        }

        return commentsTracker.get(parentId).size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (viewType == COMMENT_TYPE) {
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else if (viewType == HEADER) {
            View view = inflater.inflate(R.layout.template_post, parent, false);
            return new PostViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_more, parent, false);
            return new MoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == COMMENT_TYPE) {
            Comment comment = (Comment) data.get(position);
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.body.setText(Html.fromHtml(comment.bodyHtml));
            commentViewHolder.body.setText(comment.body);
            commentViewHolder.comment = comment;
            commentViewHolder.info.setText(
                    String.format(
                            Locale.US,
                            "%s | %d points | %s ",
                            comment.author,
                            comment.score,
                            DateUtils.getRelativeTimeSpanString(comment.created.toEpochMilli())
                    )
            );
            commentViewHolder.container.setPadding(dpToPixels((comment.depth * 5) - 5), 0, 0, 0);
            commentViewHolder.depthIndicator.setBackgroundColor(getColor(comment.depth));
        } else if (getItemViewType(position) == HEADER) {
            Post post = (Post) data.get(position);
            PostViewHolder postViewHolder = (PostViewHolder) holder;
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
        } else {
            More more = (More) data.get(position);
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.setMore(more);
            moreViewHolder.container.setPadding(dpToPixels((more.depth * 5) - 5), 0, 0, 0);
            moreViewHolder.depthIndicator.setBackgroundColor(getColor(more.depth));
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }

        RedditObject redditObject = data.get(position);
        if (redditObject instanceof Comment) {
            return COMMENT_TYPE;
        } else {
            return MORE_TYPE;
        }
    }

    public Context getContext() {
        return context;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        Comment comment;
        @BindView(R.id.depth_indicator) View depthIndicator;
        @BindView(R.id.comment_container) View container;
        @BindView(R.id.info) TextView info;
        @BindView(R.id.body) TextView body;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }
    }

    public class MoreViewHolder extends RecyclerView.ViewHolder {
        More more;
        @BindView(R.id.load_more) TextView loadMore;
        @BindView(R.id.depth_indicator) View depthIndicator;
        @BindView(R.id.more_container) View container;

        public MoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMore(More more) {
            this.more = more;
        }

        @OnClick(R.id.load_more)
        public void loadMoreListener() {
            commentsTracker.get(more.parentId).remove(more);
            data.remove(more);
            postPresenter.loadMore(this.more);
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.info) TextView info;
        @BindView(R.id.details) View details;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.save)
        CheckBox save;
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
                postPresenter.unsave(this.post);
            } else {
                postPresenter.save(this.post);
            }

            this.post.saved = !post.saved;
            RecentActions.save(this.post.name, this.post.saved);
        }

        @OnClick(R.id.upvote)
        void upvoteListener() {
            if (this.downvote.isChecked()) {
                this.downvote.setChecked(false);
            }

            if (this.upvote.isChecked()) {
                postPresenter.upvote(post);
                RecentActions.vote(this.post.name, true);
            } else {
                postPresenter.removeVote(post);
                RecentActions.vote(this.post.name, null);
            }

            RecentActions.vote(this.post.name, true);
        }

        @OnClick(R.id.downvote)
        void downvoteListener() {
            if (this.upvote.isChecked()) {
                this.upvote.setChecked(false);
            }

            if (this.downvote.isChecked()) {
                postPresenter.downvote(post);
                RecentActions.vote(this.post.name, false);
            } else {
                postPresenter.removeVote(post);
                RecentActions.vote(this.post.name, null);
            }

        }

        @OnClick(R.id.title)
        void goToPostListener() {
            postPresenter.goToUrl(post.url);
        }

        @OnClick(R.id.image)
        void goToUrl() {
            postPresenter.goToUrl(post.url);
        }

    }

    private int dpToPixels(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private int getColor(int depth) {
        switch (depth) {
            case 0:
                return Color.rgb(255, 255, 255);
            case 1:
                return Color.rgb(0, 179, 239);
            case 2:
                return Color.rgb(105, 25, 255);
            case 3:
                return Color.rgb(0, 239, 71);
            case 4:
                return Color.rgb(237, 169, 0);
            case 5:
                return Color.rgb(58, 196, 191);
            case 6:
                return Color.rgb(0, 63, 255);
            case 7:
                return Color.rgb(97, 97, 97);
            case 8:
                return Color.rgb(119, 192, 229);
            case 9:
                return Color.rgb(130, 118, 226);
            default:
                return Color.rgb(0, 0, 0);
        }
    }
}
