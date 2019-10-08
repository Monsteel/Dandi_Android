package org.techtown.schooler.NavigationFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.techtown.schooler.Channels.CreateChannel;
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

public class ChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<ChannelInfo> DataList= new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences Login;
    EditText search;
    Spinner field;


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getActivity(), CreateChannel.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_channel, container, false);



        recyclerView = rootView.findViewById(R.id.ChannelRecyclerView);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LinearLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        search = (EditText)rootView.findViewById(R.id.searchChannel);
        field = (Spinner)rootView.findViewById(R.id.channelField);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        Login = getActivity().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        onRefresh();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CHANNELS");

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onRefresh() {
        onSearch();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);// 딜레이를 준 후 시작

    }


    String keyword ="";
    Integer pick = null;

    public void onSearch(){

        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().SearchChannel(Login.getString("token",""),"");//token불러오기

        res.enqueue(new Callback<Response<Data>>() {


            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if(response.code() == 200){//만약 스테터스 값이 200이면
                    if (response.body().getData().getChannels().size() != 0) {
                        DataList = response.body().getData().getChannels();
                        ChannelListAdapter adapter = new ChannelListAdapter(DataList);
                        recyclerView.setAdapter(adapter);

                        adapter.filter(keyword,pick);

                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                //에딧텍스트 만들고, 그 값을 필터에 매개변수로 넣는 작업.
                                keyword = charSequence.toString();
                                adapter.filter(keyword,pick);
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {
                                //입력후
                            }
                        });

                        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {

                                if(position == 0){
                                    pick = null;//전체
                                }else if(position == 1){
                                    pick = 2;//가입한채널
                                }else if(position == 2){
                                    pick = 3;//내가만든채널
                                }else if(position == 3){
                                    pick = 0;//가입안된 채널
                                }else if(position == 4){
                                    pick = 1;//승인대기중인 채널
                                }

                                adapter.filter(keyword,pick);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }else{
                        Log.e("","채널이 없어요");
                    }
                }else if(response.code() == 400){//만약 Status값이 400이면 check에 false를 주고, 로그인 엑티비티로 이동
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.commit();

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
    }
}
