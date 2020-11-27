package com.example.project3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumModel implements Parcelable {
    String id,albumName,artistName,genre,imageUrl,deskripsi,years,artistcover;
    int plays;


    public AlbumModel() {
    }

    protected AlbumModel(Parcel in) {
        id = in.readString();
        albumName = in.readString();
        artistName = in.readString();
        genre = in.readString();
        imageUrl = in.readString();
        deskripsi = in.readString();
        years = in.readString();
        artistcover = in.readString();
        plays = in.readInt();
    }

    public static final Creator<AlbumModel> CREATOR = new Creator<AlbumModel>() {
        @Override
        public AlbumModel createFromParcel(Parcel in) {
            return new AlbumModel(in);
        }

        @Override
        public AlbumModel[] newArray(int size) {
            return new AlbumModel[size];
        }
    };

    public String getArtistcover() {
        return artistcover;
    }

    public void setArtistcover(String artistcover) {
        this.artistcover = artistcover;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(albumName);
        dest.writeString(artistName);
        dest.writeString(genre);
        dest.writeString(imageUrl);
        dest.writeString(deskripsi);
        dest.writeString(years);
        dest.writeString(artistcover);
        dest.writeInt(plays);
    }
}
