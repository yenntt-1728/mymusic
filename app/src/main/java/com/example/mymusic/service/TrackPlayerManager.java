package com.example.mymusic.service;

import android.content.Context;

import com.example.mymusic.model.Track;
import com.example.mymusic.utils.LoopType;
import com.example.mymusic.utils.Shuffle;

import java.util.List;

public interface TrackPlayerManager {
    int getCurrentState();

    void playNextTrack();

    void playPreviousTrack();

    void changeTrackState();

    void release();

    void seekTo(int position);

    Track getCurrentTrack();

    void addToNextUp(Track track);

    void setTrackInfoListener(TrackPlayerController.TrackInfoListener listener);

    int getCurrentTrackPosition();

    List<Track> getListTracks();

    void playTrackAtPosition(int position, List<Track> listTrack);

    @LoopType
    int getLoopType();

    @Shuffle
    int getShuffleMode();

    void changeLoopType();

    void changeShuffleState();

    int getCurrentPosition();

    int getDuration();
}
