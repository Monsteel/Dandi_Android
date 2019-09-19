package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;

public class CreateChannel extends AppCompatActivity {

    View colorView;
    EditText name;
    EditText explain;
    Switch isPublic;
    SharedPreferences Login;

    public static CreateChannelRequest createChannelRequest = new CreateChannelRequest("","","","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        colorView = (View)findViewById(R.id.Color);
        name =(EditText)findViewById(R.id.InputChannelName);
        explain = (EditText)findViewById(R.id.InputChannelExplain);
        isPublic = (Switch)findViewById(R.id.isPublicForChannel);

        createChannelRequest.setIsPublic("true");
        createChannelRequest.setExplain("");
        createChannelRequest.setName("");
        createChannelRequest.setColor(null);

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
                    if(response.isSuccessful()){
                        startActivity(new Intent(CreateChannel.this, setChannelImage.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    // Status != 200
                    else{
                        Toast.makeText(CreateChannel.this,"서버 오류발생",Toast.LENGTH_LONG);
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