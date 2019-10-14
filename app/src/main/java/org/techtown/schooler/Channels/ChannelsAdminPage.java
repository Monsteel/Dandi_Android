package org.techtown.schooler.Channels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.techtown.schooler.Model.ChannelEditRequest;
import org.techtown.schooler.Model.CreateChannelRequest;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;

public class ChannelsAdminPage extends AppCompatActivity {

    Toolbar toolbar1;
    String Channel_name;
    String Channel_color;
    String Channel_explain;
    String Channel_check;
    String Channel_id;


    private EditText inputName;
    private EditText inputExplain;
    private Switch inputCheck;
    private CardView inputColor;

    public static ChannelEditRequest channelEditRequest  = new ChannelEditRequest("","","");

    SharedPreferences Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_admin_page);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar3);

        Channel_id = getIntent().getStringExtra("channel_id");
        Channel_name = getIntent().getStringExtra("channel_name");
        Channel_color = getIntent().getStringExtra("channel_color");
        Channel_explain = getIntent().getStringExtra("channel_explain");
        Channel_check = getIntent().getStringExtra("channel_check");
        Login = getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        inputName = (EditText)findViewById(R.id.EditChannelName);
        inputExplain = (EditText)findViewById(R.id.EditChannelExplain);
        inputCheck = (Switch)findViewById(R.id.EditChannelIsPublic);
        inputColor = (CardView)findViewById(R.id.EditChannelColor);

        inputName.setText(Channel_name);
        inputExplain.setText(Channel_explain);
        inputColor.setBackgroundColor(Color.parseColor(Channel_color));

        channelEditRequest.setExplain(Channel_explain);
        channelEditRequest.setColor(Channel_color);
        channelEditRequest.setIsPublic(Channel_check);

        if(Channel_check.equals(1)){
            inputCheck.setChecked(false);
        }else{
            inputCheck.setChecked(true);
        }

        inputCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    channelEditRequest.setIsPublic("false");
                }else{
                    channelEditRequest.setIsPublic("true");
                }
            }
        });

        setSupportActionBar(toolbar1);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f5f5")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("채널 설정");

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void PickColor(View view){
        openColorPicker();
    }

    public void ChannelUpdate(View view) {
        channelEditRequest.setExplain(inputExplain.getText().toString());
        Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().UpdateChannelInfo(Login.getString("token",""),Channel_id,channelEditRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.e("[Retrofit]",response.code()+"");
                onBackPressed();
            }
            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
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
                            Toast.makeText(ChannelsAdminPage.this,"색상이 선택되지 않았습니다",Toast.LENGTH_LONG).show();
                            openColorPicker();
                        }else{
                            inputColor.setBackgroundColor(color);
                            String strColor = String.format("#%06X", 0xFFFFFF & color);//Color int를 String 으로 변환하기
                            channelEditRequest.setColor(strColor);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
