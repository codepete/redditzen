package com.peterung.redditzen.ui.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    int previousCount;
    boolean loading;
    int threshold = 5;
    LinearLayoutManager linearLayoutManager;

    public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        this.loading = true; // we manually trigger the first page so this must be true initially
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int currentCount = linearLayoutManager.getItemCount();
        int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

        if (currentCount < previousCount) {
            previousCount = currentCount;
            if (currentCount == 0) {
                loading = true;
            }
        }

        if (loading && (currentCount > previousCount)) {
            loading = false;
            previousCount = currentCount;
        }


        if (!loading && (lastVisiblePosition + threshold > currentCount)) {
            loading = true;
            onLoadMore();
        }

    }

    public abstract void onLoadMore();
}
