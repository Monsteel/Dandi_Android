package org.techtown.schooler.network.retrofit.interfaces;

import org.techtown.schooler.Model.User;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUp {
    @POST("auth/signup")
    Call<Response<Data>> signupPost(@Body User user);
}