package com.project.szayel.androidproject.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class Anime implements Parcelable{
    private int id;
    private String status;
    private String title;
    private int episodes;
    private int duration;
    private String imageUrl;
    private Date started_airing;
    private Date finished_airing;
    private String showType;
    private double rating;
    private String ageRating;
    private List<String> genres;
    private String synopsis;
    private String hummigbirdUrl;

    public Anime() {}

    public Anime(Parcel in) {
        Log.d("AnimeModel", "Anime: FROM PARCEL");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        id = in.readInt();
        status = in.readString();
        title = in.readString();
        episodes = in.readInt();
        duration = in.readInt();
        imageUrl = in.readString();

        try {
            started_airing = new Date(formatter.parse(in.readString()).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            started_airing = null;
        }

        try {
            finished_airing = new Date(formatter.parse(in.readString()).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            finished_airing = null;
        }

        showType = in.readString();
        rating = in.readDouble();
        ageRating = in.readString();
        genres = genresFromString(in.readString());
        synopsis = in.readString();
        hummigbirdUrl = in.readString();
    }

    public static final Parcelable.Creator<Anime> CREATOR
            =new Parcelable.Creator<Anime>() {
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("ID: ").append(id).append("\n");
        result.append("Status: ").append(status).append("\n");
        result.append("Title: ").append(title).append("\n");
        result.append("Episodes: ").append(episodes).append("\n");
        result.append("Duration: ").append(duration).append("\n");
        result.append("Image: ").append(imageUrl).append("\n");
        result.append("Started airing: ").append(started_airing).append("\n");
        result.append("Finished airing: ").append(finished_airing).append("\n");
        result.append("Show type: ").append(showType).append("\n");
        result.append("Rating: ").append(rating).append("\n");
        result.append("Age rating: ").append(ageRating).append("\n");
        result.append("Genres: ").append(genresToString()).append("\n");
        result.append("Synopsis: ").append(synopsis).append("\n");
        result.append("hummigbird: ").append(hummigbirdUrl).append("\n");

        return result.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getStarted_airing() {
        return started_airing;
    }

    public void setStarted_airing(Date started_airing) {
        this.started_airing = started_airing;
    }

    public Date getFinished_airing() {
        return finished_airing;
    }

    public void setFinished_airing(Date finished_airing) {
        this.finished_airing = finished_airing;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String genresToString() {
        if (this.genres != null && this.genres.size() != 0) {
            StringBuilder genres = new StringBuilder();

            for (int i = 0; i < this.genres.size() - 1; i++) {
                genres.append(this.genres.get(i)).append(", ");
            }

            genres.append(this.genres.get(this.genres.size() - 1));

            return genres.toString();
        }

        return "Unknown genres";
    }

    public List<String> genresFromString(String genres) {
        if (genres != null) {
            return Arrays.asList(genres.split("\\s*,\\s*"));
        } else {
            return null;
        }
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getHummigbirdUrl() {
        return hummigbirdUrl;
    }

    public void setHummigbirdUrl(String hummigbirdUrl) {
        this.hummigbirdUrl = hummigbirdUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(status);
        dest.writeString(title);
        dest.writeInt(episodes);
        dest.writeInt(duration);
        dest.writeString(imageUrl);

        if (started_airing != null) {
            dest.writeString(started_airing.toString());
        } else {
            dest.writeString(null);
        }

        if (finished_airing != null) {
            dest.writeString(finished_airing.toString());
        } else {
            dest.writeString(null);
        }

        dest.writeString(showType);
        dest.writeDouble(rating);

        if (ageRating != null) {
            dest.writeString(ageRating);
        } else {
            dest.writeString(null);
        }

        dest.writeString(genresToString());
        dest.writeString(synopsis);
        dest.writeString(hummigbirdUrl);
    }
}
