package dgsw.bind4th.dandi.network.retrofit.interfaces;

import dgsw.bind4th.dandi.network.Data;
import dgsw.bind4th.dandi.network.response.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SchoolEvent {

    @GET("/school/events")
    Call<Response<Data>> GetSchoolEvent(@Header("x-access-token")String token,
                                        @Query("year") String year,
                                        @Query("month") int month);
}
