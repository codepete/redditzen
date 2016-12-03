package com.peterung.redditzen.data.db;


import android.content.Context;

import com.peterung.redditzen.BuildConfig;
import com.peterung.redditzen.data.db.entities.Models;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    SingleEntityStore<Persistable> provideSingleEntityStore(Context context) {
        // override onUpgrade to handle migrating to a new version
        DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        if (BuildConfig.DEBUG) {
            // use this in development mode to drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }
        Configuration configuration = source.getConfiguration();
        return RxSupport.toReactiveStore(new EntityDataStore<>(configuration));
    }
}
