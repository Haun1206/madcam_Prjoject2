package com.example.tabswithanimatedswipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Result_p {
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

