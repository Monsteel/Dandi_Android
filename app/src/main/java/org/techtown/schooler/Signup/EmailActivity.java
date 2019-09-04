package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.Email;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;
import org.w3c.dom.Text;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class EmailActivity extends AppCompatActivity {

    EditText InputEmail;
    EditText InputAuth;
    TextView SendAuthCode;
    TextView ErrorEmail;
    LinearLayout EmailAuthCheck;
    String AuthCode;
    ImageView CheckAuth;
    TextView NextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        InputEmail = (EditText)findViewById(R.id.InputEmail);
        SendAuthCode = (TextView)findViewById(R.id.SendAuthCode);
        InputAuth = (EditText)findViewById(R.id.InputAuth);
        CheckAuth = (ImageView)findViewById(R.id.CheckAuth);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("Name");

        SendAuthCode.setEnabled(false);
        SendAuthCode.setTextColor(Color.parseColor("#FF5C5C5C"));
        ErrorEmail = (TextView)findViewById(R.id.ErrorEmail);
        EmailAuthCheck = (LinearLayout) findViewById(R.id.EmailAuthCheck);
        NextButton = (TextView)findViewById(R.id.GotoPhoneNumber);

        ErrorEmail.setVisibility(View.INVISIBLE);
        EmailAuthCheck.setVisibility(View.INVISIBLE);

        CheckAuth.setVisibility(View.INVISIBLE);

        NextButton.setEnabled(false);
        NextButton.setTextColor(Color.parseColor("#FF5C5C5C"));


        InputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(InputEmail.getText().toString()).matches()){
                   SendAuthCode.setEnabled(false);
                   SendAuthCode.setTextColor(Color.parseColor("#FF5C5C5C"));
                }else{
                    SendAuthCode.setEnabled(true);
                    SendAuthCode.setTextColor(Color.parseColor("#2349E6"));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //입력후
            }
        });

        InputAuth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(InputAuth.getText().toString().equals(AuthCode)){
                    CheckAuth.setVisibility(View.VISIBLE);
                    CheckAuth.setImageResource(R.drawable.okay);
                    NextButton.setEnabled(true);
                    NextButton.setTextColor(Color.parseColor("#2349E6"));
                }else{
                    CheckAuth.setVisibility(View.VISIBLE);
                    CheckAuth.setImageResource(R.drawable.no);
                    NextButton.setEnabled(false);
                    NextButton.setTextColor(Color.parseColor("#FF5C5C5C"));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //입력후
            }
        });
    }

    public void toClickSendAuth(View view){


        SendAuthCode.setTextColor(Color.parseColor("#FF5C5C5C"));
        SendAuthCode.setEnabled(false);

        ErrorEmail.setTextColor(Color.parseColor("#FFE73E0A"));
        ErrorEmail.setText("이메일 전송중..");
        ErrorEmail.setVisibility(View.VISIBLE);

        Email email = new Email(InputEmail.getText().toString());

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().eamilPost(email);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // Status == 200
                if(response.isSuccessful()){

                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
                    AuthCode = response.body().getData().getAuthCode(); // authCode(이메일 코드)
                    Log.d("[SandEmail] Status", Status + ":" + Message);
                    SendEmailSuccess();

                }

                // Status != 200
                else{
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        Integer Error =errorBody.getInt("status");//error status value
                        if (Error == 500) {
                            Response response1 = new Response();
                            response1.setStatus(errorBody.getInt("status"));
                            response1.setMessage(errorBody.getString("message"));
                            Log.e("[SandEmail] Status", errorBody.getString("message"));
                            SendEmailFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
                SendEmailFail();
            }
        });
    }

    public void SendEmailSuccess() {
        ErrorEmail.setTextColor(Color.parseColor("#0ec600"));
        ErrorEmail.setText("이메일 전송에 성공했습니다");
        ErrorEmail.setVisibility(View.VISIBLE);
        EmailAuthCheck.setVisibility(View.VISIBLE);
        InputEmail.setInputType(InputType.TYPE_NULL);
    }

    public void SendEmailFail() {
        ErrorEmail.setVisibility(View.VISIBLE);
        ErrorEmail.setTextColor(Color.parseColor("#bc0000"));
        ErrorEmail.setText("이메일 전송에 실패했습니다.\n이 문제가 계속되면 관리자에게 문의하십시오");

        SendAuthCode.setTextColor(Color.parseColor("#2349E6"));
        SendAuthCode.setEnabled(true);
    }

    public void toGoBack(View view){
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void toGoPhoneNumber(View view){
        Intent intent = new Intent(getApplicationContext(), PhoneNumberActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password",getIntent().getStringExtra("Password"));
        intent.putExtra("Email",InputEmail.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
