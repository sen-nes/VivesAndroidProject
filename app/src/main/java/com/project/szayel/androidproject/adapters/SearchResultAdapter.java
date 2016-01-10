package com.project.szayel.androidproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.szayel.androidproject.R;
import com.project.szayel.androidproject.models.Anime;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder>{
    private static final String TAG = "SearchResultAdapter";

    private List<Anime> animes;
    private ItemClickListener mListener;
    private Context context;

    public SearchResultAdapter(List<Anime> animes, ItemClickListener listener, Context context) {
        this.animes = animes;
        mListener = listener;
        this.context = context;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View animeView  = inflater.inflate(R.layout.item_search_results, parent, false);
        SearchResultViewHolder searchResultViewHolder = new SearchResultViewHolder(animeView, new SearchResultViewHolder.OnItemClickListener() {

            public void onItemClick(int position) {
                Log.d("SearchResultAdapter", "Item clicked.");
                mListener.getAnimeFromRecycler(animes.get(position));
            }
        });

        return searchResultViewHolder;
    }



    @Override
    public void onBindViewHolder(SearchResultViewHolder searchResultViewHolder, int position) {
        Anime anime = animes.get(position);

        ImageView imgCover = searchResultViewHolder.imgCover;
        Picasso.with(searchResultViewHolder.getContext())
                .load(anime.getImageUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.na_series))
                .into(imgCover);

        TextView tvTitle = searchResultViewHolder.tvTitle;
        tvTitle.setText(anime.getTitle());

        TextView tvStatus = searchResultViewHolder.tvStatus;
        tvStatus.setText(anime.getStatus());

        TextView tvType = searchResultViewHolder.tvType;
        String type = anime.getShowType();
        if (type.equals("")) {
            tvType.setText("Unknown");
        } else {
            tvType.setText(type);
        }

        TextView tvEpisodes = searchResultViewHolder.tvEpisodes;
        int episodes = anime.getEpisodes();
        if (episodes == 0) {
            tvEpisodes.setText("-");
        } else {
            tvEpisodes.setText("" + anime.getEpisodes());
        }

        TextView tvRating = searchResultViewHolder.tvRating;
        if (anime.getAgeRating() == null) {
            tvRating.setText("None");
        } else {
            tvRating.setText(anime.getAgeRating());
        }

        TextView tvScore = searchResultViewHolder.tvScore;
        DecimalFormat formatter = new DecimalFormat("0.00");
        double score = anime.getRating();
        if (score == 0) {
            tvScore.setText("N/A");
        } else {
            tvScore.setText(String.format(formatter.format(score)));
        }

        TextView tvGenres = searchResultViewHolder.tvGenres;
        tvGenres.setText(anime.genresToString());
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public interface ItemClickListener {
        void getAnimeFromRecycler(Anime anime);
    }
}
