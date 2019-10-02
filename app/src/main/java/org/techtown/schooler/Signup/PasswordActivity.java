package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.schooler.R;
import org.techtown.schooler.network.SHA512;

public class PasswordActivity extends AppCompatActivity {


    EditText InputPassword;
    EditText InputCheckPassword;
    String password;
    TextView NoticePassword;
    TextView NoticeCheckPassword;
    ImageView GotoEmail;
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
        GotoEmail = (ImageView)findViewById(R.id.next_email);

        GotoEmail.setEnabled(false);
        GotoEmail.setImageResource(R.drawable.ic_chevron_right_black_24dp);

        NoticePassword.setText("");
        NoticeCheckPassword.setText("");

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
                    GotoEmail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                }else{
                    NoticePassword.setText("사용할 수 있는 비밀번호 입니다.");
                    NoticePassword.setTextColor(Color.parseColor("#0ec600"));
                    Password = true;
                    GotoEmail.setEnabled(false);
                    GotoEmail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
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
                    GotoEmail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
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
                        GotoEmail.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                        password = InputPassword.getText().toString();


                        System.out.print(password);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (CheckPassword && Password) {
                    GotoEmail.setEnabled(true);
                    GotoEmail.setImageResource(R.drawable.ic_chevron_right_yellow_24dp);
                }
            }
        });

        InputCheckPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    InputCheckPassword.setText("");
                }else{

                }
            }
        });

        InputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    InputPassword.setText("");
                    InputCheckPassword.setText("");
                    NoticePassword.setText("");
                    NoticeCheckPassword.setText("");
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

    public void toGoNext(View view){
        Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password", SHA512.getSHA512(password));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
