package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.schooler.R;

import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity {


    EditText InputPassword;
    EditText InputCheckPassword;
    String password;
    TextView NoticePassword;
    TextView NoticeCheckPassword;
    TextView GotoEmail;
    boolean Password;
    boolean CheckPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        InputPassword = (EditText)findViewById(R.id.InputPassword);
        InputCheckPassword = (EditText)findViewById(R.id.InputCheckPassword);
        NoticePassword = (TextView)findViewById(R.id.NoticePassword);
        NoticeCheckPassword =(TextView)findViewById(R.id.NoticeCheckPassword);
        GotoEmail = (TextView)findViewById(R.id.GotoEmail);

        GotoEmail.setEnabled(false);
        GotoEmail.setTextColor(Color.parseColor("#FF5C5C5C"));

        InputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(InputPassword.getText().toString().length() < 8) {
                    NoticePassword.setText("비밀번호를 8자리 이상으로 구성해주세요");
                    NoticePassword.setTextColor(Color.parseColor("#bc0000"));
                    Password= false;
                    GotoEmail.setEnabled(false);
                    GotoEmail.setTextColor(Color.parseColor("#FF5C5C5C"));
                }else{
                    NoticePassword.setText("사용할 수 있는 비밀번호 입니다.");
                    NoticePassword.setTextColor(Color.parseColor("#0ec600"));
                    Password = true;
                    GotoEmail.setEnabled(false);
                    GotoEmail.setTextColor(Color.parseColor("#FF5C5C5C"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력후
            }
        });

        InputCheckPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(InputPassword.getText().toString().length() < 8) {
                    NoticeCheckPassword.setText("비밀번호를 8자리 이상으로 구성해주세요");
                    NoticeCheckPassword.setTextColor(Color.parseColor("#bc0000"));
                    GotoEmail.setEnabled(false);
                    GotoEmail.setTextColor(Color.parseColor("#FF5C5C5C"));
                }else {
                    if (InputCheckPassword.getText().toString().equals(InputPassword.getText().toString())) {
                        NoticeCheckPassword.setTextColor(Color.parseColor("#0ec600"));
                        NoticeCheckPassword.setText("비밀번호가 일치합니다.");
                        CheckPassword = true;
                    } else {
                        NoticeCheckPassword.setTextColor(Color.parseColor("#FFC00000"));
                        NoticeCheckPassword.setText("비밀번호가 일치하지 않습니다.");
                        CheckPassword = false;
                        GotoEmail.setEnabled(false);
                        GotoEmail.setTextColor(Color.parseColor("#FF5C5C5C"));
                        password = InputPassword.getText().toString();


                        System.out.print(password);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (CheckPassword && Password) {
                    GotoEmail.setEnabled(true);
                    GotoEmail.setTextColor(Color.parseColor("#2349E6"));
                }
            }
        });

        InputCheckPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    InputCheckPassword.setText("");
                    NoticePassword.setText("");
                    NoticeCheckPassword.setText("");
                }else{

                }
            }
        });

        InputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    InputPassword.setText("");
                    NoticePassword.setText("");
                    NoticeCheckPassword.setText("");
                    InputCheckPassword.setText("");
                }else {
                }
            }
        });

    }

    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void toGoEmail(View view){
        Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password", password);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
