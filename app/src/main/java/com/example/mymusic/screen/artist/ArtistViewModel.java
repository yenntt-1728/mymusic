package com.example.mymusic.screen.artist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import com.example.mymusic.model.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ArtistViewModel extends Observable {
    private MutableLiveData<List<Artist>> artists = new MutableLiveData<>();
    private List<Artist> mArtists = new ArrayList<>();

    public void getArtist(Context context){
        Uri uriSong = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;

        Cursor songCursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            songCursor = context.getContentResolver().query(uriSong,
                    null,
                    null,
                    null,
                    null);
        }
        if (songCursor != null && songCursor.moveToFirst()) {
            int columnArtistName  = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int columnAlbumCount = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int columTrackCount = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            songCursor.moveToFirst();
            while (!songCursor.isAfterLast()) {
                String artistName = songCursor.getString(columnArtistName);
                int albumCount = Integer.parseInt(songCursor.getString(columnAlbumCount));
                int trackCount = Integer.parseInt(songCursor.getString(columTrackCount));
                Artist artist = new Artist(artistName, albumCount, trackCount);
                mArtists.add(artist);
                songCursor.moveToNext();
            }
        }
        artists.postValue(mArtists);
    }

    public MutableLiveData<List<Artist>> getArtists(){
        return artists;
    }
}
