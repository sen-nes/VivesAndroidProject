package com.project.szayel.androidproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.szayel.androidproject.R;

public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imgCover;
    public TextView tvTitle;
    public TextView tvStatus;
    public TextView tvType;
    public TextView tvScore;
    public TextView tvEpisodes;

    private OnItemClickListener mListener;
    private static Context context;

    public Context getContext() {
        return context;
    }

    public FavoritesViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mListener = listener;
        context = itemView.getContext();

        imgCover = (ImageView) itemView.findViewById(R.id.ivCover);
        imgCover.setImageResource(R.drawable.na_series);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        tvType = (TextView) itemView.findViewById(R.id.tvType);
        tvScore = (TextView) itemView.findViewById(R.id.tvScore);
        tvEpisodes = (TextView) itemView.findViewById(R.id.tvEpisodes);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick (View view) {
        mListener.onItemClick(this.getAdapterPosition());
    }

    public static interface OnItemClickListener {
        void onItemClick(int position);
    }
}
