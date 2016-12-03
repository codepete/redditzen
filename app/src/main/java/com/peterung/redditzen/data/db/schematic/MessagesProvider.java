package com.peterung.redditzen.data.db.schematic;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = MessagesProvider.AUTHORITY,
        database = MessagesDatabase.class,
        packageName = "com.peterung.redditzen.data.provider")
public final class MessagesProvider {
    public static final String AUTHORITY = "com.peterung.redditzen.provider.MessagesProvider";

    @TableEndpoint(table = MessagesDatabase.MESSAGES) public static class Messages {

        @ContentUri(
                path = "messages",
                type = "vnd.android.cursor.dir/message",
                defaultSort = MessageColumns.CREATED + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");

    }
}
