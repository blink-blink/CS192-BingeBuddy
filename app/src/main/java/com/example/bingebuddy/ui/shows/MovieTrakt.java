package com.example.bingebuddy.ui.shows;

import com.google.gson.annotations.SerializedName;

public class MovieTrakt {
    @SerializedName("title")
    public String title;
    @SerializedName("year")
    public int year;
    @SerializedName("ids")
    public MovieIdsTrakt id;

    public int getTmdbId(){
        return id.getTmdbId();
    }

    public String getTitle(){
        return title;
    }
}
