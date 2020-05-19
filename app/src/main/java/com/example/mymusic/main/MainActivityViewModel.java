package com.example.mymusic.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainActivityViewModel extends Observable {

    MutableLiveData<List<Track>> tracks = new MutableLiveData<>();
    private List<Track> mTracks = new ArrayList<>();

    public void getTrackLocal(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriSong = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor songCursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            songCursor = contentResolver.query(uriSong, null, null, null);
        }
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int songUrl = songCursor.getColumnIndex(String.valueOf(MediaStore.Audio.Media.getContentUriForPath(String.valueOf(MediaStore.Audio.Media.INTERNAL_CONTENT_URI))));
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.BOOKMARK);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            songCursor.moveToFirst();
            while (!songCursor.isAfterLast()) {
                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
//                String url = songCursor.getString(songUrl);
                String image = songCursor.getString(songImage);
                int duration = songCursor.getInt(songDuration);
                Track track = new Track(title, artist, image, null,duration);
                mTracks.add(track);
                songCursor.moveToNext();
            }
        }
        tracks.postValue(mTracks);
    }

    public MutableLiveData<List<Track>> getTracks() {
        return tracks;
    }

}
