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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import org.techtown.schooler.NavigationFragment.AccountFragment;
import org.techtown.schooler.NavigationFragment.ChannelFragment;
import org.techtown.schooler.NavigationFragment.MainFragment;
import org.techtown.schooler.NavigationFragment.Ready2Fragment;
import org.techtown.schooler.NavigationFragment.SettingFragment;
import org.techtown.schooler.StartMemberActivity.LoginActivity;

import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;


import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView; // NavigationView
    DrawerLayout drawerLayout; // DrawerLayout
    View main_nav_header; // 헤더 부분
    ImageView profile; // 헤더 부분 이미지
    MainFragment main = new MainFragment(); // 메인 프레그먼트
    SharedPreferences Login;
    Toolbar toolbar; // Toolbar

    TextView userName; // 이름
    TextView school; // 학교
    TextView grade; // 학반

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout); // DrawerLayout
        navigationView = (NavigationView) findViewById(R.id.navigationView); // NavigationView
        main_nav_header = navigationView.getHeaderView(0); // main_nav_header
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Toolbar
        profile = main_nav_header.findViewById(R.id.profile); // 헤더부분 이미지 즉 프로필
        userName = main_nav_header.findViewById(R.id.userName); // 이름
        school = main_nav_header.findViewById(R.id.school); // 학교
        grade = main_nav_header.findViewById(R.id.grade); // 학반

        Login = getSharedPreferences("Login", MODE_PRIVATE);

        if(Login.getString("token",null) == null){
            // Intent 클래스를 사용해서 LoginActivity 화면으로 전환을 합니다.

            Intent StatLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatLogin);

            Log.e("[LoginCheck]", "로그인 X");

        }else{
            Log.e("[LoginCheck] ", "로그인 O");
        }

        // 메인 프레그먼트를 먼저 설정해둡니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, main).commit();

        // toolbar 를 사용할 수 있도록 설정합니다.
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp2);


        // 제목을 보이지 않도록 합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f5f5")));

        // setNavigationItemSelectedListener 매서드를 사용하여 navigationView 객체에서 이벤트를 받으려면 리스너 설정을 해야한다.
        navigationView.setNavigationItemSelectedListener(MainActivity.this);


        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        // 바로가기 메뉴 정보를 불러오기 위한 userInformation() 매서드 호출
        userInformation();
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
            case R.id.textView:

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

    public void userInformation(){

        Call<Response<Data>> res = NetRetrofit.getInstance().getProfile().GetProfile(Login.getString("token",""),"");
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if(response.code() == 200)
                {
                    String name = response.body().getData().getUserInfo().getUser_name();
                    String schoolName = response.body().getData().getUserInfo().getSchool().getSchool_name();
                    String school_grade = response.body().getData().getUserInfo().getSchool_grade();
                    String school_class = response.body().getData().getUserInfo().getSchool_class();
                    String user_profile = response.body().getData().getUserInfo().getProfile_pic();

                    userName.setText(name);
                    school.setText(schoolName);
                    grade.setText(school_grade + "학년 " + school_class + "반");

                    new DownloadImageFromInternet(profile)
                            .execute(user_profile);

                } else if(response.code() == 410){

                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.commit();

                    Log.e("[status 410]","토큰 만료");
                    Toast.makeText(MainActivity.this, "토큰 만료, 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else if(response.code() == 500){

                    Log.e("[status 500]", response.body().getMessage());
                    Toast.makeText(MainActivity.this, "프로필 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "oh no", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("[ImageDownLoad][Error]", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}

