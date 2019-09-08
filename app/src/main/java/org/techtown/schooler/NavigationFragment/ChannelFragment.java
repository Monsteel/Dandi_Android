package org.techtown.schooler.NavigationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.techtown.schooler.R;

public class ChannelFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel, container, false);

//        final Call<Response<Data>> res<ChannelInfo> = NetRetrofit.getInstance().getChannel().SearchShannel("","");
//        res.enqueue(new Callback<Response<Data>>() {
//            @Override
//            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
//                Log.d("Retrofit", response.toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<Response<Data>> call, Throwable t) {
//                Log.e("Err", "네트워크 연결오류");
//            }
//        });
    }


}
