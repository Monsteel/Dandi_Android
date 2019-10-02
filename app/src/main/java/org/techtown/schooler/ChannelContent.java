package org.techtown.schooler;

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
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;

import retrofit2.Call;

public class ChannelContent extends AppCompatActivity {

    ImageButton backButton; // 뒤로가기 버튼

    SharedPreferences Login; // SharedPreferences 선언

    // 각각의 변수
    String start_date;
    String end_date;
    String user_name;
    String user_id;
    String title;
    String content;
    String channel_name;
    String channel_color;
    String channel_image;

    LinearLayout layout; // Background
    LinearLayout channel_layout;

    TextView event_title;
    TextView name;
    TextView id;
    TextView start;
    TextView end;
    TextView event_content;
    TextView event_channel_name;
    ImageView profile; // Channel Profile


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_content);

        profile = findViewById(R.id.profile);
        backButton = findViewById(R.id.backButton);
        Login = this.getSharedPreferences("Login", MODE_PRIVATE); //SharedPreferences 선언

        Intent intent = getIntent(); // Intent

        // 각각의 변수에 부가 데이터로 부터 전달 받은 값들을 저장합니다.
        start_date = intent.getExtras().getString("start_date");
        end_date = intent.getExtras().getString("end_date");
        user_name = intent.getExtras().getString("user_name");
        user_id = intent.getExtras().getString("user_id");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        channel_name = intent.getExtras().getString("channel_name");
        channel_color = intent.getExtras().getString("channel_color");
        channel_image = intent.getExtras().getString("channel_image");

        layout = findViewById(R.id.layout);
        channel_layout = findViewById(R.id.channel_layout);
        event_title = findViewById(R.id.event_title);
        name = findViewById(R.id.user_name);
        id = findViewById(R.id.user_id);
        start = findViewById(R.id.start_date);
        end = findViewById(R.id.end_date);
        event_content = findViewById(R.id.event_content);
        event_channel_name = findViewById(R.id.channel_name);


        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        // 뒤로가기 버튼을 클릭 시 MainActivity 화면 전환
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChannelContent.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        // 레이아웃 배경을 채널의 색상으로 설정합니다.
        layout.setBackgroundColor(Color.parseColor(channel_color));
        channel_layout.setBackgroundColor(Color.parseColor(channel_color));

        event_title.setText(title);
        name.setText(user_name);
        id.setText(user_id);
        start.setText(start_date);
        end.setText(end_date);
        event_content.setText(content);
        event_channel_name.setText(channel_name);

        new MainActivity.DownloadImageFromInternet(profile)
                .execute(channel_image);

        // schedule_title 타이틀 내용이 만약 글자 수를 초과할 시 흐르게 보여줍니다.
        event_title.setSingleLine(true);
        event_title.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        event_title.setSelected(true);

    }

    public static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("[ImageDownLoad][Error]", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


}
