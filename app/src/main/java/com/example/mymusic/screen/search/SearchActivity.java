package com.example.mymusic.screen.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;

import com.example.mymusic.R;
import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.databinding.ActivitySearchBinding;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.track.TrackFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private SearchViewModel searchViewModel;
    private ActivitySearchBinding searchBinding;
    private TracksAdapter tracksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        searchBinding.imageBack.setOnClickListener(this);
        searchBinding.search.setActivated(true);
        searchBinding.search.onActionViewExpanded();
        searchBinding.search.setIconified(false);
        searchBinding.search.clearFocus();
        searchBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                final List<Track> listSearchTrack = new ArrayList<>();
                searchViewModel = new SearchViewModel();
                searchViewModel.getTrackLocal(getApplicationContext());
                searchViewModel.tracks.observe(SearchActivity.this, new Observer<List<Track>>() {
                    @Override
                    public void onChanged(List<Track> tracks) {
                        for (int i = 0; i < tracks.size(); i++){
                            if(tracks.get(i).getTitle().contains(newText)){
                                listSearchTrack.add(tracks.get(i));
                            }
                        }
                        tracksAdapter = new TracksAdapter(getApplicationContext(), listSearchTrack);
                        searchBinding.recyclerSearch.setAdapter(tracksAdapter);
                    }
                });
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
