package com.peterung.redditzen.data.api.oauth;


import com.f2prateek.rx.preferences.Preference;
import com.peterung.redditzen.data.annotations.AccessToken;
import com.peterung.redditzen.data.api.RedditService;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.CacheControl;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


@Singleton
public class OauthInterceptor implements Interceptor {
    Preference<String> accessToken;

    @Inject
    public OauthInterceptor(@AccessToken Preference<String> accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = chain.request();

        String path = request.url().encodedPath();
        if (path.contains("access_token")) {
            builder.addHeader("Authorization", Credentials.basic(RedditService.CLIENT_ID, ""));

        } else if (accessToken.isSet()) {
            HttpUrl url = request.url().newBuilder()
                    .host("oauth.reddit.com")
                    .build();

            builder.url(url)
                   .addHeader("Authorization", String.format("bearer %s", accessToken.get()));
        }

        return chain.proceed(builder.build());
    }
}
