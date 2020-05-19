package com.example.mymusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mymusic.R;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.playtrack.PlayTrackActivity;
import com.example.mymusic.utils.LoopType;
import com.example.mymusic.utils.Shuffle;
import com.example.mymusic.utils.State;

import java.util.List;

public class TrackService extends Service {
    private static final String TITLE_PLAY = "Play";
    private static final String TITLE_PAUSE = "Pause";
    private static final String TITLE_NEXT = "Next";
    private static final String TITLE_PREVIOUS = "Previous";

    public static final String ACTION_CHANGE_STATE = "ACTION_CHANGE_STATE";
    public static final String ACTION_NEXT_TRACK = "ACTION_NEXT_TRACK";
    public static final String ACTION_PREVIOUS_TRACK = "ACTION_PREVIOUS_TRACK";
    public static final String ACTION_OPEN_PLAY_TRACK_ACTIVITY = "ACTION_OPEN_PLAY_TRACK_ACTIVITY";
    public static final int SECONDS_FACTOR = 1000;
    public static final int DEFAULT_NOTIFY_SIZE = 100;

    private Bitmap mBitmap;
    private static final int NOTIFY_ID = 1;
    private TrackPlayerManager mTrackPlayerManager;
    private PendingIntent mPendingOpenApp;
    private PendingIntent mPendingPrevious;
    private PendingIntent mPendingState;
    private PendingIntent mPendingNext;
    private NotificationCompat.Builder mBuilder;
    private Handler mTimerHandler;
    private Runnable mRunnable;
    private PlayTrackActivity playTrackActivity;
    private final IBinder mBinder = new LocalBinder();
    private List<Track> mTrack;

    public TrackService(){
        playTrackActivity = new PlayTrackActivity();
        mTrack = playTrackActivity.getListTrack();
        mTrackPlayerManager = new TrackPlayerController(this, mTrack);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mTrackPlayerManager = new TrackPlayerController(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
//        startForeground(1,mBuilder.build());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handleIntent(intent);
        return mBinder;
    }

    public void handleIntent(Intent intent) {
        mTrack=intent.getParcelableArrayListExtra("list");
        playTrackAtPosition(0,mTrack);
        if (intent == null || intent.getAction() == null) return;
        switch (intent.getAction()) {
            case ACTION_CHANGE_STATE:
                if (getMediaState() != State.PREPARE) {
                    changeTrackState();
                }
                break;
            case ACTION_PREVIOUS_TRACK:
                playPrevious();
                break;
            case ACTION_NEXT_TRACK:
                playNext();
                break;
            default:
                break;
        }
    }

    public void setTrackInfoListener(TrackPlayerController.TrackInfoListener listener) {
        if (mTrackPlayerManager == null) return;
        mTrackPlayerManager.setTrackInfoListener(listener);
    }

    public Track getCurrentTrack() {
        return mTrackPlayerManager != null ? mTrackPlayerManager.getCurrentTrack() : null;
    }

    public void actionSeekTo(int userSelectedPosition) {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.seekTo(userSelectedPosition);
        }
    }

