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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{
    private static final String TAG = "FavoritesAdapter";

    private List<Anime> favorites;
    private ItemClickListener mListener;
    private Context context;

    public FavoritesAdapter(List<Anime> favorites, ItemClickListener listener, Context context) {
        this.favorites = favorites;
        mListener = listener;
        this.context = context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View animeView  = inflater.inflate(R.layout.item_favorites, parent, false);
        FavoritesViewHolder viewHolder = new FavoritesViewHolder(animeView, new FavoritesViewHolder.OnItemClickListener() {

            public void onItemClick(int position) {
                Log.d("SearchResultAdapter", "Item clicked.");
                mListener.getAnimeFromRecycler(favorites.get(position));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder favoritesViewHolder, int position) {
        Anime anime = favorites.get(position);

        ImageView imgCover = favoritesViewHolder.imgCover;
        Picasso.with(favoritesViewHolder.getContext())
                .load(anime.getImageUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.na_series))
                .into(imgCover);

        TextView tvTitle = favoritesViewHolder.tvTitle;
        tvTitle.setText(anime.getTitle());

        TextView tvStatus = favoritesViewHolder.tvStatus;
        tvStatus.setText(anime.getStatus());

        TextView tvType = favoritesViewHolder.tvType;
        String type = anime.getShowType();
        if (type.equals("")) {
            tvType.setText("Unknown");
        } else {
            tvType.setText(type);
        }

        TextView tvEpisodes = favoritesViewHolder.tvEpisodes;
        int episodes = anime.getEpisodes();
        if (episodes == 0) {
            tvEpisodes.setText("-");
        } else {
            tvEpisodes.setText("" + anime.getEpisodes());
        }

        TextView tvScore = favoritesViewHolder.tvScore;
        DecimalFormat formatter = new DecimalFormat("0.00");
        double score = anime.getRating();
        if (score == 0) {
            tvScore.setText("N/A");
        } else {
            tvScore.setText(String.format(formatter.format(score)));
        }
    }

    public void updateList(List<Anime> favorites) {
        this.favorites = favorites;
    }

    public void removeItem(Anime anime) {
        favorites.remove(anime);
    }

    public void addItem(Anime anime) {
        favorites.add(anime);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public interface ItemClickListener {
        void getAnimeFromRecycler(Anime anime);
    }
}
