package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.schooler.Model.CreateChannelRequest;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.util.ArrayList;
import java.util.regex.Pattern;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;

public class CreateChannel extends AppCompatActivity {

    View colorView;
    EditText name;
    EditText explain;
    Switch isPublic;
    SharedPreferences Login;
    TextView Next;
    InputMethodManager imm;

    boolean next1 = false;
    boolean next2 = false;


    public static CreateChannelRequest createChannelRequest = new CreateChannelRequest("","","","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        colorView = (View)findViewById(R.id.Color);
        name =(EditText)findViewById(R.id.InputChannelName);
        explain = (EditText)findViewById(R.id.InputChannelExplain);
        isPublic = (Switch)findViewById(R.id.isPublicForChannel);
        Next = (TextView)findViewById(R.id.Finish);

        createChannelRequest.setIsPublic("true");
        createChannelRequest.setExplain("");
        createChannelRequest.setName("");
        createChannelRequest.setColor(null);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        Next.setEnabled(false);
        Next.setTextColor(Color.parseColor("#FF5C5C5C"));

        Login = getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //승인 후 가입
                    createChannelRequest.setIsPublic("false");
                }else{
                    //승인없이 바로 가입.
                    createChannelRequest.setIsPublic("true");
                }
            }
        });


        //               Next.setEnabled(true);
        //               Next.setTextColor(Color.parseColor("#2349E6"));




        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name.getText().toString().equals("")){
                    next1 = false;
                }else{
                    next1 = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (next1 && next2) {
                    Next.setEnabled(true);
                    Next.setTextColor(Color.parseColor("#2349E6"));
                }else{
                    Next.setEnabled(false);
                    Next.setTextColor(Color.parseColor("#FF5C5C5C"));
                }
            }
        });

        explain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(explain.getText().toString().equals("")){
                    next2 = false;
                }else{
                    next2 = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (next1 && next2) {
                    Next.setEnabled(true);
                    Next.setTextColor(Color.parseColor("#2349E6"));
                }else{
                    Next.setEnabled(false);
                    Next.setTextColor(Color.parseColor("#FF5C5C5C"));
                }
            }
        });

    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                    @Override
                    public void onChooseColor(int position, int color) {// OK 버튼 클릭 시 이벤트
                        if(color == 0){
                            Toast.makeText(CreateChannel.this,"색상이 선택되지 않았습니다",Toast.LENGTH_LONG).show();
                            openColorPicker();
                        }else{
                            colorView.setBackgroundColor(color);
                            String strColor = String.format("#%06X", 0xFFFFFF & color);//Color int를 String 으로 변환하기
                            createChannelRequest.setColor(strColor);
                        }
                    }

                    @Override
                    public void onCancel() {
                         // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    public void PickColor(View view){
        openColorPicker();
        colorView.setBackgroundColor(0);
    }

    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Create (View view) {


        if(name.getText().equals("")||explain.getText().equals("")){
            //비워져있으면
        }else{
            createChannelRequest.setName(name.getText()+"");
            createChannelRequest.setExplain(explain.getText()+"");

            //서버통신 시작


            final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().AddChannel(Login.getString("token",""),createChannelRequest);
            res.enqueue(new Callback<Response<Data>>() {
                @Override
                public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                    // Status == 200
                    if(response.code() == 200){
                        startActivity(new Intent(CreateChannel.this, setChannelImage.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else if(response.code() == 400){
                        Toast.makeText(CreateChannel.this,"동일한 이름의 채널이 이미 존재합니다.",Toast.LENGTH_LONG).show();
                        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    }
                    // Status != 200
                    else{
                        Toast.makeText(CreateChannel.this,"서버 오류발생",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<Data>> call, Throwable t) {
                    Log.e("Err", "네트워크 연결오류");
                }
            });
        }

        //만약 채워지지 않은 부분이 있으면,
        //뷰 아래에 빨간색 텍스트뷰 띄우기
        //아니면 채널 가입 요청 보내고, 다음 엑티비티로 넘어가기
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}