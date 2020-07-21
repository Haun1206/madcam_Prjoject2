package com.example.tabswithanimatedswipe;

import com.google.gson.JsonObject;

public class DashResult {
    private String result;
    private int error;
    private JsonObject data;

    public String getResult() {
        return result;
    }

    public int getError() {
        return error;
    }

    public JsonObject getData() {
        return data;
    }
}
