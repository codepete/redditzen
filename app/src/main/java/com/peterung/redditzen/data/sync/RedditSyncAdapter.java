package com.peterung.redditzen.data.sync;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.api.model.Comment;
import com.peterung.redditzen.data.api.model.Message;
import com.peterung.redditzen.data.db.schematic.MessageColumns;
import com.peterung.redditzen.data.db.schematic.MessagesProvider;
import com.peterung.redditzen.utils.RedditObjectConverter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RedditSyncAdapter extends AbstractThreadedSyncAdapter {

    @Inject
    RedditService redditService;
    String after;

    public RedditSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        RedditZenApplication.getComponent().inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        redditService.getMessages(20, after)
                .subscribeOn(Schedulers.io())
                .map(redditResponse -> {
                    List<Comment> messages = RedditObjectConverter.convertToComments(redditResponse.data.children);
                    addMessages(messages);
                    return redditResponse.data.after;
                })
                .takeUntil(after -> after == null)
                .subscribe(
                        success -> {
                            Timber.d("Successfully updated messages");
                        },
                        error -> {
                            Timber.d("Error updating messages");
                        }
                );
    }


    public int addMessages(List<Comment> messages) {
        ArrayList<ContentValues> cVVector = new ArrayList<>();

        for (Comment message: messages) {
            ContentValues trailerValues = new ContentValues();

            trailerValues.put(MessageColumns.NAME, message.name);
            trailerValues.put(MessageColumns.CREATED, message.createdUtc.toString());
            trailerValues.put(MessageColumns.SUBJECT, message.subject);
            trailerValues.put(MessageColumns.AUTHOR, message.author);
            trailerValues.put(MessageColumns.BODY, message.body);
            trailerValues.put(MessageColumns.UNREAD, message.unread ? 1 : 0);


            cVVector.add(trailerValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] values = new ContentValues[cVVector.size()];
            cVVector.toArray(values);

            return getContext().getContentResolver().bulkInsert(MessagesProvider.Messages.CONTENT_URI, values);
        }

        return 0;
    }

}
