package com.tecno.udemy.fernando.sqliterealmdemo.model;

import com.tecno.udemy.fernando.sqliterealmdemo.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Board extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String title;
    @Required
    private Date createdAt;

    private RealmList<Note> notes;

    public Board(){}

    public Board(String title) {
        this.title = title;
        id = MyApplication.BoardID.incrementAndGet();
        createdAt = new Date();
        notes = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }
}
