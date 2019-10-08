package org.techtown.schooler.Channels;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.schooler.Model.CreateChannelRequest;
import org.techtown.schooler.R;
import org.techtown.schooler.Signup.PhoneNumberActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    String channel_name = null;
    String create_user = null;
    String channel_explain = null;
    String channel_isPublic = null;
    String channel_id = null;

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

    //컬러피커
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

    //컬러피커
    public void PickColor(View view){
        openColorPicker();
        colorView.setBackgroundColor(0);
    }

    //스크린 뒤로가기 버튼 이벤트
    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //채널만들기
    public void CreateChannel(View view){
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
                        Intent intent = new Intent(CreateChannel.this, FinishCreateChannels.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        channel_name = response.body().getData().getCreatedChannel().getName();
                        create_user = response.body().getData().getCreatedChannel().getCreate_user();
                        channel_explain = response.body().getData().getCreatedChannel().getExplain();
                        channel_isPublic = response.body().getData().getCreatedChannel().getIsPublic();
                        channel_id = response.body().getData().getCreatedChannel().getId();

                        intent.putExtra("channel_name",channel_name);
                        intent.putExtra("create_user",create_user);
                        intent.putExtra("channel_explain",channel_explain);
                        intent.putExtra("channel_isPublic",channel_isPublic);
                        intent.putExtra("channel_id",channel_id);

                        startActivity(intent);

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

    //물리적 뒤로가기 버튼 이벤트
    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}