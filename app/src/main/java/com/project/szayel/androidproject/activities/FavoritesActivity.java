package com.project.szayel.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.fragments.DetailsFragment;
import com.project.szayel.androidproject.fragments.FavoritesFragment;
import com.project.szayel.androidproject.models.Anime;

public class FavoritesActivity extends AppCompatActivity implements FavoritesFragment.FavoritesFragmentListener, DetailsFragment.DetailsFragmentListener {
    private static final String TAG = "FavoritesActivity";

    private Toolbar toolbar;
    private FavoritesFragment favoritesFragment;
    private DetailsFragment detailsFragment;
    private static boolean favoritesChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState != null) {
            favoritesFragment = (FavoritesFragment) getSupportFragmentManager().findFragmentByTag("favorites");
            detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details");
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            favoritesFragment = new FavoritesFragment();
            ft.replace(R.id.fragment_container, favoritesFragment, "favorites").commit();
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void selectedAnimeFromFavorites(Anime anime) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("anime", anime);

        startActivity(intent);
    }

    @Override
    public boolean checkForChanges() {
        return favoritesChanges;
    }

    public static void notifyOfChanges() {
        Log.d(TAG, "notifyOfChanges: notified");
        favoritesChanges = true;
    }

    public static void dismissChanges() {
        favoritesChanges = false;
    }

    @Override
    public Anime getAnime() {
        return null;
    }
}
