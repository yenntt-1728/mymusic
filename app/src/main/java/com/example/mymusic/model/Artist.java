package com.example.mymusic.model;

import java.io.Serializable;

public class Artist implements Serializable {
    private String artist;
    int albumCount;
    int trackCount;

    public Artist(String artist, int albumCount, int trackCount) {
        this.artist = artist;
        this.albumCount = albumCount;
        this.trackCount = trackCount;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }
}
