package com.peterung.redditzen;

import android.app.Application;
import android.content.Context;

import com.peterung.redditzen.data.DataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {DataModule.class})
public class RedditZenModule {
    private final Context context;
    private final Application app;

    public RedditZenModule(Application app) {
        this.context = app;
        this.app = (RedditZenApplication) app;
    }

    @Singleton
    @Provides
    public Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

}
