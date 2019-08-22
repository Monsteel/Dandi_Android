package org.techtown.schooler.network.retrofit.interfaces;

import org.techtown.schooler.Model.LoginPostRequest;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.response.Response;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Login {

    @POST("auth/login")
    Call<Response<Data>> loginPost(@Body LoginPostRequest loginPostRequest);
}
