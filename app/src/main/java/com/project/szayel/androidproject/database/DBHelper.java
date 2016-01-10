package com.project.szayel.androidproject.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, FavoritesContract.DATABASE_NAME, null, FavoritesContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL(FavoritesContract.FavoritesEntry.CREATE);
        db.execSQL(FavoritesContract.GenresEntry.CREATE);
        db.execSQL(FavoritesContract.FavoritesGenres.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FavoritesContract.FavoritesEntry.DELETE);
        db.execSQL(FavoritesContract.GenresEntry.DELETE);
        db.execSQL(FavoritesContract.FavoritesGenres.DELETE);
        onCreate(db);
    }
}
