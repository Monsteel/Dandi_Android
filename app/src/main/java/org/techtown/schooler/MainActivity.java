package org.techtown.schooler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.techtown.schooler.NavigationFragment.AccountFragment;
import org.techtown.schooler.NavigationFragment.ChannelFragment;
import org.techtown.schooler.NavigationFragment.LogoutFragment;
import org.techtown.schooler.NavigationFragment.MainFragment;
import org.techtown.schooler.NavigationFragment.Ready2Fragment;
import org.techtown.schooler.NavigationFragment.ReadyFragment;
import org.techtown.schooler.NavigationFragment.SettingFragment;
import org.techtown.schooler.SplashActivity.SplashActivity;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.retrofit.interfaces.Login;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.techtown.schooler.StartMemberActivity.LoginActivity.number;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView; // NavigationView
    DrawerLayout drawerLayout; // DrawerLayout
    View main_nav_header; // 헤더 부분
    ImageView profile; // 헤더 부분 이미지
    MainFragment main = new MainFragment(); // 메인 프레그먼트
    SharedPreferences Login;
    Toolbar toolbar; // Toolbar

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout); // DrawerLayout
        navigationView = (NavigationView) findViewById(R.id.navigationView); // NavigationView
        main_nav_header = navigationView.getHeaderView(0); // main_nav_header
        profile = main_nav_header.findViewById(R.id.profile); // 헤더부분 이미지 즉 프로필
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Toolbar

        // 메인 프레그먼트를 먼저 설정해둡니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, main).commit();

        // toolbar 를 사용할 수 있도록 설정합니다.
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        // 제목을 보이지 않도록 합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // setNavigationItemSelectedListener 매서드를 사용하여 navigationView 객체에서 이벤트를 받으려면 리스너 설정을 해야한다.
        navigationView.setNavigationItemSelectedListener(MainActivity.this);



        Login = getSharedPreferences("Login", MODE_PRIVATE);

        if(Login.getString("token",null) == null){
            // Intent 클래스를 사용해서 LoginActivity 화면으로 전환을 합니다.

            Intent StatLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatLogin);

            Log.e("[LoginCheck]", "로그인 X");

        }else{
            Log.e("[LoginCheck] ", "로그인 O");
        }


        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

    }


    // 뒤로가기 버튼 이벤트
    public void onBackPressed(){

        // drawerLayout 이 열려있는 상태에서 뒤로가기 버튼을 클릭 시 네비게이션 뷰가 닫힌다.
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }

        // 그냥 뒤로 닫으면 앱을 종료한다.
        else {
            ActivityCompat.finishAffinity(this);
        }
    }

    // NavigationItem 선택 시 발생하는 이벤트
    public boolean onNavigationItemSelected(MenuItem item){

        Fragment channel; // 채널 Fragment
        Fragment account; // 계정 Fragment
        Fragment setting; // 설정 Fragment
        Fragment ready2; // 준비중2 Fragment
        Fragment logout;
        Fragment main;


        switch (item.getItemId()){

            // channel 채널
            case R.id.channel:

                channel = new ChannelFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.layout, channel).commit();

                break;

            // 준비중 채널
            case R.id.main:

                main = new MainFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.layout, main).commit();


                break;

            // 준비중2 채널
            case R.id.ready2:
                ready2 = new Ready2Fragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.layout, ready2).commit();

                break;

            // account 계정
            case R.id.account:
                account = new AccountFragment();

                // MainActivity 의 layout 에 account 프레그먼트를 띄워줍니다.
                getSupportFragmentManager().beginTransaction().replace(R.id.layout, account).commit();

                break;

            // setting 설정
            case R.id.setting:

                setting = new SettingFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.layout, setting).commit();

                break;

            case R.id.logout:
//
//                logout = new LogoutFragment();
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.layout, logout).commit();

                LogoutMessage();


        }

        // NavigationView 중 아이템을 선택 시 drawerLayout 이 종료됩니다.
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void LogoutMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setIcon(Integer.parseInt(String.valueOf(R.drawable.ic_exit_to_app_black_24dp)));

        drawerLayout.closeDrawer(GravityCompat.START);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);


                SharedPreferences.Editor editor = Login.edit();

                editor.putString("token",null);

                editor.commit();

                startActivity(intent);
                overridePendingTransition(R.anim.loadfadein, R.anim.loadfadeout);
                Toast.makeText(MainActivity.this, "로그아웃을 정상적으로 수행하였습니다.", Toast.LENGTH_SHORT).show();

                // NavigationView 중 아이템을 선택 시 drawerLayout 이 종료됩니다.
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(MainActivity.this, "로그아웃을 취소하였습니다.", Toast.LENGTH_SHORT).show();

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.toolbar, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                drawerLayout.openDrawer(navigationView);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}