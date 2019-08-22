package org.techtown.schooler.Activity.Login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            String id = "대소고 존잘남:이영은";
            String pw = "없다";
            login(new LoginPostRequest(id, pw));

        }

        private void login(LoginPostRequest loginPostRequest) {
            final Call<Response<Data>> res = NetRetrofit.getInstance().getService().loginPost(loginPostRequest);
            res.enqueue(new Callback<Response<Data>>() {
                @Override
                public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                    Integer Status = response.code();
                    String Message = response.message();
                    try {
                        if (Status == 200 || Status == 401 || Status == 403 || Status == 500) {
                            Toast.makeText(LoginActivity.this, Status + ":" + Message, Toast.LENGTH_SHORT).show();
                            Log.d("[Login] Status", Status + ":" + Message);
                        }
                    } catch (Exception err) {
                        //Toast.makeText(MainActivity.this, "네트워크 연결 오류", Toast.LENGTH_SHORT).show();
                        Log.e("[Login][ERROR] : ", "네트워크 연결 오류");
                    }
                }

                @Override
                public void onFailure(Call<org.techtown.schooler.network.response.Response<Data>> call, Throwable t) {
                    Log.e("Err", t.getMessage());
                }
            });
        }

}

