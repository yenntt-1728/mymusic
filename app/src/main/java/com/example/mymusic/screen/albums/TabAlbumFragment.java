package com.example.mymusic.screen.albums;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic.R;
import com.example.mymusic.adapter.AlbumAdapter;
import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.databinding.FragmentTabAlbumBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.track.TrackFragmentViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAlbumFragment extends Fragment {
    private FragmentTabAlbumBinding binding;
    private TabAlbumViewModel albumViewModel;
    private AlbumAdapter albumAdapter;

    public TabAlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_album, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        albumViewModel = new TabAlbumViewModel();
        albumViewModel.getAlbumLocal(getContext());
        observerData();
    }

    private void observerData() {
        albumViewModel.getAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                albumAdapter = new AlbumAdapter(albums, getContext());
                binding.recyclerAlbums.setLayoutManager(new GridLayoutManager(getContext(), 3));
                binding.recyclerAlbums.setAdapter(albumAdapter);
            }
        });
    }


}
