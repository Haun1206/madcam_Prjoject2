package com.example.tabswithanimatedswipe;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
public interface RetrofitInterface {

    @GET("/api/phonebook")
    Call<InitData> executeGet();

    @POST("/api/phonebook")
    Call<Result_p> executePost(@Body HashMap<String, String> map);

    @HTTP(method = "DELETE", path = "/api/phonebook", hasBody = true)
    Call<ResponseBody> executeDelete(@Body HashMap<String, String> map);


}

