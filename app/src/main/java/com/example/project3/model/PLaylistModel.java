package com.example.project3.model;

public class PLaylistModel {

    private Integer id;
    String name;
    int totalsong;
    String imgurl;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
