package dgsw.bind4th.dandi.network;

import dgsw.bind4th.dandi.network.retrofit.interfaces.ChannelEvent;
import dgsw.bind4th.dandi.network.retrofit.interfaces.Login;
import dgsw.bind4th.dandi.network.retrofit.interfaces.Profile;
import dgsw.bind4th.dandi.network.retrofit.interfaces.SchoolEvent;
import dgsw.bind4th.dandi.network.retrofit.interfaces.SignUp;
import dgsw.bind4th.dandi.network.retrofit.interfaces.Channel;

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
            .baseUrl("서버주소")//AWS
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

    Channel Channel = retrofit.create(Channel.class);
    public Channel getChannel() {return Channel;}

    ChannelEvent channelEvent = retrofit.create(ChannelEvent.class);

    public ChannelEvent getChannelEvent() {
        return channelEvent;
    }

    SchoolEvent schoolEvent = retrofit.create(SchoolEvent.class);

    public SchoolEvent getSchoolEvent() {
        return schoolEvent;
    }

    Profile profile = retrofit.create(Profile.class);

    public Profile getProfile() {
        return profile;
    }
}

