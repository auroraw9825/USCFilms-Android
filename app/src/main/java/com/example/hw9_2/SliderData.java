package com.example.hw9_2;

public class SliderData {
    // image url is used to
    // store the url of image
    private String poster_path;
    private int id;
    private String media_type;

    // Constructor method.
    public SliderData(String media_type, int id, String poster_path) {
        this.poster_path = poster_path;
        this.id = id;
        this.media_type = media_type;
    }

    // Getter method
    public String getPoster_path() {
        return poster_path;
    }
    public String getId() {
        String s=String.valueOf(this.id);
        return s;
    }
    public String getMedia_type() {
        return media_type;
    }

}