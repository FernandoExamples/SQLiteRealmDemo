package com.tecno.udemy.fernando.sqliterealmdemo.model;

import com.tecno.udemy.fernando.sqliterealmdemo.app.MyApplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String description;
    @Required
    private Date createdAt;

    public Note() {
    }

    public Note(String description) {
        id = MyApplication.NoteID.incrementAndGet();
        this.description = description;
        createdAt = new Date();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
