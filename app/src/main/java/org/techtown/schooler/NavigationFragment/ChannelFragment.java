package org.techtown.schooler.NavigationFragment;

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
import org.techtown.schooler.network.ChannelListAdapter;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


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


        ////
        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().SearchChannel("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoia2p5MTMyOTkiLCJpYXQiOjE1NjgxNjc1OTYsImV4cCI6MTU2ODIxMDc5NiwiaXNzIjoiZGFuZGkiLCJzdWIiOiJ0b2tlbiJ9.A6y6PW0gV50eRbUM0Me_OBhaeHSmynX08vzWg3cs594","");
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.body().getData() != null) {
                    DataList = response.body().getData().getChannels();
                    System.out.print(response.body().getStatus());
                    ChannelListAdapter adapter = new ChannelListAdapter(DataList);
                    recyclerView.setAdapter(adapter);
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
