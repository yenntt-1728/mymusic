package com.example.mymusic.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mymusic.screen.albums.TabAlbumFragment;
import com.example.mymusic.screen.artist.ArtistFragment;
import com.example.mymusic.screen.track.TrackFragment;

public class MainAdapter extends FragmentPagerAdapter {
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TrackFragment();
                break;
            case 1:
                fragment = new TabAlbumFragment();
                break;
            case 2:
                fragment = new ArtistFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "List Track";
            case 1:
                return "Albums";
            case 2:
                return "Artist";
        }
        return super.getPageTitle(position);
    }
}
