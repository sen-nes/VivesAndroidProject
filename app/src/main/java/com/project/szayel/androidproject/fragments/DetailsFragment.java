package com.project.szayel.androidproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.activities.FavoritesActivity;
import com.project.szayel.androidproject.dao.AnimeDAOImpl;
import com.project.szayel.androidproject.models.Anime;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";

    private DetailsFragmentListener mListener;
    private AnimeDAOImpl animeDAO;
    private Anime anime;
    private SharedPreferences preferences;
    boolean favorite;
    private boolean showFullTitle;
    private TextView tvTitle;
    private ImageView ivCover;
    private FloatingActionButton buttonFavorites;
    private TextView tvType;
    private TextView tvEpisodes;
    private TextView tvDuration;
    private TextView tvStatus;
    private TextView tvAired;
    private TextView tvScore;
    private TextView tvRating;
    private TextView tvGenres;
    private TextView tvSynopsis;
    private TextView visitHummingbird;

    public DetailsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeDAO = new AnimeDAOImpl(getContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (savedInstanceState != null) {
            anime = savedInstanceState.getParcelable("animeD");
            favorite = savedInstanceState.getBoolean("favorite");
            showFullTitle = savedInstanceState.getBoolean("show_full_title");
        } else {
            anime = mListener.getAnime();
            favorite = preferences.contains(String.valueOf(anime.getId()));
            showFullTitle = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ivCover = (ImageView) view.findViewById(R.id.ivCover);
        buttonFavorites = (FloatingActionButton) view.findViewById(R.id.buttonFavorites);
        tvType = (TextView) view.findViewById(R.id.tvType);
        tvEpisodes = (TextView) view.findViewById(R.id.tvEpisodes);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        tvAired = (TextView) view.findViewById(R.id.tvAired);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvGenres = (TextView) view.findViewById(R.id.tvGenres);
        tvSynopsis = (TextView) view.findViewById(R.id.tvSynopsis);
        visitHummingbird = (TextView) view.findViewById(R.id.visitHummingbird);

        if(favorite) {
            buttonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            buttonFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        tvTitle.setText(anime.getTitle());
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFullTitle == false) {
                    tvTitle.setMaxLines(Integer.MAX_VALUE);
                    tvTitle.setEllipsize(null);
                    showFullTitle = true;
                } else {
                    tvTitle.setMaxLines(1);
                    tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                    showFullTitle = false;
                }
            }
        });

        Picasso.with(view.getContext())
                .load(anime.getImageUrl())
                .placeholder(getResources().getDrawable(R.drawable.na_series))
                .into(ivCover);

        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                if (favorite) {
                    animeDAO.delete(anime);
                    preferences.edit().remove(String.valueOf(anime.getId())).apply();
                    buttonFavorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                    toast.setText("Removed from favorites");
                    toast.show();

                    favorite = false;
                } else {
                    animeDAO.add(anime);
                    preferences.edit().putBoolean(String.valueOf(anime.getId()), true).apply();
                    buttonFavorites.setImageResource(R.drawable.ic_favorite_black_24dp);

                    //toast.setText("Added to favorites");
                    //toast.show();

                    favorite = true;
                }
                FavoritesActivity.notifyOfChanges();
            }
        });

        String type = anime.getShowType();
        if (type.equals("")) {
            tvType.setText("Unknown");
        } else {
            tvType.setText(type);
        }

        int episodes = anime.getEpisodes();
        if (episodes == 0) {
            tvEpisodes.setText("Unknown");
        } else {
            tvEpisodes.setText("" + anime.getEpisodes());
        }

        int duration = anime.getDuration();
        if (duration == 0) {
            tvDuration.setText("Unknown");
        } else {
            tvDuration.setText(anime.getDuration() + " min");
        }

        tvStatus.setText(anime.getStatus());

        Date started = anime.getStarted_airing();
        Date finished = anime.getFinished_airing();

        DateFormat dateformatter = new SimpleDateFormat("MMMMMM dd, yyyy");
        String aired;

        switch(anime.getStatus()) {
            case "Not Yet Aired":
                if (started == null && finished == null) {
                    aired = "Not Available";
                } else {
                    if (finished == null) {
                        if ((anime.getShowType().equals("TV") || anime.getShowType().equals("Special") || anime.getShowType().equals("OVA")) && anime.getEpisodes() != 1) {
                            aired = dateformatter.format(started) + " to ?";
                        } else {
                            aired = dateformatter.format(started);
                        }
                    } else {
                        if (started.toString().equals(finished.toString())) {
                            aired = dateformatter.format(started);
                        } else {
                            aired = dateformatter.format(started) + " to " + dateformatter.format(finished);
                        }
                    }
                }
                break;

            case "Currently Airing":
                if (finished == null) {
                    aired = dateformatter.format(started) + " to ?";
                } else {
                    aired = dateformatter.format(started) + " to " + dateformatter.format(finished);
                }
                break;

            case "Finished Airing":
                if (finished == null) {
                    aired = dateformatter.format(started);
                } else {
                    if (started.toString().equals(finished.toString())) {
                        aired = dateformatter.format(started);
                    } else {
                        aired = dateformatter.format(started) + " to " + dateformatter.format(finished);
                    }
                }
                break;

            default:
                aired = "switch shit";
        }

        tvAired.setText(aired);

        DecimalFormat formatter = new DecimalFormat("0.00");
        double score = anime.getRating();
        if (score == 0) {
            tvScore.setText("N/A");
        } else {
            tvScore.setText(String.format(formatter.format(score)));
        }

        if (anime.getAgeRating() == null) {
            tvRating.setText("None");
        } else {
            tvRating.setText(anime.getAgeRating());
        }


        if(anime.genresToString() == null) {
            tvGenres.setText("No genres available");
        } else {
            tvGenres.setText(anime.genresToString());
        }

        if(anime.getSynopsis() == null) {
            tvSynopsis.setText("No synopsis available.");
        } else {
            tvSynopsis.setText(anime.getSynopsis());
        }

        visitHummingbird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(anime.getHummigbirdUrl()));
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("animeD", anime);
        outState.putBoolean("favorite", favorite);
        outState.putBoolean("show_full_title", showFullTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsFragmentListener) {
            mListener = (DetailsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DetailsFragmentListener");
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

    public interface DetailsFragmentListener {
        Anime getAnime();
    }
}
