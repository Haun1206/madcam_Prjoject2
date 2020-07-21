package com.example.tabswithanimatedswipe;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

interface DownloadService {
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);
}