    public void changeTrackState() {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.changeTrackState();
        }
    }

    public void playNext() {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.playNextTrack();
        }
    }

    public void playPrevious() {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.playPreviousTrack();
        }
    }

    public int getMediaState() {
        if (mTrackPlayerManager == null) return State.INVALID;
        return mTrackPlayerManager.getCurrentState();
    }

    public List<Track> getListTrack() {
        if (mTrackPlayerManager == null) return null;
        return mTrackPlayerManager.getListTracks();
    }

    public void playTrackAtPosition(int position,List<Track> tracks) {
        if (mTrackPlayerManager == null) {
            mTrackPlayerManager = new TrackPlayerController(this, mTrack);
        }
        mTrackPlayerManager.playTrackAtPosition(position, tracks);
    }

    public void addToNextUp(Track track) {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.addToNextUp(track);
        }
    }

    public int getCurrentTrackPosition() {
        if (mTrackPlayerManager == null) return 0;
        return mTrackPlayerManager.getCurrentTrackPosition();
    }

    public void changeLoopType() {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.changeLoopType();
        }
    }

    @LoopType
    public int getLoopType() {
        if (mTrackPlayerManager == null) return LoopType.NO_LOOP;
        return mTrackPlayerManager.getLoopType();
    }

    public void changeShuffleState() {
        if (mTrackPlayerManager != null) {
            mTrackPlayerManager.changeShuffleState();
        }
    }

    public int getShuffleMode() {
        if (mTrackPlayerManager != null) {
            return mTrackPlayerManager.getShuffleMode();
        }
        return Shuffle.OFF;
    }

    public void createNotification(@State int state) {
        if (getCurrentTrack() == null) return;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setupBaseNotification();
        setupNotificationChannel(notificationManager);
        mBuilder.setChannelId("chanel_id");
        if (state == State.PAUSE) {
            stopForeground(false);
            mBuilder.setOngoing(false)
                    .addAction(R.drawable.ic_skip_previous_black_24dp, TITLE_PREVIOUS, mPendingPrevious)
                    .addAction(R.drawable.ic_play_circle_filled_black_24dp, TITLE_PLAY, mPendingState)
                    .addAction(R.drawable.ic_next_black_24dp, TITLE_NEXT, mPendingNext);
            notificationManager.notify(NOTIFY_ID, mBuilder.build());
        } else {
            mBuilder.addAction(R.drawable.ic_skip_previous_black_24dp, TITLE_PREVIOUS, mPendingPrevious)
                    .addAction(R.drawable.ic_pause_circle_filled_black_24dp, TITLE_PAUSE, mPendingState)
                    .addAction(R.drawable.ic_next_black_24dp, TITLE_NEXT, mPendingNext);
            startForeground(NOTIFY_ID, mBuilder.build());
        }
    }

    private void setupNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelID = "chanel_id";
            String channelName = "chanel_name";
            String channelDescription = "description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channelID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channelID, channelName, importance);
                mChannel.setDescription(channelDescription);
                mChannel.enableVibration(false);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }

    public void loadImage() {
        if (getCurrentTrack() == null) return;
        if(getCurrentTrack().getUrl() == null) {
            return;
        }
        Glide.with(this)
                .asBitmap()
                .load(getCurrentTrack().getUrl())
                .apply(new RequestOptions().error(R.drawable.ic_launcher_background))
                .into(new SimpleTarget<Bitmap>(DEFAULT_NOTIFY_SIZE, DEFAULT_NOTIFY_SIZE) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        mBitmap = resource;
                        mBuilder.setLargeIcon(mBitmap);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFY_ID, mBuilder.build());
                    }
                });
    }

    private void setupBaseNotification() {
        Intent notificationIntent = new Intent(getApplicationContext(), PlayTrackActivity.class);
        notificationIntent.setAction(ACTION_OPEN_PLAY_TRACK_ACTIVITY);
        mPendingOpenApp = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Intent nextIntent = new Intent(getApplicationContext(), TrackService.class);
        nextIntent.setAction(ACTION_NEXT_TRACK);
        mPendingNext = PendingIntent.getService(getApplicationContext(), 0, nextIntent, 0);

        Intent prevIntent = new Intent(getApplicationContext(), TrackService.class);
        prevIntent.setAction(ACTION_PREVIOUS_TRACK);
        mPendingPrevious = PendingIntent.getService(getApplicationContext(), 0, prevIntent, 0);

        Intent stateIntent = new Intent(getApplicationContext(), TrackService.class);
        stateIntent.setAction(ACTION_CHANGE_STATE);
        mPendingState = PendingIntent.getService(getApplicationContext(), 0, stateIntent, 0);

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "Chanel_id")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getCurrentTrack().getTitle())
                .setColor(getResources().getColor(R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setContentIntent(mPendingOpenApp);
        if (mBitmap == null) {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_music_note_black_24dp));
        } else {
            mBuilder.setLargeIcon(mBitmap);
        }
    }

    public int getCurrentPosition() {
        if (mTrackPlayerManager != null) {
            return mTrackPlayerManager.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (mTrackPlayerManager != null) {
            return mTrackPlayerManager.getDuration();
        }
        return 0;
    }

    public void startTimer(long delay) {
        if (mTrackPlayerManager == null) return;
        mTimerHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTrackPlayerManager.release();
                stopForeground(true);
                stopSelf();
            }
        };
        mTimerHandler.postDelayed(mRunnable, delay * SECONDS_FACTOR);
    }

    public void cancelTimer() {
        if (mTrackPlayerManager == null || mTimerHandler == null) return;
        mTimerHandler.removeCallbacks(mRunnable);
        mTimerHandler = null;
        mRunnable = null;
    }

    public class LocalBinder extends Binder {
        public TrackService getService() {
            return new TrackService();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
