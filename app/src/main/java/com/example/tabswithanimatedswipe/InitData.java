package com.example.tabswithanimatedswipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class InitData {
    private String result;
    private int error;
    private JsonArray data;

    public String getResult() {
        return result;
    }

    public int getError() {
        return error;
    }

    public JsonArray getData() {
        return data;
    }
}
