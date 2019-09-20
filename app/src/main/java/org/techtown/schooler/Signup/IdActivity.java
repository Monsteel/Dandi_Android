package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.Model.IsOverlapped;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class IdActivity extends AppCompatActivity {


    String name;
    TextView noticeIdError;
    String ID;
    EditText InputId;
    TextView CheckID;
    TextView GotoPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        noticeIdError = (TextView) findViewById(R.id.noticeIdError);
        InputId = (EditText) findViewById(R.id.InputId);
        CheckID =(TextView) findViewById(R.id.CheckID);
        GotoPassword = (TextView)findViewById(R.id.GotoPassword);

        GotoPassword.setEnabled(false);
        GotoPassword.setTextColor(Color.parseColor("#FF5C5C5C"));


        CheckID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // idCheck 매서드를 호출하면서 파라미터로 IsOverlapped 클래스의 파라미터인 editText 에 입력한 값을 전달한다.
                idCheck(new IsOverlapped(InputId.getText().toString()));
            }
        });

        InputId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!InputId.equals(ID)){
                    GotoPassword.setEnabled(false);
                    GotoPassword.setTextColor(Color.parseColor("#FF5C5C5C"));
                    noticeIdError.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //입력후
            }
        });

    }
    private void idCheck(IsOverlapped isOverlapped) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().isoverlapped(isOverlapped);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // 아이디를 입력하지 않은 경우
                if (InputId.getText().toString().length() == 0) {

                    noticeIdError.setText("아이디를 입력해주세요.");
                    noticeIdError.setTextColor(Color.parseColor("#bc0000"));
                    GotoPassword.setEnabled(false);
                    GotoPassword.setTextColor(Color.parseColor("#FF5C5C5C"));
                }




                // 중복이 아닐 경우
                else if (response.code() == 200) {

                    // 사용자가 입력한 id 를 저장합니다.
                    ID = InputId.getText().toString();

                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
//                    Boolean data = response.body().getData().getOverlapped(); // true or false 값
                    Log.d("[isOverlapped] Status", Status + ":" + Message);

                    noticeIdError.setText("사용 가능한 아이디 입니다.");
                    noticeIdError.setTextColor(Color.parseColor("#0ec600"));

                    GotoPassword.setTextColor(Color.parseColor("#2349E6"));
                    GotoPassword.setEnabled(true);

                }


                // 중복일 경우
                else {
//                    try {
//                        JSONObject errorBody = new JSONObject(response.errorBody().string());
//                        Integer Error = errorBody.getInt("status");//error status value
//
//                        if (Error == 400 || Error == 500){
//                            Response response1 = new Response();
//                            response1.setStatus(errorBody.getInt("status"));
//                            response1.setMessage(errorBody.getString("message"));
//                            Log.e("[Login] Status", errorBody.getString("message"));
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    //에러바디를 읽는 코드

                    noticeIdError.setText("중복한 아이디가 존재합니다.");
                    noticeIdError.setTextColor(Color.parseColor("#bc0000"));
                    GotoPassword.setEnabled(false);
                    GotoPassword.setTextColor(Color.parseColor("#FF5C5C5C"));
                }
            }




            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");

            }
        });

    }

    public void toGoSchool(View view){
        Intent intent = new Intent(getApplicationContext(), PasswordActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",ID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
}
