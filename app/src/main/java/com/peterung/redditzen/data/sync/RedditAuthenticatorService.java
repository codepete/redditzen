package com.peterung.redditzen.data.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class RedditAuthenticatorService extends Service {

    private RedditAuthenticator redditAuthenticator;

    @Override
    public void onCreate() {
        redditAuthenticator = new RedditAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return redditAuthenticator.getIBinder();
    }
}
