package com.example.project3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PLaylistModel implements Parcelable {

    private Integer id;
    String name;
    int totalsong;
    String imgurl;

    protected PLaylistModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        totalsong = in.readInt();
        imgurl = in.readString();
    }


    public PLaylistModel() {
    }

    public static final Creator<PLaylistModel> CREATOR = new Creator<PLaylistModel>() {
        @Override
        public PLaylistModel createFromParcel(Parcel in) {
            return new PLaylistModel(in);
        }

        @Override
        public PLaylistModel[] newArray(int size) {
            return new PLaylistModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeInt(totalsong);
        dest.writeString(imgurl);
    }
}
