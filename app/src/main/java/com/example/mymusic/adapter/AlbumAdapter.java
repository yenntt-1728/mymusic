package com.example.mymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.databinding.ItemAlbumBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.screen.albumdetail.AlbumDetailActivity;
import com.example.mymusic.screen.albums.TabAlbumFragment;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> mAlbums;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ItemAlbumBinding mBinding;
    private OnItemClickListener mListener;

    public AlbumAdapter(List<Album> mAlbums, Context mContext) {
        this.mAlbums = mAlbums;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_album, parent, false);
        mBinding.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Album album) {
                Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("album", album);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return new ViewHolder(mBinding, mListener);
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
        ItemAlbumBinding binding;
        OnItemClickListener itemClickListener;
        public ViewHolder(@NonNull ItemAlbumBinding itemView, OnItemClickListener listener) {
            super(itemView.getRoot());
            binding = itemView;
            itemClickListener = listener;
        }
        public void bindData(Album album){
            binding.setViewModel(album);
            binding.executePendingBindings();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Album album);
    }
}
