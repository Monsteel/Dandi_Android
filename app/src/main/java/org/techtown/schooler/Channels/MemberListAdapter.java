package org.techtown.schooler.Channels;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.network.ChannelListAdapter;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder>{

    private final List<User> mDataList;
    SharedPreferences Login;
    String channel_id;
    String create_user;

    public void CatchChannelId(String channel_id, String Create_user){
        this.channel_id = channel_id;
        this.create_user = Create_user;
    }

    public MemberListAdapter(List<User> dataList){
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        //뷰홀더때문에 노가다했었음....
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User item = mDataList.get(position);
        String user_id;
        Login = holder.Id.getContext().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        holder.Name.setText(item.getUser_name());
        holder.Id.setText(item.getUser_id());
        user_id = item.getUser_id();

        Glide.with(holder.Name.getContext()).load(item.getProfile_pic()).into(holder.Profile);

        if(Login.getString("id","").equals(create_user)){
            holder.MemberCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popup = new PopupMenu(holder.MemberCardView.getContext(), holder.MemberCardView);
                    if(!create_user.equals(user_id)) {
                        popup.inflate(R.menu.user_long_click);
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Call<Response<Data>> res4 = NetRetrofit.getInstance().getChannel().ForcedExit(Login.getString("token",""),channel_id,user_id);
                            res4.enqueue(new Callback<Response<Data>>() {
                                @Override
                                public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                                    Toast.makeText(holder.MemberCardView.getContext(), "강퇴처리 되었습니다.", Toast.LENGTH_SHORT).show();
                                    mDataList.remove(position);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<Response<Data>> call, Throwable t) {
                                    Toast.makeText(holder.MemberCardView.getContext(), "네트워크에 오류가 생겼습니다. 잠시 후 다시 시도 해 주세요", Toast.LENGTH_SHORT).show();
                                    Log.e("Err", "네트워크 연결오류");
                                }
                            });



                            return true;
                        }
                    });

                    popup.show();
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Id;
        ImageView Profile;
        CardView MemberCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MemberCardView = itemView.findViewById(R.id.User_Info);
            Name = itemView.findViewById(R.id.Name_Info);
            Id = itemView.findViewById(R.id.ID_Info);
            Profile = itemView.findViewById(R.id.Profile_Info);
        }
    }
}
