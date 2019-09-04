package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.techtown.schooler.R;

import java.util.regex.Pattern;

public class PhoneNumberActivity extends AppCompatActivity {

    String PhoneNumber;
    EditText InputPhoneNumber;
    TextView noticePhoneNumberError;
    TextView GotoSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        InputPhoneNumber = (EditText)findViewById(R.id.InputPhoneNumber);
        noticePhoneNumberError = (TextView)findViewById(R.id.noticePhoneNumberError);
        GotoSchool = (TextView)findViewById(R.id.GotoSchool);
        noticePhoneNumberError.setText("");

        GotoSchool.setEnabled(false);
        GotoSchool.setTextColor(Color.parseColor("#FF5C5C5C"));

        InputPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        InputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력중
                if(!android.util.Patterns.PHONE.matcher(InputPhoneNumber.getText().toString()).matches()){
                        GotoSchool.setEnabled(false);
                        GotoSchool.setTextColor(Color.parseColor( "#FF5C5C5C"));

                        noticePhoneNumberError.setTextColor(Color.parseColor("#F80000"));
                        noticePhoneNumberError.setText("올바른 형식의 전화번호를 입력하세요");
                }else{
                    GotoSchool.setEnabled(true);
                    GotoSchool.setTextColor(Color.parseColor("#2349E6"));

                    noticePhoneNumberError.setTextColor(Color.parseColor("#0078F8"));
                    noticePhoneNumberError.setText("올바른 형식의 전화번호가 입력되었습니다");
                    PhoneNumber = InputPhoneNumber.getText().toString();
                    Log.e("",PhoneNumber+"");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //입력후

            }
        });
    }

    public void toGoSchool(View view){
        Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password",getIntent().getStringExtra("Password"));
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("PhoneNumber",PhoneNumber+"");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void toGoBack(View view){
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
