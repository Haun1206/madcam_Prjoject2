package com.example.tabswithanimatedswipe;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {
    @SerializedName("_id")
    String id;
    private String name;
    ArrayList<Result> data;
    //constructor
    Result(String id, String name){
        this.id = id;
        this.name = name;
}
    //getters
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    //setter
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Result> getList() {
        return data;
    }

}

