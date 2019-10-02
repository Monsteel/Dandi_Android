package org.techtown.schooler.network.retrofit.interfaces;

import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Profile {

    @GET("/profile")
    Call<Response<Data>> GetProfile(@Header("x-access-token") String token,
                                    @Query("user_id") String user_id);
}
