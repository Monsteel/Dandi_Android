package org.techtown.schooler.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // String 형을 사용해서 id, pw 변수에 데이터를 저장합니다.
        String id = "uesr";
        String pw = "user";

        // login 매서드를 호출하면서 login 매서드의 파라미터인 LoginPostRequest 클래스로 위에서 생성한 id, pw 를 전달하고있습니다.
        login(new LoginPostRequest(id, pw));

    }

    // login 매서드
    private void login(LoginPostRequest loginPostRequest) {


        final Call<Response<Data>> res = NetRetrofit.getInstance().getService().loginPost(loginPostRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Integer Status = response.body().getStatus();
                String Message = response.body().getMessage();
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
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}

