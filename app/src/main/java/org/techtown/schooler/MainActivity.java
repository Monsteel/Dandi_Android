package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        String id = "user";
        String pw = "user";
        login(new LoginPostRequest(id, pw));

    }

    private void login(LoginPostRequest loginPostRequest) {
        final Call<Response<Data>> res = NetRetrofit.getInstance().getService().loginPost(loginPostRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<org.techtown.schooler.network.response.Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                int Status = response.code();

                if (Status == 200) {
                    Toast.makeText(MainActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[Login]", "로그인에 성공했습니다.");
                }
                else {
                    Toast.makeText(MainActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[Login]", "로그인에 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<org.techtown.schooler.network.response.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}