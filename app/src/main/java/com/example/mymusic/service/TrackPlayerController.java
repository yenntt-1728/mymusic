package com.example.mymusic.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.mymusic.model.Track;
import com.example.mymusic.screen.playtrack.PlayTrackActivityViewModel;
import com.example.mymusic.utils.LoopType;
import com.example.mymusic.utils.Shuffle;
import com.example.mymusic.utils.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TrackPlayerController implements TrackPlayerManager {

    private static final long DELAY_MILLIS = 500;
    private static final String DOWNLOAD_DIRECTORY = "MyMusic";
    @State
    private int mState;
    @LoopType
    private int mLoopType = LoopType.NO_LOOP;
    @Shuffle
    private int mShuffleMode;
    private int mCurrentTrackPosition;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private TrackService mTrackService;
    private TrackInfoListener mInfoListener;
    private List<Track> mTracks;
    private List<Track> mOriginalTracks;

    public TrackPlayerController(TrackService trackService, List<Track> tracks) {
        mTrackService = trackService;
        mTracks = tracks;
    }

    private PlayTrackActivityViewModel viewModel;
    private final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };

    private final MediaPlayer.OnCompletionListener mCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            notifyStateChanged(State.PAUSE);
            handlePlayTrackWithLoopType();
        }
    };

    private final MediaPlayer.OnPreparedListener mOnPrepared = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
            notifyStateChanged(State.PLAYING);
        }
    };


    @Override
    public int getCurrentState() {
        return mState;
    }

    @Override
    public void playPreviousTrack() {
        if (mCurrentTrackPosition > 0) {
            mCurrentTrackPosition--;
            prepareTrack();
        }
    }

    @Override
    public void changeTrackState() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            notifyStateChanged(State.PAUSE);
        } else {
            mMediaPlayer.start();
            notifyStateChanged(State.PLAYING);
            // TODO
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void seekTo(int percent) {
        if (mState == State.PLAYING) {
            mMediaPlayer.seekTo(mMediaPlayer.getDuration() / 100 * percent);
        }
    }

    @Override
    public void setTrackInfoListener(TrackInfoListener listener) {
        mInfoListener = listener;
    }

    @Override
    public Track getCurrentTrack() {
        return mTracks == null ? null : mTracks.get(mCurrentTrackPosition);
    }

    @Override
    public int getCurrentTrackPosition() {
        return mCurrentTrackPosition;
    }

    @Override
    public List<Track> getListTracks() {
        return mTracks;
    }

    @Override
    public void playNextTrack() {
        if (mCurrentTrackPosition < mTracks.size() - 1) {
            mCurrentTrackPosition++;
            prepareTrack();
        }
    }

    @Override
    public void playTrackAtPosition(int position, List<Track> tracks) {
        if (tracks == null && mTracks == null) {
            notifyStateChanged(State.INVALID);
            return;
        }

        if ((tracks == null || tracks.size() == 0) && mCurrentTrackPosition == position) return;
        if (tracks != null && tracks.size() != 0) {
            mTracks = new ArrayList<>();
            mTracks.addAll(tracks);
        }
        mCurrentTrackPosition = position;
        prepareTrack();
    }

    @Override
    public void addToNextUp(Track track) {
        if (mTracks == null || mTracks.isEmpty()) return;
        mTracks.add(track);
        if (mShuffleMode == Shuffle.ON) {
            mOriginalTracks.add(track);
        }
    }

    @Override
    public int getLoopType() {
        return mLoopType;
    }

    @Override
    public int getShuffleMode() {
        return mShuffleMode;
    }

    @Override
    public void changeLoopType() {
        switch (mLoopType) {
            case LoopType.NO_LOOP:
                mLoopType = LoopType.LOOP_LIST;
                break;
            case LoopType.LOOP_LIST:
                mLoopType = LoopType.LOOP_ONE;
                break;
            case LoopType.LOOP_ONE:
                mLoopType = LoopType.NO_LOOP;
                break;
        }
        if (mInfoListener == null) return;
        mInfoListener.onLoopChanged(mLoopType);
    }

    @Override
    public void changeShuffleState() {
        Track currentTrack = null;
        switch (mShuffleMode) {
            case Shuffle.ON:
                mShuffleMode = Shuffle.OFF;
                currentTrack = mTracks.get(mCurrentTrackPosition);
                mTracks = new ArrayList<>();
                mTracks.addAll(mOriginalTracks);
                break;
            case Shuffle.OFF:
                mShuffleMode = Shuffle.ON;
                mOriginalTracks = new ArrayList<>();
                mOriginalTracks.addAll(mTracks);
                currentTrack = mTracks.get(mCurrentTrackPosition);
                Collections.shuffle(mTracks);
                break;
        }
        mCurrentTrackPosition = mTracks.indexOf(currentTrack);
        if (mInfoListener == null) return;
        mInfoListener.onShuffleChanged(mShuffleMode);
    }

    private void notifyStateChanged(@State int state) {
        mState = state;
        if (mTrackService != null) {
            if (state == State.PREPARE) mTrackService.loadImage();
            mTrackService.createNotification(state);
        }
        if (mInfoListener == null) return;
        mInfoListener.onStateChanged(mState);
    }

    private void prepareTrack() {
        if (mTracks == null || mTracks.isEmpty()) {
            notifyStateChanged(State.INVALID);
            return;
        }
        release();
        notifyStateChanged(State.PREPARE);
        loadTrack();
        if (mInfoListener == null) return;
        mInfoListener.onTrackChanged(mTracks.get(mCurrentTrackPosition));
    }

    private void loadTrack() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String path = mTracks.get(mCurrentTrackPosition).getData();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnCompletionListener(mCompletion);
            mMediaPlayer.setOnPreparedListener(mOnPrepared);
            mMediaPlayer.setOnErrorListener(mErrorListener);
        } catch (IOException e) {
            notifyStateChanged(State.INVALID);
            if (mCurrentTrackPosition < mTracks.size()) playNextTrack();
            Logger.getLogger(e.toString());
        }
    }

    private void handlePlayTrackWithLoopType() {
        switch (mLoopType) {
            case LoopType.NO_LOOP:
                playNextTrack();
                break;
            case LoopType.LOOP_ONE:
                mMediaPlayer.start();
                notifyStateChanged(State.PLAYING);
                break;
            case LoopType.LOOP_LIST:
                if (mCurrentTrackPosition == mTracks.size() - 1) {
                    mCurrentTrackPosition = -1;
                }
                playNextTrack();
                break;
        }
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer == null) return 0;
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer == null) return 0;
        return mMediaPlayer.getDuration();
    }

    public interface TrackInfoListener {

        void onTrackChanged(Track track);

        void onStateChanged(@State int state);

        void onLoopChanged(@LoopType int loopType);

        void onShuffleChanged(@Shuffle int shuffleMode);
    }

}
