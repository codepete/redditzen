package com.peterung.redditzen.ui.login;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.f2prateek.rx.preferences.Preference;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.annotations.AccessToken;
import com.peterung.redditzen.data.annotations.RefreshToken;
import com.peterung.redditzen.data.api.RedditService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.web_view) WebView webView;
    @Inject @AccessToken Preference<String> accessToken;
    @Inject @RefreshToken Preference<String> refreshToken;
    @Inject RedditService redditService;
    CompositeSubscription compositeSubscription;
    Uri redditAuth;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        RedditZenApplication.getComponent().inject(this);
        setupAnalytics();

        compositeSubscription = new CompositeSubscription();

        getSupportActionBar().hide();

        redditAuth = new Uri.Builder()
                .scheme("https")
                .authority("www.reddit.com")
                .path("/api/v1/authorize.compact")
                .appendQueryParameter("client_id", RedditService.CLIENT_ID)
                .appendQueryParameter("response_type", RedditService.DEFAULT_RESPONSE_TYPE)
                .appendQueryParameter("redirect_uri", RedditService.DEFAULT_REDIRECT_URI)
                .appendQueryParameter("duration", RedditService.DEFAULT_DURATION)
                .appendQueryParameter("scope", RedditService.DEFAULT_SCOPE)
                .appendQueryParameter("state", "redditzen")
                .build();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(redditAuth.toString());
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("?code=") || url.contains("&code=")) {
                    getToken(url);
                    return false;
                }

                view.loadUrl(url);
                return true;
            }


            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("?code=") || url.contains("&code=")) {
                    getToken(url);
                    return false;
                }

                view.loadUrl(url);
                return true;
            }
        });

    }

    private void getToken(String url) {
        Uri uri = Uri.parse(url);
        String authCode = uri.getQueryParameter("code");
        Subscription subscription = redditService.getToken(authCode, RedditService.DEFAULT_GRANT_TYPE, RedditService.DEFAULT_REDIRECT_URI)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        authTokenResponse -> {
                            accessToken.set(authTokenResponse.accessToken);
                            refreshToken.set(authTokenResponse.refreshToken);
                            finish();
                        },
                        error -> {
                            Timber.d(error, "Error retrieving access token and refresh token");
                            webView.loadUrl(redditAuth.toString());
                        }
                );

        compositeSubscription.add(subscription);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("login");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getApplication();
        tracker = app.getDefaultTracker();
    }
}
