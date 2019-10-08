package org.techtown.schooler.Channels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.schooler.ChannelEvents.CreateChannelEvents;
import org.techtown.schooler.MainActivity;
import org.techtown.schooler.Model.Channel;
import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.ChannelListAdapter;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ChannelsInfo extends AppCompatActivity {

    String Channel_id;
    String Channel_name;
    String Channel_content;
    String Channel_create_user;
    String Channel_thumbnail;
    String Channel_Color;
    String usr_id;
    Toolbar toolbar;
    Menu menu;

    SharedPreferences Login;

    TextView name;
    TextView content;
    TextView Create_user;
    ImageView thumbnail;
    View color;
    RecyclerView recyclerView;
    List<User> DataList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels_info);
        Channel_id = getIntent().getStringExtra("channel_id");
        System.out.println("");

        name = (TextView)findViewById(R.id.title_TextView2);
        content = (TextView)findViewById(R.id.content_TextView2);
        Create_user = (TextView)findViewById(R.id.makeUser_TextView2);
        thumbnail = (ImageView)findViewById(R.id.backgroundImage);
        color = (View)findViewById(R.id.Color);
        Login = getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_Member);
        LinearLayoutManager LinearLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(LinearLayoutManager);

        toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f5f5f5")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().ChannelInfo(Login.getString("token",""),Channel_id);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Channel_name = response.body().getData().getChannelInfo().getName();
                Channel_content = response.body().getData().getChannelInfo().getExplain();
                Channel_create_user = response.body().getData().getChannelInfo().getCreate_user();
                Channel_thumbnail = response.body().getData().getChannelInfo().getThumbnail();
                Channel_Color = response.body().getData().getChannelInfo().getColor();

                name.setText(Channel_name);
                getSupportActionBar().setTitle(Channel_name);
                content.setText("\""+Channel_content+"\"");
                Create_user.setText("Master : "+Channel_create_user);
                color.setBackgroundColor(Color.parseColor(Channel_Color));

                new DownloadImageFromInternet(thumbnail)
                        .execute(Channel_thumbnail);


                DataList = response.body().getData().getChannelInfo().getUsers();
                MemberListAdapter adapter = new MemberListAdapter(DataList);
                recyclerView.setAdapter(adapter);


                final Call<Response<Data>> res2 = NetRetrofit.getInstance().getProfile().GetProfile(Login.getString("token",""),"");
                res2.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        usr_id = response.body().getData().getUserInfo().getUser_id();
                        System.out.println("");
                        if(Channel_create_user.equals(usr_id))
                            getMenuInflater().inflate(R.menu.channels_admin_page_menu, menu);
                    }

                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Log.e("Err", "네트워크 연결오류");
                    }
                });
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });
        this.menu = menu;
        return true;
    }



    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
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
