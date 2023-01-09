package com.example.hw9_2;

public class WatchlistData implements Comparable<WatchlistData> {
    private String media_type;
    private String id;
    private String poster_path;
    private String title;
    private String index;

    public WatchlistData( String id, String media_type, String poster_path, String title){
        this.id = id;
        this.media_type = media_type;
        this.poster_path = poster_path;
        this.title = title;
    }
    public void setIndex(String index){
        this.index = index;
    }

    public String getTitle(){ return this.title;}
    public String getPoster_path(){
        return this.poster_path;
    }
    public String getMedia_type(){
        return this.media_type;
    }
    public String getId(){
        return this.id;
    }
    public String getIndexString(){return this.index;}
    public int getIndexInt(){
        int ind = Integer.parseInt(this.index);
        return ind;
    }

    public int compareTo(WatchlistData t){
        return this.getIndexInt()-t.getIndexInt();
    }

}
