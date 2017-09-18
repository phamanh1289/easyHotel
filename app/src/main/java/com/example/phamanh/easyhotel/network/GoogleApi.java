package com.example.phamanh.easyhotel.network;


import com.example.phamanh.easyhotel.model.NearPlacesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GoogleApi {
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyAJcr3XOh_QLsrPLcTAUUd8eRkMd9nuuL8")
    Call<NearPlacesModel> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
