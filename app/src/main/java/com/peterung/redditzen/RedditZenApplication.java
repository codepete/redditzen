package com.peterung.redditzen;


import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import timber.log.Timber;

public class RedditZenApplication extends Application {
    private static RedditZenComponent component;
    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        component.inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Nothing yet
        }
    }

    public static RedditZenComponent getComponent() {
        return component;
    }

    public RedditZenComponent buildComponent() {
        return RedditZenComponent.Initializer.init(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }
}
