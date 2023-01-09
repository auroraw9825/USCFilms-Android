package com.example.hw9_2;

public class ReviewData {
    private String author;
    private String content;
    private String creatTime;
    private int rating;

    public ReviewData( String author, String content){
        this.author = author;
        this.content= content;
    }
    public void setCreatTime(String time){
        this.creatTime = time;
    }
    public void setRating(int rating){
        this.rating=rating;
    }

    public String getAuthor(){
        return this.author;
    }
    public String getContent(){
        return this.content;
    }
    public String getRating(){
        String s=String.valueOf(this.rating/2);// 7/2=3
        return s;
    }
    public String getCreatTime(){
        return this.creatTime;
    }
}
