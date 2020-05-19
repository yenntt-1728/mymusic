package com.example.mymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.databinding.ItemAlbumBinding;
import com.example.mymusic.databinding.ItemAlbumDetailBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.albumdetail.AlbumDetailActivity;
import com.example.mymusic.utils.StringUtils;

import java.util.List;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {
    private List<Track> mAlbums;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ItemAlbumDetailBinding mBinding;
    private AlbumAdapter.OnItemClickListener mListener;

    public AlbumDetailAdapter(List<Track> mAlbums, Context mContext) {
        this.mAlbums = mAlbums;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_album_detail, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mAlbums.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbums.size() !=0 ? mAlbums.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ItemAlbumDetailBinding binding;
        AlbumAdapter.OnItemClickListener itemClickListener;
        public ViewHolder(@NonNull ItemAlbumDetailBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void bindData(Track album){
            binding.setViewModel(album);
            binding.duration.setText(StringUtils.convertMilisecondToTimer(album.getDuration()));
            binding.executePendingBindings();
        }
    }

}
