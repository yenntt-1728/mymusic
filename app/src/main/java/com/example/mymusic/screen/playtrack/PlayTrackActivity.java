package com.example.mymusic.screen.playtrack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.mymusic.R;
import com.example.mymusic.databinding.ActivityPlayTrackBinding;
import com.example.mymusic.model.Track;
import com.example.mymusic.service.TrackPlayerController;
import com.example.mymusic.service.TrackService;
import com.example.mymusic.utils.LoopType;
import com.example.mymusic.utils.Shuffle;
import com.example.mymusic.utils.State;

import java.util.ArrayList;
import java.util.List;

public class PlayTrackActivity extends AppCompatActivity implements TrackPlayerController.TrackInfoListener, View.OnClickListener {
    private ActivityPlayTrackBinding playTrackBinding;
    private PlayTrackActivityViewModel viewModel;

    private boolean mBound = false;
    private int mUserSelectPosition = 0;
    private static final int DELAY_MILLIS = 500;
    private Track mCurrentTrack;
    private Track trackPosition;
    private List<Track> mTracks;
    private TrackService mService;
    private Handler mHandler;
    private Runnable mRunnable;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MediaPlayer mMediaPlayer = new MediaPlayer();


    public PlayTrackActivity() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTracks = getIntent().getParcelableArrayListExtra("tracks");
        Intent intent = new Intent(this, TrackService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) mTracks);
        intent.setAction(TrackService.ACTION_CHANGE_STATE);
        startService(intent);
        setupEvents();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playTrackBinding = DataBindingUtil.setContentView(PlayTrackActivity.this, R.layout.activity_play_track);
        viewModel = new PlayTrackActivityViewModel();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTracks = bundle.getParcelableArrayList("tracks");
            int position = bundle.getInt("position");
            trackPosition = mTracks.get(position);
            mCurrentTrack = trackPosition;
            viewModel = new PlayTrackActivityViewModel();
        }
        playTrackBinding.setViewModel(mCurrentTrack);
        playTrackBinding.buttonStateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(mConnection);
        mBound = false;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            mService = ((TrackService.LocalBinder) service).getService();
            mService.setTrackInfoListener(PlayTrackActivity.this);
//            mCurrentTrack = mService.getCurrentTrack();

            playTrackBinding.setViewModel(mCurrentTrack);

            setupEvents();
            updateTrackInfo();
            updateStateInfo(mService.getMediaState());
            updateLoopState(mService.getLoopType());
            updateShuffleState(mService.getShuffleMode());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            mService.setTrackInfoListener(null);
        }
    };

    public void updateTrackInfo() {
        if (mCurrentTrack == null || mService == null) return;
        playTrackBinding.textTittle.setText(mCurrentTrack.getTitle());
        playTrackBinding.textArtist.setText(mCurrentTrack.getArtist());
        playTrackBinding.textTimeEnd.setText(String.valueOf((mCurrentTrack.getDuration())));
        playTrackBinding.textTimeStart.setText(String.valueOf(0));
        if (mCurrentTrack.getUrl() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playTrackBinding.imageTrack.setImageDrawable(getDrawable(R.drawable.ic_music_note_black_24dp));
            }
        } else {
            Glide.with(getApplicationContext()).load(mCurrentTrack.getImage()).into(playTrackBinding.imageTrack);
        }
    }

    public void updateStateInfo(@State int state) {
        switch (state) {
            case State.PREPARE:
                stopUpdate();
            case State.PLAYING:
                playTrackBinding.buttonStateChange.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                updateSeekBar();
                break;
            case State.PAUSE:
                playTrackBinding.buttonStateChange.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
                break;
            default:
                break;
        }
    }

    private void stopUpdate() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void updateLoopState(@LoopType int loopType) {
        switch (loopType) {
            case LoopType.NO_LOOP:
                playTrackBinding.imageLoop.setImageResource(R.drawable.ic_repeat_black_24dp);
                break;
            case LoopType.LOOP_ONE:
                playTrackBinding.imageLoop.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                break;
            case LoopType.LOOP_LIST:
                playTrackBinding.imageLoop.setImageResource(R.drawable.ic_replay_black_24dp);
                break;
        }
    }

    public List<Track> getListTrack() {
        viewModel = new PlayTrackActivityViewModel();
        viewModel.tracks.observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                mTracks.addAll(tracks);
            }
        });
        return mTracks;
    }

    private void updateShuffleState(@Shuffle int shuffleMode) {
        switch (shuffleMode) {
            case Shuffle.OFF:
                playTrackBinding.imageShuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                break;
            case Shuffle.ON:
                playTrackBinding.imageShuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
        }
    }

    @Override
    public void onTrackChanged(Track track) {
        mCurrentTrack = track;
        updateTrackInfo();
    }

    @Override
    public void onStateChanged(int state) {
        updateStateInfo(state);
    }

    @Override
    public void onLoopChanged(int loopType) {
        updateLoopState(loopType);
    }

    @Override
    public void onShuffleChanged(int shuffleMode) {
        updateShuffleState(shuffleMode);
    }

    private void setupEvents() {
        playTrackBinding.setViewModel(mCurrentTrack);
        playTrackBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        playTrackBinding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.playNext();
            }
        });
        playTrackBinding.seekBarDuration.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            mUserSelectPosition = progress;
//            mMediaPlayer.start();
//            seekBar.setProgress(mMediaPlayer.getCurrentPosition());
//            playTrackBinding.textTimeStart.setText(mMediaPlayer.getCurrentPosition());
            playTrackBinding.seekBarDuration.setProgress(progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mBound) {
                mService.actionSeekTo(mUserSelectPosition);
                playTrackBinding.textTimeStart.setText(String.valueOf(mUserSelectPosition));
            }
        }
    };

    public void updateSeekBar() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                playTrackBinding.seekBarDuration.setProgress(0);
                playTrackBinding.textTimeStart.setText(String.valueOf(mService.getCurrentPosition()));
                mHandler.postDelayed(this, DELAY_MILLIS);
            }
        };
        mHandler.post(mRunnable);
    }

    @Override
    public void onClick(View v) {
    }

    public void onClickButtonBack(){
        onBackPressed();
    }

    public void clickPause(){
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
    }

}
