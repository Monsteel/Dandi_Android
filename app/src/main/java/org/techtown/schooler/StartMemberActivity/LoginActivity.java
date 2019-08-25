package org.techtown.schooler.StartMemberActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.schooler.R;
//import org.techtown.schooler.SignUpViewPager.Activity.SignupActivity;
import org.techtown.schooler.SigninUser.SignupActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity{


    EditText Id_EditText; // Id 입력 창
    EditText Pw_EditText; // Pw 입력 창

    Button button; // Login 버튼

    TextView textView; // 회원가입 버튼

    InputMethodManager imm; // 가상 키패드 내리기

    LinearLayout layout; // 레이아웃

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 사용자가 화면을 세로로 할 시 activity_login 화면이 출력이 되고 반면에 가로로 할 시 activity_login_land 화면이 출력됩니다.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_login);
        } else {
            setContentView(R.layout.activity_login_land);
        }

        Id_EditText = findViewById(R.id.Id_EditText); // ID
        Pw_EditText = findViewById(R.id.Pw_EditText); // PW
        button = findViewById(R.id.Login_Button); // Login 버튼
        textView = findViewById(R.id.join_TextView); // 회원가입 텍스트

        layout = findViewById(R.id.layout); // 레이아웃

        // 가상 키패드를 내리기 위한 코드이다.
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // 레이아웃을 클릭 시 가상 키패드를 내린다.
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imm.hideSoftInputFromWindow(Id_EditText.getWindowToken(), 0);
            }
        });
        // Login 버튼을 클릭 시 login() 매서드를 호출합니다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // login() 매서드의 파라미터로 사용자가 입력한 Id, Pw 를 전달하고있습니다.
                login(new LoginPostRequest(Id_EditText.getText().toString(), Pw_EditText.getText().toString()));
            }
        });


        // 회원가입 버튼을 클릭 시 SignupActivity 즉 회원가입 페이지로 화면을 전환합니다.
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent 클래스를 참조해서 화면을 전환하고있습니다.
                Intent StartSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(StartSignup);
            }
        });


    }


    // login 매서드
    private void login(LoginPostRequest loginPostRequest) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getLogin().loginPost(loginPostRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {


                if(Id_EditText.getText().toString().length() == 0 || Pw_EditText.getText().toString().length() == 0){

                    Toast.makeText(LoginActivity.this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    Log.d("[Login]","아이디 비밀번호를 입력해주세요");
                }
                else if (response.isSuccessful()) {
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

    // onBackPresses() 매서드를 사용하여 뒤로가기 버튼을 방지합니다.
    @Override
    public void onBackPressed(){

        // 뒤로가기 버튼을 클릭 시 앱이 종료된다.
        ActivityCompat.finishAffinity(this);
    }

   public void touchLayout(){

       imm.hideSoftInputFromWindow(Id_EditText.getWindowToken(), 0);

       imm.hideSoftInputFromWindow(Pw_EditText.getWindowToken(), 0);
   }

}

