package org.techtown.schooler.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.techtown.schooler.ChannelEvents.CreateChannelEvents;
import org.techtown.schooler.MainActivity;
import org.techtown.schooler.NavigationFragment.AccountFragment;
import org.techtown.schooler.NavigationFragment.MainFragment;
import org.techtown.schooler.R;
import org.techtown.schooler.RecyclerView_main.JoinedChannelAdapter;
import org.techtown.schooler.RecyclerView_main.NoScheduleAdapter;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class JoinedChannel extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences Login;

    ArrayList<org.techtown.schooler.Model.JoinedChannel> joinedChannel = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(JoinedChannel.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_channel);

        recyclerView = findViewById(R.id.recyclerView);

        // Actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_gray_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Login = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        showChannel();
    }

    // ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // ActionBar Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);;
    }

    public void showChannel(){

        Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().GetChannel(Login.getString("token",""));
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if(response.code() == 200){

                    joinedChannel = (ArrayList<org.techtown.schooler.Model.JoinedChannel>) response.body().getData().getJoinedChannel();

                    JoinedChannelAdapter myAdapter = new JoinedChannelAdapter(joinedChannel);
                    recyclerView.setAdapter(myAdapter);

                }else if(response.code() == 410){//만약 Status값이 400이면 check에 false를 주고, 로그인 엑티비티로 이동
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.putString("id",null);
                    editor.commit();

                    startActivity(new Intent(JoinedChannel.this,LoginActivity.class));
                    Log.e("","토큰 만료");
                    Toast.makeText(JoinedChannel.this, "토큰이 만료되었습니다\n다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(JoinedChannel.this,"서버에서 오류가 발생했습니다.\n문제가 지속되면 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("","네트워크 오류");
                Toast.makeText(JoinedChannel.this, "네크워크 상태가 원할하지 않습니다.\n잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
