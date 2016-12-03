package com.peterung.redditzen.ui.widget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.api.RedditService;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public class KarmaWidgetProvider extends AppWidgetProvider {

    @Inject
    RedditService redditService;

    public KarmaWidgetProvider() {
        RedditZenApplication.getComponent().inject(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        redditService.getAccount()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        account -> {
                            for (int i = 0; i < count; i++) {
                                int widgetId = appWidgetIds[i];

                                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                                        R.layout.widget_karma);
                                remoteViews.setTextViewText(R.id.username, account.name);
                                remoteViews.setTextViewText(R.id.link_karma, "" + account.linkKarma);
                                remoteViews.setTextViewText(R.id.comment_karma, "" + account.commentKarma);

                                appWidgetManager.updateAppWidget(widgetId, remoteViews);
                            }
                        },
                        error -> {

                        }
                );

    }
}
