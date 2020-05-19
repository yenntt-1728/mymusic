package com.example.mymusic.screen.artist;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic.R;
import com.example.mymusic.adapter.AlbumAdapter;
import com.example.mymusic.adapter.ArtistAdapter;
import com.example.mymusic.databinding.FragmentArtistBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.model.Artist;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {
    private FragmentArtistBinding mBinding;
    private ArtistViewModel mViewModel;
    private ArtistAdapter mAdapter;
    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ArtistViewModel();
        mViewModel.getArtist(getContext());
        observerData();
    }
    private void observerData() {
        mViewModel.getArtists().observe(this, new Observer<List<Artist>>() {
            @Override
            public void onChanged(List<Artist> artists) {
                mAdapter = new ArtistAdapter(artists, getContext());
                mBinding.recyclerArtist.setAdapter(mAdapter);
            }
        });
    }
}
