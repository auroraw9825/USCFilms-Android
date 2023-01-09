package com.example.hw9_2;

public class SearchData {
    private String backdrop_path;
    private String media_type;
    private String title;
    private int rating;
    private int id;
    private String release_year;

    public SearchData( int id, String title, String backdrop_path){
        this.id = id;
        this.title = title;
        this.backdrop_path = backdrop_path;
    }
    public void setMedia_type(String media_type){
        this.media_type = media_type;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
    public void setRelease_year(String release_year){ this.release_year = release_year; }

    public String getBackdrop_path(){
        return this.backdrop_path;
    }
    public String getMedia_type(){
        return this.media_type;
    }
    public String getTitle(){
        return this.title;
    }
    public String getRating(){
        String s=String.valueOf(this.rating/2.0); // total is 5
        return s;
    }
    public String getId(){
        String s=String.valueOf(this.id);
        return s;
    }
    public String getRelease_year(){
        return this.release_year;
    }


}
