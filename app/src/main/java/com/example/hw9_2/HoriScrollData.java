package com.example.hw9_2;

public class HoriScrollData {
    private String poster_path;
    private int id;
    private String title;
    private String media_type;

    public HoriScrollData(String poster_path, int id, String title, String media_type) {
        this.poster_path = poster_path;
        this.id = id;
        this.title = title;
        this.media_type = media_type;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public String getTitle() {
        return this.title;
    }

    public String getId() {
        String s = String.valueOf(this.id);
        return s;
    }
    public String getMedia_type(){
        return this.media_type;
    }

}
