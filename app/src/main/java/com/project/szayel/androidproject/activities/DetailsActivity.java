package com.project.szayel.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.fragments.DetailsFragment;
import com.project.szayel.androidproject.models.Anime;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.DetailsFragmentListener {
    private static final String TAG = "DetailsActivity";

    private DetailsFragment fragment;
    private Toolbar toolbar;
    private Anime anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState != null) {
            anime = savedInstanceState.getParcelable("anime");
            fragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details");
        } else {
            Intent intent = getIntent();
            anime = intent.getParcelableExtra("anime");

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new DetailsFragment();

            ft.replace(R.id.fragment_container, fragment, "details").commit();
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("anime", anime);
    }

    @Override
    public Anime getAnime() {
        return anime;
    }
}
