package org.techtown.schooler.SigninUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


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


    TabLayout mTabLayout; // TabLayout 입니다.
    ViewPager pager; // ViewPager 입니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mTabLayout = findViewById(R.id.layout_tab); // TabLayout
        pager = findViewById(R.id.pager); // ViewPager


        // 각각의 TabLayout 의 탭을 생성하고있습니다.
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.start));//start icon
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.id));//id logo
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.password));//password icon
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.email));//email icon
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.school));//school icon
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.user_info));//user_info icon
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.complete));//complete icon


        // SignupPagerAdapter 클래스를 사용하여 signupPagerAdapter 클래스를 참조하여 인스턴스를 생성하고있습니다.
        SignupPagerAdapter signupPagerAdapter = new SignupPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());

        // pager 속성을 사용하여 signupPagerAdapter 클래스를 전달하고있습니다.
        pager.setAdapter(signupPagerAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//         스크롤을 움직이게 해주는 코드
        //mTabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


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
                    Toast.makeText(SignupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignupActivity.this, "네트워크에 연결되지 않았습니다.\nError:200", Toast.LENGTH_SHORT).show();

            }
        });

    }



}
