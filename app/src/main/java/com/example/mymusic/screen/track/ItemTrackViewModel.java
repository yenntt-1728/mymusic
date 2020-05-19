package com.example.mymusic.screen.track;

import android.view.View;

import com.example.mymusic.adapter.TracksAdapter;

import java.util.Observable;

public class ItemTrackViewModel extends Observable {
    private TracksAdapter.OnItemClickListener mListener;
    public ItemTrackViewModel(TracksAdapter.OnItemClickListener listener){
        mListener = listener;
    }

}
