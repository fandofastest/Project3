package com.example.project3.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyPlaylistModel extends RealmObject {
    @PrimaryKey
    private Integer id;
    String name;
    int totalsong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalsong() {
        return totalsong;
    }

    public void setTotalsong(int totalsong) {
        this.totalsong = totalsong;
    }
}
