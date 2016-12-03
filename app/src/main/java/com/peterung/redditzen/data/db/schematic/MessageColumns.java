package com.peterung.redditzen.data.db.schematic;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface MessageColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NotNull String AUTHOR = "author";
    @DataType(TEXT) @NotNull @Unique(onConflict = ConflictResolutionType.REPLACE) String NAME = "name";
    @DataType(TEXT) @NotNull String BODY = "body";
    @DataType(TEXT) @NotNull String SUBJECT = "subject";
    @DataType(INTEGER) @NotNull String UNREAD = "unread";
    @DataType(TEXT) @NotNull String CREATED = "created";
}
