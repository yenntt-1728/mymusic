package com.example.mymusic.screen.track;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymusic.R;
import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.databinding.FragmentTrackBinding;
import com.example.mymusic.model.Track;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackFragment extends Fragment {
    private FragmentTrackBinding trackBinding;
    private TracksAdapter tracksAdapter;
    private TrackFragmentViewModel trackViewModel;
    private List<Track> mTracks;

    public TrackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        trackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track,container, false);
        return trackBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackViewModel = new TrackFragmentViewModel();
        trackViewModel.getTrackLocal(getContext());
        observerData();
    }

    private void observerData() {
        trackViewModel.getTracks().observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                tracksAdapter = new TracksAdapter(getContext(), tracks);
                trackBinding.recyclerView.setAdapter(tracksAdapter);
            }
        });
    }
}
