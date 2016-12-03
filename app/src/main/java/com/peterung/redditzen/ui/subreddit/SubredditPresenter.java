package com.peterung.redditzen.ui.subreddit;

import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.repository.PostRepository;
import com.peterung.redditzen.utils.RedditObjectConverter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class SubredditPresenter implements SubredditContract.UserActionsListener {
    private PostRepository postRepository;
    private final int PAGE_LIMIT = 20;
    private SubredditContract.View view;
    private CompositeSubscription compositeSubscription;
    private String after; // used to get next page
    private String subreddit;
    private String sort;

    public SubredditPresenter(PostRepository postRepository) {
        this.postRepository = postRepository;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadMore() {
        if (after == null) {
            return;
        }

        loadPosts(subreddit, sort);
    }

    @Override
    public void loadPosts(String subreddit, String sort) {
        this.subreddit = subreddit;
        this.sort = sort;

        Subscription subscription = postRepository.getPosts(subreddit, sort, PAGE_LIMIT, after)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        redditResponse -> {
                            List<Post> posts = RedditObjectConverter.convertToPosts(redditResponse.data.children);
                            for (Post post: posts) {
                                if (post.over18) {
                                    posts.remove(post);
                                }
                            }
                            view.showPosts(posts, after != null);
                            after = redditResponse.data.after;
                            Timber.d("Successfully loaded posts");
                        },
                        error -> {
                            Timber.e(error, "Loading posts broke");
                        }
                );

        compositeSubscription.add(subscription);
    }

    @Override
    public void initialize(SubredditContract.View view) {
        this.view = view;
        this.after = null;
    }

    @Override
    public void upvote(Post post) {
        postRepository.upvote(post.name)
                .observeOn(Schedulers.io())
                .subscribe(
                        success -> {
                            Timber.d("Successfully upvoted post");
                        },
                        error -> {
                            Timber.e(error, "Error upvoting post");
                        }
                );
    }

    @Override
    public void downvote(Post post) {
        postRepository.downvote(post.name)
                .observeOn(Schedulers.io())
                .subscribe(
                        success -> {
                            Timber.d("Successfully downvoted post");
                        },
                        error -> {
                            Timber.e(error, "Error downvoting post");
                        }
                );
    }

    @Override
    public void removeVote(Post post) {
        postRepository.removeVote(post.name)
                .observeOn(Schedulers.io())
                .subscribe(
                        success -> {
                            Timber.d("Successfully removed vote");
                        },
                        error -> {
                            Timber.e(error, "Error removing vote");
                        }
                );
    }

    @Override
    public void save(Post post) {
        postRepository.save(post)
                .observeOn(Schedulers.io())
                .subscribe(
                        success -> {
                            Timber.d("Successfully saved post");
                        },
                        error -> {
                            Timber.e(error, "Error saving post");
                        }
                );
    }

    @Override
    public void unsave(Post post) {
        postRepository.unsave(post)
                .observeOn(Schedulers.io())
                .subscribe(
                        success -> {
                            Timber.d("Successfully unsaved post");
                        },
                        error -> {
                            Timber.e(error, "Error saving post");
                        }
                );
    }

    @Override
    public void goToPost(Post post) {
        view.viewPost(post);
    }

    @Override
    public void goToUrl(String url) {
        view.viewUrl(url);
    }

    @Override
    public void release() {
        view = null;
        compositeSubscription.clear();
        after = null;
        subreddit = null;
        sort = null;
    }
}
