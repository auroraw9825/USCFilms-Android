package com.example.hw9_2;

public class CastData {
    private String profile_path;
    private String name;
    private String character;
    private int id;

    public CastData( String name, String profile_path){
        this.name = name;
        this.profile_path = profile_path;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setCharacter(String character){
        this.character = character;
    }

    public String getProfile_path(){
        return this.profile_path;
    }
    public String getName(){
        return this.name;
    }
    public String getId(){
        String s=String.valueOf(this.id);
        return s;
    }
    public String getCharacter(){
        return this.character;
    }

}
