package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.techtown.schooler.StartMemberActivity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SharedPreferences 클래스를 사용해서 LoginCheck 변수를 생성합니다.
        // 그 후 LoginCheck 변수에 (아직은 저장 기능을 만들지 않았습니다.) 앞서 저장한 것을 불러옵니다.
        SharedPreferences LoginCheck = getSharedPreferences("Check", MODE_PRIVATE);

        // boolean 자료형을 사용하여 first 변수를 생성 후 앞서 생성한 Login8Check 변수에 있는 값을 getBoolean 속성을 사용합니다.
        // 파라미터로 위에서 생성한 getSharedPreferences 의 name 부분이 만약 null 값이라면 first 변수를 false 로 지정합니다.
        final boolean first = LoginCheck.getBoolean("Check", false); //첫 실행임

        // first 변수가 false 일 경우
        if(first==false){
            // Intent 클래스를 사용해서 LoginActivity 화면으로 전환을 합니다.
            Intent StatLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatLogin);
            Log.d("[LoginCheck]", "로그인 X");
            // first 변수가 true 일 경우
        }else{
            Log.d("[LoginCheck] ", "로그인 O");
        }
    }
}