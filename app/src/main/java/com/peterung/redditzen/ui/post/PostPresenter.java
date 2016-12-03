package com.peterung.redditzen.ui.post;

import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.More;
import com.peterung.redditzen.data.api.model.Post;
import com.peterung.redditzen.data.repository.CommentRepository;
import com.peterung.redditzen.data.repository.PostRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class PostPresenter implements PostContract.UserActionsListener {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private CompositeSubscription compositeSubscription;
    private PostContract.View view;
    private final int PAGE_LIMIT = 50;

    public PostPresenter(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize(PostContract.View view) {
        this.view = view;
    }

    @Override
    public void release() {
        compositeSubscription.clear();
        view = null;
    }


    @Override
    public void loadPost(String subreddit, String postId) {
        commentRepository.getComments(subreddit, postId, "confidence")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    listings -> {
                        view.showPost(listings.get(0).data);
                        view.showComments(listings.get(1).data.children, false);
                        Timber.d("Success in retrieving data");
                    },
                    error -> {
                        Timber.e(error, "Error retrieving data");
                    }
                );
    }


    @Override
    public void loadMore(More more) {
        commentRepository.getComments(more)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        moreChildrenResponse -> {
                            view.showComments(moreChildrenResponse.things, true);
                        },
                        error -> {
                            Timber.e(error, "Error retrieving more comments");
                        }
                );
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
    public void upvote(Comment comment) {

    }

    @Override
    public void downvote(Comment comment) {

    }

    @Override
    public void removeVote(Comment comment) {

    }

    @Override
    public void goToUrl(String url) {

    }
}
