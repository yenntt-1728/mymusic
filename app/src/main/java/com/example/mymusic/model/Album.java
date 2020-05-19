package com.example.mymusic.model;

import java.io.Serializable;

public class Album implements Serializable {
    private String albumId;
    private String numberOfTrack;
    private String albumName;
    private String albumArtist;

    public Album(String albumName, String albumArtist) {
        this.albumName = albumName;
        this.albumArtist = albumArtist;
    }

    public Album(String numberOfTrack, String albumName, String albumArtist) {
        this.numberOfTrack = numberOfTrack;
        this.albumName = albumName;
        this.albumArtist = albumArtist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getNumberOfTrack() {
        return numberOfTrack;
    }

    public void setNumberOfTrack(String numberOfTrack) {
        this.numberOfTrack = numberOfTrack;
    }
}
