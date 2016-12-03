package com.peterung.redditzen.data.db.schematic;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = MessagesDatabase.VERSION,
        packageName = "com.peterung.redditzen.data.provider")
public final class MessagesDatabase {
    public static final int VERSION = 1;

    @Table(MessageColumns.class) public static final String MESSAGES = "messages";

}
