package com.peterung.redditzen.ui.subscriptions;


import com.peterung.redditzen.data.api.model.Subreddit;

import java.util.List;

public interface SubscriptionsContract {
   interface View {
      void showSubscriptions(List<Subreddit> subreddits, boolean append);
      void viewSubreddit(Subreddit subreddit);
   }

   interface UserActionsListener {
      void loadSubscriptions();
      void loadMore();
      void initialize(View view);
      void release();
      void goToSubreddit(Subreddit subreddit);
   }
}
