package com.example.tabswithanimatedswipe;

        import com.example.tabswithanimatedswipe.models.ImageResponse;
        import com.google.gson.JsonObject;

        import java.util.HashMap;

        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import okhttp3.ResponseBody;
        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.GET;
        import retrofit2.http.HTTP;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.PUT;
        import retrofit2.http.Part;

interface ApiService {
    @Multipart
    @POST("/upload")
    Call<ImageResponse> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @GET("/")
    Call<Result> getImage();

    @GET("/api/store")
    Call<InitData> executeGet();

    @POST("/api/store")
    Call<DashResult> executePost(@Body HashMap<String, String> map);

    @PUT("/api/store")
    Call<ResponseBody> executePut(@Body HashMap<String, String> map);

    @HTTP(method = "DELETE", path = "/api/store", hasBody = true)
    Call<ResponseBody> executeDelete(@Body HashMap<String, String> map);

}