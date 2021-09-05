package com.example.hackathon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderAPI {

    @GET(".")
    Call<List<ProductDetails>> getDetails(@Query("q") String id);
}
