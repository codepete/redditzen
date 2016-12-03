package com.peterung.redditzen.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.peterung.redditzen.data.annotations.AccessToken;
import com.peterung.redditzen.data.annotations.RefreshToken;
import com.peterung.redditzen.data.annotations.Username;
import com.peterung.redditzen.data.api.ApiModule;
import com.peterung.redditzen.data.api.InstantDeserializer;
import com.peterung.redditzen.data.api.LoginResponseDeserializer;
import com.peterung.redditzen.data.api.MediaDeserializer;
import com.peterung.redditzen.data.api.MoreChildrenResponseDeserializer;
import com.peterung.redditzen.data.api.RedditObjectDeserializer;
import com.peterung.redditzen.data.api.model.LoginResponse;
import com.peterung.redditzen.data.api.model.Media;
import com.peterung.redditzen.data.api.model.MoreChildrenResponse;
import com.peterung.redditzen.data.api.model.RedditObject;
import com.peterung.redditzen.data.db.DatabaseModule;
import com.peterung.redditzen.data.repository.AccountRepository;
import com.peterung.redditzen.data.repository.CommentRepository;
import com.peterung.redditzen.data.repository.PostRepository;
import com.peterung.redditzen.data.repository.RepositoryModule;
import com.peterung.redditzen.data.repository.SubredditRepository;
import com.peterung.redditzen.ui.post.PostPresenter;
import com.peterung.redditzen.ui.profile.ProfilePresenter;
import com.peterung.redditzen.ui.subreddit.SubredditPresenter;
import com.peterung.redditzen.ui.subscriptions.SubscriptionsPresenter;

import org.threeten.bp.Instant;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;
import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;


@Module(includes = {ApiModule.class, DatabaseModule.class, RepositoryModule.class})
public class DataModule {
    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("iaccess", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Instant.class, new InstantDeserializer())
                .registerTypeAdapter(Media.class, new MediaDeserializer())
                .registerTypeAdapter(RedditObject.class, new RedditObjectDeserializer())
                .registerTypeAdapter(MoreChildrenResponse.class, new MoreChildrenResponseDeserializer())
                .registerTypeAdapter(LoginResponse.class, new LoginResponseDeserializer())
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app).build();
    }

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
        return RxSharedPreferences.create(prefs);
    }

    @Provides
    @Singleton
    @AccessToken
    Preference<String> provideAccessToken(RxSharedPreferences prefs) {
        // Return an endpoint-specific preference.
        return prefs.getString("access-token");
    }

    @Provides
    @Singleton
    @RefreshToken
    Preference<String> provideRefreshToken(RxSharedPreferences prefs) {
        // Return an endpoint-specific preference.
        return prefs.getString("refresh-token");
    }

    @Provides
    @Singleton
    @Username
    Preference<String> provideUsername(RxSharedPreferences prefs) {
        // Return an endpoint-specific preference.
        return prefs.getString("username");
    }



    @Provides
    @Singleton
    SubredditPresenter provideSubredditPresenter(PostRepository postRepository) {
        return new SubredditPresenter(postRepository);
    }

    @Provides
    @Singleton
    PostPresenter providePostPresenter(CommentRepository commentRepository, PostRepository postRepository) {
        return new PostPresenter(commentRepository, postRepository);
    }

    @Provides
    @Singleton
    ProfilePresenter provideProfilePresenter(AccountRepository accountRepository) {
        return new ProfilePresenter(accountRepository);
    }

    @Provides
    @Singleton
    SubscriptionsPresenter provideSubscriptionsPresenter(SubredditRepository subredditRepository) {
        return new SubscriptionsPresenter(subredditRepository);
    }

    static OkHttpClient.Builder createOkHttpClient(Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache);
    }

}
