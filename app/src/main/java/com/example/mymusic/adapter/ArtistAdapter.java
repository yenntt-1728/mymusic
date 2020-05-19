package com.example.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.databinding.ItemAlbumBinding;
import com.example.mymusic.databinding.ItemArtistBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.model.Artist;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private List<Artist> mArtist;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ItemArtistBinding mBinding;

    public ArtistAdapter(List<Artist> mArtist, Context mContext) {
        this.mArtist = mArtist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_artist, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mArtist.get(position));
    }


    @Override
    public int getItemCount() {
        return mArtist.size() !=0 ? mArtist.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemArtistBinding binding;
        public ViewHolder(@NonNull ItemArtistBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        public void bindData(Artist artist){
            binding.setViewModel(artist);
            binding.textViewAlbumCount.setText(String.valueOf(artist.getAlbumCount()));
            binding.executePendingBindings();
        }
    }
}
