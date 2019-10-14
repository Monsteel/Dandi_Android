package org.techtown.schooler.Channels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.Model.UserInfo;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MemberAllowActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    List<UserInfo> DataList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences Login;
    EditText search;
    String user_id;
    String keyword = "";
    String Channel_id;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_allow);
        toolbar = (Toolbar) findViewById(R.id.toolbar4);

        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f5f5")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("채널가입 승인하기");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Login = getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout2);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        Channel_id = getIntent().getStringExtra("channel_id");
        search = (EditText) findViewById(R.id.searchMember);
        recyclerView = (RecyclerView) findViewById(R.id.AwaitRecyclerView);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LinearLayoutManager);
        Road();
    }

    @Override
    public void onRefresh() {
        Road();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);// 딜레이를 준 후 시작

    }

    public void Road(){
        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().ShowAwaitUser(Login.getString("token",""),Channel_id);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                if(response.code() == 200){
                    DataList = response.body().getData().getAwaitUsers();
                    AwaitUserAdapter adapter = new AwaitUserAdapter(DataList);
                    recyclerView.setAdapter(adapter);
                    adapter.filter(keyword);
                    adapter.CatchChannelId(Channel_id);
                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            //에딧텍스트 만들고, 그 값을 필터에 매개변수로 넣는 작업.
                            keyword = charSequence.toString();
                            adapter.filter(keyword);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            //입력후
                        }
                    });
                }else if(response.code() == 204){
                    //승인대기 유저 존재x
                }else if(response.code() == 403) {
                    Toast.makeText(MemberAllowActivity.this, "조회권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else if(response.code() == 410){
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.putString("id",null);
                    editor.commit();
                    startActivity(new Intent(MemberAllowActivity.this, LoginActivity.class));
                    Log.e("","토큰 만료");
                    Toast.makeText(MemberAllowActivity.this, "토큰이 만료되었습니다\n다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
                } else{
                    Log.e("","오류 발생");
                    Toast.makeText(MemberAllowActivity.this, "서버에서 오류가 발생했습니다.\n문제가 지속되면 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("","네트워크 오류");
                Toast.makeText(MemberAllowActivity.this, "네크워크 상태가 원할하지 않습니다.\n잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}