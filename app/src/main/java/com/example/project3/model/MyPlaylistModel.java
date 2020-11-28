package com.example.project3.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyPlaylistModel extends RealmObject implements Parcelable {
    @PrimaryKey
    private Integer id;
    String name;
    int totalsong;

    protected MyPlaylistModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        totalsong = in.readInt();
    }

    public MyPlaylistModel() {
    }

    public static final Creator<MyPlaylistModel> CREATOR = new Creator<MyPlaylistModel>() {
        @Override
        public MyPlaylistModel createFromParcel(Parcel in) {
            return new MyPlaylistModel(in);
        }

        @Override
        public MyPlaylistModel[] newArray(int size) {
            return new MyPlaylistModel[size];
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
    }
}
