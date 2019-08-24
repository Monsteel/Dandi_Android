package org.techtown.schooler.SignUpViewPager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar;
        TabLayout tabLayout;
        ViewPager viewPager;
        PageAdapter pageAdapter;
        TabItem tabCalendar;
        TabItem tabSchoolMeals;
        TabItem tabTimeTable;
        TabItem tabWeather;

        private final long FINISH_INTERVAL_TIME = 2000;
        private long   backPressedTime = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            toolbar = findViewById(R.id.MainToolbar);
            toolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(toolbar);

            tabLayout = findViewById(R.id.tablayout);
            tabCalendar = findViewById(R.id.tabCalendar);
            tabSchoolMeals = findViewById(R.id.tabSchoolMeals);
            tabWeather = findViewById(R.id.tabWeather);
            tabTimeTable = findViewById(R.id.tabTimeTable);
            viewPager = findViewById(R.id.viewPager);


            SharedPreferences pref = getSharedPreferences("FirstStart", MODE_PRIVATE);
            boolean first = pref.getBoolean("Code", false);
            if(first==false){
                Intent newIntent = new Intent(MainActivity.this, SchoolRegistrationActivity.class);
                startActivity(newIntent);
            }else{
                Log.d("Is first Time?", "not first");
                pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(pageAdapter);
                viewPager.setOffscreenPageLimit(4);//시작시, 정보다받아옴
            }


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        String id ="";
        String pw="";
        String permission="";
        String email="";
        String phone="";
        String school="";
        String Class="";
        String grade="";
        String pic="";
        String pushNotify="";
        String isPublic="";

        signup(new User(id,pw,permission,email,phone,school,Class,grade,pic,pushNotify,isPublic));
    }

    // signup 메소드
    private void signup(User user) {
        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().signupPost(user);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if (response.isSuccessful()) {
                    Integer Status = response.body().getStatus();
                    String Message = response.body().getMessage();
                    Toast.makeText(SignupActivity.this, Status + ":" + Message, Toast.LENGTH_SHORT).show();
                    Log.d("[SignUp] Status", Status + ":" + Message);
                }else{
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        Integer Error =errorBody.getInt("status");

                        if (Error == 401 ||Error == 405) {
                            Response response1 = new Response();
                            response1.setStatus(errorBody.getInt("status"));
                            response1.setMessage(errorBody.getString("message"));
                            Log.e("[SignUp] Status", errorBody.getString("message"));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });
    }
}
