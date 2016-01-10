package com.project.szayel.androidproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.adapters.SearchResultAdapter;
import com.project.szayel.androidproject.models.Anime;
import com.project.szayel.androidproject.services.ResultsService;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends Fragment implements SearchResultAdapter.ItemClickListener, ResultsService.AsyncResultListener {
    private static final String FRAGMENT_TAG = "search_result_fragment";
    private static final String TAG = "SearchResultFragment";
    private static final String BASE_URL = "http://hummingbird.me/api/v1/search/anime?query=";

    private SearchResultFragmentListener mListener;
    private List<Anime> animes;
    private String search;
    private View progressBar;
    private View noResponsePanel;
    private View noInternetConnectionPanel;
    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            search = savedInstanceState.getString("search");
        } else {
            search = mListener.getSearch();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        progressBar = view.findViewById(R.id.loadingPanel);
        noResponsePanel = view.findViewById(R.id.noResponsePanel);
        noInternetConnectionPanel = view.findViewById(R.id.noInternetConenctionPanel);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvResults);

        if (getActivity().getCurrentFocus() != null) {
            view.findViewById(R.id.searchResultsLayout).requestFocus();
        }

        if (savedInstanceState != null) {
            animes = savedInstanceState.getParcelableArrayList("results");
            if(animes != null) {
                adapter = new SearchResultAdapter(animes, this, getContext());
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                if (!mListener.isConnected()) {
                    progressBar.setVisibility(View.GONE);
                    noInternetConnectionPanel.setVisibility(View.VISIBLE);
                }
            }

        } else {
            if (mListener.isConnected()) {
                new ResultsService(this).execute(BASE_URL + search);
            } else {
                progressBar.setVisibility(View.GONE);
                noInternetConnectionPanel.setVisibility(View.VISIBLE);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", search);
        outState.putParcelableArrayList("results", (ArrayList<Anime>) animes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchResultFragmentListener) {
            mListener = (SearchResultFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void getAnimeFromRecycler(Anime anime) {
        mListener.showDetails(anime);
    }

    @Override
    public void getSearchResult(List<Anime> animes) {
        this.animes = animes;

        if (animes != null) {
            adapter = new SearchResultAdapter(animes, this, getContext());
            recyclerView.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            noResponsePanel.setVisibility(View.VISIBLE);
        }
    }

    public interface SearchResultFragmentListener {
        String getSearch();
        void showDetails(Anime anime);
        boolean isConnected();
    }
}
