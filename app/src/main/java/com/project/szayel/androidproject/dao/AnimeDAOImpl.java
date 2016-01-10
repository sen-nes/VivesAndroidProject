package com.project.szayel.androidproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.project.szayel.androidproject.database.DBHelper;
import com.project.szayel.androidproject.database.FavoritesContract;
import com.project.szayel.androidproject.models.Anime;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AnimeDAOImpl implements AnimeDAO {
    private  DBHelper dbHelper;
    private SQLiteDatabase db;
    private SharedPreferences preferences;

    public AnimeDAOImpl(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public boolean add(Anime anime) {
        ContentValues values = new ContentValues();

        values.put(FavoritesContract.FavoritesEntry.COLUMN_ANIME_IN, anime.getId());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_STATUS, anime.getStatus());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, anime.getTitle());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_EPISODES, anime.getEpisodes());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_DURATION, anime.getDuration());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_IMAGEURL, anime.getImageUrl());

        if (anime.getStarted_airing() != null) {
            values.put(FavoritesContract.FavoritesEntry.COLUMN_STARTED_AIRING, anime.getStarted_airing().toString());
        }

        if (anime.getFinished_airing() != null) {
            values.put(FavoritesContract.FavoritesEntry.COLUMN_FINISHED_AIRING, anime.getFinished_airing().toString());
        }

        values.put(FavoritesContract.FavoritesEntry.COLUMN_SHOW_TYPE, anime.getShowType());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, anime.getRating());

        if (anime.getAgeRating() != null) {
            values.put(FavoritesContract.FavoritesEntry.COLUMN_AGE_RATING, anime.getAgeRating());
        }

        values.put(FavoritesContract.FavoritesEntry.COLUMN_GENRES, anime.genresToString());

        List<String> genresList = anime.getGenres();
        long genres[] = new long[anime.getGenres().size()];
        ContentValues genre;
        for (int i = 0; i < genresList.size(); i++) {
            if(!preferences.contains(genresList.get(i))) {
                genre = new ContentValues();
                genre.put(FavoritesContract.GenresEntry.COLUMN_GENRE, anime.getGenres().get(i));
                genres[i] = db.insertOrThrow(
                        FavoritesContract.GenresEntry.TABLE_NAME,
                        null,
                        genre);
                preferences.edit().putLong(anime.getGenres().get(i), genres[i]).apply();
            } else {
                genres[i] = preferences.getLong(genresList.get(i), -2);
            }
        }

        values.put(FavoritesContract.FavoritesEntry.COLUMN_SYNOPSIS, anime.getSynopsis());
        values.put(FavoritesContract.FavoritesEntry.COLUMN_URL, anime.getHummigbirdUrl());

        long inserted = db.insertOrThrow(
                FavoritesContract.FavoritesEntry.TABLE_NAME,
                null,
                values);

        if(inserted != -1) {
            ContentValues join = new ContentValues();
            for(int i = 0; i < genres.length; i++) {
                join.put(FavoritesContract.FavoritesGenres.COLUMN_FAVORITE, anime.getId());
                join.put(FavoritesContract.FavoritesGenres.COLUMN_GENRE, genres[i]);

                db.insertOrThrow(
                        FavoritesContract.FavoritesGenres.TABLE_NAME,
                        null,
                        join
                );
            }
        }

        return inserted == -1 ? false : true;
    }

    @Override
    public void delete(Anime anime) {
        String selection = FavoritesContract.FavoritesEntry.COLUMN_ANIME_IN + " = ?";
        String genresSelection = FavoritesContract.FavoritesGenres.COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = { String.valueOf(anime.getId())};

        db.delete(
                FavoritesContract.FavoritesEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        db.delete(
                FavoritesContract.FavoritesGenres.TABLE_NAME,
                genresSelection,
                selectionArgs
        );
    }

    @Override
    public void clear() {
        db.execSQL(FavoritesContract.FavoritesEntry.DELETE_RECORDS);
        db.execSQL(FavoritesContract.GenresEntry.DELETE_RECORDS);
        db.execSQL(FavoritesContract.FavoritesGenres.DELETE_RECORDS);

        preferences.edit().clear().apply();
    }

    @Override
    public List<Anime> getAll() {
        List<Anime> animes = new ArrayList<>();
        String[] genreNames = null;

        String[] genreNamesProjection = {
                FavoritesContract.GenresEntry._ID,
                FavoritesContract.GenresEntry.COLUMN_GENRE
        };

        Cursor genreNamesCursor = db.query(
                FavoritesContract.GenresEntry.TABLE_NAME,
                genreNamesProjection,
                null,
                null,
                null,
                null,
                null
        );

        if (genreNamesCursor.getCount() > 0) {
            genreNamesCursor.moveToFirst();
            int g = 0;
            genreNames = new String[genreNamesCursor.getCount()];
            do {
                genreNames[g++] = genreNamesCursor.getString(genreNamesCursor.getColumnIndex(FavoritesContract.GenresEntry.COLUMN_GENRE));
            } while(genreNamesCursor.moveToNext());

        }

        String[] projection = {
                FavoritesContract.FavoritesEntry._ID,
                FavoritesContract.FavoritesEntry.COLUMN_ANIME_IN,
                FavoritesContract.FavoritesEntry.COLUMN_STATUS,
                FavoritesContract.FavoritesEntry.COLUMN_TITLE,
                FavoritesContract.FavoritesEntry.COLUMN_EPISODES,
                FavoritesContract.FavoritesEntry.COLUMN_DURATION,
                FavoritesContract.FavoritesEntry.COLUMN_IMAGEURL,
                FavoritesContract.FavoritesEntry.COLUMN_STARTED_AIRING,
                FavoritesContract.FavoritesEntry.COLUMN_FINISHED_AIRING,
                FavoritesContract.FavoritesEntry.COLUMN_SHOW_TYPE,
                FavoritesContract.FavoritesEntry.COLUMN_RATING,
                FavoritesContract.FavoritesEntry.COLUMN_AGE_RATING,
                FavoritesContract.FavoritesEntry.COLUMN_GENRES,
                FavoritesContract.FavoritesEntry.COLUMN_SYNOPSIS,
                FavoritesContract.FavoritesEntry.COLUMN_URL
        };
        String sortOrder =
                FavoritesContract.FavoritesEntry.COLUMN_TITLE + " ASC";

        Cursor cursor = db.query(
                FavoritesContract.FavoritesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Anime anime = new Anime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                int id = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ANIME_IN));
                String status = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_STATUS));
                String title = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE));
                int episodes = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_EPISODES));
                int duration = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_DURATION));
                String imageUrl = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_IMAGEURL));
                String started_airing = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_STARTED_AIRING));
                String finished_airing = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_FINISHED_AIRING));
                String showType = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_SHOW_TYPE));
                double rating = cursor.getDouble(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RATING));
                String ageRating = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_AGE_RATING));
                String synopsis = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_SYNOPSIS));
                String url = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_URL));

                String[] genresProjection = {
                        FavoritesContract.FavoritesGenres.COLUMN_GENRE
                };

                String selection = FavoritesContract.FavoritesGenres.COLUMN_FAVORITE + "=? ";
                String[] selectionArgs = {String.valueOf(cursor.getLong(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ANIME_IN)))};
                Cursor genresCursor = db.query(
                        FavoritesContract.FavoritesGenres.TABLE_NAME,
                        genresProjection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                List<String> genres = new ArrayList<>();
                if (genresCursor.getCount() > 0) {
                    if (genreNames != null) {
                        genresCursor.moveToFirst();
                        do {
                            int genreId = genresCursor.getInt(genresCursor.getColumnIndex(FavoritesContract.FavoritesGenres.COLUMN_GENRE));
                            genres.add(genreNames[genreId - 1]);
                        } while (genresCursor.moveToNext()) ;
                    }
                }

                anime.setId(id);
                anime.setStatus(status);
                anime.setTitle(title);
                anime.setEpisodes(episodes);
                anime.setDuration(duration);
                anime.setImageUrl(imageUrl);

                try {
                    anime.setStarted_airing(new Date(formatter.parse(started_airing).getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    anime.setFinished_airing(new Date(formatter.parse(finished_airing).getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                anime.setShowType(showType);
                anime.setRating(rating);
                anime.setAgeRating(ageRating);
                anime.setGenres(genres);
                anime.setSynopsis(synopsis);
                anime.setHummigbirdUrl(url);

                animes.add(anime);

            } while (cursor.moveToNext());
        }

        return animes;
    }

    @Override
    public void close() {
        db.close();
    }
}
