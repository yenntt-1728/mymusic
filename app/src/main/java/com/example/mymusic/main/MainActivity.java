package com.example.mymusic.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.adapter.MainAdapter;
import com.example.mymusic.adapter.TracksAdapter;
import com.example.mymusic.databinding.ActivityMainBinding;
import com.example.mymusic.databinding.FragmentTrackBinding;
import com.example.mymusic.model.Track;
import com.example.mymusic.screen.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding mainBinding;
    private MainAdapter mainAdapter;
    private TracksAdapter tracksAdapter;
    private MainActivityViewModel mainViewModel;
    private FragmentTrackBinding trackBinding;
    private static final int PERMISSION_REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.toolBar.inflateMenu(R.menu.menu_action_toolbar);
        setSupportActionBar(mainBinding.toolBar);

        mainBinding.toolBar.setNavigationIcon(R.drawable.ic_reorder_white_24dp);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mainBinding.drawerLayout, mainBinding.toolBar,R.string.app_name, R.string.app_name);
        mDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        mainBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        setupPermission();
        mainBinding.imageSearch.setOnClickListener(this);
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23){
            if(checkPermission()){
                mainAdapter = new MainAdapter(getSupportFragmentManager());
                mainBinding.viewPager.setAdapter(mainAdapter);
                mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);
            }else
                requestPermission();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        final List<Track> listSearchTrack = new ArrayList<>();
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        getMenuInflater().inflate(R.menu.menu_action_toolbar, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(final String query) {
//                mainViewModel = new MainActivityViewModel();
//                mainViewModel.getTrackLocal(getApplicationContext());
//                mainViewModel.getTracks().observe(MainActivity.this, new Observer<List<Track>>() {
//                    @Override
//                    public void onChanged(List<Track> tracks) {
//                        for(int i = 0; i < tracks.size(); i++){
//                            if(tracks.get(i).getTitle().contains(query)){
//                                listSearchTrack.add(tracks.get(i));
//                            }
//                        }
//                        tracksAdapter = new TracksAdapter(MainActivity.this, listSearchTrack);
//                        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//                        recyclerView.setAdapter(tracksAdapter);
//                        tracksAdapter.notifyDataSetChanged();
//                    }
//                });
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store " +
                    "images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
