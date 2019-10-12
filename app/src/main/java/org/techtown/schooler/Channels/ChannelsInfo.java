package org.techtown.schooler.Channels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
import java.io.PushbackInputStream;
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
    String Channel_check;
    DrawerLayout drawerLayout;
    int activityBrequestCode = 0;

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
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        name = (TextView) findViewById(R.id.title_TextView2);
        content = (TextView) findViewById(R.id.content_TextView2);
        Create_user = (TextView) findViewById(R.id.makeUser_TextView2);
        thumbnail = (ImageView) findViewById(R.id.backgroundImage);
        color = (View) findViewById(R.id.Color);
        Login = getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Member);
        LinearLayoutManager LinearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(LinearLayoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
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
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == R.id.FixChannel) {
            Intent intent = new Intent(this, ChannelsAdminPage.class);
            intent.putExtra("channel_name", Channel_name);
            intent.putExtra("channel_color",Channel_Color);
            intent.putExtra("channel_explain",Channel_content);
            intent.putExtra("channel_check",Channel_check);
            intent.putExtra("channel_id",Channel_id);


            startActivityForResult(intent, activityBrequestCode);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if (id == R.id.UploadThumbnail) {
            Intent intent = new Intent(this, UploadChannelsThumbnail.class);
            intent.putExtra("channel_name", Channel_name);
            intent.putExtra("create_user", Channel_create_user);
            intent.putExtra("channel_color", Channel_Color);
            intent.putExtra("channel_explain", Channel_content);
            intent.putExtra("channel_isPublic", Channel_check);
            intent.putExtra("channel_id", Channel_id);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if (id == R.id.LeaveChannel) {
            ChannelLeave();
        }

        if (id == R.id.deleteChannel) {
            ChannelDelete();
        }

        if (id == R.id.FixMember) {
            Intent intent = new Intent(this, MemberAllowActivity.class);
            intent.putExtra("channel_id", Channel_id);
            startActivityForResult(intent, activityBrequestCode);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == activityBrequestCode && resultCode == RESULT_OK){
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Road();
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

    public void ChannelLeave(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("채널을 탈퇴하시겠습니까?");
        builder.setIcon(Integer.parseInt(String.valueOf(R.drawable.ic_exit_to_app_black_24dp)));

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Call<Response<Data>> res3 = NetRetrofit.getInstance().getChannel().LeaveChannel(Login.getString("token",""),Channel_id);
                res3.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        Toast.makeText(ChannelsInfo.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Toast.makeText(ChannelsInfo.this, "네트워크에 오류가 생겼습니다. 잠시 후 다시 시도 해 주세요", Toast.LENGTH_SHORT).show();
                        Log.e("Err", "네트워크 연결오류");
                    }
                });
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ChannelsInfo.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void ChannelDelete(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고");
        builder.setMessage("채널을 삭제하시겠습니까?");
        builder.setIcon(Integer.parseInt(String.valueOf(R.drawable.ic_exit_to_app_black_24dp)));

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Call<Response<Data>> res4 = NetRetrofit.getInstance().getChannel().DeleteChannel(Login.getString("token",""),Channel_id);
                res4.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        Toast.makeText(ChannelsInfo.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Toast.makeText(ChannelsInfo.this, "네트워크에 오류가 생겼습니다. 잠시 후 다시 시도 해 주세요", Toast.LENGTH_SHORT).show();
                        Log.e("Err", "네트워크 연결오류");
                    }
                });
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ChannelsInfo.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Road(){
        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().ChannelInfo(Login.getString("token",""),Channel_id);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if(response.code() != 404) {
                    Channel_name = response.body().getData().getChannelInfo().getName();
                    Channel_content = response.body().getData().getChannelInfo().getExplain();
                    Channel_create_user = response.body().getData().getChannelInfo().getCreate_user();
                    Channel_thumbnail = response.body().getData().getChannelInfo().getThumbnail();
                    Channel_Color = response.body().getData().getChannelInfo().getColor();
                    Channel_check = response.body().getData().getChannelInfo().getIsPublic();

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
                    adapter.CatchChannelId(Channel_id,Channel_create_user);

                    final Call<Response<Data>> res2 = NetRetrofit.getInstance().getProfile().GetProfile(Login.getString("token",""),"");
                    res2.enqueue(new Callback<Response<Data>>() {
                        @Override
                        public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                            usr_id = response.body().getData().getUserInfo().getUser_id();
                            if(Channel_create_user.equals(usr_id))
                                getMenuInflater().inflate(R.menu.channels_admin_page_menu, menu);
                            else if(getIntent().getStringExtra("userStatus").equals("2"))
                                getMenuInflater().inflate(R.menu.channel_user_page_menu, menu);
                        }

                        @Override
                        public void onFailure(Call<Response<Data>> call, Throwable t) {
                            Log.e("Err", "네트워크 연결오류");
                        }
                    });

                }else{
                    Toast.makeText(ChannelsInfo.this, "존재하지 않는 채널입니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });
    }
}
