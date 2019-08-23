package org.techtown.schooler.network;

import org.techtown.schooler.network.retrofit.interfaces.Login;
import org.techtown.schooler.network.retrofit.interfaces.SignUp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    private static NetRetrofit ourInstance = new NetRetrofit();
    public static NetRetrofit getInstance() {
        return ourInstance;
    }

    private NetRetrofit() {

    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.80.162.191:3000")
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

    Login login = retrofit.create(Login.class);
    public Login getLogin() {
        return login;
    }
    //로그인 인터페이스 등록

    SignUp Signup = retrofit.create(SignUp.class);
    public SignUp getSignup() {
        return Signup;
    }
    //로그인 인터페이스 등록

}

