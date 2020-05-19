package com.example.mymusic.screen.search;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import com.example.mymusic.model.Track;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel {
    MutableLiveData<List<Track>> tracks = new MutableLiveData<>();
    private List<Track> mTracks = new ArrayList<>();

    public void getTrackLocal(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uriSong, null, null, null,null);
        if (songCursor != null) {
            int idColumn = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songImage = songCursor.getColumnIndex(MediaStore.Audio.Media.BOOKMARK);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            songCursor.moveToFirst();
            while (!songCursor.isAfterLast()) {
                int id = songCursor.getInt(idColumn);
                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
                String data = songCursor.getString(songData);
                String image = songCursor.getString(songImage);
                int duration = songCursor.getInt(songDuration);
                Track track = new Track(id, title, data, artist, image, null,duration);
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
