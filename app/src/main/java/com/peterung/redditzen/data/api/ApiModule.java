package com.peterung.redditzen.data.api;

import com.google.gson.Gson;
import com.peterung.redditzen.data.api.oauth.OauthAuthenticator;
import com.peterung.redditzen.data.api.oauth.OauthInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class ApiModule {
    public static final HttpUrl URL = HttpUrl.parse("https://api.reddit.com");

    @Provides
    @Singleton
    HttpUrl provideRedditHttpUrl() {
        return ApiModule.URL;
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    @Named("RedditOkHttpClient")
    OkHttpClient provideRedditApiClient(OkHttpClient client,
                                        OauthInterceptor oauthInterceptor,
                                        HttpLoggingInterceptor loggingInterceptor) {
        return createApiClient(client, oauthInterceptor, new OauthAuthenticator())
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRedditRetrofit(HttpUrl baseUrl, @Named("RedditOkHttpClient") OkHttpClient client, Gson gson) {
        return new Retrofit.Builder() //
                .client(client) //
                .baseUrl(baseUrl) //
                .addConverterFactory(GsonConverterFactory.create(gson)) //
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //
                .build();
    }

    @Provides
    @Singleton
    RedditService provideRedditService(Retrofit retrofit) {
        return retrofit.create(RedditService.class);
    }

    private static OkHttpClient.Builder createApiClient(OkHttpClient client,
                                                        OauthInterceptor oauthInterceptor,
                                                        OauthAuthenticator oauthAuthenticator) {
        return client.newBuilder()
                .authenticator(oauthAuthenticator)
                .addInterceptor(oauthInterceptor);
    }
}
