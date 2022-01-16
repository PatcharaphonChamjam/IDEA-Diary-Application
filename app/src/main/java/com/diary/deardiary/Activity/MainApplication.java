package com.diary.deardiary.Activity;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.rx.RealmObservableFactory;

/**
 * Created by USER on 28/7/2560.
 */

public class MainApplication extends Application{

    RealmConfiguration config;
    private final String DATABASE_NAME = "Diarycat.realm";

    public MainApplication(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        try {
            config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(0)
                    .rxFactory(new RealmObservableFactory())
                    .build();
            Realm.setDefaultConfiguration(config);
        } catch (RealmMigrationNeededException rme) {//มีการเปลี่ยนแปลงโครงสร้าง DB
            config = new RealmConfiguration.Builder()
                    .name(DATABASE_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);

        } catch (Exception e) {
            config = new RealmConfiguration.Builder()
                    .name(DATABASE_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);
            Log.e("initListener: " + e.getMessage(),e.toString());
        }
        finally {
            Realm.getDefaultInstance().setAutoRefresh(true);
            Log.e("LOG: ", Realm.getDefaultInstance().getPath() );
        }

    }
}
