package com.peterung.redditzen;

import com.peterung.redditzen.data.api.oauth.OauthAuthenticator;
import com.peterung.redditzen.data.sync.RedditSyncAdapter;
import com.peterung.redditzen.ui.MainActivity;
import com.peterung.redditzen.ui.login.LoginActivity;
import com.peterung.redditzen.ui.post.PostFragment;
import com.peterung.redditzen.ui.profile.ProfileFragment;
import com.peterung.redditzen.ui.subreddit.SubredditFragment;
import com.peterung.redditzen.ui.subscriptions.SubscriptionsFragment;
import com.peterung.redditzen.ui.widget.KarmaWidgetProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RedditZenModule.class})
public interface RedditZenComponent {
    void inject(RedditZenApplication app);
    void inject(OauthAuthenticator oauthAuthenticator);
    void inject(SubredditFragment subredditFragment);
    void inject(PostFragment postFragment);
    void inject(ProfileFragment profileFragment);
    void inject(SubscriptionsFragment subscriptionsFragment);
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(RedditSyncAdapter redditSyncAdapter);
    void inject(KarmaWidgetProvider karmaWidgetProvider);

    final class Initializer {
        public static RedditZenComponent init(RedditZenApplication app) {
            return DaggerRedditZenComponent.builder()
                    .redditZenModule(new RedditZenModule(app))
                    .build();
        }
    }
}
