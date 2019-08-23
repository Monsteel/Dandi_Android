package org.techtown.schooler.StartMember;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity{


    EditText Id_EditText; // Id 입력 창
    EditText Pw_EditText; // Pw 입력 창

    // String 형 Id, Pw 변수에 EditText 에서 입력한 값들을 저장합니다.
    String Id;
    String Pw;

    Button button; // Login 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Id_EditText = findViewById(R.id.Id_EditText);
        Pw_EditText = findViewById(R.id.Pw_EditText);
        button = findViewById(R.id.Login_Button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login(new LoginPostRequest(Id_EditText.getText().toString(), Pw_EditText.getText().toString()));
            }
        });



        // Id, Pw 변수에 editText 값에 입력한 값들을 저장하고있습니다.


    }

    // login 매서드
    private void login(LoginPostRequest loginPostRequest) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getLogin().loginPost(loginPostRequest);
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

                        Integer Error =errorBody.getInt("status");//error status value
                        if (Error == 401 ||Error == 405) {
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
                Log.e("Err", "네트워크 연결오류");
            }
        });
    }



}

