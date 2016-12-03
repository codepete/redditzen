package com.peterung.redditzen.data.api.oauth;

import com.f2prateek.rx.preferences.Preference;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.annotations.AccessToken;
import com.peterung.redditzen.data.annotations.RefreshToken;
import com.peterung.redditzen.data.api.RedditService;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class OauthAuthenticator implements Authenticator {
    @Inject @AccessToken Preference<String> accessToken;
    @Inject @RefreshToken Preference<String> refreshToken;
    @Inject RedditService redditService;

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (responseCount(response) >= 3) {
            return null;
        }

        RedditZenApplication.getComponent().inject(this);

        redditService.refreshToken(refreshToken.get(), "refresh_token")
                .subscribeOn(Schedulers.io())
                .subscribe(
                        authTokenResponse -> {
                            accessToken.set(authTokenResponse.accessToken);
                            Timber.d("Successfully refreshed token");
                        },
                        error -> {
                            Timber.e(error, "Unable to refresh token");
                        }
                );

        return response.request().newBuilder()
                .header("Authorization", "bearer " + accessToken.get())
                .build();
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
