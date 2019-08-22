package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.techtown.schooler.Activity.Login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences LoginCheck = getSharedPreferences("Check", MODE_PRIVATE);
        boolean first = LoginCheck.getBoolean("Check", false);//첫 실행임
        if(first==false){
            Intent StatLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatLogin);
            Log.d("[LoginCheck]", "로그인 X");
        }else{
            Log.d("[LoginCheck] ", "로그인 O");
        }
    }
}