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
import org.techtown.schooler.Model.SchoolList;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ChannelFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = getActivity().findViewById(R.id.ChannelRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        onSearch();
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }

    private void onSearch() {



//        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().SearchChannel("","");
//        res.enqueue(new Callback<Response<Data>>() {
//            @Override
//            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
//                Log.d("Retrofit", response.toString());
//                if (response.body() != null) {
//                    String[] contents = new String[response.body().getData().getSchoolInfo().size()];
//                    String[] titles = new String[response.body().getData().getSchoolInfo().size()];
//
//                    String SchoolName = "";
//                    String SchoolLocal = "";
//
//                    if (response.body().getData() != null) {
//                        for (int A = 0; A < response.body().getData().getSchoolInfo().size(); A++) {
//                            SchoolList dto = new SchoolList();
//                            SchoolName = response.body().getData().getSchoolInfo().get(A).getSchool_name();
//                            SchoolLocal = response.body().getData().getSchoolInfo().get(A).getSchool_locate();
//
//                            contents[A] = SchoolLocal;
//                            titles[A] = SchoolName;
//
//                            dto.setTitle(titles[A]);
//                            dto.setContent(contents[A]);
//                            adapter.addItem(dto);
//                        }
//                    }
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response<Data>> call, Throwable t) {
//                Log.e("Err", "네트워크 연결오류");
//            }
//        });




    }
}
