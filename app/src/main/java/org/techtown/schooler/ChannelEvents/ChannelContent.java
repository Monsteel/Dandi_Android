package org.techtown.schooler.ChannelEvents;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.techtown.schooler.Channels.ChannelsInfo;
import org.techtown.schooler.MainActivity;
import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;

public class ChannelContent extends AppCompatActivity {

    // SharedPreferences 선언
    SharedPreferences Login;

    // 부가 데이터
    String start_date;
    String end_date;
    String user_name;
    String user_id;
    String title;
    String content;
    String channel_name;
    String channel_color;
    String channel_image;
    String event_id;
    String channel_id;

    // XML View
    TextView event_title;
    TextView name;
    TextView id;
    TextView start;
    TextView end;
    TextView event_content;
    TextView event_channel_name;
    ImageView profile;
    ImageButton delete;
    ImageButton edit;
    ImageButton backButton;
    LinearLayout layout; // Layout Background
    LinearLayout channel_layout; // Button Background
    LinearLayout content_layout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_content);

        Login = this.getSharedPreferences("Login", MODE_PRIVATE); //SharedPreferences 선언

        // 부가 데이터 저장
        Intent intent = getIntent();

        // 각각의 변수에 부가 데이터를 저장합니다.
        start_date = intent.getExtras().getString("start_date");
        end_date = intent.getExtras().getString("end_date");
        user_name = intent.getExtras().getString("user_name");
        user_id = intent.getExtras().getString("user_id");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        channel_name = intent.getExtras().getString("channel_name");
        channel_color = intent.getExtras().getString("channel_color");
        channel_image = intent.getExtras().getString("channel_image");
        event_id = intent.getExtras().getString("event_id");
        channel_id = intent.getExtras().getString("channel_id");

        // XML View 를 초기화합니다.
        profile = findViewById(R.id.profile);
        backButton = findViewById(R.id.backButton);
        layout = findViewById(R.id.layout);
        channel_layout = findViewById(R.id.channel_layout);
        event_title = findViewById(R.id.event_title);
        name = findViewById(R.id.user_name);
        id = findViewById(R.id.user_id);
        start = findViewById(R.id.start_date);
        end = findViewById(R.id.end_date);
        event_content = findViewById(R.id.event_content);
        event_channel_name = findViewById(R.id.channel_name);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        content_layout = findViewById(R.id.content_layout);

        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        if(channel_color != null){

            // 레이아웃 배경을 채널의 색상으로 설정합니다.
            layout.setBackgroundColor(Color.parseColor(channel_color)); // Layout Background
            channel_layout.setBackgroundColor(Color.parseColor(channel_color)); // Button Background
        } else {

            // 레이아웃 배경을 채널의 색상으로 설정합니다.
            layout.setBackgroundColor(Color.parseColor("#F1B71C")); // Layout Background
            channel_layout.setBackgroundColor(Color.parseColor("#F1B71C")); // Button Background
        }

        // XML View 에 전달받은 부가 데이터를 저장합니다.
        event_title.setText(title);
        name.setText(user_name);
        id.setText(user_id);
        start.setText(start_date);
        end.setText(end_date);

        if(content == null){

            event_content.setText("학사 일정은 상세 내용이 존재하지 않습니다.");
        }else {

            event_content.setText(content);
        }


        event_channel_name.setText(channel_name);

        if(channel_image != null){
            Glide.with(ChannelContent.this).load(channel_image).into(profile);
        } else {

            profile.setImageResource(R.drawable.dgsw);
        }


        // schedule_title 타이틀 내용이 만약 글자 수를 초과할 시 흐르게 보여줍니다.
        event_title.setSingleLine(true);
        event_title.setEllipsize(TextUtils.TruncateAt.END);
        event_title.setSelected(true);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChannelContent.this, ChannelsInfo.class);
                intent.putExtra("channel_id", channel_id);
                intent.putExtra("userStatus", "2");
                startActivity(intent);
            }
        });

        channel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChannelContent.this, ChannelsInfo.class);
                intent.putExtra("channel_id", channel_id);
                intent.putExtra("userStatus", "2");
                startActivity(intent);
            }
        });



    }

    // 일정 삭제 (Retrofit2)
    public void eventsDelete(){

        Call<Response<Data>> res = NetRetrofit.getInstance().getChannelEvent().DeleteChannelEvent(Login.getString("token",""),event_id);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Toast.makeText(ChannelContent.this, "일정을 삭제하였습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChannelContent.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

                Toast.makeText(ChannelContent.this, "오류", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 일정 수정 (Retrofit2)
    public void updateEvents(){

        Intent intent = new Intent(this, UpdateEvents.class);

        intent.putExtra("start_date",start_date);
        intent.putExtra("end_date", end_date);
        intent.putExtra("user_name", user_name);
        intent.putExtra("user_id", user_id);
        intent.putExtra("channel_name", user_name);
        intent.putExtra("title", title);
        intent.putExtra("content",content);
        intent.putExtra("event_id", event_id);

        startActivity(intent);
    }

    // backButton (Onclick)
    public void back(View view){

        Intent intent = new Intent(ChannelContent.this, MainActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // delete (Onclick)
    public void delete(View view){

        if(content == null){

            Toast.makeText(ChannelContent.this, "학사 일정은 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else{

            eventsDelete();
        }
    }

    // edit (Onclick)
    public void edit(View view){

        if(content == null){

            Toast.makeText(ChannelContent.this, "학사 일정은 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {

            updateEvents();
        }

    }

}
