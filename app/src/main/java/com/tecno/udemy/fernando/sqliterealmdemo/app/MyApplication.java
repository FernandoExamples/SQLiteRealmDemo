package com.tecno.udemy.fernando.sqliterealmdemo.app;

import android.app.Application;

import com.tecno.udemy.fernando.sqliterealmdemo.model.Board;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger BoardID = new AtomicInteger();
    public static AtomicInteger NoteID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConfig();
        Realm realmDB = Realm.getDefaultInstance();
        BoardID = getIdByTable(realmDB, Board.class);
        NoteID = getIdByTable(realmDB, Note.class);
        realmDB.close();
    }

    private void setUpRealmConfig(){
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realmDB, Class<T> anyClass){
        RealmResults results = realmDB.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger(1);
    }
}
