package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.techtown.schooler.Activity.LoginActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;
import org.techtown.schooler.network.retrofit.interfaces.Login;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences LoginCheck = getSharedPreferences("Check", MODE_PRIVATE);
        boolean first = LoginCheck.getBoolean("Check", false);//첫 실행임
        if(first==false){
            Intent StatMember = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(StatMember);
        }else{
            Log.d("Is first Time?", "not first");
        }
    }
}