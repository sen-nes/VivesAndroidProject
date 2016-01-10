package com.project.szayel.androidproject.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.fragments.DetailsFragment;
import com.project.szayel.androidproject.fragments.SearchFragment;
import com.project.szayel.androidproject.fragments.SearchResultFragment;
import com.project.szayel.androidproject.models.Anime;

public class SearchActivity extends AppCompatActivity implements SearchFragment.SearchFragmentListener, SearchResultFragment.SearchResultFragmentListener, DetailsFragment.DetailsFragmentListener {
    private static final String TAG = "SearchActivity";

    private SearchResultFragment searchResultFragment;
    private DetailsFragment detailsFragment;
    private SearchFragment searchFragment;
    private Toolbar toolbar;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState != null) {
            searchResultFragment = (SearchResultFragment) getSupportFragmentManager().findFragmentByTag("search_results");
            detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details");
            searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("search");
            search = savedInstanceState.getString("search");
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SearchFragment searchFragment = new SearchFragment();

            getSupportFragmentManager().beginTransaction().add(searchFragment, "search").commit();
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", search);
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public void showDetails(Anime anime) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("anime", anime);

        startActivity(intent);
    }

    @Override
    public boolean isConnected() {
        return checkNetwork();
    }

    private boolean checkNetwork() {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                Log.d(TAG, "No network connection available.");
                return false;
            }
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public Anime getAnime() {
        return null;
    }

    @Override
    public void search(String search) {
        this.search = search;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        searchResultFragment = new SearchResultFragment();

        ft.replace(R.id.fragment_container, searchResultFragment, "search_results").commit();
    }
}
