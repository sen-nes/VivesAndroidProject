package com.project.szayel.androidproject.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.activities.FavoritesActivity;
import com.project.szayel.androidproject.activities.SearchActivity;
import com.project.szayel.androidproject.adapters.FavoritesAdapter;
import com.project.szayel.androidproject.dao.AnimeDAOImpl;
import com.project.szayel.androidproject.models.Anime;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesAdapter.ItemClickListener {
    public static final String FRAGMENT_TAG = "favorites_fragment";
    private static final String TAG = "FavoritesFragment";

    private AnimeDAOImpl animeDAO;
    private FavoritesFragmentListener mListener;
    private List<Anime> favorites;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private View noFavoritesPanel;

    public FavoritesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        animeDAO = new AnimeDAOImpl(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        noFavoritesPanel = view.findViewById(R.id.noFavoritesPanel);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvFavorites);

        if (savedInstanceState != null) {
            favorites = savedInstanceState.getParcelableArrayList("favorites");
        } else {
            favorites = animeDAO.getAll();
        }

        if (favorites. size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noFavoritesPanel.setVisibility(View.VISIBLE);
        } else {
            noFavoritesPanel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new FavoritesAdapter(favorites, this, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("favorites", (ArrayList<Anime>) favorites);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener.checkForChanges()) {
            favorites = animeDAO.getAll();

            if (favorites.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                noFavoritesPanel.setVisibility(View.VISIBLE);
            } else {
                noFavoritesPanel.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            adapter.updateList(favorites);
            adapter.notifyDataSetChanged();
            FavoritesActivity.dismissChanges();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoritesFragmentListener) {
            mListener = (FavoritesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FavoritesFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        animeDAO.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu.size() == 0) {
            inflater.inflate(R.menu.menu_favorites, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_clear:
                confirmRemove();

                return true;

            case R.id.action_do_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void getAnimeFromRecycler(Anime anime) {
        mListener.selectedAnimeFromFavorites(anime);
    }

    public void confirmRemove() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        RemoveConfirmationDialog dialog = new RemoveConfirmationDialog();
        dialog.setMessage("Clear favorites?");
        DialogFragment confirmationDialog = dialog;
        confirmationDialog.setTargetFragment(this, 1);
        confirmationDialog.show(getFragmentManager(), "RemoveConfirmationDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    new AnimeDAOImpl(getContext()).clear();

                    Toast toast = Toast.makeText(getContext(), "Cleared favorites", Toast.LENGTH_SHORT);
                    toast.show();

                    favorites = animeDAO.getAll();
                    adapter.updateList(favorites);
                    adapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.INVISIBLE);
                    noFavoritesPanel.setVisibility(View.VISIBLE);
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // Nothing.
                }

                break;
        }
    }

    public interface FavoritesFragmentListener {
        void selectedAnimeFromFavorites(Anime anime);
        boolean checkForChanges();
    }
}

