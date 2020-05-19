package com.example.mymusic.screen.albums;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import com.example.mymusic.model.Album;
import com.example.mymusic.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class TabAlbumViewModel extends Observable {
    private MutableLiveData<List<Album>> albums = new MutableLiveData<>();
    private List<Album> albumList = new ArrayList<>();

    public void getAlbumLocal(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriSong = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor songCursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            songCursor = contentResolver.query(uriSong, null, null, null);
        }
        if (songCursor != null && songCursor.moveToFirst()) {
            int columnAlbumName = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int columnAlbumArt = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int columnNumberOfTrack = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            songCursor.moveToFirst();
            while (!songCursor.isAfterLast()) {
                String albumName = songCursor.getString(columnAlbumName);
                String albumAritst = songCursor.getString(columnAlbumArt);
                String numberOfTrack = songCursor.getString(columnNumberOfTrack);
                Album album = new Album(numberOfTrack, albumName, albumAritst);
                albumList.add(album);
                songCursor.moveToNext();
            }
        }
        albums.postValue(albumList);
    }

    public MutableLiveData<List<Album>> getAlbums() {
        return albums;
    }

}
