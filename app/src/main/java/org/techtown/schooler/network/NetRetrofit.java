package org.techtown.schooler.network;

import org.techtown.schooler.network.retrofit.interfaces.Login;
import org.techtown.schooler.network.retrofit.interfaces.SignUp;
import org.techtown.schooler.network.retrofit.interfaces.Channel;

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
//            .baseUrl("http://10.80.162.124:5000")//BIND Server
            .baseUrl("http://10.80.162.191:5000")//BIND Server
//            .baseUrl("http://162.168.1.13:5000")
//            .baseUrl("http://bind4th.kro.kr:3000")//TeamViewer Server
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .build();

//http://10.80.162.124:5000 바인드 서버
//http://192.168.64.1:5000 진우 서버

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

    Channel Channel = retrofit.create(Channel.class);
    public Channel getChannel() {return Channel;}

}

