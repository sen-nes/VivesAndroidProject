package com.project.szayel.androidproject.dao;

import android.content.SharedPreferences;

import com.project.szayel.androidproject.models.Anime;
import java.util.List;

public interface AnimeDAO {
    void open();
    boolean add(Anime anime);
    void delete(Anime anime);
    void clear();
    List<Anime> getAll();
    void close();
}
