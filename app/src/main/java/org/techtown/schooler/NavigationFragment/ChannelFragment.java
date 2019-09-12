package org.techtown.schooler.NavigationFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.ChannelListAdapter;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


public class ChannelFragment extends Fragment {

    RecyclerView recyclerView;
    List<ChannelInfo> DataList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_channel, container, false);
        recyclerView = rootView.findViewById(R.id.ChannelRecyclerView);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LinearLayoutManager);


        SharedPreferences Login = getActivity().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().SearchChannel(Login.getString("token",""),"");//token불러오기
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if(response.code() == 200){//만약 스테터스 값이 200이면
                    if (response.body().getData().getChannels().size() != 0) {
                        DataList = response.body().getData().getChannels();
                        System.out.print(response.body().getStatus());
                        ChannelListAdapter adapter = new ChannelListAdapter(DataList);
                        recyclerView.setAdapter(adapter);
                    }else{
                        Log.e("","채널이 없어요");
                    }
                }else if(response.code() == 400){//만약 Status값이 400이면 check에 false를 주고, 로그인 엑티비티로 이동
                    Login.getBoolean("check",false);
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                    Log.e("","토큰 만료");
                }else{
                    Log.e("","오류가 발생했어요");
                }
            }


            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });
        ///
        return rootView;
    }
}
