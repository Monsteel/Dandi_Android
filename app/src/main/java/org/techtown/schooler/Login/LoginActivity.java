package org.techtown.schooler.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // String 형을 사용해서 id, pw 변수에 데이터를 저장합니다.
        String id = "user";
        String pw = "user";


        Button button = findViewById(R.id.Login_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "hello", Toast.LENGTH_SHORT).show();
            }
        });

        // login 매서드를 호출하면서 login 매서드의 파라미터인 LoginPostRequest 클래스로 위에서 생성한 id, pw 를 전달하고있습니다.
        login(new LoginPostRequest(id, pw));

    }

    // login 매서드
    private void login(LoginPostRequest loginPostRequest) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getService().loginPost(loginPostRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if (response.isSuccessful()) {
                    Integer Status = response.body().getStatus();
                    String Message = response.body().getMessage();
                    Toast.makeText(LoginActivity.this, Status + ":" + Message, Toast.LENGTH_SHORT).show();
                    Log.d("[Login] Status", Status + ":" + Message);
                }else{
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        Integer Error =errorBody.getInt("status");
                        if (Error == 401 ||errorBody.getInt("status") == 405) {
                            Response response1 = new Response();
                            response1.setStatus(errorBody.getInt("status"));
                            response1.setMessage(errorBody.getString("message"));
                            Log.e("[Login] Status", errorBody.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}

