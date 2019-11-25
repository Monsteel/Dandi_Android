package dgsw.bind4th.dandi.network.retrofit.interfaces;

import dgsw.bind4th.dandi.Model.LoginPostRequest;
import dgsw.bind4th.dandi.network.Data;
import dgsw.bind4th.dandi.network.response.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Login {
    @POST("auth/login")
    Call<Response<Data>> loginPost(@Body LoginPostRequest loginPostRequest);
}