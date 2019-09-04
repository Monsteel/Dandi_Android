package org.techtown.schooler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.techtown.schooler.StartMemberActivity.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView; // NavigationView
    DrawerLayout drawerLayout; // DrawerLayout
    Button button; // 바로가기 버튼
    TextView textView; // 날짜 텍스트
    TextView textView2; // 시간 텍스트
    View main_nav_header; // 헤더 부분
    ImageView profile; // 헤더 부분 이미지
    FrameLayout layout;

    long mNow;  // 현재 mNow
    Date mDate;  // 현재 mDate

    // Date 클래스 today 인스턴스
    Date today = new Date();

    // SimpleDateFormat 클래스 date 날짜
    SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일");

    // SimpleDateFormat 클래스 time 시간
    SimpleDateFormat time = new SimpleDateFormat("hh시 mm분 ss초");

    // CountDownTimer 클래스 _timer 변수
    private CountDownTimer _timer;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout); // DrawerLayout
        navigationView = (NavigationView) findViewById(R.id.navigationView); // NavigationView
        button = (Button)findViewById(R.id.button); // 바로가기 버튼
        textView = (TextView)findViewById(R.id.textView); // 날짜 설정
        textView2 = (TextView)findViewById(R.id.name); // 시간 설정
        main_nav_header = navigationView.getHeaderView(0); // main_nav_header
        profile = main_nav_header.findViewById(R.id.profile); // 헤더부분 이미지 즉 프로필
        layout = main_nav_header.findViewById(R.id.layout); // 헤더부분 레이아웃

        // setNavigationItemSelectedListener 매서드를 사용하여 navigationView 객체에서 이벤트를 받으려면 리스너 설정을 해야한다.
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        //-------------------------------------------------------------------------------//
        SharedPreferences LoginCheck = getSharedPreferences("Check", MODE_PRIVATE);

        final boolean first = LoginCheck.getBoolean("Check", false); //첫 실행임

        /*if(first==false){
            // Intent 클래스를 사용해서 LoginActivity 화면으로 전환을 합니다.
            Intent StatLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatLogin);
            Log.d("[LoginCheck]", "로그인 X");

        }else{
            Log.d("[LoginCheck] ", "로그인 O");
        }*/

        //-------------------------------------------------------------------------------//


        // 버튼 클릭 시 네비게이션 뷰를 띄워줍니다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // openDrawer 속성을 사용하여 navigationView 를 오픈합니다.
                drawerLayout.openDrawer(navigationView);
            }
        });

        // 실시간 날짜를 설정하는 코드
        textView.setText(date.format(today));

        // 실시간 시계 설정하는 코드
        _timer = new CountDownTimer(10 * 1000, 1000) {   //_timer 객체에 10*1000 밀리초(10초) 가 1000밀리초마다 1씩달게한다.

            public void onTick(long millisUntilFinished) {

                // 시간 텍스트를 설정한다.
                textView2.setText(getTime());


            }

            public void onFinish() {

                _timer.cancel();
                _timer.start(); // 끝났을때 재설정을 통한 무한 반복문 실행

            }

        };
        _timer.start();

        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }


    // 실시간 시계를 담당하는 매서드
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return time.format(mDate);
    }

    // 뒤로가기 버튼 이벤트
    public void onBackPressed(){

        // drawerLayout 이 열려있는 상태에서 뒤로가기 버튼을 클릭 시 네비게이션 뷰가 닫힌다.
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }

        // 그냥 뒤로 닫으면 앱을 종료한다.
        else{
            super.onBackPressed();
        }

    }

    // NavigationItem 선택 시 발생하는 이벤트
    public boolean onNavigationItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.account:
                Toast.makeText(this, "account 체크", Toast.LENGTH_SHORT).show();
        }

        // NavigationView 중 아이템을 선택 시 drawerLayout 이 종료됩니다.
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


}