package com.example.mymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.databinding.ItemTracksBinding;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.playtrack.PlayTrackActivity;
import com.example.mymusic.screen.track.ItemTrackViewModel;

import java.util.ArrayList;
import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {
    private List<Track> mTracks;
    private Context mContext;
    private ItemTracksBinding itemTracksBinding;
    private LayoutInflater layoutInflater;
    private OnItemClickListener mListener;

    public TracksAdapter(Context context, List<Track> tracks) {
        this.mTracks = tracks;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemTracksBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_tracks, parent, false);
        return new ViewHolder(itemTracksBinding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mTracks.get(position));
        holder.itemClick(position, mTracks);
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTracksBinding itemTracksBinding;
        OnItemClickListener mListener;
        ItemTrackViewModel itemTrackViewModel;

        private ViewHolder(@NonNull ItemTracksBinding itemView, OnItemClickListener listener) {
            super(itemView.getRoot());
            itemTracksBinding = itemView;
            mListener = listener;
            itemTrackViewModel = new ItemTrackViewModel(mListener);
            itemView.setListener(itemTrackViewModel);
        }

        private void bindData(Track track) {
            itemTracksBinding.setViewModel(track);
            itemTracksBinding.executePendingBindings();
        }

        private void itemClick(final int position, final List<Track> tracks){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayTrackActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("tracks", (ArrayList<? extends Parcelable>) tracks);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Track track);
    }
}
