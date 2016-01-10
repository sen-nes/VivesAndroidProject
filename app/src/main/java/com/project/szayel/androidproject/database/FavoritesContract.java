package com.project.szayel.androidproject.database;

import android.provider.BaseColumns;

public final class FavoritesContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AndroidProject.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    public FavoritesContract() {}

    public static abstract class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ANIME_IN = "animeIn";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_EPISODES = "episodes";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_IMAGEURL = "imageUrl";
        public static final String COLUMN_STARTED_AIRING = "startedAiring";
        public static final String COLUMN_FINISHED_AIRING = "finishedAiring";
        public static final String COLUMN_SHOW_TYPE = "showType";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_AGE_RATING = "ageRating";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_URL = "url";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_ANIME_IN + INT_TYPE + COMMA_SEP +
                COLUMN_STATUS + TEXT_TYPE + COMMA_SEP +
                COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                COLUMN_EPISODES + INT_TYPE + COMMA_SEP +
                COLUMN_DURATION + INT_TYPE + COMMA_SEP +
                COLUMN_IMAGEURL + TEXT_TYPE + COMMA_SEP +
                COLUMN_STARTED_AIRING + TEXT_TYPE + COMMA_SEP +
                COLUMN_FINISHED_AIRING + TEXT_TYPE + COMMA_SEP +
                COLUMN_SHOW_TYPE + TEXT_TYPE + COMMA_SEP +
                COLUMN_RATING + REAL_TYPE + COMMA_SEP +
                COLUMN_AGE_RATING + TEXT_TYPE + COMMA_SEP +
                COLUMN_GENRES + TEXT_TYPE + COMMA_SEP +
                COLUMN_SYNOPSIS + TEXT_TYPE + COMMA_SEP +
                COLUMN_URL + TEXT_TYPE +
                " )";

        public static final String DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String DELETE_RECORDS =
                "DELETE FROM " + TABLE_NAME;
    }

    public static abstract class GenresEntry implements BaseColumns {
        public static final String TABLE_NAME = "genres";

        public static final String COLUMN_GENRE = "genre";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_GENRE + TEXT_TYPE +
                        " )";

        public static final String DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String DELETE_RECORDS =
                "DELETE FROM " + TABLE_NAME;
    }

    public static abstract class FavoritesGenres implements BaseColumns {
        public static final String TABLE_NAME = "favorites_genres";

        public static final String COLUMN_GENRE = "genreid";
        public static final String COLUMN_FAVORITE = "favoriteid";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_GENRE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_FAVORITE + TEXT_TYPE +
                        " )";

        public static final String DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String DELETE_RECORDS =
                "DELETE FROM " + TABLE_NAME;
    }
}
