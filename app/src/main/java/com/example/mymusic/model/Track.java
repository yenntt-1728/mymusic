package com.example.mymusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Track implements Parcelable  {
    private int id;
    private String title;
    private String artist;
    private String image;
    private String url;
    private int duration;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Track(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public Track(int id, String title, String artist, String image, String url, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url = url;
        this.duration = duration;
    }

    public Track(int id, String title, String data,  String artist, String image, String url, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url = url;
        this.duration = duration;
        this.data=data;
    }

    public Track(String title, String artist, String image, String url, int duration) {
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url = url;
        this.duration = duration;
    }

    public Track(){}

    protected Track(Parcel in) {
        id = in.readInt();
        title = in.readString();
        artist = in.readString();
        image = in.readString();
        url = in.readString();
        duration = in.readInt();
        data = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(image);
        dest.writeString(url);
        dest.writeInt(duration);
        dest.writeString(data);
    }
}
