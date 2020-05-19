package com.example.mymusic.screen.albumdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.mymusic.R;
import com.example.mymusic.adapter.AlbumDetailAdapter;
import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.databinding.ActivityAlbumDetailBinding;
import com.example.mymusic.model.Album;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.albums.TabAlbumFragment;
import com.example.mymusic.screen.track.TrackFragmentViewModel;

import java.util.List;

public class AlbumDetailActivity extends AppCompatActivity {
    private ActivityAlbumDetailBinding mBinding;
    private AlbumDetailAdapter mAdapter;
    private TrackFragmentViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_album_detail);
        Bundle bundle = getIntent().getExtras();
        Album album = (Album) bundle.getSerializable("album");
        mBinding.setViewModel(album);
        mViewModel = new TrackFragmentViewModel();
        mViewModel.getTrackLocal(AlbumDetailActivity.this);
        getListTrack();
    }

    private void getListTrack() {
        mViewModel.getTracks().observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                mAdapter = new AlbumDetailAdapter(tracks, getApplicationContext());
                mBinding.recyclerAlbumTrack.setAdapter(mAdapter);
            }
        });

    }
}
