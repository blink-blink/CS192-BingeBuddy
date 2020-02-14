/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
//File Creation Date: 02/14/2020
//Software purpose: class for shows in watchlist

//changelog
//Update: Samuel Jose, 02/14/2020
package com.example.bingebuddy.ui.shows;

public class Show {
    //Essential attributes
    private Integer id;

    private String title;

    private String description;

    private String posterPath;

    //to be added
    private String releaseDate;

    private Double popularity;

    private Double voteCount;

    public Show(Integer id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    //image poster
    String baseImageUrl = "";

    public String getPosterPath(){
        return baseImageUrl+posterPath;
    }

    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

}
